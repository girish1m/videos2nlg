import mindseye.*;

import java.util.*;

import edu.berkeley.nlp.lm.io.LmReaders;

public class TemplateBasedApproach {
	static List<String> sentenceStarts = new ArrayList<String>();
	static List<String> sentenceConjunctions = new ArrayList<String>();

	public static void addSentenceStartTemplates() {
		sentenceStarts.add("Here, ");
		sentenceStarts.add("In this video, ");
		sentenceStarts.add("");
		sentenceStarts.add("");
	}

	public static void addSentenceConjunctionsEndTemplates() {
		sentenceConjunctions.add(" and ");
		sentenceConjunctions.add(" and then ");
		sentenceConjunctions.add(". ");
		sentenceConjunctions.add(". ");
		sentenceConjunctions.add(". Then, ");
	}

	public static String constructCompleteSentence(List<String> sentences,
			Random generator) {

		String completeSentence = "";
		for (int i = 0; i < sentences.size(); i++) {
			if (i == 0) {
				int randomIndex = generator.nextInt(sentenceStarts.size());
				completeSentence += sentenceStarts.get(randomIndex);
				StringBuffer start = new StringBuffer(sentences.get(i));
				if (sentenceStarts.get(randomIndex).equals("")) {
					start.delete(0, start.length());
					start.append(
							Character.toUpperCase(sentences.get(i).charAt(0)))
							.append(sentences.get(i).substring(1));
				}
				completeSentence += start;
			} else {
				int randomIndex = generator
						.nextInt(sentenceConjunctions.size());
				completeSentence += sentenceConjunctions.get(randomIndex);
				StringBuffer start = new StringBuffer(sentences.get(i));
				if (completeSentence.endsWith(". ")) {
					start.delete(0, start.length());
					start.append(
							Character.toUpperCase(sentences.get(i).charAt(0)))
							.append(sentences.get(i).substring(1));
				}
				completeSentence += start;
			}
		}
		completeSentence += ".";
		return completeSentence;
	}

	public static void main(String args[]) {
		List<List<String>> triples = new ArrayList<List<String>>();
		List<String> triple = new ArrayList<String>();
		triple.add("person");
		triple.add("pass");
		triple.add("ball");
		triples.add(triple);

		triple.clear();
		triple.add("person");
		triple.add("pass");
		triple.add("ball");
		triples.add(triple);

		List<String> sentences = new ArrayList<String>();
		// generate sentence for both triples
		// load object List
	//	match.readObjectList("/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/expanded_object_list.txt");
	//	System.out.println(" expanded object list "
	//			+ match.expanded_object_list);

		// load verb list
	//	match.readVerbList("/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/all_forms_verb.txt");
	//	System.out.println(" expanded verb list " + match.expanded_verb_list);

	/*	for (int i = 0; i < triples.size(); i++) {
			String svoPath = triples.get(i).get(0) + "_"
					+ triples.get(i).get(1) + "_" + triples.get(i).get(2);
			// inDir: this is the directory containing new shortened N grams, I
			// will send you this directory containing from unigram till 5 grams
			// files (shortened)
			String inDir = "/home/niveda/Documents/RA_work/Tanvi_code/prog_output";
			// output phrases are stored in this location
			String outFile = "/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/"
					+ svoPath + "/all_phrases";

			// this function finds all (subject-verb) phrases and
			// (verb-object)phrases respectively
			match.processTriple(triples.get(i).get(0), triples.get(i).get(1),
					triples.get(i).get(2), inDir, outFile);

			match.mergeLists("/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/"
					+ svoPath + "/final_phrases.txt");

			// This function merges two phrases and assigns it a probability
			// score based on a language model
			// mergeLists_LanguageModel("/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/person_pass_ball/final_lm_phrases.txt");

			// sorts by these counts in descending order and write it to this
			// final
			match.sort_phrases("/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/"
					+ svoPath + "/sort_final_phrases.txt");
			// sort_phrases_LanguageModel("/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/person_pass_ball/sort_final_lm_phrases.txt");
			phrase_parse.subject_words = match.expanded_object_list.get(triples
					.get(i).get(0));
			phrase_parse.verb_words = match.expanded_verb_list.get(triples.get(
					i).get(1));
			phrase_parse.object_words = match.expanded_object_list.get(triples
					.get(i).get(2));

			// output of Match class
			String inFile = "/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/"
					+ svoPath + "//sort_final_phrases.txt";
			// output of this class (no sort)
			String outFileFinal = "/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/"
					+ svoPath + "//PCFG_final_phrases.txt";
			// does parsing and puts output in output file
			phrase_parse.readPhrases(inFile, outFileFinal);

			// sorts by pcfg scores
			String sortoutFile_pcfg = "/home/niveda/Documents/RA_work/Tanvi_code/submit/data_files/"
					+ svoPath + "//sort_pcfg_score_PCFG_final_phrases.txt";
			phrase_parse.sort_phrases_acc_pcfg_score(sortoutFile_pcfg);
			PCFGphrase current_phrase = (PCFGphrase) phrase_parse.p[0];
			sentences.add(current_phrase.line);
		}*/
		sentences.add("the person sat on the motorcycle");
		sentences.add("the person rode the motorocycle");
		addSentenceStartTemplates();
		addSentenceConjunctionsEndTemplates();
		// construct complete sentence
		Random generator = new Random();
		for (int x = 0; x < 5; x++)
			System.out.println(constructCompleteSentence(sentences, generator));

	}
}
