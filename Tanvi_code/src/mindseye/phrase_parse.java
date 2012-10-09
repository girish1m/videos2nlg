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




public class phrase_parse {
  
    static String parserPath = "/home/niveda/Documents/RA_work/Tanvi_code/submit/dependencies/englishPCFG.ser.gz";
    static ArrayList<PCFGphrase> pcfg_phrases = new ArrayList<PCFGphrase>();
    
    public static ArrayList<String> subject_words = new ArrayList<String>();
    public static ArrayList<String> verb_words = new ArrayList<String>();
    public static ArrayList<String> object_words = new ArrayList<String>();
    public static Object[] p=null;
     
     public static void readPhrases(String inFile, String outFile){
        
        try{
                File output_file = new File(outFile);
                FileWriter fstream = new FileWriter(output_file);
                BufferedWriter out = new BufferedWriter(fstream);
          
                LexicalizedParser lp = new LexicalizedParser(parserPath);
                lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});
         
                FileInputStream fis = new FileInputStream(inFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    String line = dis.readLine();
                    int idx1 = line.indexOf(":");
                    int idx2 = line.lastIndexOf(":");
                    String phrase = line.substring(idx1+1, idx2-5);
                    String frequency = line.substring(idx2+1);
                    
                    String pattern = "[\\s]+";
                    Pattern splitter = Pattern.compile(pattern);
                    String[] words = splitter.split(phrase);

                    //parsing
                    //Tree parse = (Tree) lp.apply(Arrays.asList(words));
                    Tree parse = (Tree) lp.apply(phrase);
                    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
                    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
                    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
                    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
                    //System.out.println(tdl);
                    //System.out.println();
                    double pcfg_score = lp.getPCFGScore();
                    //System.out.println("do pcfg " + pcfg_score);
                    
                    Iterator tdl_i = tdl.iterator();
                    String verb_nsubj = null;
                    String verb_dobj = null;
                    boolean contains_nsubj = false;
                    boolean contains_dobj = false;
                    
                    while(tdl_i.hasNext()){
                        TypedDependency td = (TypedDependency)tdl_i.next();
                        if(td.reln().toString().equals("nsubj")) {
                             verb_nsubj = td.gov().toString().split("-")[0];
                             if (  (verb_words.contains(td.gov().toString().split("-")[0])) &&  (subject_words.contains(td.dep().toString().split("-")[0]))  ) contains_nsubj = true;
                             //System.out.println("subject" + nounLabel);
                        }
                        if(td.reln().toString().equals("dobj")) {
                             verb_dobj = td.gov().toString().split("-")[0];
                             if (  (verb_words.contains(td.gov().toString().split("-")[0])) &&  (object_words.contains(td.dep().toString().split("-")[0]))  ) contains_dobj = true;
                             //System.out.println("object" + dobj);
                        }
                    }
                    
                    if (contains_dobj && contains_nsubj){//valid phrase
                        PCFGphrase newphrase = new PCFGphrase();
                        String enterPhrase = "";
                        for (int i = 0 ; i<words.length;i++){
                            enterPhrase +=words[i]+" ";
                        }
                        newphrase.line=enterPhrase;
                        newphrase.frequency=Integer.parseInt(frequency);
                        newphrase.pcfg_score=pcfg_score;
                        pcfg_phrases.add(newphrase);
                        out.write("Phrase:"+enterPhrase+";Freq:"+frequency+";PCFGscore:"+pcfg_score+"\n");
                        
                    }

        
                }
                
                
                out.close();
                
            
        }catch(Exception e){System.out.println("exception" +e);}
        
    }
     
     
     public static void sort_phrases_acc_pcfg_score(String outFile){
         
         try{
            File output_file = new File(outFile);
            FileWriter fstream = new FileWriter(output_file);
            BufferedWriter out = new BufferedWriter(fstream);
         
             // sortin
             p = pcfg_phrases.toArray();
             Arrays.sort(p, new Comparator() {

                @Override
                public int compare(Object o1, Object o2) {
                    PCFGphrase p1 = (PCFGphrase)o1;
                    PCFGphrase p2 = (PCFGphrase)o2;
                    int ret = p1.pcfg_score < p2.pcfg_score ? 1 : (p1.pcfg_score == p2.pcfg_score) ? 0 : -1;
                    return ret;
                }
             });
             
             for (int i = 0 ; i<p.length;i++){
                 PCFGphrase current_phrase= (PCFGphrase)p[i];
                 out.write("Phrase:"+current_phrase.line+";Freq:"+current_phrase.frequency+";PCFGscore:"+current_phrase.pcfg_score+"\n");                        
             }
             out.close();
         }catch(Exception e){System.out.println("exception" +e);}
         
         
         
     }
     
     
       public static void sort_phrases_acc_freq(String outFile){
         
         try{
            File output_file = new File(outFile);
            FileWriter fstream = new FileWriter(output_file);
            BufferedWriter out = new BufferedWriter(fstream);
         
             // sortin
             Object[] p = pcfg_phrases.toArray();
             Arrays.sort(p, new Comparator() {

                @Override
                public int compare(Object o1, Object o2) {
                    PCFGphrase p1 = (PCFGphrase)o1;
                    PCFGphrase p2 = (PCFGphrase)o2;
                    int ret = p1.frequency < p2.frequency ? 1 : (p1.frequency == p2.frequency) ? 0 : -1;
                    return ret;
                }
             });
             
             for (int i = 0 ; i<p.length;i++){
                 PCFGphrase current_phrase= (PCFGphrase)p[i];
                 out.write("Phrase:"+current_phrase.line+";Freq:"+current_phrase.frequency+";PCFGscore:"+current_phrase.pcfg_score+"\n");                        
             }
             out.close();
         }catch(Exception e){System.out.println("exception" +e);}
         
         
         
     }
    
    /*
        * This class then takes the output of Match class as input, and parses the phrases,
        * it makes sure that the phrases are grammatically correct and it checks whether the
        * expected subject is actually the subject and so on for verb and object.
        * It commutes the PCFG scores and outputs the phrases with counts and pcfg scores into a file
        */
    
    public static void main(String args[]){
        
        
        
         //expansion
         String subject = "person";
         String verb = "pass";
         String object = "ball";
         
         // read object list
         match.readObjectList("/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/expanded_object_list.txt");
         System.out.println(" expanded object list " + match.expanded_object_list);
         
         //read verb list
         match.readVerbList("/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/all_forms_verb.txt");
         System.out.println(" expanded verb list " + match.expanded_verb_list);
         
         subject_words = match.expanded_object_list.get(subject);
         verb_words = match.expanded_verb_list.get(verb);
         object_words = match.expanded_object_list.get(object);
         

        //output of Match class
        String inFile = "/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/person_pass_ball/sort_final_phrases.txt";
        // output of this class (no sort)
        String outFile = "/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/person_pass_ball/PCFG_final_phrases.txt";
        // does parsing and puts output in output file
        readPhrases(inFile,outFile);
        
        // sorts by pcfg scores
        String sortoutFile_pcfg = "/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/person_pass_ball/sort_pcfg_score_PCFG_final_phrases.txt";
        sort_phrases_acc_pcfg_score(sortoutFile_pcfg);
        
        // sorts by freq counts
        String sortoutFile_freq = "/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/person_pass_ball/sort_freq_PCFG_final_phrases.txt";
        sort_phrases_acc_freq(sortoutFile_freq);
        
            
         //Testing
                    /*
                    LexicalizedParser lp = new LexicalizedParser("C:/Users/tanvi/Desktop/nlp-research/stanford-parser-2011-09-14/stanford-parser-2011-09-14/stanford-parser-2011-09-14/grammar/englishPCFG.ser.gz");
                    lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});
         
                    String sentence = "a person approached a car";
                    String words[] = sentence.split(" ");
                    Tree parse = (Tree) lp.apply(Arrays.asList(words));
                    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
                    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
                    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
                    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
                    System.out.println(tdl);
                    System.out.println();
                    
                    Iterator tdl_i = tdl.iterator();
                    while(tdl_i.hasNext()){
                        TypedDependency td = (TypedDependency)tdl_i.next();
                        System.out.println(" td gov "+ td.gov() + "td dep "+ td.dep());
                    }
                     * 
                     */
    }
    
}
