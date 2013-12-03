package com.google.android.velvet.actions;

import android.os.Parcelable;

import javax.annotation.Nullable;

public class Disambiguation<T extends Candidate<?>, extends Parcelable>
        implements Parcelable
        {
public static final Parcelable.Creator<Disambiguation>CREATOR=new Parcelable.Creator()
        {
public Disambiguation<?>createFromParcel(Parcel paramAnonymousParcel)
        {
        return new Disambiguation(paramAnonymousParcel,getClass().getClassLoader());
        }

public Disambiguation[]newArray(int paramAnonymousInt)
        {
        return new Disambiguation[paramAnonymousInt];
        }
        };
private static final boolean DBG=false;
private static final int STATUS_AUTO_COMPLETED=3;
private static final int STATUS_COMPLETED_BY_USER=2;
private static final int STATUS_NO_MATCH=4;
private static final int STATUS_ONGOING=1;
private static final int STATUS_UNINITIALIZED=0;
private static final String TAG="Velvet.Disambiguation";
private List<T>mCandidates;
private String mCanonicalRelationship;
private boolean mIsRelationship;
private ProgressListener<T>mListener;
private T mResult;
private int mStatus;
private String mTitle;

public Disambiguation()
        {
        this.mStatus=0;
        }

protected Disambiguation(Parcel paramParcel,ClassLoader paramClassLoader)
        {
        Parcelable[]arrayOfParcelable=paramParcel.readParcelableArray(paramClassLoader);
        if(arrayOfParcelable!=null)
        {
        this.mCandidates=Lists.newArrayList();
        int i=arrayOfParcelable.length;
        for(int j=0;j<i;j++)
        {
        Parcelable localParcelable=arrayOfParcelable[j];
        this.mCandidates.add((Candidate)localParcelable);
        }
        }
        this.mResult=((Candidate)paramParcel.readParcelable(paramClassLoader));
        this.mStatus=paramParcel.readInt();
        this.mTitle=paramParcel.readString();
        }

public Disambiguation(Disambiguation<T>paramDisambiguation)
        {
        this.mTitle=paramDisambiguation.getTitle();
        this.mCandidates=paramDisambiguation.mCandidates;
        this.mResult=paramDisambiguation.mResult;
        this.mStatus=paramDisambiguation.mStatus;
        this.mIsRelationship=false;
        this.mCanonicalRelationship=this.mTitle;
        }

private T autoSelectCandidate(List<T>paramList)
        {
        Iterator localIterator=paramList.iterator();
        while(localIterator.hasNext())
        {
        Candidate localCandidate=(Candidate)localIterator.next();
        if(autoSelectItem(localCandidate)){
        return localCandidate;
        }
        }
        return null;
        }

private void checkIsCompleted()
        {
        Preconditions.checkState(isCompleted());
        }

private void checkStatus(int...paramVarArgs)
        {
        int i=paramVarArgs.length;
        for(int j=0;j<i;j++){
        if(paramVarArgs[j]==this.mStatus){
        return;
        }
        }
        throw new IllegalStateException("Illegal status: "+this.mStatus);
        }

public static boolean haveSameState(Disambiguation<?>paramDisambiguation1,Disambiguation<?>paramDisambiguation2)
        {
        boolean bool1=true;
        boolean bool2;
        boolean bool3;
        label23:
        label29:
        boolean bool4;
        if((paramDisambiguation1==null)||(paramDisambiguation2==null)){
        if(paramDisambiguation1==null)
        {
        bool2=bool1;
        if(paramDisambiguation2!=null){
        break label40;
        }
        bool3=bool1;
        if(bool2!=bool3){
        break label46;
        }
        bool4=bool1;
        }
        }
        label40:
        label46:
        boolean bool6;
        do
        {
        boolean bool5;
        do
        {
        int i;
        int j;
        do
        {
        return bool4;
        bool2=false;
        break;
        bool3=false;
        break label23;
        bool1=false;
        break label29;
        i=paramDisambiguation1.mStatus;
        j=paramDisambiguation2.mStatus;
        bool4=false;
        }while(i!=j);
        bool5=Objects.equal(paramDisambiguation1.mCandidates,paramDisambiguation2.mCandidates);
        bool4=false;
        }while(!bool5);
        bool6=Objects.equal(paramDisambiguation1.mResult,paramDisambiguation2.mResult);
        bool4=false;
        }while(!bool6);
        if(paramDisambiguation1.mResult==null){
        return bool1;
        }
        return Objects.equal(paramDisambiguation1.mResult.getSelectedItem(),paramDisambiguation2.mResult.getSelectedItem());
        }

public static boolean isCompleted(@Nullable Disambiguation<?>paramDisambiguation)
        {
        return(paramDisambiguation!=null)&&(paramDisambiguation.isCompleted());
        }

private void notifyListener()
        {
        if(this.mListener!=null){
        this.mListener.onDisambiguationProgress(this);
        }
        }

private String tag()
        {
        return"Velvet.Disambiguation"+this;
        }

public boolean autoSelectItem(T paramT)
        {
        return true;
        }

public int describeContents()
        {
        return 0;
        }

public T get()
        {
        checkIsCompleted();
        return(Candidate)Preconditions.checkNotNull(this.mResult);
        }

public List<T>getCandidates()
        {
        return this.mCandidates;
        }

public String getCanonicalRelationship()
        {
        return this.mCanonicalRelationship;
        }

public String getFormattedTitle()
        {
        String[]arrayOfString=this.mTitle.split(" ");
        int i=0;
        if(i<arrayOfString.length)
        {
        if(arrayOfString[i].length()<2){
        arrayOfString[i]=arrayOfString[i].toUpperCase();
        }
        for(;;)
        {
        i++;
        break;
        arrayOfString[i]=(arrayOfString[i].substring(0,1).toUpperCase()+arrayOfString[i].substring(1));
        }
        }
        return TextUtils.join(" ",arrayOfString);
        }

public boolean getIsRelationship()
        {
        return this.mIsRelationship;
        }

public int getNumSelectableItems(List<T>paramList)
        {
        return paramList.size();
        }

public String getTitle()
        {
        return this.mTitle;
        }

public boolean hasAlternativeCandidates()
        {
        return(hasNoResults())&&(!this.mCandidates.isEmpty());
        }

public boolean hasNoResults()
        {
        return this.mStatus==4;
        }

public boolean isCompleted()
        {
        return(this.mStatus==2)||(this.mStatus==3);
        }

public boolean isOngoing()
        {
        return this.mStatus==1;
        }

public boolean isSelectedByUser()
        {
        checkIsCompleted();
        return this.mStatus==2;
        }

public void refineCandidates(List<T>paramList)
        {
        int i=getNumSelectableItems(paramList);
        this.mCandidates=paramList;
        switch(i)
        {
default:
        this.mStatus=1;
        notifyListener();
        return;
        case 0:
        this.mStatus=4;
        notifyListener();
        return;
        }
        select(autoSelectCandidate(paramList));
        }

public void select(T paramT)
        {
        Preconditions.checkArgument(this.mCandidates.contains(paramT));
        this.mResult=((Candidate)Preconditions.checkNotNull(paramT));
        if((this.mResult.getSelectedItem()!=null)||(autoSelectItem(this.mResult)))
        {
        this.mStatus=2;
        notifyListener();
        }
        }

public void setCandidates(List<T>paramList,boolean paramBoolean)
        {
        checkStatus(new int[]{0});
        int i=getNumSelectableItems(paramList);
        this.mCandidates=paramList;
        switch(i)
        {
default:
        this.mStatus=1;
        }
        for(;;)
        {
        notifyListener();
        return;
        this.mStatus=4;
        continue;
        this.mResult=autoSelectCandidate(paramList);
        if(paramBoolean){
        this.mStatus=2;
        }else{
        this.mStatus=3;
        }
        }
        }

public void setCanonicalRelationship(String paramString)
        {
        this.mCanonicalRelationship=paramString;
        }

public void setIsRelationship(boolean paramBoolean)
        {
        this.mIsRelationship=paramBoolean;
        }

public void setListener(ProgressListener<T>paramProgressListener)
        {
        this.mListener=paramProgressListener;
        }

public void setTitle(String paramString)
        {
        this.mTitle=paramString;
        }

public String toString()
        {
        return"[candidates="+this.mCandidates+", result="+this.mResult+", title="+this.mTitle+", status="+this.mStatus+"]";
        }

public void writeToParcel(Parcel paramParcel,int paramInt)
        {
        if(this.mStatus==0){
        VelvetStrictMode.logW("Velvet.Disambiguation","Should not write to parcel: "+toString());
        }
        if(this.mCandidates==null){}
        for(Parcelable[]arrayOfParcelable=null;;arrayOfParcelable=(Parcelable[])this.mCandidates.toArray(new Parcelable[this.mCandidates.size()]))
        {
        paramParcel.writeParcelableArray(arrayOfParcelable,0);
        paramParcel.writeParcelable(this.mResult,0);
        paramParcel.writeInt(this.mStatus);
        paramParcel.writeString(this.mTitle);
        return;
        }
        }

public static abstract interface Candidate<T extends Parcelable>
        extends Parcelable {
    public abstract T getSelectedItem();
}

public static abstract interface ProgressListener<T extends Disambiguation.Candidate<?>> {
    public abstract void onDisambiguationProgress(Disambiguation<T> paramDisambiguation);
}
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.actions.Disambiguation

 * JD-Core Version:    0.7.0.1

 */