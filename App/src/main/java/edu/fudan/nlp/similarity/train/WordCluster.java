package edu.fudan.nlp.similarity.train;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import edu.fudan.data.reader.Reader;
import edu.fudan.ml.types.alphabet.LabelAlphabet;
import edu.fudan.nlp.similarity.Cluster;
import edu.fudan.util.MyHashSparseArrays;
import gnu.trove.iterator.TIntFloatIterator;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.hash.TIntFloatHashMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;
import gnu.trove.set.hash.TLinkedHashSet;
/**
 * Brown 词聚类算法，单线程版
 * @author xpqiu
 *
 */
public class WordCluster implements Serializable{

	
	private static final long serialVersionUID = 1632709924496094832L;
	private static float ENERGY = 0.999f;
	public int slotsize = 50;	
	int lastid;

	LabelAlphabet alpahbet = new LabelAlphabet();

	TIntObjectHashMap<TIntHashSet> leftnodes = new TIntObjectHashMap<TIntHashSet>();
	TIntObjectHashMap<TIntHashSet> rightnodes = new TIntObjectHashMap<TIntHashSet>();
	TIntObjectHashMap<Cluster> clusters = new TIntObjectHashMap<Cluster>();

	/**
	 * 父节点
	 */
	TIntIntHashMap heads = new TIntIntHashMap(200,0.5f,-1,-1);

	TIntHashSet slots = new TIntHashSet();

	/**
	 * 有向边
	 */
	TIntObjectHashMap<TIntFloatHashMap> pcc = new TIntObjectHashMap<TIntFloatHashMap>();
	/**
	 * 无向边
	 */
	TIntObjectHashMap<TIntFloatHashMap> wcc = new TIntObjectHashMap<TIntFloatHashMap>();

	TIntFloatHashMap wordProb = new TIntFloatHashMap();

	public int totalword;
	/**
	 * 是否持续合并到一个类
	 */
	private boolean meger = true;

	public WordCluster(){

	}

	/**
	 * 读文件，并统计每个字的字频
	 */
	public void read(Reader reader) {
		while (reader.hasNext()) {
			String content = (String) reader.next().getData();
			int prechar = -1;
			wordProb.adjustOrPutValue(prechar, 1, 1);
			for (int i = 0; i < content.length()+1; i++) {
				int idx;
				if(i<content.length()){
					String c = String.valueOf(content.charAt(i));
					idx = alpahbet.lookupIndex(c);					
				}
				else{
					idx = -2;					
				}
				wordProb.adjustOrPutValue(idx, 1, 1);


				TIntFloatHashMap map = pcc.get(prechar);
				if(map==null){
					map = new TIntFloatHashMap();
					pcc.put(prechar, map);
				}				
				map.adjustOrPutValue(idx, 1, 1);

				TIntHashSet left = leftnodes.get(idx);
				if(left==null){
					left = new TIntHashSet();
					leftnodes.put(idx, left);

				}
				left.add(prechar);

				TIntHashSet right = rightnodes.get(prechar);
				if(right==null){
					right = new TIntHashSet();
					rightnodes.put(prechar, right );
				}
				right.add(idx);		
				prechar = idx;
			}
		}
		lastid = alpahbet.size();
		totalword = alpahbet.size();
		System.out.println("[总个数：]\t" + totalword);

		statisticProb();

	}

	/**
	 * 一次性统计概率，节约时间
	 */
	private void statisticProb() {
		System.out.println("统计概率");
		float totalword = alpahbet.size();
		TIntFloatIterator it = wordProb.iterator();
		while(it.hasNext()){
			it.advance();
			float v = it.value()/totalword;
			it.setValue(v);
			Cluster cluster = new Cluster(it.key(),v,alpahbet.lookupString(it.key()));
			clusters.put(it.key(), cluster);
		}

		TIntObjectIterator<TIntFloatHashMap> it1 = pcc.iterator();
		while(it1.hasNext()){
			it1.advance();
			TIntFloatHashMap map = it1.value();
			TIntFloatIterator it2 = map.iterator();
			while(it2.hasNext()){
				it2.advance();
				it2.setValue(it2.value()/totalword);
			}
		}

	}


	/**
	 * total graph weight
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	private float weight(int c1, int c2) {
		float w;
		float pc1 = wordProb.get(c1);
		float pc2 = wordProb.get(c2);
		if (c1==c2) {
			float pcc = getProb(c1,c1);
			w =  clacW(pcc,pc1,pc2);
		} else {
			float pcc1 = getProb(c1, c2);			
			float p1= clacW(pcc1,pc1,pc2);			

			float pcc2 = getProb(c2, c1);			
			float p2 = clacW(pcc2,pc2,pc1);			
			w =  p1 + p2;
		}
		setweight(c1, c2, w);
		return w;
	}


	/**
	 * 计算c1,c2合并后与k的权重
	 * @param c1
	 * @param c2
	 * @param k
	 * @return
	 */
	private float weight(int c1, int c2, int k) {
		float w;
		float pc1 = wordProb.get(c1);
		float pc2 = wordProb.get(c2);
		float pck = wordProb.get(k);
		//新类的概率
		float pc = pc1+pc2;

		if (c1==k) {			
			float pcc1 = getProb(c1,c1);
			float pcc2 = getProb(c2,c2);
			float pcc3 = getProb(c1,c2);
			float pcc4 = getProb(c2,c1);
			float pcc = pcc1 + pcc2 + pcc3 + pcc4;
			w = clacW(pcc,pc,pc);			

		} else {

			float pcc1 = getProb(c1,k);
			float pcc2 = getProb(c2,k);

			float pcc12 = pcc1 + pcc2;			
			float p1 = clacW(pcc12,pc,pck);

			float pcc3 = getProb(k,c1);
			float pcc4 = getProb(k,c2);			
			float pcc34 = pcc3 + pcc4;			
			float p2 = clacW(pcc34,pck,pc);
			w =  p1 + p2;
		}
		return w;
	}

	private float clacW(float pcc, float pc1, float pc2) {
		float p= 0;
		if(pcc!=0f)
			p =pcc *  (float) (Math.log(pcc) - Math.log(pc1) - Math.log(pc2));
		//		if(Float.isInfinite(p)||Float.isNaN(p))
		//			return p;		
		return p;
	}

	private float getProb(int c1, int c2) {
		float p;
		TIntFloatHashMap map = pcc.get(c1);
		if(map == null){
			p = 0f;
		}else{
			p = pcc.get(c1).get(c2);						
		}
		return p;
	}


	/**
	 * merge clusters
	 */
	public void mergeCluster() {
		int maxc1 = -1;
		int maxc2 = -1;
		float maxL = Float.NEGATIVE_INFINITY;
		TIntIterator it1 = slots.iterator();		
		while(it1.hasNext()){
			int i = it1.next();
			TIntIterator it2 = slots.iterator();
			//			System.out.print(i+": ");
			while(it2.hasNext()){
				int j= it2.next();

				if(i>=j)
					continue;
				//				System.out.print(j+" ");
				float L = calcL(i, j);
				//				System.out.print(L+" ");
				if (L > maxL) {
					maxL = L;
					maxc1 = i;
					maxc2 = j;
				}
			}
			//			System.out.println();
		}
		//		if(maxL == Float.NEGATIVE_INFINITY )
		//			return;

		merge(maxc1,maxc2);
	}
	
	/**
	 * 合并c1和c2
	 * @param c1
	 * @param c2
	 */

	protected void merge(int c1, int c2) {
		int newid = lastid++;
		heads.put(c1, newid);
		heads.put(c2, newid);
		TIntFloatHashMap newpcc = new TIntFloatHashMap();
		TIntFloatHashMap inewpcc = new TIntFloatHashMap();
		TIntFloatHashMap newwcc = new TIntFloatHashMap();
		float pc1 = wordProb.get(c1);
		float pc2 = wordProb.get(c2);		
		//新类的概率
		float pc = pc1+pc2;

		float w;
		{
			float pcc1 = getProb(c1,c1);
			float pcc2 = getProb(c2,c2);
			float pcc3 = getProb(c1,c2);
			float pcc4 = getProb(c2,c1);
			float pcc = pcc1 + pcc2 + pcc3 + pcc4;
			if(pcc!=0.0f)
				newpcc.put(newid, pcc);
			w = clacW(pcc,pc,pc);
			if(w!=0.0f)
				newwcc.put(newid, w);
		}
		TIntIterator it = slots.iterator();
		while(it.hasNext()){
			int k = it.next();

			float pck = wordProb.get(k);			
			if (c1==k||c2==k) {			
				continue;
			} else {				
				float pcc1 = getProb(c1,k);
				float pcc2 = getProb(c2,k);
				float pcc12 = pcc1 + pcc2;
				if(pcc12!=0.0f)
					newpcc.put(newid, pcc12);
				float p1 = clacW(pcc12,pc,pck);

				float pcc3 = getProb(k,c1);
				float pcc4 = getProb(k,c2);			
				float pcc34 = pcc3 + pcc4;
				if(pcc34!=0.0f)
					inewpcc.put(k, pcc34);	
				float p2 = clacW(pcc34,pck,pc);
				w =  p1 + p2;
				if(w!=0.0f)
					newwcc.put(newid, w);
			}
		}

		//更新slots
		slots.remove(c1);
		slots.remove(c2);
		slots.add(newid);
		pcc.put(newid, newpcc);
		pcc.remove(c1);
		pcc.remove(c2);
		TIntFloatIterator it2 = inewpcc.iterator();
		while(it2.hasNext()){
			it2.advance();
			TIntFloatHashMap pmap = pcc.get(it2.key());
			//						if(pmap==null){
			//							pmap = new TIntFloatHashMap();
			//							pcc.put(it2.key(), pmap);
			//						}
			pmap.put(newid, it2.value());
			pmap.remove(c1);
			pmap.remove(c2);
		}


		//
		//newid 永远大于 it3.key;
		wcc.put(newid, new TIntFloatHashMap());
		wcc.remove(c1);
		wcc.remove(c2);
		TIntFloatIterator it3 = newwcc.iterator();
		while(it3.hasNext()){
			it3.advance();
			TIntFloatHashMap pmap = wcc.get(it3.key());
			pmap.put(newid, it3.value());
			pmap.remove(c1);
			pmap.remove(c2);
		}

		wordProb.remove(c1);
		wordProb.remove(c2);
		wordProb.put(newid, pc);

		//修改cluster
		Cluster cluster = new Cluster(newid, clusters.get(c1),clusters.get(c2),pc);
		clusters.put(newid, cluster);
		System.out.println("合并："+cluster.rep);
		
	}

	/**
	 * calculate the value L
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	public float calcL(int c1, int c2) {
		float L = 0;

		TIntIterator it = slots.iterator();
		while(it.hasNext()){
			int k = it.next();
			if(k==c2)
				continue;
			L += weight(c1,c2,k);
		}

		it = slots.iterator();
		while(it.hasNext()){
			int k = it.next();
			L -= getweight(c1,k);
			L -= getweight(c2, k);
		}
		return L;

	}



	private void setweight(int c1, int c2, float w) {
		if(w==0.0f)
			return;
		int max,min;
		if(c1<=c2){
			max = c2;
			min = c1;
		}else{
			max = c1;
			min = c2;
		}
		TIntFloatHashMap map2 = wcc.get(min);
		if(map2==null){
			map2 = new TIntFloatHashMap();
			wcc.put(min, map2);
		}
		map2.put(max, w);
	}

	private float getweight(int c1, int c2) {
		int max,min;
		if(c1<=c2){
			max = c2;
			min = c1;
		}else{
			max = c1;
			min = c2;
		}
		float w;
		TIntFloatHashMap map2 = wcc.get(min);
		if(map2==null){
			w = 0;
		}else
			w = map2.get(max);
		return w;
	}

	/**
	 * start clustering
	 */
	public Cluster startClustering() {



//		int[] idx = MyCollection.sort(wordProb);

		int[] idx = MyHashSparseArrays.trim(wordProb, ENERGY);

		int mergeCount  = idx.length;
		int remainCount  = idx.length;
		
		System.out.println("[待合并个数：]\t" +mergeCount );
		System.out.println("[总个数：]\t" + totalword);
		
		int round;
		for (round = 0; round< Math.min(slotsize,mergeCount); round++) {
			slots.add(idx[round]);
			System.out.println(round + "\t" + alpahbet.lookupString(idx[round]) + "\t" + slots.size());

		}
		TIntIterator it1 = slots.iterator();

		while(it1.hasNext()){
			int i = it1.next();
			TIntIterator it2 = slots.iterator();
			while(it2.hasNext()){
				int j= it2.next();
				if(i>=j)
					continue;
				weight(i, j);
			}
		}
		
		while (slots.size()>1) {
			if(round < mergeCount)
				System.out.println(round + "\t" + alpahbet.lookupString(idx[round]) + "\tSize:\t" +slots.size());
			else
				System.out.println(round + "\t" + "\tSize:\t" +slots.size());
			System.out.println("[待合并个数：]\t" + remainCount-- );
			long starttime = System.currentTimeMillis();
			mergeCluster();
			long endtime = System.currentTimeMillis();
			System.out.println("\tTime:\t" + (endtime-starttime)/1000.0);
			if(round < mergeCount){
				int id = idx[round];
				slots.add(id);
				TIntIterator it = slots.iterator();
				while(it.hasNext()){
					int j= it.next();
					weight(j, id);
				}
			}else{
				if(!meger )
					return null;
			}
			try {
				saveTxt("res-"+round);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			round++;
		}

		return clusters.get(slots.toArray()[0]);
		

	}

	public String toString(){
		StringBuilder sb = new StringBuilder();

		TIntObjectHashMap<TLinkedHashSet<String>> sets = new TIntObjectHashMap<TLinkedHashSet<String>>();

		for(int i=0;i<alpahbet.size();i++){
			int head = getHead(i);
			TLinkedHashSet<String> s = sets.get(head);
			if(s==null){
				s = new TLinkedHashSet();
				sets.put(head, s);
			}
			s.add(alpahbet.lookupString(i));
		}

		TIntObjectIterator<TLinkedHashSet<String>> it = sets.iterator();
		while(it.hasNext()){
			it.advance();
			if(it.value().size()<2)
				continue;
			sb.append(wordProb.get(it.key()));
			sb.append(" ");
			TObjectHashIterator<String> itt = it.value().iterator();
			while(itt.hasNext()){
				String ss = itt.next();
				sb.append(ss);
				sb.append(" ");
			}
			sb.append("\n");
		}

		return sb.toString();

	}

	private int getHead(int i) {
		int h = heads.get(i);
		if(h==-1)
			return i;
		else
			return getHead(h);
	}

	/**
	 * 将模型存储到文件
	 * @param file
	 * @throws IOException
	 */
	public void saveModel(String file) throws IOException {
		File f = new File(file);
		File path = f.getParentFile();
		if(!path.exists()){
			path.mkdirs();
		}
		ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(
				new BufferedOutputStream(new FileOutputStream(file))));
		out.writeObject(this);
		out.close();
	}

	public static  WordCluster loadFrom(String file) throws IOException,
	ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(
				new BufferedInputStream(new FileInputStream(file))));
		WordCluster cl = (WordCluster) in.readObject();
		in.close();
		return cl;
	}

	/**
	 * 将结果保存到文件
	 * @param file
	 * @throws Exception
	 */
	public void saveTxt(String file) throws Exception {
		FileOutputStream fos = new FileOutputStream(file);
		BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(
				fos, "UTF8"));
		bout.write(this.toString());
		bout.close();

	}
}