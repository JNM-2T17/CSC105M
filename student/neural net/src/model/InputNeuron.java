package neuralnet.model;

public class InputNeuron extends Neuron {
	private double inputVal;
	private boolean propagate;

	/**
	 * constructs a basic input neuron
	 */
	public InputNeuron(int id,boolean propagate) {
		super(id,false,0);
		this.propagate = propagate;
	}

	/**
	 * this method should compute for the value of the derivative of the 
	 * activation function used in {@link #activate(double)}.
	 * @return derivative of the activation function
	 */
	protected double derivative() {
		return 0;
	}

	/**
	 * returns the output value of this neuron
	 * @return output of this neuron
	 */
	public double getValue() {
		return inputVal;
	}

	/**
	 * subclasses should define how they allow setting of values
	 * @param value value to set
	 */
	public void setValue(double value) {
		this.inputVal = value;
		if(propagate) {
			resetOutput(true);
		}
	}

	/**
	 * using the linear combination of weights and inputs, this method runs the
	 * activation function on the value
	 * @param value linear combination of weights and inputs
	 * @return activated value
	 */
	protected double activate(double value) {
		return 0;
	}
}