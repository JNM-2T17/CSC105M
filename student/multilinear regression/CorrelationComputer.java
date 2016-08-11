import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CorrelationComputer {
	public static final double THRESHOLD = 0.6;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
		PrintWriter pw = new PrintWriter(new FileWriter(new File(transform(args[0]))));
		String curr = br.readLine();
		String[] cols = curr.split(",");
		curr = br.readLine();
		for(String s : cols) {
			pw.print("," + s + ",");
		}
		pw.println();

		ArrayList<Row> rows = new ArrayList<Row>();
		System.out.println("GETTING INPUT");
		double[] mins = new double[cols.length];
		double[] maxs = new double[cols.length];
		for(int i = 0; i < cols.length; i++) {
			mins[i] = Double.MAX_VALUE;
			maxs[i] = -Double.MAX_VALUE;
		}

		while(curr != null && curr.length() > 0 ) {
			String[] vals = curr.split(",");
			double[] numVals = new double[vals.length];
			for(int i = 0; i < vals.length; i++) {
				if( vals[i].charAt(0) == '"' && vals[i].charAt(vals[i].length() - 1) == '"') {
					vals[i] = vals[i].substring(1,vals[i].length() - 1);
				}
				numVals[i] = Double.parseDouble(vals[i]);
				if( numVals[i] > maxs[i] ) {
					maxs[i] = numVals[i];
				} else if( numVals[i] < mins[i]) {
					mins[i] = numVals[i];
				}
			}
			rows.add(new Row(numVals));
			curr = br.readLine();
		}
		for(Row r : rows) {
			for(int i = 0; i < cols.length; i++) {
				r.vals[i] = (r.vals[i] - mins[i]) / (maxs[i] - mins[i]);
			}
		}
		double[] totals = new double[cols.length];
		double[][] prodTotals = new double[cols.length][cols.length];

		System.out.println("GETTING SUMS");
		for(Row r : rows) {
			for(int i = 0; i < cols.length; i++) {
				totals[i] += r.vals[i];
				for(int j = i; j < cols.length; j++) {
					prodTotals[i][j] += r.vals[i] * r.vals[j];
				}
			}
		}

		System.out.println("GETTING CORRELS");
		double[][] correls = new double[cols.length][cols.length];
		double aveCor = 0;
		for(int i = 0; i < cols.length; i++) {
			pw.print(cols[i]);
			for(int j = i; j < cols.length; j++) {
				double ssxy = prodTotals[i][j] - totals[i] * totals[j] * 1.0 / rows.size();
				double ssx2 = prodTotals[i][i] - totals[i] * totals[i] * 1.0 / rows.size();
				double ssy2 = prodTotals[j][j] - totals[j] * totals[j] * 1.0 / rows.size();

				correls[i][j] = correls[j][i] = ssxy / Math.sqrt(ssx2 * ssy2);
				aveCor += correls[i][j];
			}
			for(int j = 0; j < cols.length; j++) {
				pw.print("," + correls[i][j] + "," + (i == j ? "-" : (Math.abs(correls[i][j]) >= THRESHOLD ? "HIGH" : "LOW") ));
			}
			pw.println();
		}
		aveCor /= cols.length * (cols.length + 1) / 2;
		System.out.println("Average correlation: " + aveCor);


		pw.close();
		br.close();
	}

	private static String transform(String filename) {
		System.out.println(filename);
		String[] parts = filename.split("\\.");
		for(String s : parts ) {
			System.out.println(s);
		}
		String newFilename = "";
		for(int i = 0; i < parts.length - 1; i++) {
			if( i > 0 ) {
				newFilename += ".";
			}
			newFilename += parts[i];
		}
		newFilename += "-correl." + parts[parts.length - 1];
		return newFilename;
	}
}

class Row {
	public double[] vals;

	public Row(double[] vals) {
		this.vals = vals;
	}
}