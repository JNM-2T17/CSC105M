package neuralnet.model;

public class LinearNeuron extends Neuron {
	/**
	 * Constructs a new unthresholded linear neuron with the given learning 
	 * rate. It also sets whether this neuron is an output neuron.
	 * @param isOutput whether this neuron is an output neuron
	 * @param learningRate learning rate
	 */
	public LinearNeuron(int id,boolean isOutput, double learningRate) {
		super(id,isOutput,learningRate);
	}

	/**
	 * Constructs a new unthresholded linear neuron with the given learning rate
	 * and momentum. It also sets whether this neuron is an output neuron.
	 * @param isOutput whether this neuron is an output neuron
	 * @param learningRate learning rate
	 * @param momentum momentum of this neuron
	 */
	public LinearNeuron(int id,boolean isOutput,double learningRate,double momentum) {
		super(id,isOutput,learningRate,momentum);
	}

	/**
	 * this method should compute for the value of the derivative of the 
	 * activation function used in {@link #activate(double)}.
	 * @return derivative of the activation function
	 */
	protected double derivative() {
		return 1.0;
	}

	/**
	 * subclasses should define how they allow setting of values
	 * @param value value to set
	 */
	public void setValue(double value) {}

	/**
	 * using the linear combination of weights and inputs, this method runs the
	 * activation function on the value
	 * @param value linear combination of weights and inputs
	 * @return activated value
	 */
	protected double activate(double value) {
		return value;
	}
}