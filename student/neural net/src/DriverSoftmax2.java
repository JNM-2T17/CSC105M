package neuralnet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import neuralnet.model.*;

public class DriverSoftmax2 {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
		String curr = br.readLine();
		curr = br.readLine();

		ArrayList<RowSM2> rows = new ArrayList<RowSM2>();

		String[] vals = null;
		double[] mins = null;
		double[] maxs = null;

		while( curr != null && curr.length() > 0 ) {
			vals = curr.split(",");
			if(mins == null && maxs == null) {
				mins = new double[vals.length - 3];
				maxs = new double[vals.length - 3];
				for(int i = 0; i < vals.length - 3; i++) {
					mins[i] = Double.MAX_VALUE;
					maxs[i] = -Double.MAX_VALUE;
				}				
			}
			RowSM2 r = new RowSM2();
			for(int i = 0; i < vals.length - 3; i++) {
				if( vals[i].charAt(0) == '"' && vals[i].charAt(vals[i].length() - 1) == '"') {
					vals[i] = vals[i].substring(1,vals[i].length() - 1);
				}
				double val = Double.parseDouble(vals[i]);
				r.addCol(val);
				if(val > maxs[i]) {
					maxs[i] = val;
				} else if(val < mins[i]) {
					mins[i] = val;
				}
			}
			r.setOutput(Double.parseDouble(vals[vals.length - 1]));
			rows.add(r);
			curr = br.readLine();
		}	
		for(int i = 0; i < mins.length; i++) {
			if(mins[i] == maxs[i]) {
				maxs[i]++;
			}
			// System.out.println(mins[i] + " " + maxs[i]);
		}
		br.close();

		NeuralNetwork nn = new NeuralNetwork(vals.length - 3,0.3,0.2);
		// NeuralNetwork nn = new NeuralNetwork(3);
		nn.addHiddenLayer((vals.length + 1) / 2);
		// nn.addHiddenLayer((vals.length + 1) / 2);
		// nn.addHiddenLayer(3);
		// nn.addHiddenLayer(3);
		// nn.addHiddenLayer(3,0.15,0.25);
		nn.addOutput(NeuralNetwork.SIGMOID);
		nn.coalesce();
		double[][] inputs = new double[rows.size()][];
		double[][] outputs = new double[rows.size()][];
		for(int i = 0; i < inputs.length; i++) {
			inputs[i] = rows.get(i).getData();
			for(int j = 0; j < inputs[i].length; j++) {
				inputs[i][j] = (inputs[i][j] - mins[j])/(maxs[j] - mins[j]);
			}
			outputs[i] = new double[] {
				(rows.get(i).getOutput() / 20.0)
			};
		}
		nn.train(inputs,outputs,0.00001,500);
	}
}

class RowSM2 {
	private ArrayList<Double> data;
	private double output;

	public RowSM2() {
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