package ml;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class NaiveBayes_ {
	private static NaiveBayes nb = new NaiveBayes();
	
	// NaiveBayes
		public static double [] run(Instances train, Instances test) {
			Evaluation eval = null;
			try {
				nb.buildClassifier(train);
				eval = new Evaluation(train);
				eval.evaluateModel(nb, test);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			double [] rs=new double[3];
			rs[0]=1-eval.errorRate();
			rs[1]=eval.weightedAreaUnderROC();
			rs[2]=eval.weightedFMeasure();
			return rs;
		}

}
