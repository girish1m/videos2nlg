package mindseye;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;
/**
 *
 * @author niveda
 */
//This program takes a list of input files and creates folders with 1,2,3,4 and 5 grams
public class NgramsGenerator {

    /**
     * @param args the command line arguments
     */
    static int file_no=1;
     public static void createNgramsFromFolder(File input_folder, File output_folder, int ngram_value) {
        Stack<File> stack = new Stack<File>();
        stack.push(input_folder);
        while(!stack.isEmpty()) {
        File child = stack.pop();
        if (child.isDirectory()) {
            for(File f : child.listFiles()) stack.push(f);
        } else if (child.isFile()) {
         try{
                System.out.println("Processing: "+child.getAbsolutePath());
                FileReader fr=new FileReader(child.getAbsolutePath());
                FileWriter outputFile = new FileWriter(output_folder+"/file"+file_no);
                BufferedReader br=new BufferedReader(fr);
                String readline = "";
                while((readline = br.readLine())!=null){
                        String[] words = readline.split("\\s+");
                        for(int i=0;i<words.length-ngram_value+1;i++){
                            String ngram="";
                            for(int j=0; j<ngram_value; j++)
                                ngram = ngram+" "+words[i+j];
                            outputFile.write(ngram+"\n");
                        }
                }
                file_no++;
                outputFile.close();
                br.close();
                fr.close();
            }
            catch(Exception e){
                System.out.println("File not found:"+e);
            }
    }
  }
}
    public static void main(String[] args) {
        // TODO code application logic here
        String input_folder = "/home/niveda/Pictures/Test";
        String ngrams_folder = "/home/niveda/Documents/RA_work/ngrams";
        final File folder = new File(input_folder);
        for(int i=2;i<=5;i++)
        {
            String ngrams_subfolder = ngrams_folder + "/ngrams-" +i;
            File new_ngram_folder = new File(ngrams_subfolder);
            if(!new_ngram_folder.exists())
            {
                new_ngram_folder.mkdir();
            }
            createNgramsFromFolder(folder, new_ngram_folder, i);
            file_no=1;
        }

    }
}
