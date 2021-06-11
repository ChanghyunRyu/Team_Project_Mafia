package Client_CM;

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Interface_GUI.AdminDialog;
import Interface_GUI.GameFrame;
import Interface_GUI.MainFrame;
import Interface_GUI.StartFrame;
import Logic_Basic.Player;
import Logic_Basic.Room;
import Logic_Basic.Timer;
import Logic_Basic.User;
import kr.ac.konkuk.ccslab.cm.entity.CMUser;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.info.CMInteractionInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

public class Controller {
	final int start_time = 10;
	//속성
	int status; // 0-밤,1-낮,2-투표시간,3-최후변론,4-찬반투표,5-게임진행x
	private User user = null;
	private Player player = null;
	private Vector<Player> otherPlayers = new Vector<>(); 
	
	// 인터페이스
	JFrame now_UI;
	
	//CM
	private CMClientStub m_clientStub;
	private CMClientHandler m_eventHandler;
	private boolean loginsend = false;
	
	public Controller(String userID, String nickname) {
		this.status = 5;
		now_UI = new MainFrame(this);
		this.user = new User(userID,nickname,"");
		m_clientStub = new CMClientStub();
		m_eventHandler = new CMClientHandler(this);
		m_clientStub.setAppEventHandler(m_eventHandler);
		m_clientStub.startCM();
		this.m_clientStub.loginCM(userID, "testUser");
	}
	///////////////
	public Controller(String userID, String nickname, String password) {
		this.status = 5;
		this.user = new User(userID,nickname,password);
		m_clientStub = new CMClientStub();
		m_eventHandler = new CMClientHandler(this);
		m_clientStub.setAppEventHandler(m_eventHandler);
		m_clientStub.startCM();
		this.m_clientStub.loginCM(userID, password);
		now_UI = new MainFrame(this);
	}
	public void requestsession() {
		// TODO Auto-generated method stub
		boolean bRequestResult = false;
		bRequestResult = m_clientStub.requestSessionInfo();
		System.out.print("============================================\n");
		if(bRequestResult)
		{
			System.out.print("successfully sent the session-info request.\n");
		}
		else
			System.out.print("failed the session-info request!\n");
	}
	public void joinsession(String session) {
		// TODO Auto-generated method stub
		boolean bRequestResult = false;
		bRequestResult = m_clientStub.joinSession(session);
		if(bRequestResult)
		{
			System.out.print("successfully sent the session-join request. ->" + session + "\n");
		}
		else
			System.out.print("failed the session-join request!\n");
		
	}
	/////////////////////////
	public void end_now_UI() {
		now_UI.dispose();
	}
	public CMClientStub getM_clientStub() {
		return m_clientStub;
	}
	public void resetController() {
		status = 5;
		player = null;
		otherPlayers.clear();
	}
	public CMClientHandler getM_eventHandler() {
		return m_eventHandler;
	}
	public void createRoom(String name) {
		// 방 만들기 신청
		CMInteractionInfo interInfo = m_clientStub.getCMInfo().getInteractionInfo();
		CMUser me = interInfo.getMyself();
		
		CMDummyEvent createRoom = new CMDummyEvent();
		createRoom.setHandlerGroup(me.getCurrentGroup());
		createRoom.setHandlerSession(me.getCurrentSession());
		createRoom.setDummyInfo("createRoom#" + name + "#" + this.user.getNickname());
		m_clientStub.send(createRoom, "Server");
		createRoom = null;
	}
	public void IDtogroup(int room_ID) {
		String session ="session" + (room_ID+1);
		System.out.println("[session]:" + session);
		m_clientStub.leaveSession();
		m_clientStub.joinSession(session);
	}
	public void startRoom(int room_ID,String name) {
		// 방 만들기 요청 결과에 수행
		if(room_ID == 0) {
			JOptionPane.showMessageDialog(null, "방이 꽉차서 만들 수가 없습니다.", "Message", JOptionPane.PLAIN_MESSAGE); 
		}
		now_UI.dispose();
		now_UI = new StartFrame(name,this,true);
		StartFrame start = (StartFrame)now_UI;
		start.initPlayer(user.getNickname());
		player = new Player(user.getNickname(),user.getUser_ID(),true);
		IDtogroup(room_ID);
		player.setRoom_ID(room_ID);
	}
	public void showRoom() {
		// 방 입장을 위한 방 리스트 요청
		CMInteractionInfo interInfo = m_clientStub.getCMInfo().getInteractionInfo();
		CMUser me = interInfo.getMyself();
		
		CMDummyEvent showRoom = new CMDummyEvent();
		showRoom.setHandlerGroup(me.getCurrentGroup());
		showRoom.setHandlerSession(me.getCurrentSession());
		showRoom.setDummyInfo("showRoom#");
		m_clientStub.send(showRoom, "Server");
		showRoom = null;
	}
	public void enterRoom(int room_ID) {
		// 방 입장 요청
		CMInteractionInfo interInfo = m_clientStub.getCMInfo().getInteractionInfo();
		CMUser me = interInfo.getMyself();
		CMDummyEvent enterRoom = new CMDummyEvent();
		enterRoom.setHandlerGroup(me.getCurrentGroup());
		enterRoom.setHandlerSession(me.getCurrentSession());
		enterRoom.setDummyInfo("enterRoom#"+room_ID+"#"+this.user.getNickname());
		m_clientStub.send(enterRoom, "Server");
		enterRoom = null;
	}
	public void exit() {
		m_clientStub.logoutCM();
		this.sendLogoutMsg();
		this.resetController();
		this.now_UI.dispose();
		this.now_UI = null;
	}
	
	public void showRoomInfo(int room_ID, String room_name, int playerNum) {
		// 방 입장을 위한 방 리스트 출력을 위한 함수
		MainFrame main = (MainFrame)now_UI;
		Room room = new Room(room_ID,room_name);
		main.initRoom(room,playerNum);
	}
	public void enterRoomResult(int room_ID, String room_name,String players) {
		// 방 입장 결과 출력
		System.out.println("[EnterRoom]:"+ room_ID);
		String[] spit = players.split("-");
		now_UI.dispose();
		now_UI = new StartFrame(room_name,this,false);
		StartFrame start = (StartFrame)now_UI;
		player = new Player(user.getNickname(),user.getUser_ID(),false);
		start.initPlayer(player.getNickname());
		for(int i = 0; i< spit.length; i++) {
			String[] spit2 = spit[i].split(",");
			if(!this.player.getNickname().equals(spit2[0])) {
				start.initPlayer(spit2[0]);
				otherPlayers.add(new Player(spit2[0],spit2[1]));
			}		
			// 각 클라이언트가 다른 플레이어의 생존 여부만을 저장하기 위해 만든 변수
		}
		IDtogroup(room_ID);
		player.setRoom_ID(room_ID);
	}
	public void showPlayer(String user_name, String user_id) {
		// TODO 다른 플레이어가 들어왔을때 보여주기 위한 함수
		if(now_UI instanceof StartFrame) {
			if(!this.player.getNickname().equals(user_name)) {
				otherPlayers.add(new Player(user_name,user_id));
				StartFrame start = (StartFrame)now_UI;
				start.initPlayer(user_name);
			}	
		}
	}
	
	public void sendGameStart() {
		// TODO 게임 시작 요청
		CMInteractionInfo interInfo = m_clientStub.getCMInfo().getInteractionInfo();
		CMUser me = interInfo.getMyself();
		
		CMDummyEvent enterRoom = new CMDummyEvent();
		enterRoom.setHandlerGroup(me.getCurrentGroup());
		enterRoom.setHandlerSession(me.getCurrentSession());
		enterRoom.setDummyInfo("gameStart#"+this.player.getRoom_ID());
		m_clientStub.send(enterRoom, "Server");
		enterRoom = null;
	}
	
	public void sendVote(String user_id) {
		CMInteractionInfo interInfo = m_clientStub.getCMInfo().getInteractionInfo();
		CMUser me = interInfo.getMyself();
		
		CMDummyEvent vote = new CMDummyEvent();
		vote.setHandlerGroup(me.getCurrentGroup());
		vote.setHandlerSession(me.getCurrentSession());
		vote.setDummyInfo("vote#" + player.getRoom_ID() + "#" + user_id);
		m_clientStub.send(vote, "Server");
		vote = null;
	}
	
	public void sendAbility(String user_id) {
		CMInteractionInfo interInfo = m_clientStub.getCMInfo().getInteractionInfo();
		CMUser me = interInfo.getMyself();
		
		CMDummyEvent ability = new CMDummyEvent();
		ability.setHandlerGroup(me.getCurrentGroup());
		ability.setHandlerSession(me.getCurrentSession());
		
		
		ability.setDummyInfo("ability#"+ this.player.getRoom_ID() + "#" + this.player.getJob() + "#" + user_id);
		m_clientStub.send(ability, "Server");
		
		
		ability = null;
	}
	
	public void gameStart(String result,int job) {
		// TODO Auto-generated method stub
		if(result.equals("true")) {
			now_UI.dispose();
			this.player.setJob(job);
			Timer timer = new Timer(start_time,"");
			now_UI = new GameFrame(this.player,this,timer);
			GameFrame gameframe = (GameFrame)now_UI;
			gameframe.pushPlayer(this.player);
			for(int i = 0; i < otherPlayers.size(); i++) {
				if(otherPlayers.elementAt(i) == null) {break;}
				gameframe.pushPlayer(otherPlayers.elementAt(i));
			}
		}else {
			// 인원수 부족으로 실패했다는 메시지 출력
			JOptionPane.showMessageDialog(null, "인원 수가 부족하여 시작할 수 없습니다.", "Message", JOptionPane.PLAIN_MESSAGE); 
		}
	}
	
	public void sendChat(String chat, int status) {
		// TODO 채팅 전송 status는 밤인 경우 마피아들에게만 전달하기 위함
		//status가 밤이라면
			m_clientStub.chat("/g", chat);
	}
	
	public void showChat(String chat) {
		if( (status == 0) && (player.getJob() == 4)) {
			if(now_UI instanceof GameFrame) {
				((GameFrame) now_UI).inputChat(chat);
			}		
		}
		else if(status != 0) {
		if(now_UI instanceof GameFrame) {
				((GameFrame) now_UI).inputChat(chat);
		}
		}
	}
	public void changeStatus(int status,int time) {
		// TODO 게임 진행시간 변경
		this.status = status;
		if(now_UI instanceof GameFrame) {
			((GameFrame)now_UI).setStatus(status,time);
		}
	}
	public void setPlayerDead(String player_ID) {
		// TODO 플레이어 사망
		if(player_ID.equals("not_dead")) {
			if(now_UI instanceof GameFrame) {
				((GameFrame)now_UI).setPlayerDead(player_ID);
			}
			return;
		}
		if(this.player.getUserID().equals(player_ID)) {
			this.player.setLive(false);
		}else {
			for(int i = 0; i < otherPlayers.size(); i++) {
				if(otherPlayers.elementAt(i).getUserID().equals(player_ID)) {
					otherPlayers.elementAt(i).setLive(false);
				}
			}
		}
		if(now_UI instanceof GameFrame) {
			((GameFrame)now_UI).setPlayerDead(player_ID);
		}
	}
	
	public void pro_ability(boolean result, String player_ID, int job) {
		((GameFrame)now_UI).AbilityResult(result, player_ID, job);
	}
	public void sendExitGameMessage() {
		// TODO Auto-generated method stub
		CMInteractionInfo interInfo = m_clientStub.getCMInfo().getInteractionInfo();
		CMUser me = interInfo.getMyself();
		
		CMDummyEvent exit = new CMDummyEvent();
		exit.setHandlerGroup(me.getCurrentGroup());
		exit.setHandlerSession(me.getCurrentSession());
		exit.setDummyInfo("exitGame#"+this.player.getRoom_ID());
		m_clientStub.send(exit, "Server");
		exit = null;
	}
	public void sendExitRoomMessage() {
		// TODO Auto-generated method stub
		CMInteractionInfo interInfo = m_clientStub.getCMInfo().getInteractionInfo();
		CMUser me = interInfo.getMyself();
		
		CMDummyEvent exit = new CMDummyEvent();
		exit.setHandlerGroup(me.getCurrentGroup());
		exit.setHandlerSession(me.getCurrentSession());
		exit.setDummyInfo("exitRoom#"+this.player.getRoom_ID());
		m_clientStub.send(exit, "Server");
		this.resetController();
		exit = null;
	}
	public void gameEnd(String winner) {
		// TODO Auto-generated method stub
		if(now_UI instanceof GameFrame) {
			((GameFrame)now_UI).gameEnd(winner);
		}
	}
	public void returnMain() {
		// TODO Auto-generated method stub
		this.resetController();
		now_UI = new MainFrame(this);
		m_clientStub.logoutCM();
		m_clientStub = null;
		m_clientStub = new CMClientStub();
		m_eventHandler = new CMClientHandler(this);
		m_clientStub.setAppEventHandler(m_eventHandler);
		m_clientStub.startCM();
		this.m_clientStub.loginCM(user.getUser_ID(), user.getPassword());
	}
	public void castLogin(String user_ID,String user_nickname) {
		// TODO Auto-generated method stub
		if(now_UI instanceof MainFrame) {
			((MainFrame)now_UI).showLogin(user_ID, user_nickname);
		}
	}
	public void castLogout(String user_ID,String user_nickname) {
		// TODO Auto-generated method stub
		if(now_UI instanceof MainFrame) {
			((MainFrame)now_UI).showLogout(user_ID, user_nickname);
		}
	}
	public void sendLoginMsg() {
		// TODO Auto-generated method stub
		CMInteractionInfo interInfo = m_clientStub.getCMInfo().getInteractionInfo();
		CMUser me = interInfo.getMyself();
		CMDummyEvent login = new CMDummyEvent();
		login.setHandlerGroup(me.getCurrentGroup());
		login.setHandlerSession(me.getCurrentSession());
		login.setDummyInfo("login#"+this.user.getUser_ID()+"#"+this.user.getNickname()+"#"+this.loginsend);
		this.loginsend = true;
		m_clientStub.send(login, "Server");
	}
	public void sendLogoutMsg() {
		// TODO Auto-generated method stub
		CMInteractionInfo interInfo = m_clientStub.getCMInfo().getInteractionInfo();
		CMUser me = interInfo.getMyself();
		CMDummyEvent logout = new CMDummyEvent();
		logout.setHandlerGroup(me.getCurrentGroup());
		logout.setHandlerSession(me.getCurrentSession());
		logout.setDummyInfo("logout#"+this.user.getUser_ID()+"#"+this.user.getNickname());
		m_clientStub.send(logout, "Server");
	}
	public void roomOut(String player_ID) {
		// TODO Auto-generated method stub
		for(int i = 0; i < this.otherPlayers.size(); i++) {
			if(otherPlayers.elementAt(i).getUserID().equals(player_ID)) {
				otherPlayers.remove(i);
				break;
			}
		}
		if(now_UI instanceof StartFrame) {
			((StartFrame)now_UI).clearPlayerList();
			((StartFrame)now_UI).initPlayer(this.player.getNickname());
			for(int i = 0; i < this.otherPlayers.size(); i++) {
				((StartFrame)now_UI).initPlayer(otherPlayers.elementAt(i).getNickname());
			}
		}
	}
	
}
