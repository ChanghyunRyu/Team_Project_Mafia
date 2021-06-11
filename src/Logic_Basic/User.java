package Logic_Basic;

public class User {
	
	private String nickname;
	private String user_ID;
	private String password;
	
	public User(String id,String nickname,String password) {
		this.user_ID = id;
		this.nickname = nickname;
		this.password = password;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getNickname() {
		return nickname;
	}

	public String getUser_ID() {
		return user_ID;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setUser_ID(String user_ID) {
		this.user_ID = user_ID;
	}

}
