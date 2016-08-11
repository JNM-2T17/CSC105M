package neuralnet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import neuralnet.model.*;

public class Driver {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
		String curr = br.readLine();
		curr = br.readLine();

		ArrayList<Row> rows = new ArrayList<Row>();

		String[] vals = null;

		while( curr != null && curr.length() > 0 ) {
			vals = curr.split(",");
			Row r = new Row();
			for(int i = 0; i < vals.length - 1; i++) {
				if( vals[i].charAt(0) == '"' && vals[i].charAt(vals[i].length() - 1) == '"') {
					vals[i] = vals[i].substring(1,vals[i].length() - 1);
				}
				r.addCol(Double.parseDouble(vals[i]));
			}
			r.setOutput(vals[vals.length - 1].equals("Pass") ? 1.0 : 0.0);
			rows.add(r);
			curr = br.readLine();
		}	
		br.close();

		NeuralNetwork nn = new NeuralNetwork(vals.length - 1,0.01,0.4);
		// NeuralNetwork nn = new NeuralNetwork(3);
		nn.addHiddenLayer((vals.length));
		// nn.addHiddenLayer(3,0.15,0.25);
		nn.addOutput(NeuralNetwork.THRESHOLD);
		nn.coalesce();
		double[][] inputs = new double[rows.size()][];
		double[][] outputs = new double[rows.size()][];
		for(int i = 0; i < inputs.length; i++) {
			inputs[i] = rows.get(i).getData();
			outputs[i] = new double[] {
				rows.get(i).getOutput()
			};
		}
		nn.train(inputs,outputs,0.04,500);
	}
}

class Row {
	private ArrayList<Double> data;
	private double output;

	public Row() {
		data = new ArrayList<Double>();
		this.output = output;
	}

	public void addCol(double dataPoint) {
		data.add(dataPoint);
	}

	public double[] getData() {
		double[] vals = new double[data.size()];
		for(int i = 0; i < vals.length; i++) {
			vals[i] = data.get(i).doubleValue();
		}
		return vals;
	}

	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}

	public String toString() {
		String ret = "";
		for(int i = 0; i < data.size(); i++) {
			if( i > 0 ) {
				ret += ",";
			}
			ret += data.get(i);
		}
		return ret;
	}
}