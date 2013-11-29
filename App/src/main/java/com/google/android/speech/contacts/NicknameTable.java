package com.google.android.speech.contacts;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NicknameTable
{
  private static final Map<String, String[]> nicknameToNameMap;
  
  static
  {
    HashMap localHashMap = Maps.newHashMap();
    localHashMap.put("al", new String[] { "allan", "albert", "alfred" });
    localHashMap.put("aleks", new String[] { "aleksandar", "aleksander", "aleksandra" });
    localHashMap.put("alex", new String[] { "alexander", "alexandra", "alexis" });
    localHashMap.put("ali", new String[] { "alice", "alison", "allison" });
    localHashMap.put("ang", new String[] { "angela" });
    localHashMap.put("ann", new String[] { "annie", "annamaria" });
    localHashMap.put("art", new String[] { "arthur" });
    localHashMap.put("ash", new String[] { "ashley" });
    localHashMap.put("barb", new String[] { "barbara" });
    localHashMap.put("bea", new String[] { "beatrice" });
    localHashMap.put("ben", new String[] { "benjamin" });
    localHashMap.put("bev", new String[] { "beverly" });
    localHashMap.put("brad", new String[] { "bradford", "bradley" });
    localHashMap.put("carol", new String[] { "caroline" });
    localHashMap.put("cat", new String[] { "catherine" });
    localHashMap.put("chris", new String[] { "christian", "christopher", "christine" });
    localHashMap.put("cris", new String[] { "cristian", "cristopher", "cristin" });
    localHashMap.put("dan", new String[] { "daniel", "danny" });
    localHashMap.put("deb", new String[] { "deborah", "debbie" });
    localHashMap.put("di", new String[] { "diana", "diane" });
    localHashMap.put("dom", new String[] { "dominic", "dominique" });
    localHashMap.put("don", new String[] { "donald" });
    localHashMap.put("doug", new String[] { "douglas" });
    localHashMap.put("ed", new String[] { "edward", "eddie" });
    localHashMap.put("em", new String[] { "emily", "emma" });
    localHashMap.put("fab", new String[] { "fabian", "fabiola" });
    localHashMap.put("fitz", new String[] { "fitzpatrick" });
    localHashMap.put("flo", new String[] { "florence", "florian" });
    localHashMap.put("fran", new String[] { "frances", "francis" });
    localHashMap.put("fred", new String[] { "frederic", "frederick", "freddie" });
    localHashMap.put("gab", new String[] { "gabriel", "gabrielle", "gabriella" });
    localHashMap.put("gil", new String[] { "gilbert" });
    localHashMap.put("glo", new String[] { "gloria" });
    localHashMap.put("greg", new String[] { "gregory" });
    localHashMap.put("gus", new String[] { "gustav", "gustavo" });
    localHashMap.put("hil", new String[] { "hilary", "hillary" });
    localHashMap.put("jan", new String[] { "janet", "janette" });
    localHashMap.put("jeff", new String[] { "jeffrey" });
    localHashMap.put("jen", new String[] { "jenifer", "jennifer", "jennie" });
    localHashMap.put("jer", new String[] { "jeremy", "jeromy" });
    localHashMap.put("jess", new String[] { "jessica", "jessie" });
    localHashMap.put("jill", new String[] { "jillian" });
    localHashMap.put("jo", new String[] { "joanna", "joanne" });
    localHashMap.put("jon", new String[] { "jonathan", "johnathan" });
    localHashMap.put("jojn", new String[] { "jonathan", "johnathan" });
    localHashMap.put("joe", new String[] { "joseph" });
    localHashMap.put("josh", new String[] { "joshua" });
    localHashMap.put("kath", new String[] { "kathryn" });
    localHashMap.put("kel", new String[] { "kellie", "kelly" });
    localHashMap.put("ken", new String[] { "kenny", "kenneth" });
    localHashMap.put("kim", new String[] { "kimberly", "kimberley" });
    localHashMap.put("kris", new String[] { "kristina", "kristen", "kristian" });
    localHashMap.put("leo", new String[] { "leonard", "leonardo" });
    localHashMap.put("les", new String[] { "lesley", "leslie" });
    localHashMap.put("lili", new String[] { "lilian", "liliana" });
    localHashMap.put("lou", new String[] { "louis" });
    localHashMap.put("magda", new String[] { "magdalena" });
    localHashMap.put("manu", new String[] { "manuel", "manuela" });
    localHashMap.put("matt", new String[] { "mathew", "matthew" });
    localHashMap.put("max", new String[] { "maximilian", "maxwell" });
    localHashMap.put("meg", new String[] { "megan", "meghan" });
    localHashMap.put("mel", new String[] { "melanie", "melinda", "melvin" });
    localHashMap.put("mich", new String[] { "michelle", "michael" });
    localHashMap.put("mona", new String[] { "monalisa" });
    localHashMap.put("natasha", new String[] { "natalie", "natalia" });
    localHashMap.put("tasha", new String[] { "natasha" });
    localHashMap.put("nate", new String[] { "nathaniel" });
    localHashMap.put("nev", new String[] { "neville" });
    localHashMap.put("nico", new String[] { "nicola", "nicolas" });
    localHashMap.put("norm", new String[] { "normal" });
    localHashMap.put("oli", new String[] { "oliver", "olivier" });
    localHashMap.put("pam", new String[] { "pamela", "pammy" });
    localHashMap.put("pat", new String[] { "patricia", "patrick" });
    localHashMap.put("phil", new String[] { "philip", "phillip", "philippe" });
    localHashMap.put("rafa", new String[] { "rafael", "rafaela" });
    localHashMap.put("ric", new String[] { "ricardo" });
    localHashMap.put("rich", new String[] { "richard", "richie" });
    localHashMap.put("rob", new String[] { "robert", "robbie" });
    localHashMap.put("rog", new String[] { "roger" });
    localHashMap.put("ron", new String[] { "ronald", "ronnie" });
    localHashMap.put("roz", new String[] { "rosalind", "rosalyn" });
    localHashMap.put("roze", new String[] { "rosemary" });
    localHashMap.put("sal", new String[] { "salvador", "salvatore" });
    localHashMap.put("sam", new String[] { "samuel", "samantha", "sammy" });
    localHashMap.put("shar", new String[] { "sharon" });
    localHashMap.put("shel", new String[] { "shelley", "shelly" });
    localHashMap.put("stef", new String[] { "stefan", "stefanie", "stephanie" });
    localHashMap.put("steph", new String[] { "stephen", "stephanie" });
    localHashMap.put("steve", new String[] { "steven" });
    localHashMap.put("theo", new String[] { "theodore" });
    localHashMap.put("tiff", new String[] { "tiffany" });
    localHashMap.put("tim", new String[] { "timothy" });
    localHashMap.put("tom", new String[] { "tommy" });
    localHashMap.put("val", new String[] { "valerie", "valorie", "valentina" });
    localHashMap.put("vern", new String[] { "vernon" });
    localHashMap.put("vick", new String[] { "victor" });
    localHashMap.put("viv", new String[] { "vivian", "vivien" });
    localHashMap.put("vlad", new String[] { "vladimir" });
    localHashMap.put("walt", new String[] { "walter" });
    localHashMap.put("wes", new String[] { "wesley" });
    localHashMap.put("wil", new String[] { "william" });
    localHashMap.put("will", new String[] { "william" });
    localHashMap.put("wolf", new String[] { "wolfgang" });
    localHashMap.put("xavi", new String[] { "xavier" });
    nicknameToNameMap = Collections.unmodifiableMap(localHashMap);
  }
  
  @Nullable
  public static String[] getNamesForNickname(@Nonnull String paramString)
  {
    return (String[])nicknameToNameMap.get(paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.NicknameTable
 * JD-Core Version:    0.7.0.1
 */