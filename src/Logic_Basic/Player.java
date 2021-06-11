package Logic_Basic;

public class Player {

	// 속성
	private String nickname;
	private String userID;
	private boolean isLive = true;
	private boolean isMaster;
	private int job;
	private int room_ID = -1;
	
	public Player(String nickname,String ID,boolean isMaster) {
		this.userID = ID;
		this.nickname = nickname;
		this.isMaster = isMaster;
		this.job = 1;
	}
	public Player(String nickname,String ID) {
		this.userID = ID;
		this.nickname = nickname;
		this.isMaster = false;
		this.job = -1;
	}
	// getter setter
	public String getNickname() {
		return nickname;
	}

	public String getUserID() {
		return userID;
	}

	public boolean isLive() {
		return isLive;
	}

	public boolean isMaster() {
		return isMaster;
	}

	public int getJob() {
		return job;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	public void setJob(int job) {
		this.job = job;
	}
	
	public int getRoom_ID() {
		return room_ID;
	}
	public void setRoom_ID(int room_ID) {
		this.room_ID = room_ID;
	}
}
