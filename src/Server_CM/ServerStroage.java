package Server_CM;

import java.util.HashMap;

import Logic_Basic.Player;
import Logic_Basic.Room;

public class ServerStroage {

	private HashMap<Integer,Room> roomList = new HashMap<>();
	private int roomNum = 0;
	///////////////////////////////////
	private HashMap<String, String> UserList = new HashMap();
	private boolean isRoomUse[] = new boolean[9];
	
	public ServerStroage(){
		for(int i = 0; i < isRoomUse.length; i++) {
			isRoomUse[i] = false;
		}
	}
	
	public int makeNewRoom(String room_name, String master_id,String master_nickname) {
		int roomID = 0; 
		for(int i = 0; i < isRoomUse.length; i++) {
			if(isRoomUse[i] == false) {
				roomID = i+1;
				isRoomUse[i] = true;
				break;
			}
		}
		if(roomID == 0)
			return 0;
		Room room = new Room(roomID,room_name);
		Player master = new Player(master_nickname,master_id,true);
		room.putPlayer(master);
		roomList.put(roomID, room);
		return roomID;
	}

	public HashMap<Integer, Room> getRoomList() {
		return roomList;
	}
	public Room getRoom(int room_id) {
		return roomList.get(room_id);
	}
	
	// 0 : 로그인, 1 : 회원가입 후 로그인, -1 : 로그인 실패
	public int UserLogin(String id, String password) {
		if(UserList.get(id) == null) {
			UserList.put(id, password);
			return 1;
		}
		else if(!UserList.get(id).equals(password)) {
			System.out.println("check : " + UserList.get(id) + " // " + password);
			return -1;
		}
		System.out.println("check11 : " + UserList.get(id) + " // " + password);
		return 0;
	}
	public void removeRoom(int room_ID) {
		isRoomUse[room_ID-1] = false;
		this.roomList.remove(room_ID);
	}
	///////////////////////////////////////
	
}
