import net.sourceforge.jswarm_pso.FitnessFunction;
import java.lang.Math;

public class MyFitnessFunction extends FitnessFunction {
	private InstanceSet is;
	private int numAtts;

	public MyFitnessFunction(InstanceSet pIS) {
		is = pIS;
		numAtts=Attributes.getNumAttributes();
	}

	public double evaluate(double position[]) {
		Classifier cs = buildClassifier(position);
		return cs.computeFitness(is);
	}

	public Classifier buildClassifier(double position[]) {
		int index = 0;
		double []center = new double[numAtts];
		for(int i=0;i<numAtts;i++) {
			center[i] = position[index++];
		}
		double radius = position[index++];
		int classOfSphere = (int)position[index];
	
		return new ClassifierSphere(center,radius,classOfSphere);
	}
} 
