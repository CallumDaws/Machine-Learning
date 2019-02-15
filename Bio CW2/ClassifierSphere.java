/**
 * Class that implements a sphere classifier
 */

public class ClassifierSphere extends Classifier implements Cloneable {
	double []center;
	double radius;
	int classOfSphere;
	int numAtt;

	public ClassifierSphere(double []pCenter,double pRadius, int pClass) {
		//Copying the sphere definition from the received parameters
		numAtt=Attributes.getNumAttributes();
		center = new double[numAtt];
		for(int i=0;i<numAtt;i++) {
			center[i]=pCenter[i];
		}

		radius = pRadius;
		classOfSphere = pClass;
	}

	public int classifyInstance(Instance ins) {
		double dist=0;
		
		for(int i=0;i<numAtt;i++) {
			dist+=Math.pow((center[i]-ins.getAttribute(i)),2);
		}
		dist=Math.sqrt(dist);

		if(dist<=radius) {
			return classOfSphere;
		}
		return -1;
	}
	
	public void printClassifier() {
		String cl="Center ";
		for(int i=0;i<numAtt;i++) {
			cl+=center[i]+",";
		}
		cl+=" radius "+radius+", class "+classOfSphere;

		System.out.println(cl);
	}

	public int classifierClass() {
		return classOfSphere;
	}

}
