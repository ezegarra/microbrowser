package microbrowser.data;

import java.util.ArrayList;

/*data model for pattern*/

public class VDPattern {
	private String patternId;
	private int x;
	private int y;
	
	/*locations of threads*/
	private ArrayList<Integer> threadXs = new ArrayList<Integer>();
	private ArrayList<Integer> threadYs = new ArrayList<Integer>();
	private ArrayList<String> threadIds = new ArrayList<String>();
	
	public VDPattern() {
	}
	
	public VDPattern(String id) {
		this.patternId = id;
	}
	
	public VDPattern(String id,int x,int y) {
		this.patternId = id;
		this.x = x;
		this.y = y;
	}
	
	public void addThread(VDThread thread) {
		this.threadXs.add(thread.getX());
		this.threadYs.add(thread.getY());
		this.threadIds.add(thread.getThreadid());
	}
	
	public void setXY() {
		
	}
	
	public String getPatternId() {
		return patternId;
	}
	public void setPatternId(String patternId) {
		this.patternId = patternId;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
}
