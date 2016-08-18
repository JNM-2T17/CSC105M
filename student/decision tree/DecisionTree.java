import java.util.Map;

public class DecisionTree {
	private String feature;
	private int threshold;
	private boolean isLeaf;
	private DecisionTree below;
	private DecisionTree above;
	private String value;
	private DecisionTree parent;
	private DecisionTree last;
	private int count;

	public DecisionTree(String feature, int threshold) {
		this.feature = feature;
		this.threshold = threshold;
		this.isLeaf = false;
	}

	public DecisionTree(String value) {
		this.isLeaf = true;
		this.value = value;
		this.count++;
		this.last = this;
	}

	public void setBelow(DecisionTree below) {
		this.below = below;
	}

	public void setAbove(DecisionTree above) {
		this.above = above;
	}

	public void incrementCount() {
		last.incrementCount();
	}

	public DecisionTree getLast() {
		return last;
	}

	public String evaluate(Map<String,Integer> input) {
		if( isLeaf) {
			return value;
		} else {
			Integer val = input.get(feature);
			String answer = null;
			if( val <= threshold) {
				answer = below.evaluate(input);
				last = below.getLast();
			} else {
				answer = above.evaluate(input);
				last = above.getLast();
			}
			return answer;
		}
	}

	public String toString() {
		return toString(0);
	}

	public String toString(int indent) {
		String ret = "";
		// System.out.println("-----------------------------" + indent + "---------------------------------------");
			
		if( isLeaf ) {
			return ": " + value;
		} else {
			ret += "\n";
			for(int i = 0; i < indent; i++) {
				ret += "|   ";
			}	
			ret += feature + " <= " + threshold + below.toString(indent + 1) + "\n";

			for(int i = 0; i < indent; i++) {
				ret += "|   ";
			}	
			ret += feature + " > " + threshold + above.toString(indent + 1);
			return ret;
		}
	}
}