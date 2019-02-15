/*
 * Control.java
 *
 */

public class Control {
	
	/** Creates a new instance of Control */
	public Control() {
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		int numReps = 10;
		for(int i=0;i<numReps;i++) {
			run(args);
		}
	}

	public static void run(String[] args) {
		// Start clock
		long t1=System.currentTimeMillis();

		// Loading the training data, initialising the object that will collect the classifiers	
		InstanceSet trainingSet = new InstanceSet(args[0],true);
		ClassifierAggregated solution = new ClassifierAggregated();
		int countIterations=0;
		System.out.println();

		// We will train classifiers while there are instances in the training set to cover
		while(trainingSet.numInstances()>0) {
			// Calling a (nature-inspired) optimisation method to generate one classifiers
			Classifier subSolution = generateSubsolution(trainingSet);
			// Printing the stats on training data of that classifier
			System.out.print("Classifier of iteration "+countIterations+". ");
			subSolution.computeStats(trainingSet);

			// Adding the classifier to the collection and removing the instances from the training set that have been covered by that classifier	
			solution.addSubSolution(subSolution);
			int numRemoved = trainingSet.removeInstances(subSolution);
			System.out.println("Iteration "+countIterations+", removed "+numRemoved+" instances, instances left "+trainingSet.numInstances());

			// Printing the stats of the current collection of classifiers on training data
			System.out.print("Overall stats at iteration "+countIterations+". ");
			solution.computeStats(trainingSet);
			countIterations++;
			System.out.println();
		}

		// Printing the final collection of classifiers
		System.out.println("Final classifier");
		solution.printClassifier();
		System.out.println();

		// Computing the stats of the final collection of classifiers on test data
		InstanceSet testSet = new InstanceSet(args[1],false);
		System.out.println("Stats on test data");
		solution.computeStats(testSet);
		System.out.println();

		// End clock and printing the time it took the algorithm to run	
		long t2=System.currentTimeMillis();
		System.out.println("Total time: "+((t2-t1)/1000.0));

		// Reset the static attributes class so we can call this method again;
		Attributes.reset();
	}

	public static Classifier generateSubsolution(InstanceSet trainingSet) {
		GAwrapper wrapper = new GAwrapper(trainingSet);
		return wrapper.generateClassifier();

		//GPwrapper wrapper = new GPwrapper(trainingSet);
		//return wrapper.generateClassifier();

		//PSOwrapper wrapper = new PSOwrapper(trainingSet);
		//return wrapper.generateClassifier();

		//return new ClassifierMLP(trainingSet);
	}
}
