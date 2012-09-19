/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mindseye;
import java.util.*;
import java.io.*;
import java.util.regex.Pattern;
/**
 *
 * @author tanvi
 */
public class ngrams_short {
    
    
    static HashSet<String> verb_list = new HashSet<String>();
    static HashSet<String> object_list = new HashSet<String>();
    
    public static void readVerbList(String inputFile){
        
        try{
                
                FileInputStream fis = new FileInputStream(inputFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    String verb = dis.readLine();
                    System.out.println("line" + verb);
                    //String verb= line.substring(1,line.length()-1).toLowerCase();
                    if (verb.contains("_")) {
                       
                        verb = verb.substring(0, verb.indexOf("_"));
                        System.out.println("found verb " + verb);
                    }
                    verb_list.add(verb);
                    //System.out.println("added " + verb);
                }
                
                
            
            
        }catch(Exception e){System.out.println("exception" +e);}
        
    }
    public static void readObjectList2(String objectFile1, String objectFile2){
        
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
                
                
            
            
        }catch(Exception e){System.out.println("exception" +e);}
        
    }
    
    
     public static void readObjectList(String inputDir){
        
        try{
            File dir = new File(inputDir);
            File[] files = dir.listFiles();
            
            for (int i = 0 ; i<files.length;i++){
                
                FileInputStream fis = new FileInputStream(files[i]);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    String line = dis.readLine();
                    System.out.println("line" + line);
                    String object = line.split(" ")[9];
                    object= object.substring(1,object.length()-1);
                    object_list.add(object);
                    System.out.println("adding object " + object);
                }
                
                
            }
            
        }catch(Exception e){System.out.println("exception" +e);}
        
    }
     
     
     public static void readExpandedObjectList(String objectFile){
        
        try{
                
                FileInputStream fis = new FileInputStream(objectFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    String line = dis.readLine();
                    //object_list.add(line);
                    String words[] = line.split(" ");
                    for (int i = 0 ; i<words.length;i++){
                        object_list.add(words[i]);
                    }
                    //System.out.println("added " + line);
                }
                
                
            
        }catch(Exception e){System.out.println("exception" +e);}
        
    }
    
     public static void writeObjectList (String writeFile){
         try{
          File output_file = new File(writeFile);
          FileWriter fstream = new FileWriter(output_file);
          BufferedWriter out = new BufferedWriter(fstream);
          
          Iterator it = object_list.iterator();
          while(it.hasNext()){
              Object key = it.next();
              out.write(key + "\n");
          }
          
          out.close();
          }catch(Exception e){System.out.println("exception" +e);}
       
         
     }
     
     
     public static void shortNgrams(String ngramsDir, String outFile){
         
         try{
            
            File output_file = new File(outFile);
            FileWriter fstream = new FileWriter(output_file);
            BufferedWriter out = new BufferedWriter(fstream); 
             
            File dir = new File(ngramsDir);
            
            
                
            File[] files = dir.listFiles();

            for (int j = 0 ; j<files.length ; j++){
                String filename = files[j].getName();
                if (filename.contains(".gz") || filename.contains(".idx")) continue;
                System.out.println(" processing file " + filename);
                FileInputStream fis = new FileInputStream(files[j]);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    String line = dis.readLine();
                    String pattern = "[\\s]+";
                    Pattern splitter = Pattern.compile(pattern);
                    String[] words = splitter.split(line);
                    //boolean contains = false;
                    boolean alpha = true;
                    for (int k = 0 ; k<words.length-1;k++){
                        String current = words[k];
                        //if (!current.matches("[A-Za-z]*")) alpha = false;
                        if (object_list.contains(current) || verb_list.contains(current) || (object_list.contains(current.substring(0,current.length()-1)) && current.charAt(current.length()-1)=='s' ) )
                        {
                            //System.out.println(" contains !!! line="+ line + "current = " + current + " reason:"+" object list = "+ object_list.contains(current) +" object list with s= "+ (object_list.contains(current.substring(0,current.length()-1)) && current.charAt(current.length()-1)=='s' ) +" verb list = "+ verb_list.contains(current));
                            //contains = true;
                            out.write(line + "\n");
                            break;
                        }

                    }
                    /*
                    if (contains) {
                        //System.out.println("line" + line);
                        out.write(line + "\n");
                    }
                     * 
                     */
                    //System.out.println("line" + line);


                }
                
                
                
                
                
            }
            out.close();
            
            
           
            
        }catch(Exception e){System.out.println("exception" +e);}
         
     }
    
     /*
      * This file takes the large data dump of N grams and shortens it according to verb and object List
      * 
      * verbFile: file path of list of verbs needed in N grams
      * ObjectList: file path of list of objects needed in N grams
      * ngram_input: folder, containing all files (ignores files with extension .gz), Just give a folder with all files, do not give a folder with folders
      * ngram_output: output file path
      */
    public static void main(String args[]) {
        
        
        
        /*
        String verbFile = "C:/Users/tanvi/Desktop/mindseye/verb_list.txt";
        readVerbList(verbFile);
        System.out.println("read the file "+ verb_list.size());
        
        
        readObjectList("C:/Users/tanvi/Desktop/mindseye/vaticlabels_C-D1_0819.tar/vaticlabels_C-D1_0819/vaticlabels_C-D1_0819/labels");
        System.out.println("object hashset" + object_list);
        
        writeObjectList("C:/Users/tanvi/Desktop/mindseye/new_object_list.txt");
         * 
         */
        
        String verbFile = "/Users/girish/Dropbox/Tanvi_code/submit/data_files/all_forms_verb.txt";
        readVerbList(verbFile);
        System.out.println("made verb list"+ verb_list);
        
        String objectList = "/Users/girish/Dropbox/Tanvi_code/submit/data_files/expanded_object_list.txt";
        readExpandedObjectList(objectList);
        System.out.println("made object list" + object_list + " size " + object_list.size());
        
        /*
        String objectList1 = "C:/Users/tanvi/Desktop/mindseye/new_object_list_no_hyphen.txt";
        String objectList2 = "C:/Users/tanvi/Desktop/mindseye/object_classes_no_hyphen.txt";
        readObjectList2(objectList1, objectList2);
        System.out.println("made object list" + object_list);
         * 
         */
        
        
        String ngrams_input = "J:/nlp-research/googleNgram/web_5gram/data/5gms";
        String ngrams_output = "J:/nlp-research/googleNgram/web_5gram/prog_output/5gms_new.txt";
        shortNgrams(ngrams_input, ngrams_output);
        
        /*
        System.out.println(" DONE 2 Grams , STARTING 3 Grams");
        
        ngrams_input = "J:/nlp-research/googleNgram/web_5gram/data/3gms";
        ngrams_output = "J:/nlp-research/googleNgram/web_5gram/prog_output/3gms_new.txt";
        shortNgrams(ngrams_input, ngrams_output);
        
        
        System.out.println(" DONE 4 Grams , STARTING 5 Grams");
        
        ngrams_input = "J:/nlp-research/googleNgram/web_5gram/data/5gms";
        ngrams_output = "J:/nlp-research/googleNgram/web_5gram/prog_output/5gms_new.txt";
        shortNgrams(ngrams_input, ngrams_output);
        
        
        ngrams_input = "J:/nlp-research/googleNgram/web_5gram/data/5gms";
        ngrams_output = "J:/nlp-research/googleNgram/web_5gram/prog_output/5gms.txt";
        shortNgrams(ngrams_input, ngrams_output);
         * 
         */
        
       
         
        
    }
    
    
}
