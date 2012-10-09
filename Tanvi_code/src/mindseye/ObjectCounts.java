package mindseye;
import java.io.*;
import java.util.HashMap;
import java.util.Stack;

public class ObjectCounts {
	static HashMap<String, Integer> object_words = new HashMap<String, Integer>();
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

	
	public static void main(String args[]){
		String objectListPath = "/u/ml/mindseye/nlg_lm/LM/lesk_syns_purdue.txt";
		int no_objects = readObjectList(objectListPath);
		String outputMatrixPath = "/u/ml/mindseye/nlg_lm/LM/object_count.txt";
		
		int[] object_counts = new int[no_objects];
		String ldcCorpusPath = "/scratch/cluster/niveda/extracted_original";
		File input_folder = new File(ldcCorpusPath);
		Stack<File> stack = new Stack<File>();
		stack.push(input_folder);
		int x=1;
		try{
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
				
				while ((readLine = br.readLine()) != null) {
				
					String words[] = readLine.split("\\s+");
					for(int i=0;i<words.length;i++){
						if(object_words.containsKey(words[i]))
							object_counts[object_words.get(words[i])]+=1;
					}
				}
				br.close();
				fr.close();
			}

		}
		FileWriter fw = new FileWriter(outputMatrixPath);
		fw.write("[OBJECT_COUNTS]\n");
		for (int i = 0; i < no_objects; i++) {
			fw.write(object_counts[i]+"\t");
		}
		fw.close();
		}
		catch(Exception e){
			System.out.println("Exception caught:"+e);
			e.printStackTrace();
		}
	}
}
