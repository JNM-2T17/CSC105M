package neuralnet.model;

import java.util.ArrayList;

/**
 * This class models a generic neuron
 * @author Austin Fernandez
 */
public abstract class Neuron {
	private int id;
	private ArrayList<Neuron> inputs;
	private ArrayList<Neuron> outputs;
	private ArrayList<Double> weights;
	private ArrayList<Double> delta;
	private boolean hasValue;
	private boolean hasError;
	private double target;
	private double learningRate;
	private boolean isOutput;
	private double error;
	private double value;
	private double momentum;
	
	/**
	 * Constructs a new neuron with the given learning rate. It 
	 * also sets whether this neuron is an output neuron.
	 * @param isOutput whether this neuron is an output neuron
	 * @param learningRate learning rate
	 */
	public Neuron(int id, boolean isOutput, double learningRate) {
		initNeuron();
		this.id = id;
		this.isOutput = isOutput;
		this.learningRate = learningRate;
	}

	/**
	 * Constructs a new neuron with the given learning rate and momentum. It 
	 * also sets whether this neuron is an output neuron.
	 * @param isOutput whether this neuron is an output neuron
	 * @param learningRate learning rate
	 * @param momentum momentum of this neuron
	 */
	public Neuron(int id,boolean isOutput,double learningRate,double momentum) {
		initNeuron();
		this.id = id;
		this.isOutput = isOutput;
		this.learningRate = learningRate;
		this.momentum = momentum;
	}

	/**
	 * initializes the values of this neuron
	 */
	private void initNeuron() {
		inputs = new ArrayList<Neuron>();
		outputs = new ArrayList<Neuron>();
		weights = new ArrayList<Double>();
		delta = new ArrayList<Double>();
		addWeight();
		target = 0;
		isOutput = false;
		this.learningRate = 0.05;
		this.momentum = 0;
	}

	/**
	 * forces this neuron to recompute the output value;
	 */
	public void resetOutput(boolean propagate) {
		hasValue = false;
		hasError = false;
		if( propagate ) {
			propagate = true;
			for(Neuron n : outputs ) {
				n.resetOutput(propagate);
				propagate = false;
			}
		}
	}

	/**
	 * returns the id of this neuron
	 * @return the id of this neuron
	 */
	public int id() {
		return id;
	}

	/**
	 * sets the id of this neuron
	 * @param id new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * returns the target output of this neuron
	 * @return the target output of this neuron
	 */
	public double target() {
		return target;
	}

	/**
	 * sets the target output of this neuron
	 * @param target the target output of this neuron
	 */
	public void setTarget(double target) {
		if( isOutput) {
			this.target = target;
		}
	}

	/**
	 * returns the momentum of this neuron
	 * @return the momentum of this neuron
	 */
	public double momentum() {
		return momentum;
	}

	/**
	 * sets the momentum of this neuron
	 * @param momentum the momentum of this neuron
	 */
	public void setMomentum(double momentum) {
		this.momentum = momentum;
	}

	/**
	 * returns the learning rate of this neuron
	 * @return the learning rate of this neuron
	 */
	public double learningRate() {
		return learningRate;
	}

	/**
	 * sets the learning rate of this neuron
	 * @param learningRate the learning rate of this neuron
	 */
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	/**
	 * returns whether this neuron is an output neuron
	 * @return whether this neuron is an output neuron
	 */
	public boolean isOutput() {
		return isOutput;
	}

	/**
	 * sets whether this neuron is an output neuron
	 * @param isOutput whether this neuron is an output neuron
	 */
	public void setIsOutput(boolean isOutput) {
		this.isOutput = isOutput;
	}

	/**
	 * computes the error of this neuron
	 * @return error of this neuron
	 */
	public double error() {
		// System.out.println("Error Node " + id + " " + hasError + " " + hasValue);
		if( !hasError ) {
			// String errorComp = "";
			if( isOutput ) {
				// errorComp += ("Delta " + id + " = (" + target + " - " + value + ")");
				error = (target - value);
			} else {
				error = 0;
				// System.out.println(outputs.size());
				// errorComp += ("Delta " + id + " = ");
				// int i = 0;
				for(Neuron n : outputs) {
					// System.out.println(n.getWeight(this));
					// if( i > 0 ) {
						// errorComp += (" + ");
					// }
					// errorComp += (n.error() + " * " + n.getWeight(this));
					// i++;
					error += n.error() * n.getWeight(this);
				}
				// errorComp += (" = " + error);
			}
			error *= derivative();
			// errorComp += (" * " + value + " * (1 - " + value + ") = " + error);
			// System.out.println(errorComp);
			hasError = true;
		}
		// System.out.println("Exit error node " + id);
		return error;
	}

	/**
	 * updates the weights for this neuron considering the output of the output
	 * neurons
	 */
	public void updateWeights() {
		// System.out.println("Updating weights");
		// System.out.print("Node " + id + " delta = " + learningRate + " * " + 
		// 						error() + " * " + "1" + 
		// 						" + " + momentum + " * " + delta.get(0));
		delta.set(0,learningRate * error() * 1 + momentum * delta.get(0));
		// System.out.print(" = " + delta.get(0) + "; Weight = " + 
		// 					weights.get(0) + " + " + delta.get(0) + " = ");
		weights.set(0,weights.get(0) + delta.get(0));
		// System.out.println(weights.get(0));
		// System.out.println("change: " + delta.get(0));
		for(int i = 0; i < inputs.size(); i++) {
			// System.out.print("Node " + id + " delta = " + learningRate + " * " + 
			// 					error() + " * " + inputs.get(i).getValue() + 
			// 					" + " + momentum + " * " + delta.get(i + 1));
			delta.set(i + 1, learningRate * error() * inputs.get(i).getValue() + momentum * delta.get(i + 1));
			// System.out.print(" = " + delta.get(i + 1) + "; Weight = " + 
			// 				weights.get(i + 1) + " + " + delta.get(i + 1) + " = ");
			weights.set(i + 1,weights.get(i + 1) + delta.get(i + 1));
			// System.out.println(weights.get(i + 1));
			// System.out.println("change: " + delta.get(i + 1));
		}
	}

	/**
	 * this method should compute for the value of the derivative of the 
	 * activation function used in {@link #activate(double)}.
	 * @return derivative of the activation function
	 */
	protected abstract double derivative();

	/**
	 * adds an input neuron to this neuron
	 * @param n neuron to add
	 */
	public void addInput(Neuron n) {
		inputs.add(n);
		addWeight();
	}

	/**
	 * returns the bias for this neuron
	 */
	public double getBias() {
		return weights.get(0);
	}

	/**
	 * returns the weight for the line connecting the output of the given neuron
	 * to the input of this neuron
	 */
	public double getWeight(Neuron n) {
		for(int i = 0; i < inputs.size(); i++) {
			if( inputs.get(i).id() == n.id() ) {
				return weights.get(i + 1);
			}
		}
		return 0;
	}

	/**
	 * Adds a neuron to this neuron's output set.
	 * @param n neuron to add to this neuron's output set
	 */
	public void addOutput(Neuron n) {
		outputs.add(n);
	}

	/**
	 * computes for the linear combination of weights and inputs and stores in 
	 * value
	 */
	private void computeLinear() {
		// String str = "Neuron #" + id + ("Computing Linear: " + weights.get(0));
		value = weights.get(0);
		for(int i = 0; i < inputs.size(); i++) {
			// str += (" + " + inputs.get(i).getValue() + " * " + weights.get(i + 1));
			value += inputs.get(i).getValue() * weights.get(i + 1);
		}
		// System.out.println(str + " = " + value);
	}

	/**
	 * returns the output value of this neuron
	 * @return output of this neuron
	 */
	public double getValue() {
		// System.out.println("Value Node " + id + " " + hasValue);
		if( !hasValue ) {
			computeLinear();
			// System.out.print("Activating f(" + value + ") = ");
			value = activate(value);
			// System.out.println(value);
			hasValue = true;
			// hasError = false;
			// error();
			boolean propagate = true;
			for(Neuron n : outputs) {
				n.resetOutput(propagate);
				propagate = false;
			}
		}
		// System.out.println("Exit value node " + id);
		return value;
	}

	/**
	 * subclasses should define how they allow setting of values
	 * @param value value to set
	 */
	public abstract void setValue(double value);

	/**
	 * using the linear combination of weights and inputs, this method runs the
	 * activation function on the value
	 * @param value linear combination of weights and inputs
	 * @return activated value
	 */
	protected abstract double activate(double value);

	/**
	 * adds a weight and a corresponding delta to this neuron for a neuron
	 */
	private void addWeight() {
		weights.add(Math.random() * 0.2 - 0.1);
		delta.add(0.0);
		// System.out.println(weights.get(weights.size() - 1));
	}

	public String toString() {
		return "Neuron #" + id;
	}
}