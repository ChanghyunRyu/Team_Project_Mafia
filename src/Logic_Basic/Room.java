package Logic_Basic;

import java.util.HashMap;

public class Room {

	private int ID;
	private String name;
	
	private int masterID;
	private boolean isOn = false;
	private boolean isUse = false;
	
	private HashMap<String,Player> playerList = new HashMap<>();
	public Moderator moderator;
	
	public Room(int ID, String name) {
		this.ID = ID;
		this.name = name;
		this.moderator = new Moderator(this);
	}
	// getter, setter
	public int getID() {
		return ID;
	}
	public void putPlayer(Player Player) {
		playerList.put(Player.getUserID(), Player);
	}
	public String getName() {
		return name;
	}
	public int getMasterID() {
		return masterID;
	}
	public Player getPlayer(String player_id) {
		return playerList.get(player_id);
	}
	public boolean isOn() {
		return isOn;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMasterID(int masterID) {
		this.masterID = masterID;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}
	public int getPlayerNumber() {
		return this.playerList.size();
	}
	public HashMap<String, Player> getPlayerList() {
		return playerList;
	}
	public void removePlayer(String player_id) {
		this.playerList.remove(player_id);
	}
}
