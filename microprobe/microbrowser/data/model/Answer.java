package microbrowser.data.model;

public class Answer extends microbrowser.data.model.Node {

	private boolean accepted;
	private int score;
	private String body;
	private String createDate;
	private String owner;
	private String ownerId;

	public boolean isAccepted() {
		return accepted;
	}
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getOwner() {
		if ( owner == null || owner.trim().equals("")) {
			owner = ownerId;
		}

		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
}
