package ml;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class J48_ {
	private static J48 j48 = new J48();

	// J48_
	public static double [] run(Instances train, Instances test) {
		
		Evaluation eval = null;
		try {
			j48.buildClassifier(train);
			eval = new Evaluation(train);
			eval.evaluateModel(j48, test);
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
