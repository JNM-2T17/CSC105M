package neuralnet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import neuralnet.model.*;

public class Driver3 {
	public static void main(String[] args) throws Exception {
		

		NeuralNetwork nn = new NeuralNetwork(2,0.3,0.2);
		// NeuralNetwork nn = new NeuralNetwork(3);
		// nn.addHiddenLayer(2,0.25,0.25);
		nn.addHiddenLayer(4);
		nn.addOutput(NeuralNetwork.SIGMOID);
		nn.addOutput(NeuralNetwork.SIGMOID);
		nn.coalesce();
		double[][] inputs = new double[][] {
			new double[] {
				0,0
			},
			new double[] {
				0,1
			},
			new double[] {
				1,0
			},
			new double[] {
				1,1
			}
		};
		double[][] outputs = new double[][] {
			new double[] {
				0.1,0.9
			},
			new double[] {
				0.9,0.1
			},
			new double[] {
				0.9,0.1
			},
			new double[] {
				0.1,0.9
			}
		};
		nn.train(inputs,outputs,0.0);
	}
}