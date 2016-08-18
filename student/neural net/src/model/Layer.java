package neuralnet.model;

import java.util.ArrayList;

public class Layer {
	private ArrayList<Neuron> neurons;

	public Layer() {
		neurons = new ArrayList<Neuron>();
	}

	public void addNeuron(Neuron n) {
		neurons.add(n);
	}

	public Neuron neuron(int i) {
		return neurons.get(i);
	}

	public int size() {
		return neurons.size();
	}

	public void setOutput(Layer l) {
		for(Neuron n1 : neurons) {
			for(int i = 0; i < l.size(); i++) {
				Neuron n2 = l.neuron(i);
				// System.out.println("Connecting " + n1 + " and " + n2);
				n1.addOutput(n2);
				n2.addInput(n1);
			}
		}
	}

	public void updateNeurons() {
		for(Neuron n : neurons) {
			n.updateWeights();
		}
	}

	public void setWeights(double[][] weights) {
		for(int i = 0; i < neurons.size() && i < weights.length; i++ ) {
			neurons.get(i).setWeights(weights[i]);
		}
	}

	public String toString() {
		String ret = neurons.size() + "\n";
		for(int i = 0; i < neurons.size(); i++) {
			if( i > 0 ) {
				ret += "\n";
			}
			ret += neurons.get(i).weightString();
		}
		return ret;
	}
}