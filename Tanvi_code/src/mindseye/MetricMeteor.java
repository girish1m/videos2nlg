package mindseye;
import edu.cmu.meteor.scorer.MeteorConfiguration;
import edu.cmu.meteor.scorer.MeteorScorer;
import edu.cmu.meteor.util.Constants;


public class MetricMeteor 
{	
	public static void main (String args[])
	{
		MeteorConfiguration config = new MeteorConfiguration();
		config.setLanguage("en");
		config.setNormalization(Constants.NORMALIZE_KEEP_PUNCT);
		MeteorScorer scorer = new MeteorScorer(config);
		double score = scorer.getMeteorStats("the cat sat on the mat", "the cat sat on the mat").score;
		System.out.println(score);
	}

}
