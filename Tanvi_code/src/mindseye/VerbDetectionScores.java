package mindseye;
import java.io.*;
import java.util.*;

class ArrayIndexComparator implements Comparator<Integer>
{
    private final Float[] array;

    public ArrayIndexComparator(Float[] array)
    {
        this.array = array;
    }

    public Integer[] createIndexArray()
    {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < array.length; i++)
        {
            indexes[i] = i; // Autoboxing
        }
        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2)
    {
         // Autounbox from Integer to int to use as array indexes
        return -array[index1].compareTo(array[index2]);
    }
}


public class VerbDetectionScores {
	static List<String> verbs = new ArrayList<String>();
	public static void main(String args[]) {
		try{
			String verbFile = "/home/niveda/Documents/RA_work/Tanvi_code/description_fortanvi/verb_codes.txt";
			FileReader f = new FileReader(verbFile);
			BufferedReader br=new BufferedReader(f);
			String readLine = "";
			
			while((readLine=br.readLine())!=null){
				verbs.add(readLine);
			}
		}
		catch(Exception e){
			System.out.println("Caught exception:"+e);
		}
		try {
			// csv file containing verb detection scores
			String strFile = "/home/niveda/Documents/RA_work/Tanvi_code/description_fortanvi/SR_jniebles_DESCR_TEST_yes10no2.CSV";

			// create BufferedReader to read csv file
			BufferedReader br = new BufferedReader(new FileReader(strFile));
			String strLine = "";
			StringTokenizer st = null;
			int lineNumber = 0, tokenNumber = 0;

			// read comma separated file line by line
			while ((strLine = br.readLine()) != null) {
				lineNumber++;
				if(!strLine.startsWith("[DATA]"))
					continue;
				else
					break;
			}
			List<Integer[]> verbs_for_videos = new ArrayList<Integer[]>(); 
			while ((strLine = br.readLine()) != null) {
				lineNumber++;
				// break comma separated line using ","
				st = new StringTokenizer(strLine, ",");
			    Float[] verb_confidence_scores = new Float[48];

				while (st.hasMoreTokens()) {
					// display csv values
					tokenNumber++;
					String tok = st.nextToken();
					
					if(tokenNumber>51) //50th csv contains the confidence value
					{
						if(tok.equals("-Inf"))
							verb_confidence_scores[tokenNumber-52]=Float.MIN_VALUE;
						else
							verb_confidence_scores[tokenNumber-52]=Float.parseFloat(tok);
					}
					
				}
				// reset token number
				tokenNumber = 0;
				ArrayIndexComparator comparator = new ArrayIndexComparator(verb_confidence_scores);
				Integer[] indexes = comparator.createIndexArray();
				Arrays.sort(indexes, comparator);
				verbs_for_videos.add(indexes);
			}
			for(int i=0;i<verbs_for_videos.size();i++){
				System.out.println("Video "+(i+1)+":"+verbs.get(verbs_for_videos.get(i)[0])+" "+verbs.get(verbs_for_videos.get(i)[1])+" "+verbs.get(verbs_for_videos.get(i)[2]));
			}
			System.out.println("Done");
		} catch (Exception e) {
			System.out.println("Caught exception:" + e);
		}
	}
}
