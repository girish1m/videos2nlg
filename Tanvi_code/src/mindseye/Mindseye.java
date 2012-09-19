/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mindseye;

import java.util.*;
import java.lang.*;
import java.io.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.process.Morphology;


/**
 *
 * @author tanvi
 */

class NLSentence{
    String vid_id;
    String sentence;
}
public class Mindseye {

    /**
     * @param args the command line arguments
     */
    
    static HashMap<String,HashSet<String>> verbList = new HashMap<String,HashSet<String>>();
    static HashMap<String,HashSet<String>> objectList = new HashMap<String,HashSet<String>>();
    static HashMap<String,String> vid_id_mapping = new HashMap<String,String>();
    static ArrayList<NLSentence> nlSentenceArray = new ArrayList<NLSentence>();
    static HashSet<String> verb_list2 = new HashSet<String>();
    
    public static void readVerbList2(String inputFile){
        
        try{
                
                FileInputStream fis = new FileInputStream(inputFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    String line = dis.readLine();
                    System.out.println("in verb list line" + line);
                    if (line.length()<=2) continue;
                    String verb= line.substring(1,line.length()-1).toLowerCase();
                    verb_list2.add(verb);
                    //System.out.println("added " + verb);
                }
                
                
            
            
        }catch(Exception e){System.out.println("exception" +e);}
        
    }
   
    public static void readVerbList(String inputFile){
        
        Morphology m = new Morphology();
        try{
            FileInputStream fis = new FileInputStream(inputFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            
            while (dis.available() != 0) {
                String line = dis.readLine();
                
                System.out.println("line" + line);
                
                String vid_id = line.split(",")[0];
                vid_id = vid_id.substring(0,vid_id.lastIndexOf("."));
                String verb = m.stem( (line.split(",")[1]).toLowerCase() );
                
                HashSet<String> value = verbList.get(vid_id);
                if (value == null) value = new HashSet<String>();
                value.add(verb);
                verbList.put(vid_id, value);
                
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
                String filename = files[i].getName();
                filename = filename.substring(0,filename.lastIndexOf("."));
                HashSet<String> value = objectList.get(filename);
                if (value == null) value = new HashSet<String>();
                while (dis.available() != 0) {
                    String line = dis.readLine();
                    System.out.println("line" + line);
                    String object = line.split(" ")[9];
                    object= object.substring(1,object.length()-1);
                    value.add(object);
                }
                objectList.put(filename, value);
                
            }
            
        }catch(Exception e){System.out.println("exception" +e);}
        
    }
    
    
     public static void getVidMapping(String inputFile){
        
        try{
            FileInputStream fis = new FileInputStream(inputFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            
            while (dis.available() != 0) {
                String line = dis.readLine();
                
                System.out.println("line" + line);
                
                String vid_id1 = line.split(",")[0];
                String vid_id2 = line.split(",")[1];
                vid_id2 = vid_id2.substring(0,vid_id2.lastIndexOf("."));
                
                vid_id_mapping.put(vid_id1, vid_id2);      
            }
        }catch(Exception e){System.out.println("exception" +e);}
        
    }
    
    public static void readNLSentences(String inputFile){
        
        try{
            FileInputStream fis = new FileInputStream(inputFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            
            while (dis.available() != 0) {
                String line = dis.readLine();
                String vid_id1 = line.split(",")[0];
                String vid_id2 = vid_id_mapping.get(vid_id1);
                String sentence= line.split(",")[2];
                //System.out.println("line" + line);
                NLSentence nl_instance = new NLSentence();
                nl_instance.sentence=sentence;
                nl_instance.vid_id=vid_id2;
                nlSentenceArray.add(nl_instance);
            }
        }catch(Exception e){System.out.println("exception" +e);}
        
    }
    
    
    public static void printParses (String outputFile){
        
        LexicalizedParser lp = new LexicalizedParser("/Users/girish/Dropbox/Tanvi_code/submit/dependencies/englishPCFG.ser.gz");
        lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});

        Morphology m = new Morphology();
    
        try{
            
            File ffile = new File(outputFile);
            FileWriter fstream = new FileWriter(ffile);
            PrintWriter out = new PrintWriter(fstream);
        
        
            File ffile2 = new File("/Users/girish/Dropbox/Tanvi_code/submit/data_files/temp_output_parsing.txt");
            FileWriter fstream2 = new FileWriter(ffile2);
            PrintWriter out2 = new PrintWriter(fstream2);
                // Accessing sentences
                Iterator it = nlSentenceArray.iterator();
                while(it.hasNext()){
                    NLSentence nl = (NLSentence)it.next();
                    String allsentence= nl.sentence;
                    System.out.println("all sentence" + allsentence);
                    String sentences[]= allsentence.split("\\. ");
                    out.write("<Video>"+"\n");
                    out.write(nl.vid_id+"\n");
                    //HashSet<String> vid_verbs = verbList.get(nl.vid_id);
                    //if (vid_verbs == null) continue;
                    System.out.println("current vid " + nl.vid_id);
                    out2.write("video:"+nl.vid_id+"\n");
                    for (int i = 0 ; i<sentences.length ;i++){
                        out2.write("sentence"+i+":");
                        String current = sentences[i];
                        if (current.length()<2) continue;
                        if (current.charAt(0)=='"') current = current.substring(1);
                        if (current.charAt(current.length()-1)=='"') current = current.substring(0,current.length()-1);
                        if (current.charAt(current.length()-1)=='.') current = current.substring(0,current.length()-1);
                        
                        out.write("<Sentence>"+"\n");
                        out.write(current+"\n");
                        System.out.println("current sentence " + current);

                        //String [] sent = current.split(" ");
                        //Tree parse = (Tree) lp.apply(Arrays.asList(sent));
                        Tree parse = (Tree) lp.apply(current);
                        
                        out.write("<ParseTree>"+"\n");
                        TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
                        tp.printTree(parse,out);
                        out.write("</ParseTree>"+"\n");
                        
                        out.write("<VerbDep>"+"\n");
                        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
                        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
                        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
                        Collection tdl = gs.typedDependenciesCollapsed();
                        //System.out.println("types dependency " + tdl);
                        Iterator tdl_it = tdl.iterator();
                        while (tdl_it.hasNext()){
                            Object current_tdl = tdl_it.next();
                            if (current_tdl == null)continue;
                            String dep = (((TypedDependency)current_tdl).dep().toString()).toLowerCase();
                            
                            dep = dep.substring(0,dep.lastIndexOf("-"));
                            dep = m.stem(dep);
                            System.out.println(" dep:" + dep);
                            String gov = (((TypedDependency)current_tdl).gov().toString()).toLowerCase();
                            
                            gov = gov.substring(0,gov.lastIndexOf("-"));
                            gov = m.stem(gov);
                            System.out.println(" gov:" + gov);
                            if (verb_list2.contains(dep) || verb_list2.contains(gov)){
                                out.write(current_tdl.toString()+"\n");
                                if (verb_list2.contains(dep)){
                                    out2.write(dep+",");
                                }
                                if (verb_list2.contains(gov)){
                                    out2.write(gov+",");
                                }
                                
                            }
                            
                        }
                        
                        out.write("</VerbDep>"+"\n");
                        out.write("</Sentence>"+"\n");
                        out2.write("\n");
                    }
                    out.write("</Video>"+"\n");
                }
                
                out.close();
                out2.close();
        
        
        }catch(Exception e){System.out.println("exception"+e);}
    
    
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        //readVerbList("C:/Users/tanvi/Desktop/mindseye/verbs.txt");
        //System.out.println("verb hashset" + verbList);
        
        
        //readObjectList("C:/Users/tanvi/Desktop/mindseye/vaticlabels_C-D1_0819.tar/vaticlabels_C-D1_0819/vaticlabels_C-D1_0819/labels");
        //System.out.println("object hashset" + objectList);
        
        String verbFile = "/Users/girish/Dropbox/Tanvi_code/submit/data_files/verb_list.txt";
        readVerbList2(verbFile);
        System.out.println("made verb list"+ verb_list2);
        System.out.println("check " + verb_list2.contains("walk"));
        
        getVidMapping("/Users/girish/Dropbox/Tanvi_code/HR_ARL_D_DES_240HP_20110520_AMZ_1_1_HR-FOR-AMZ-DES-DEV_FILENAMES.CSV");
        System.out.println("vid id mapping" + vid_id_mapping);
        
        readNLSentences("/Users/girish/Dropbox/Tanvi_code/copy_HR_ARL_D_DES_240HP_20110520_AMZ_1_1_HR-FOR-AMZ-DES-DEV.CSV");
        printParses("/Users/girish/Dropbox/Tanvi_code/submit/data_files/NLSentenceParses_new.txt");
         
        
    }
}
