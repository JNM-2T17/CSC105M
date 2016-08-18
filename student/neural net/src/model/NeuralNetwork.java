package neuralnet.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NeuralNetwork {
	public static final int SIGMOID = 0;
	public static final int LINEAR = 1;
	public static final int THRESHOLD = 2;
	private Layer inputLayer;
	private Layer outputLayer;
	private ArrayList<Layer> hiddenLayers;
	private double learningRate;
	private double momentum;
	private int ctr;

	public NeuralNetwork(int inputs,double learningRate,double momentum) {
		inputLayer = new Layer();
		boolean propagate = true;
		ctr = 0;
		for(int i = 0; i < inputs; i++) {
			inputLayer.addNeuron(new InputNeuron(ctr,propagate));
			propagate = false;
			ctr++;
		}
		outputLayer = new Layer();
		hiddenLayers = new ArrayList<Layer>();
		this.learningRate = learningRate;
		this.momentum = momentum;
	}

	public void addHiddenLayer(int neurons) {
		Layer l = new Layer();
		for(int i = 0; i < neurons; i++) {
			l.addNeuron(new SigmoidNeuron(ctr,false,learningRate,momentum));
			ctr++;
		}
		hiddenLayers.add(l);
	}

	public void addOutput(int type) {
		Neuron n = null;
		switch(type) {
			case SIGMOID:
				outputLayer.addNeuron(new SigmoidNeuron(ctr,true,learningRate,momentum));
				break;
			case LINEAR:
				outputLayer.addNeuron(new LinearNeuron(ctr,true,learningRate,momentum));
				break;
			case THRESHOLD:
				outputLayer.addNeuron(new ThresholdNeuron(ctr,true,learningRate,momentum));
				break;
			default:
		}
		ctr++;
	}

	public void coalesce() {
		if( hiddenLayers.size() > 0 ) {
			inputLayer.setOutput(hiddenLayers.get(0));
			for(int i = 0; i < hiddenLayers.size() - 1; i++) {
				hiddenLayers.get(i).setOutput(hiddenLayers.get(i + 1));
			}
			hiddenLayers.get(hiddenLayers.size() - 1).setOutput(outputLayer);
		} else {
			inputLayer.setOutput(outputLayer);
		}
	}

	public void setInputs(double[] inputs) {
		// System.out.print("Inputs: ");
		for(int i = 0; i < inputLayer.size(); i++) {
			inputLayer.neuron(i).setValue(inputs[i]);
			// System.out.print(inputs[i] + "\t");
		}
		// System.out.println();
	}

	public void setTargets(double[] outputs) {
		// System.out.print("Target Outputs: ");
		for(int i = 0; i < outputLayer.size(); i++) {
			outputLayer.neuron(i).setTarget(outputs[i]);
			// System.out.print(outputs[i] + "\t");
		}
		// System.out.println();
	}

	public double[] computeOutput() {
		// for(int i = 0; i < hiddenLayers.size(); i++) {
		// 	Layer l = hiddenLayers.get(i);
		// 	for(int j = 0; j < l.size(); j++) {
		// 		System.out.print(l.neuron(j).getBias() + "\t");
		// 		if( i == 0 ) {
		// 			for(int k = 0; k < inputLayer.size(); k++) {
		// 				System.out.print(l.neuron(j).getWeight(inputLayer.neuron(k)) + "\t");
		// 			}
		// 		} else {
		// 			Layer l2 = hiddenLayers.get(i - 1);
		// 			for(int k = 0; k < l2.size(); k++) {
		// 				System.out.print(l.neuron(j).getWeight(l2.neuron(k)) + "\t");
		// 			}
		// 		}
		// 		System.out.println();
		// 	}
		// 	System.out.println();
		// }

		// Layer l = outputLayer;
		// for(int j = 0; j < l.size(); j++) {
		// 	System.out.print(l.neuron(j).getBias() + "\t");
		// 	Layer l2 = hiddenLayers.get(hiddenLayers.size() - 1);
		// 	for(int k = 0; k < l2.size(); k++) {
		// 		System.out.print(l.neuron(j).getWeight(l2.neuron(k)) + "\t");
		// 	}
		// 	System.out.println();
		// }
		// System.out.println();

		double[] outputs = new double[outputLayer.size()];
		// System.out.print("Outputs: ");
		for(int i = 0; i < outputs.length; i++) {
			// System.out.println("Getting Output " + i);
			outputs[i] = outputLayer.neuron(i).getValue();
			// System.out.print(outputs[i] + "\t");
		}
		// System.out.println();
		return outputs;
	}

	public void updateNetwork() {
		for(int i = 0; i < hiddenLayers.size(); i++) {
			hiddenLayers.get(i).updateNeurons();
		}
		outputLayer.updateNeurons();
	}

	public void train(double[][] inputs, double[][] outputs, double errorThreshold, int iterations) {
		double error = Double.MAX_VALUE;
		int iter = 1;
		while( iter <= iterations && error > errorThreshold ) {
			System.out.print("Iteration #" + iter + ": ");
			error = 0;
			int tempError = 0;
			for(int i = 0; i < inputs.length; i++ ) {
				setInputs(inputs[i]);
				setTargets(outputs[i]);
				double[] out = computeOutput();
				// System.out.println("Computing error");
				for(int j = 0; j < outputLayer.size(); j++) {
					// System.out.println("Output " + j);
					error += Math.pow(outputLayer.neuron(j).target() - outputLayer.neuron(j).getValue(),2) / 2;
				}
				// System.out.println("Expected " + outputs[i][0] + "->" + (6 / 0.8 * (outputs[i][0] - 0.1) + 1) + " but got " + out[0] + "->" + (6 / 0.8 * (out[0] - 0.1) + 1) + "; Error: " + outputLayer.neuron(0).error());
				// System.out.println("2^" + (int)(6.0 * inputs[i][0] + 1) + " = " + (126.0 / 0.8 * (outputs[i][0] - 0.1) + 1));
				// System.out.println("Expected " + (outputs[i][0]) + " but got " + (out[0]) + "; Error: " + outputLayer.neuron(0).error());
				// System.out.println("Expected " + (int)(outputs[i][0] * 20.0) + " but got " + (int)(out[0] * 20.0) + "; Error: " + outputLayer.neuron(0).error());
				// System.out.print("\nExpected " + (outputs[i][0] > outputs[i][1] ? "0.0" : "1.0") + " but got " + (out[0] > out[1] ? "0.0" : "1.0") + "; Error: " + outputLayer.neuron(0).error());
				tempError += !(outputs[i][0] > outputs[i][1] ? "0.0" : "1.0").equals(out[0] > out[1] ? "0.0" : "1.0") ? 1 : 0;
				// tempError += (int)(outputs[i][0] * 20.0) != (int)(out[0] * 20.0) ? 1 : 0;
				updateNetwork();
			}
			// error /= inputs.length * outputLayer.size();
			error /= inputs.length;
			System.out.print("; Error: " + error);
			System.out.println("; Temp Error: " + tempError);
			iter++;
		}
	}

	public static NeuralNetwork loadNeuralNetwork(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		//read shit
		int hidden = Integer.parseInt(br.readLine());
		String[] parts = br.readLine().split(" ");
		double learningRate = Double.parseDouble(parts[0]);
		double momentum = Double.parseDouble(parts[1]);
		parts = br.readLine().split(" ");
		int inputLayer = Integer.parseInt(parts[0]);
		int[] hiddenLayers = new int[hidden];
		for(int i = 0; i < hidden; i++) {
			hiddenLayers[i] = Integer.parseInt(parts[i + 1]);
		}
		int outputLayer = Integer.parseInt(parts[parts.length - 1]);
		NeuralNetwork nn = new NeuralNetwork(inputLayer,learningRate,momentum);
		for(int i = 0; i < hidden; i++) {
			nn.addHiddenLayer(hiddenLayers[i]);
		}
		for(int i = 0; i < outputLayer; i++) {
			nn.addOutput(SIGMOID);
		}
		nn.coalesce();
		br.readLine();
		for(int i = 0; i < hidden; i++) {
			int inputSize = i == 0 ? inputLayer + 1 : hiddenLayers[i - 1] + 1;
			double[][] weights = new double[hiddenLayers[i]][inputSize];
			br.readLine();
			for(int j = 0; j < weights.length; j++) {
				parts = br.readLine().split(" ");
				for(int k = 0; k < weights[j].length; k++) {
					weights[j][k] = Double.parseDouble(parts[k]);
				}
			}
			nn.hiddenLayers.get(i).setWeights(weights);
			br.readLine();
		}

		double[][] weights = new double[outputLayer][hiddenLayers[hiddenLayers.length - 1] + 1];
		br.readLine();
		for(int j = 0; j < weights.length; j++) {
			parts = br.readLine().split(" ");
			for(int k = 0; k < weights[j].length; k++) {
				weights[j][k] = Double.parseDouble(parts[k]);
			}
		}
		nn.outputLayer.setWeights(weights);
		br.close();
		return nn;
	}

	public String toString() {
		String ret = hiddenLayers.size() + "\n" + learningRate + " " + 
						momentum + "\n" + inputLayer.size() + " ";
		for(int i = 0; i < hiddenLayers.size(); i++) {
			if( i > 0 ) {
				ret += " ";
			}
			ret += hiddenLayers.get(i).size();
		}
		ret += " " + outputLayer.size() + "\n\n";

		for(int i = 0; i < hiddenLayers.size(); i++) {
			if( i > 0 ) {
				ret += "\n\n";
			}
			ret += hiddenLayers.get(i).toString();
		}

		ret += "\n\n" + outputLayer.toString();
		return ret;
	}
}