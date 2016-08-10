package neuralnet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import neuralnet.model.*;

public class Driver2 {
	public static void main(String[] args) throws Exception {
		

		NeuralNetwork nn = new NeuralNetwork(1,0.3,0.3);
		// NeuralNetwork nn = new NeuralNetwork(3);
		nn.addHiddenLayer(4);
		// nn.addHiddenLayer(3,0.15,0.25);
		nn.addOutput(NeuralNetwork.SIGMOID);
		nn.coalesce();
		double[][] inputs = new double[][] {
			new double[] {
				(1 - 1) * (1.0 / 6.0)
			},
			new double[] {
				(2 - 1) * (1.0 / 6.0)
			},
			new double[] {
				(3 - 1) * (1.0 / 6.0)
			},
			new double[] {
				(4 - 1) * (1.0 / 6.0)
			},
			new double[] {
				(5 - 1) * (1.0 / 6.0)
			},
			new double[] {
				(6 - 1) * (1.0 / 6.0)
			},
			new double[] {
				(7 - 1) * (1.0 / 6.0)
			}
		};
		double[][] outputs = new double[][] {
			new double[] {
				(1 - 1) * (0.8 / 12.0) + 0.1
			},
			new double[] {
				(1 - 1) * (0.8 / 12.0) + 0.1
			},
			new double[] {
				(2 - 1) * (0.8 / 12.0) + 0.1
			},
			new double[] {
				(3 - 1) * (0.8 / 12.0) + 0.1
			},
			new double[] {
				(5 - 1) * (0.8 / 12.0) + 0.1
			},
			new double[] {
				(8 - 1) * (0.8 / 12.0) + 0.1
			},
			new double[] {
				(13 - 1) * (0.8 / 12.0) + 0.1
			}
		};
		nn.train(inputs,outputs,0.0);
	}
}