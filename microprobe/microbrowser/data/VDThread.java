package microbrowser.data;

/*data model of thread*/
public class VDThread {
	private String threadid;
	private int x;
	private int y;
	
	public VDThread() {
		
	}
	
	public VDThread(String threadid,int x,int y) {
		this.threadid = threadid;
		this.x = x;
		this.y = y;
	}
	
	public String getThreadid() {
		return threadid;
	}
	public void setThreadid(String threadid) {
		this.threadid = threadid;
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
