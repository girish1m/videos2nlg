import java.io.*;
import java.util.*;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class ObjectActivityCooccurrence {
	static String parserPath = "/u/ml/mindseye/nlg_lm/mindseye/englishPCFG.ser.gz";
	static HashMap<String, Integer> object_words = new HashMap<String, Integer>();
	static HashMap<String, Integer> verb_words = new HashMap<String, Integer>();
	
	/*public static void readCooccurrenceMatrix(String outputMatrixPath){
		try{
			FileReader f=new FileReader(outputMatrixPath);
			BufferedReader b=new BufferedReader(f);
			String readLine="";
			boolean subj_act=false, obj_act=false, obj_cnt=false;
			while((readLine=b.readLine())!=null){
			
			}
		}
		catch(Exception e){
			System.out.println("Exception caught:"+e);
			e.printStackTrace();
		}
	}*/
	
	public static int readObjectList(String objectListPath) {
		int object_index = 0;
		try {

			FileInputStream fis = new FileInputStream(objectListPath);
			BufferedInputStream bis = new BufferedInputStream(fis);
			DataInputStream dis = new DataInputStream(bis);
			
			while (dis.available() != 0) {
				String line = dis.readLine();
				if (line.charAt(0) != ':') {
					object_words.put(line, object_index);
				    System.out.println(line + " " + object_index);
					object_index++;

				} else {
					object_words.put(line.substring(1), object_index - 1);
					System.out.println(line.substring(1)+
					 " "+(object_index-1));
				}
			}
			System.out.println(object_words);

		} catch (Exception e) {
			System.out.println("exception" + e);
		}
		return (object_index);
	}

	public static int readVerbList(String verbListPath) {
		int verb_index = 0;
		try {

			FileInputStream fis = new FileInputStream(verbListPath);
			BufferedInputStream bis = new BufferedInputStream(fis);
			DataInputStream dis = new DataInputStream(bis);
			
			while (dis.available() != 0) {
				String line = dis.readLine();
				if (line.charAt(0) != ':') {
					verb_words.put(line, verb_index);
					System.out.println(line + " " + verb_index);
					verb_index++;

				} else {
					verb_words.put(line.substring(1), verb_index - 1);
					 System.out.println(line.substring(1)+
					" "+(verb_index-1));
				}
			}

		} catch (Exception e) {
			System.out.println("exception" + e);
		}
		return (verb_index);
	}

	public static void main(String args[]) {
		
		// Read lines from the LDC English Gigaword Corpus
		try {

			String objectListPath = "/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/expanded_object_list.txt";
			String verbListPath = "/u/ml/mindseye/nlg_lm/LM/all_forms_verb.txt";
			String outputMatrixPath = "/u/ml/mindseye/nlg_lm/LM/cooccurMatrix.txt";
			int no_objects = readObjectList(objectListPath);
			int no_verbs = readVerbList(verbListPath);

			double[][] obj_act_matrix = new double[no_objects][no_verbs];
			double[][] subj_act_matrix = new double[no_objects][no_verbs];
			int[] object_counts = new int[no_objects];
			
			LexicalizedParser lp = new LexicalizedParser(parserPath);
			lp.setOptionFlags(new String[] { "-maxLength", "80",
					"-retainTmpSubcategories" });

			String ldcCorpusPath = "/home/niveda/Public";
			File input_folder = new File(ldcCorpusPath);
			Stack<File> stack = new Stack<File>();
			stack.push(input_folder);
			int x=1;
			while (!stack.isEmpty()) {
				File child = stack.pop();
				if (child.isDirectory()) {
					for (File f : child.listFiles())
						stack.push(f);
				} else if (child.isFile()) {

					System.out
							.println("Processing: " + child.getAbsolutePath());
					FileReader fr = new FileReader(child.getAbsolutePath());
					BufferedReader br = new BufferedReader(fr);
					String readLine = "";
					boolean read = false;
					while ((readLine = br.readLine()) != null) {
					{
						
									boolean contains_nsubj = false;
									boolean contains_dobj = false;

									if (readLine.startsWith("nsubj")) {
										String dependency = readLine.substring(readLine.indexOf(",")+2);
										if ((verb_words.containsKey(readLine.substring(readLine.indexOf("(")+1, readLine.indexOf("-")))
												&& (object_words.containsKey(dependency.substring(0, dependency.indexOf("-"))))))
											contains_nsubj = true;
										if (contains_nsubj){
											subj_act_matrix[object_words.get(dependency.substring(0, dependency.indexOf("-")))]
													[verb_words
													.get(readLine.substring(readLine.indexOf("(")+1, readLine.indexOf("-")))] += 1;
										//	System.out.println("nsubj "+readLine.substring(readLine.indexOf("(")+1, readLine.indexOf("-")));
										//	System.out.println(dependency.substring(0, dependency.indexOf("-")));
										//	System.out.println(readLine);
										}}
									if (readLine.startsWith("dobj")) {
										
										String dependency = readLine.substring(readLine.indexOf(",")+2);
										if ((verb_words.containsKey(readLine.substring(readLine.indexOf("(")+1, readLine.indexOf("-")))
												&& (object_words.containsKey(dependency.substring(0, dependency.indexOf("-"))))))
											contains_dobj = true;
										if (contains_dobj){
											obj_act_matrix[object_words.get(dependency.substring(0, dependency.indexOf("-")))]
													[verb_words
													.get(readLine.substring(readLine.indexOf("(")+1, readLine.indexOf("-")))] += 1;
										//	System.out.println("dobj "+readLine.substring(readLine.indexOf("(")+1, readLine.indexOf("-")));
										//	System.out.println(dependency.substring(0, dependency.indexOf("-")));
										//	System.out.println(readLine);
										}
									}
								}
						
					}
					br.close();
					fr.close();
				}
			}
		FileWriter fw = new FileWriter(outputMatrixPath);
		fw.write("[OBJECT_ACTIVITY_COUNT]\n");
		for (int i = 0; i < no_objects; i++) {
			for (int j = 0; j < no_verbs; j++){
				fw.write(obj_act_matrix[i][j]+"\t");
			}
			 fw.write("\n");
		}
		fw.write("[SUBJECT_ACTIVITY_COUNT]\n");
		for (int i = 0; i < no_objects; i++) {
			for (int j = 0; j < no_verbs; j++){
				
					fw.write(subj_act_matrix[i][j]+"\t");
			}
			fw.write("\n");
		}
		
		fw.close();
		System.out.println("Done. Check "+outputMatrixPath+" for co-occurrence results");
		} catch (Exception e) {
			System.out.println("Exception caught:" + e);
			e.printStackTrace();
		}
		
	}
}