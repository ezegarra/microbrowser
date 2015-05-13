package microbrowser.data.model;

public class Discussion extends Node {

	private String title;
	private String body;
	private prefuse.data.Node _node;
	
	public Discussion(prefuse.data.Node n) {
		
		this.id = n.getInt("id");
		this.title = n.getString("title");
		this.body = n.getString("body");
		
		setOriginalNode(n);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public prefuse.data.Node getOriginalNode() {
		return _node;
	}

	public void setOriginalNode(prefuse.data.Node _node) {
		this._node = _node;
	}
}
