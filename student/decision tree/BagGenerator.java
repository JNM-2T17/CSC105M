import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class BagGenerator {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
		String head = br.readLine();
		String curr = br.readLine();

		ArrayList<Row> rows = new ArrayList<Row>();

		while(curr != null && curr.length() > 0 ) {
			String[] vals = curr.split(",");
			Row r = new Row();
			for(int i = 0; i < vals.length; i++) {
				if( vals[i].charAt(0) == '"' && vals[i].charAt(vals[i].length() - 1) == '"') {
					vals[i] = vals[i].substring(1,vals[i].length() - 1);
				}
				r.addCol(vals[i]);
			}
			rows.add(r);
			curr = br.readLine();
		}	
		br.close();

		int bags = Integer.parseInt(args[1]);
		for(int i = 0; i < bags; i++) {
			PrintWriter pw = new PrintWriter(new FileWriter(new File(transform(args[0],i))));
			pw.println(head);
			int size = (int)(rows.size() * Double.parseDouble(args[2]));
			for(int j = 0; j < size; j++) {
				//random with replacement
				int k = (int)(rows.size() * Math.random());
				pw.println(rows.get(k).toString());
			}

			pw.close();
		}
	}

	private static String transform(String filename,int bagNo) {
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
		newFilename += "-bag-" + bagNo + "." + parts[parts.length - 1];
		return newFilename;
	}
}

class Row {
	private ArrayList<String> data;

	public Row() {
		data = new ArrayList<String>();
	}

	public void addCol(String dataPoint) {
		data.add(dataPoint);
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