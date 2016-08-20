import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

public class DriverDT {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
		String head = br.readLine();
		String[] headers = head.split(",");
		String curr = br.readLine();

		ArrayList<RowSM> rows = new ArrayList<RowSM>();

		String[] vals = null;
		
		while( curr != null && curr.length() > 0 ) {
			vals = curr.split(",");
			RowSM r = new RowSM();
			for(int i = 0; i < vals.length - 1; i++) {
				if( vals[i].charAt(0) == '"' && vals[i].charAt(vals[i].length() - 1) == '"') {
					vals[i] = vals[i].substring(1,vals[i].length() - 1);
				}
				int val = Integer.parseInt(vals[i].replaceAll("[.][0-9]*",""));
				r.addCol(headers[i],val);
			}
			r.setOutput(vals[vals.length - 1]);
			rows.add(r);
			curr = br.readLine();
		}	
		br.close();

		DecisionTree[] dt = new DecisionTree[5];
		for(int i = 0; i < 5; i++) {
			TreeParser tp = new TreeParser();
			dt[i] = tp.parseTree("bag-" + i + "-tree.txt");
			// System.out.println("Tree # 1:\n" + dt[i]);
		}

		int pass = 0;
		int fail = 0;
		int tp = 0;
		int tn = 0;
		int fp = 0;
		int fn = 0;
		boolean[] status = new boolean[5];
		for(int i = 0; i < rows.size(); i++) {
			pass = fail = 0;
			for(int j = 0; j < 5; j++) {
				String result = dt[j].evaluate(rows.get(i).getData());
				status[j] = result.equals("Pass");
				switch(result) {
					case "Pass":
						pass++;
						break;
					case "Fail":
						fail++;
						break;
					default:	
				}
				// System.out.print(status[j] + "\t");
			}
			boolean prediction = pass >= 3;
			// System.out.println((prediction ? "Pass" : "Fail") + "\t" + rows.get(i).getOutput());
			// System.out.println(pass + " " + fail + " " + rows.get(i).getOutput() + " " + prediction);
			for(int j = 0; j < 5; j++) {
				if( rows.get(i).getOutput().equals("Pass") ? status[j] : !status[j]) {
					dt[j].incrementCount();
				} else {
					dt[j].incrementWrong();
				}
			}

			if( rows.get(i).getOutput().equals("Pass") ) {
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
		ArrayList<DecisionTree> leaves = new ArrayList<DecisionTree>();
		for(int i = 0; i < 5; i++) {
			for(DecisionTree leaf : dt[i].getLeaves() ) {
				leaves.add(leaf);
			}
		}
		Collections.sort(leaves,new Comparator<DecisionTree>() {
			public int compare(DecisionTree dt1, DecisionTree dt2) {
				return dt2.getCount() - dt1.getCount();
			}
		});
		for(int i = 0; leaves.get(i).getCount() > 10 && i < leaves.size(); i++) {
			System.out.println(leaves.get(i).getCount() + " " + 
								leaves.get(i).getWrong() + " " + leaves.get(i).getRule());
		}
	}
}

class RowSM {
	private HashMap<String,Integer> data;
	private String output;

	public RowSM() {
		data = new HashMap<String,Integer>();
		this.output = output;
	}

	public void addCol(String name, Integer dataPoint) {
		data.put(name,dataPoint);
	}

	public Map<String,Integer> getData() {
		return data;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
}