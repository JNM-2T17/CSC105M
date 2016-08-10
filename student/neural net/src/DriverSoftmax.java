package neuralnet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import neuralnet.model.*;

public class DriverSoftmax {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
		String curr = br.readLine();
		curr = br.readLine();

		ArrayList<RowSM> rows = new ArrayList<RowSM>();

		String[] vals = null;
		double[] mins = null;
		double[] maxs = null;

		while( curr != null && curr.length() > 0 ) {
			vals = curr.split(",");
			if(mins == null && maxs == null) {
				mins = new double[vals.length];
				maxs = new double[vals.length];
				for(int i = 0; i < vals.length; i++) {
					mins[i] = Double.MAX_VALUE;
					maxs[i] = Double.MIN_VALUE;
				}				
			}
			RowSM r = new RowSM();
			for(int i = 0; i < vals.length - 1; i++) {
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
			r.setOutput(vals[vals.length - 1].equals("Pass"));
			rows.add(r);
			curr = br.readLine();
		}	
		br.close();

		NeuralNetwork nn = new NeuralNetwork(vals.length - 1,0.3,0.2);
		// NeuralNetwork nn = new NeuralNetwork(3);
		nn.addHiddenLayer((vals.length + 1) / 2);
		// nn.addHiddenLayer(3,0.15,0.25);
		nn.addOutput(NeuralNetwork.SIGMOID);
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
				rows.get(i).getOutput() ? 0.1 : 0.9,
				rows.get(i).getOutput() ? 0.9 : 0.1
			};
		}
		nn.train(inputs,outputs,0.04);
	}
}

class RowSM {
	private ArrayList<Double> data;
	private boolean output;

	public RowSM() {
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

	public boolean getOutput() {
		return output;
	}

	public void setOutput(boolean output) {
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