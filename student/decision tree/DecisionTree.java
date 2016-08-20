import java.util.Map;
import java.util.ArrayList;
import java.util.Stack;

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
	private int countWrong;
	private DecisionTree[] leaves;

	public DecisionTree(String feature, int threshold) {
		this.feature = feature;
		this.threshold = threshold;
		this.isLeaf = false;
	}

	public DecisionTree(String value) {
		this.isLeaf = true;
		this.value = value;
		this.count = 0;
		this.last = this;
	}

	public DecisionTree[] getLeaves() {
		if( leaves == null ) {
			ArrayList<DecisionTree> leaves = new ArrayList<DecisionTree>();
			Stack<DecisionTree> frontier = new Stack<DecisionTree>();
			frontier.push(this);
			while(!frontier.empty()) {
				DecisionTree curr = frontier.pop();
				if( curr.isLeaf ) {
					leaves.add(curr);
				} else {
					frontier.push(curr.below);
					frontier.push(curr.above);
				}
			}
			this.leaves = leaves.toArray(new DecisionTree[0]);
		} 
		return leaves;
	}

	public int getCount() {
		if( isLeaf) {
			return count;
		} else {
			return 0;
		}
	}

	public int getWrong() {
		if( isLeaf) {
			return countWrong;
		} else {
			return 0;
		}
	}

	public void setBelow(DecisionTree below) {
		this.below = below;
		below.parent = this;
	}

	public void setAbove(DecisionTree above) {
		this.above = above;
		above.parent = this;
	}

	public void incrementCount() {
		if( isLeaf) {
			count++;
		} else {
			last.incrementCount();	
		}
	}

	public void incrementWrong() {
		if( isLeaf) {
			countWrong++;
		} else {
			last.incrementWrong();	
		}
	}

	public DecisionTree getLast() {
		return last;
	}

	public String getRule() {
		if( isLeaf ) {
			DecisionTree curr = this;
			String rule = " -> " + value;
			while(curr.parent != null) {
				rule = " ^ " + curr.parent.feature + 
						(curr == curr.parent.below ? " <= " : " > ") + 
						curr.parent.threshold + rule;
				curr = curr.parent;
			}
			return rule.substring(3);
		} else {
			return null;
		}
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