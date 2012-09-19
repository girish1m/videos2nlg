/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mindseye;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.parser.lexparser.Options;
import edu.stanford.nlp.process.Morphology;

import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 *
 * @author tanvi
 */

class Pair{
    String str1= null;
    String str2= null;
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Pair other = (Pair) obj;
        if (! str1.equals(other.str1))
            return false;
        if (! str2.equals(other.str2))
            return false;
        return true;
    }
    
}

class Phrase{
    String line;
    int frequency;
}

public class match {
    
    //object List
    static HashMap<String, ArrayList<String>> expanded_object_list = new HashMap<String, ArrayList<String>>();
    //verb List
    static HashMap<String, ArrayList<String>> expanded_verb_list = new HashMap<String, ArrayList<String>>();
    
    // subject-verb phrases
    static ArrayList<String> subject_verb = new ArrayList<String>();
    // verb-object phrases
    static HashMap<String, ArrayList<String>> verb_object = new HashMap<String, ArrayList<String>>();
    // object-verb phrases
    static ArrayList<String> object_verb = new ArrayList<String>();
    // verb-subject phrases
    static HashMap<String, ArrayList<String>> verb_subject = new HashMap<String, ArrayList<String>>();
    
    static ArrayList<Phrase> all_phrases = new ArrayList<Phrase>();
    
    
     public static void readNGrams(String inputDir, ArrayList<Pair> pairs, String outFile){
        
        try{
            File output_file = new File(outFile);
            FileWriter fstream = new FileWriter(output_file);
            BufferedWriter out = new BufferedWriter(fstream); 
            
            File dir = new File(inputDir);
            File[] files = dir.listFiles();
            
            for (int i = 0 ; i<files.length;i++){
                
                FileInputStream fis = new FileInputStream(files[i]);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    String line = dis.readLine();
                    //System.out.println("line" + line);
                    String pattern = "[\\s]+";
                    Pattern splitter = Pattern.compile(pattern);
                    String[] words = splitter.split(line);
                    Pair newpair = new Pair();
                    newpair.str1 = words[0];
                    newpair.str2 = words[words.length-2];
                    
                    //System.out.println(" line " + line +" equals= " + newpair.equals(pairs.get(0)) );
                    if (pairs.contains(newpair)) {
                        out.write(line+"\n");
                    }    
                }
            }
            
            out.close();
        }catch(Exception e){System.out.println("exception" +e);}
        
    }
     
     
     public static void processTriple(String subject, String verb, String object, String inputDir, String outDir){
         
         //expansion
         ArrayList<String> subject_words = expanded_object_list.get(subject);
         ArrayList<String> verb_words = expanded_verb_list.get(verb);
         ArrayList<String> object_words = expanded_object_list.get(object);
         
         
         try{
            File output_file_subject_verb = new File(outDir+"/subject_verb.txt");
            FileWriter fstream_subject_verb = new FileWriter(output_file_subject_verb);
            BufferedWriter out_subject_verb = new BufferedWriter(fstream_subject_verb); 
            
            File output_file_object_verb = new File(outDir+"/object_verb.txt");
            FileWriter fstream_object_verb = new FileWriter(output_file_object_verb);
            BufferedWriter out_object_verb = new BufferedWriter(fstream_object_verb); 
            
            File output_file_verb_subject = new File(outDir+"/verb_subject.txt");
            FileWriter fstream_verb_subject = new FileWriter(output_file_verb_subject);
            BufferedWriter out_verb_subject = new BufferedWriter(fstream_verb_subject); 
            
            File output_file_verb_object = new File(outDir+"/verb_object.txt");
            FileWriter fstream_verb_object = new FileWriter(output_file_verb_object);
            BufferedWriter out_verb_object = new BufferedWriter(fstream_verb_object); 
            
            File dir = new File(inputDir);
            File[] files = dir.listFiles();
            
            for (int i = 0 ; i<files.length;i++){
                
                System.out.println("file name" + files[i].getName());
                FileInputStream fis = new FileInputStream(files[i]);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    String line = dis.readLine();
                    //System.out.println("line" + line);
                    String pattern = "[\\s]+";
                    Pattern splitter = Pattern.compile(pattern);
                    String[] words = splitter.split(line);
                    
                    String first = words[0];
                    String last = words[words.length-2];
                    //System.out.println(" first " + first  + " last "+ last);
                    
                    if (subject_words.contains(first) && verb_words.contains(last)){
                        subject_verb.add(line);
                        out_subject_verb.write(line+"\n");
                    }
                    if (object_words.contains(first) && verb_words.contains(last)){
                        object_verb.add(line);
                        out_object_verb.write(line+"\n");
                    }
                    if (verb_words.contains(first) && subject_words.contains(last)){
                        ArrayList<String> value = verb_subject.get(first);
                        if (value == null) value = new ArrayList<String>();
                        value.add(line);
                        verb_subject.put(first, value);
                        out_verb_subject.write(line+"\n");
                    }
                    if (verb_words.contains(first) && object_words.contains(last)){
                        ArrayList<String> value = verb_object.get(first);
                        if (value == null) value = new ArrayList<String>();
                        value.add(line);
                        verb_object.put(first, value);
                        out_verb_object.write(line+"\n");
                    }
                    
                    
                }
            }
            
            out_subject_verb.close();
            out_object_verb.close();
            out_verb_subject.close();
            out_verb_object.close();
            
        }catch(Exception e){System.out.println("exception" +e);}
         
         
         
         
         
     }
     
     public static void mergeLists(String outFile){
         
         try{
            File output_file = new File(outFile);
            FileWriter fstream = new FileWriter(output_file);
            BufferedWriter out = new BufferedWriter(fstream); 
            
             // active phrases
             Iterator it_subject_verb = subject_verb.iterator();
             while(it_subject_verb.hasNext()){
                 String current = (String)it_subject_verb.next();
                 System.out.println("subject verb line " + current);
                 String pattern = "[\\s]+";
                 Pattern splitter = Pattern.compile(pattern);
                 String[] words = splitter.split(current);

                 String verb = words[words.length-2];
                 int frequency_subject_verb = Integer.parseInt(words[words.length-1]);

                 ArrayList current_verb_objects = verb_object.get(verb);
                 if (current_verb_objects == null) continue;

                 current = current.substring(0, current.lastIndexOf(verb));
                 Iterator it_verb_objects = current_verb_objects.iterator();
                 while(it_verb_objects.hasNext()){
                     String current_verb_object = (String)it_verb_objects.next();
                     System.out.println("verb object line " + current_verb_object);
                 
                     String[] words_verb_object = splitter.split(current_verb_object);
                     String freq_str = words_verb_object[words_verb_object.length-1];
                     String verb_object_phrase = current_verb_object.substring(0,current_verb_object.lastIndexOf(freq_str));
                     int frequency_verb_object = Integer.parseInt(freq_str);
              
                     String newphrase = current + " "+ verb_object_phrase;
                     int finalfreq = frequency_subject_verb * frequency_verb_object;
                     
                     Phrase final_phrase_obj = new Phrase();
                     final_phrase_obj.frequency=finalfreq;
                     final_phrase_obj.line=newphrase;
                     all_phrases.add(final_phrase_obj);
                     
                     System.out.println("new phrase " + newphrase + " final freq "+ finalfreq);
                     out.write("Phrase:"+ newphrase+";Freq:"+finalfreq+"\n");
                 }

             }
             
             
             
             
             
             
             
             
             
             // passive phrases
             Iterator it_object_verb = object_verb.iterator();
             while(it_object_verb.hasNext()){
                 String current = (String)it_object_verb.next();
                 System.out.println("object verb line " + current);
                 String pattern = "[\\s]+";
                 Pattern splitter = Pattern.compile(pattern);
                 String[] words = splitter.split(current);

                 String verb = words[words.length-2];
                 int frequency_object_verb = Integer.parseInt(words[words.length-1]);

                 ArrayList current_verb_subjects = verb_subject.get(verb);
                 if (current_verb_subjects == null) continue;

                 current = current.substring(0, current.lastIndexOf(verb));
                 Iterator it_verb_subjects = current_verb_subjects.iterator();
                 while(it_verb_subjects.hasNext()){
                     String current_verb_subject = (String)it_verb_subjects.next();
                     System.out.println("verb subject line " + current_verb_subject);
                 
                     String[] words_verb_subject = splitter.split(current_verb_subject);
                     String freq_str = words_verb_subject[words_verb_subject.length-1];
                     String verb_subject_phrase = current_verb_subject.substring(0,current_verb_subject.lastIndexOf(freq_str));
                     int frequency_verb_subject = Integer.parseInt(freq_str);
              
                     String newphrase = current + " "+ verb_subject_phrase;
                     int finalfreq = frequency_object_verb * frequency_verb_subject;
                     
                     Phrase final_phrase_obj = new Phrase();
                     final_phrase_obj.frequency=finalfreq;
                     final_phrase_obj.line=newphrase;
                     all_phrases.add(final_phrase_obj);
                     
                     System.out.println("new phrase " + newphrase + " final freq "+ finalfreq);
                     out.write("Phrase:"+ newphrase+";Freq:"+finalfreq+"\n");
                 }

             }
             
             
             
             
             
             
             
            
            
            
            
            out.close();
        }catch(Exception e){System.out.println("exception" +e);}
            
            
         
         
     }
     
     public static void readObjectList(String objectFile){
        
        try{
                
                FileInputStream fis = new FileInputStream(objectFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                ArrayList expanded_words = new ArrayList();
                String current_word = "";
                while (dis.available() != 0) {
                    String line = dis.readLine();
                    //object_list.add(line);
                    
                    if (line.charAt(0) != ':') {
                        expanded_object_list.put(current_word, expanded_words);
                        expanded_words = new ArrayList();
                        current_word = line;
                        expanded_words.add(current_word);
                        
                    } 
                    else{
                        String words[] = line.split(" ");
                        for (int i = 0 ; i<words.length;i++){
                            
                            expanded_words.add(words[i].substring(1));
                        }
                    }
                    //System.out.println("added " + line);
                }
                
                
            
        }catch(Exception e){System.out.println("exception" +e);}
        
    }
     
     
     public static void readVerbList(String verbFile){
        
        try{
                
                FileInputStream fis = new FileInputStream(verbFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                ArrayList expanded_words = new ArrayList();
                String current_word = "";
                while (dis.available() != 0) {
                    String line = dis.readLine();
                    //object_list.add(line);
                    
                    if (line.charAt(0) != ':') {
                        expanded_verb_list.put(current_word, expanded_words);
                        expanded_words = new ArrayList();
                        current_word = line;
                        expanded_words.add(current_word);
                        
                    }
                    else{
                        expanded_words.add(line.substring(1));
                    }
                    
                    //System.out.println("added " + line);
                }
                
                
            
        }catch(Exception e){System.out.println("exception" +e);}
        
    }
     
     
     public static void sort_phrases(String outFile){
         
         try{
            File output_file = new File(outFile);
            FileWriter fstream = new FileWriter(output_file);
            BufferedWriter out = new BufferedWriter(fstream);
         
             // sorting
             Object[] p = all_phrases.toArray();
             Arrays.sort(p, new Comparator() {

                @Override
                public int compare(Object o1, Object o2) {
                    Phrase p1 = (Phrase)o1;
                    Phrase p2 = (Phrase)o2;
                    int ret = p1.frequency < p2.frequency ? 1 : (p1.frequency == p2.frequency) ? 0 : -1;
                    return ret;
                }
             });
             
             for (int i = 0 ; i<p.length;i++){
                 Phrase current_phrase= (Phrase)p[i];
                 out.write("Phrase:"+current_phrase.line + ";Freq:"+ current_phrase.frequency+"\n");
             }
             out.close();
         }catch(Exception e){System.out.println("exception" +e);}
         
         
         
     }
     
     
     // This function just takes the N gram input and finds all S-V-O phrases
     public static void main (String args[]){
         
         
         /*
         String inDir = "C:/Users/tanvi/Desktop/mindseye/prog_output/2gms";
         String outFile = "C:/Users/tanvi/Desktop/mindseye/prog_output/2gms_out.txt";
         readNGrams(inDir, newlist, outFile);
          * 
          */
         
         // load object List
         readObjectList("/Users/girish/Dropbox/Tanvi_code/submit/data_files/expanded_object_list.txt");
         System.out.println(" expanded object list " + expanded_object_list);
         
         //load verb list
         readVerbList("/Users/girish/Dropbox/Tanvi_code/submit/data_files/all_forms_verb.txt");
         System.out.println(" expanded verb list " + expanded_verb_list);
         
         // inDir: this is the directory containing new shortened N grams, I will send you this directory containing from unigram till 5 grams files (shortened)
         String inDir = "/Users/girish/Dropbox/Tanvi_code/prog_output";
         //output phrases are stored in this location
         String outFile = "/Users/girish/Dropbox/Tanvi_code/submit/data_files/person_pass_ball/all_phrases";
         
         // this function finds all (subject-verb) phrases and (verb-object)phrases respectively
         processTriple("person","pass","ball", inDir, outFile);
         
         // This function then merges these two phrases to form a complete subject-verb-object phrases (it takes care of active as well as passive tense)
         // and writes the result to this path
         // count of this final phrase is count of S-V phrase * count of V-O phrase
         mergeLists("/Users/girish/Dropbox/Tanvi_code/submit/data_files/person_pass_ball/final_phrases.txt");
         
         // sorts by these counts in descending order and write it to this final
         sort_phrases("/Users/girish/Dropbox/Tanvi_code/submit/data_files/person_pass_ball/sort_final_phrases.txt");
         
      
     }
    
}
