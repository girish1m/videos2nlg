/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mindseye;
import edu.smu.tspell.wordnet.*;
import java.util.*;
import java.lang.*;
import java.io.*;

/**
 *
 * @author tanvi
 */
public class Expand {
    
    
    static HashSet<String> object_list = new HashSet<String>();
    static HashSet<String> verb_list = new HashSet<String>();
    
    
    public static void readVerbList(String inputFile){
        
        try{
                
                FileInputStream fis = new FileInputStream(inputFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    String line = dis.readLine();
                    //System.out.println("line" + line);
                    String verb= line.substring(1,line.length()-1).toLowerCase();
                    verb_list.add(verb);
                    System.out.println("added verb " + verb);
                }
                
                
            
            
        }catch(Exception e){System.out.println("exception" +e);}
        
    }
    
    public static void readObjectList2(String objectFile1, String objectFile2, String writeFile){
        
        try{
                
                FileInputStream fis = new FileInputStream(objectFile1);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    String line = dis.readLine();
                    object_list.add(line);
                    //System.out.println("added " + line);
                }
                
                FileInputStream fis2 = new FileInputStream(objectFile2);
                BufferedInputStream bis2 = new BufferedInputStream(fis2);
                DataInputStream dis2 = new DataInputStream(bis2);
                while (dis2.available() != 0) {
                    String line2 = dis2.readLine();                
                    object_list.add(line2);
                    //System.out.println("added " + line2);
                }
                
                 File output_file = new File(writeFile);
                 FileWriter fstream = new FileWriter(output_file);
                 BufferedWriter out = new BufferedWriter(fstream);
          
                 Iterator it = object_list.iterator();
                 while(it.hasNext()){
                     String current=(String)it.next();
                     out.write(current+"\n");
                 }
                
                 out.close();
            
        }catch(Exception e){System.out.println("exception" +e);}
        
    }
    
    
    public static void readObjectList(String objectFile){
        
        try{
                
                FileInputStream fis = new FileInputStream(objectFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    String line = dis.readLine();
                    object_list.add(line);
                    //System.out.println("added " + line);
                }
                
                
            
        }catch(Exception e){System.out.println("exception" +e);}
        
    }
    
    public static void writeExpanded(HashSet list, String writeFile,WordNetDatabase database, SynsetType senseType){
        
        try{
                  File output_file = new File(writeFile);
                  FileWriter fstream = new FileWriter(output_file);
                  BufferedWriter out = new BufferedWriter(fstream);

                  Iterator it = list.iterator();
                  while(it.hasNext()){
                      String current = (String)it.next();
                      System.out.println("current "+ current);
                      out.write(current+"\n");
                      Synset[] synsets = database.getSynsets(current, senseType);
                      if (synsets.length<=0) continue;
                      Synset most_common_synset = synsets[0];
                      String[] wordForms = most_common_synset.getWordForms();
                      for (int i = 0 ; i<wordForms.length;i++){
                          String wordform = wordForms[i].toLowerCase();
                          System.out.println("      word form "+i+" is="+ wordform);
                          if (wordform.equals(current)) continue;
                          out.write(":"+wordform+"\n");
                      }
                  }
                  out.close();
        }catch(Exception e){System.out.println("exception" +e);}

    
    }
    /*
     * this expands the verb and subject/object list using synonyms of wordnet
     */
    
    public static void main(String args[]){
        
        //Loading word net database
        System.setProperty("wordnet.database.dir", "/Users/girish/Dropbox/Tanvi_code/submit/dependencies/WordNet/2.1/dict");
        WordNetDatabase database = WordNetDatabase.getFileInstance();
        
        
        
        //loaded object list
        String objectList = "/Users/girish/Dropbox/Tanvi_code/submit/data_files/total_object_list.txt";
        readObjectList(objectList);
        System.out.println("made object list" + object_list.size());
              
        //expanding object list
        String expandedObjectList = "/Users/girish/Dropbox/Tanvi_code/submit/data_files/expanded_object_list.txt";
        writeExpanded(object_list, expandedObjectList, database, SynsetType.NOUN);
        
        
        //loaded verb list
        String verbFile = "/Users/girish/Dropbox/Tanvi_code/submit/data_files/verb_list.txt";
        readVerbList(verbFile);
        System.out.println("made verb list"+ verb_list.size());
        
        //expanding verb list
        String expandedVerbList = "/Users/girish/Dropbox/Tanvi_code/submit/data_files/expanded_verb_list.txt";
        writeExpanded(verb_list, expandedVerbList, database, SynsetType.VERB);
        
        
        
         
        
        
    }
    
}
