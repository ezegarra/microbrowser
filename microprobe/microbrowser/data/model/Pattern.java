package microbrowser.data.model;

public class Pattern extends Node {
	public Pattern(prefuse.data.Node n) {
		
		this.id = n.getInt("id");
		this.title = n.getString("title");
		this.problem = n.getString("body");
		this.solution = n.getString("solution");
	}

	private String title;
	private String problem;
	private String solution;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}
}
