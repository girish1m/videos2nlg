package mindseye;

import java.util.Arrays;
import java.util.List;

import edu.berkeley.nlp.lm.ArrayEncodedNgramLanguageModel;
import edu.berkeley.nlp.lm.StupidBackoffLm;
import edu.berkeley.nlp.lm.cache.ArrayEncodedCachingLmWrapper;
import edu.berkeley.nlp.lm.io.LmReaders;
import edu.berkeley.nlp.lm.StringWordIndexer;
import edu.berkeley.nlp.lm.util.LongRef;
import edu.berkeley.nlp.lm.map.NgramMapWrapper;
import java.util.ArrayList;

class BerkeleyLM {
	public static void main(String args[]){
		
	   NgramMapWrapper<String,LongRef> lm = ( NgramMapWrapper<String,LongRef>) LmReaders.readNgramMapFromBinary("/home/niveda/Documents/RA_work/eng.blm","/home/niveda/Documents/RA_work/vocab_cs");
	   List<String> bigram = new ArrayList<String>();
	   bigram.add("A");
	   bigram.add("dog");
	   System.out.println("Frequency count for A dog:"+lm.getMapForOrder(1).get(bigram));
		//StupidBackoffLm<String> sblm= new StupidBackoffLm(5, StringWordIndexer<String> wordIndexer, NgramMap<LongRef> map, ConfigOptions opts);
		//sblm = readGoogleLmBinary("/Users/girish/Desktop/webngrams/eng.blm",  ,"/Users/girish/Desktop/webngrams/vocab_cs");

	}
}
