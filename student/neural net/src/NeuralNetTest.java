package neuralnet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import neuralnet.model.*;

public class NeuralNetTest {
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
					maxs[i] = -Double.MAX_VALUE;
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

		double[][] inputs = new double[rows.size()][];
		for(int i = 0; i < inputs.length; i++) {
			inputs[i] = rows.get(i).getData();
			for(int j = 0; j < inputs[i].length; j++) {
				inputs[i][j] = (inputs[i][j] - mins[j])/(maxs[j] - mins[j]);
			}
		}
		NeuralNetwork[] nn = new NeuralNetwork[5];
		for(int i = 0; i < 5; i++) {
			nn[i] = NeuralNetwork.loadNeuralNetwork("nn-" + i + ".txt");
		}
		
		int pass = 0;
		int fail = 0;
		int tp = 0;
		int tn = 0;
		int fp = 0;
		int fn = 0;
		for(int i = 0; i < inputs.length; i++) {
			pass = fail = 0;
			for(int j = 0; pass < 3 && fail < 3 && j < 5; j++) {
				nn[j].setInputs(inputs[i]);
				double[] output = nn[j].computeOutput();
				if( output[0] > output[1] ) {
					fail++;
				} else {
					pass++;
				}
			}
			boolean prediction = pass == 3;
			// System.out.println(pass + " " + fail + " " + rows.get(i).getOutput() + " " + prediction);
			if( rows.get(i).getOutput() ) {
				if( prediction ) {
					tp++;
				} else {
					fn++;
				}
			} else {
				if( prediction ) {
					fp++;
				} else {
					tn++;
				}
			}
		}

		System.out.println("Classified as \t Pass\tFail\n" + 
						"Is Really\nPass\t\t" + tp + "\t" + fn + "\n" + 
									"Fail\t\t" + fp + "\t" + tn + "\n");
		System.out.println("Classification Accuracy: " + ((tp + tn) * 1.0 / (tp + tn + fp + fn)) + "\n" + 
							"Classification Error: " + ((fp + fn) * 1.0 / (tp + tn + fp + fn)) + "\n" + 
							"Sensitivity: " + ((tp) * 1.0 / (tp + fn)) + "\n" + 
							"Specificity: " + ((tn) * 1.0 / (tn + fp)));
	}
}