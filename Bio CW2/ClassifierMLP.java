/**
 * Class that implements a sphere classifier
 */

import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.TransferFunctionType;

public class ClassifierMLP extends Classifier implements Cloneable {
	MultiLayerPerceptron mlp;
	int numAtts;

	DataSetRow createInstance(Instance ins) {
		double []values = new double[numAtts];

		for(int i=0;i<numAtts;i++) {
			values[i] = ins.getAttribute(i);
		}

		return new DataSetRow(values, new double[]{ins.getClassValue()});
	}

	DataSet createDataset(InstanceSet is) {
		int numIns = is.numInstances();
		DataSet ts = new DataSet(numAtts,1);

		for(int i=0;i<numIns;i++) {
			ts.addRow(createInstance(is.getInstance(i)));
		}

		return ts;
	}

	public ClassifierMLP(InstanceSet is) {
		numAtts = Attributes.getNumAttributes();
		DataSet ts = createDataset(is);

		// The constructor of the MultiLayerPerceptron requires the choice of a activation function plus a (variable) number of layer, which is a list of numbers. 
		// The length of this list dictates the number of layers, the number in each position of the list specifies the number of neurons for that layer.
		// Below <numAtts>, 5,1 specify an input layer of size numAtts, a single hidden layer of size 5 and an output layer of size 1.
		// If you want to add more hidden layers, add more numbers to the list between the input and the output layer. 
		mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, numAtts, 5, 1);
		mlp.getLearningRule().setLearningRate(0.05);
		mlp.getLearningRule().setMaxIterations(1000);
        	mlp.learn(ts);
	}

	public int classifyInstance(Instance ins) {
		double []values = new double[numAtts];

		for(int i=0;i<numAtts;i++) {
			values[i] = ins.getAttribute(i);
		}

		mlp.setInput(values);
		mlp.calculate();
		double []output = mlp.getOutput();

		//System.out.println("Raw output is "+output[0]);

		return output[0]<0.5?0:1;
	}
	
	public void printClassifier() {
		for(double weight : mlp.getWeights()) { 
			System.out.println("Weight "+weight);
		}
	}

	public int classifierClass() {
		return 0;
	}

}
