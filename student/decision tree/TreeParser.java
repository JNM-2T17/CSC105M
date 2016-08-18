import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TreeParser {
	private ArrayList<String> input;
	private int index;

	public static void main(String[] args) throws IOException {
		System.out.println("Intiializing Tree Parser");
		TreeParser tp = new TreeParser();
		System.out.println("About to parse tree");
		DecisionTree dt = tp.parseTree(args[0]);
		System.out.println("About to print tree");
		System.out.println(dt);
		
	} 

	public TreeParser() {
		input = new ArrayList<String>();
		index = 0;
	}

	public DecisionTree parseTree(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String curr = br.readLine();
		while(curr != null && curr.length() > 0) {
			input.add(curr);
			curr = br.readLine();
		}
		br.close();
		index = 0;
		return parseTree();
	} 

	public DecisionTree parseTree() {
		DecisionTree dt = null;
		String curr = input.get(index);
		while(curr.startsWith("|   ")) {
			curr = curr.substring(4);
		}
		// System.out.println("INDEX: " + index);
		// System.out.println("CURRENT LINE: " + curr);

		String[] parts = curr.split(" ");
		index++;
		boolean willHaveLeaf = false;
		String direction = parts[1];

		if( parts[2].endsWith(":") ) {
			willHaveLeaf = true;
			parts[2] = parts[2].substring(0,parts[2].length() - 1);
		} 
		// System.out.println(willHaveLeaf);
		dt = new DecisionTree(parts[0],Integer.parseInt(parts[2]));
		if(willHaveLeaf) {
			dt.setBelow(new DecisionTree(parts[3]));
		} else {
			dt.setBelow(parseTree());
		}
		
		// System.out.println(index + " " + input.size());
		curr = input.get(index);
		while(curr.startsWith("|   ")) {
			curr = curr.substring(4);
		}
		// System.out.println("INDEX: " + index);
		// System.out.println("CURRENT LINE: " + curr);
		parts = curr.split(" ");
		index++;
		willHaveLeaf = false;
		direction = parts[1];

		if( parts[2].endsWith(":") ) {
			willHaveLeaf = true;
			parts[2] = parts[2].substring(0,parts[2].length() - 1);
		} 
		// System.out.println(willHaveLeaf);
		if(willHaveLeaf) {
			dt.setAbove(new DecisionTree(parts[3]));
		} else {
			dt.setAbove(parseTree());
		}
		// System.out.println("-------------------------------------\n" + dt + "\n---------------------------------------\n" );
		return dt;
	}
}