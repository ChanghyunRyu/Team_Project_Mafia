package Client_CM;

import java.util.Iterator;

import Interface_GUI.AdminDialog;
import kr.ac.konkuk.ccslab.cm.entity.CMSessionInfo;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMInterestEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;

public class CMClientHandler implements CMAppEventHandler {
	
	private Controller control;
	private String group;

	public CMClientHandler(Controller controller) {
		this.control = controller;
	}
	
	@Override
	public void processEvent(CMEvent event) {
		// TODO Auto-generated method stub

		//System.out.println("hello");
		switch(event.getType())
		{
		case CMInfo.CM_SESSION_EVENT:
			processSessionEvent(event);
			break;
		case CMInfo.CM_INTEREST_EVENT:
			processInterestEvent(event);
			break;
		case CMInfo.CM_DATA_EVENT:
			break;
		case CMInfo.CM_DUMMY_EVENT:
			processDummyEvent(event);
			break;
		case CMInfo.CM_USER_EVENT:
			break;
		case CMInfo.CM_FILE_EVENT:
			break;
		case CMInfo.CM_SNS_EVENT:
			break;
		case CMInfo.CM_MULTI_SERVER_EVENT:
			break;
		case CMInfo.CM_MQTT_EVENT:
			break;
		default:
			return;
		}
	}
   void processSessionEvent(CMEvent cme) {
		CMSessionEvent se = (CMSessionEvent)cme;

		//System.out.println("check0 : ");
		switch(se.getID()) {
		case CMSessionEvent.LOGIN_ACK:
			if(se.isValidUser() == 0) {
				//로그인 실패
				control.end_now_UI();
				AdminDialog test = new AdminDialog();
				System.err.println("Login fails");
			}
			else if(se.isValidUser() == -1) {
				//이미 로그인 중
				System.err.println("This client is already in the login-user list!");
			}
			else {
				// 로그인 성공
				System.out.println("This client successfullyy login to server");
				control.sendLoginMsg();
				control.requestsession();
			}
		case CMSessionEvent.SESSION_TALK:
			String s = se.getUserName() +" >> " + se.getTalk() + "\n";
			control.showChat(s);
			//System.out.println("check1 : " + s);
			break;
		case CMSessionEvent.RESPONSE_SESSION_INFO:
			processRESPONSE_SESSION_INFO(se);
			break;
		default:
			return;
		}
	}
   private void processRESPONSE_SESSION_INFO(CMSessionEvent se)
	{	
		control.joinsession("session1");
	}
	private void processInterestEvent(CMEvent cme) {
		CMInterestEvent ie = (CMInterestEvent) cme;
		switch(ie.getID()) {
		case CMInterestEvent.USER_TALK:
			String s = ie.getUserName() +" >> " + ie.getTalk() + "\n";
			control.showChat(s);
			//System.out.println("check1 : " + s);
			break;
		default:
			return;
		}
	}
	private void processDummyEvent(CMEvent event) {
		// TODO Auto-generated method stub
		CMDummyEvent due = (CMDummyEvent) event;
		String[] msg = due.getDummyInfo().split("#");
		System.out.println("");
		if(msg[0].equals("createRoom")) {
			control.startRoom(Integer.parseInt(msg[1]),msg[2]);
		}else if(msg[0].equals("showRoom")) {
			control.showRoomInfo(Integer.parseInt(msg[1]),msg[2],Integer.parseInt(msg[3]));
		}else if(msg[0].equals("enterRoom")) {
			if(msg[1].equals("true"))
				control.enterRoomResult(Integer.parseInt(msg[2]), msg[3], msg[4]);		
		}else if(msg[0].equals("showPlayer")) {
			control.showPlayer(msg[1],msg[2]);
		}else if(msg[0].equals("gameStart")) {
			control.gameStart(msg[1],Integer.parseInt(msg[2]));
		}else if(msg[0].equals("changeStatus")) {
			control.changeStatus(Integer.parseInt(msg[1]),Integer.parseInt(msg[2]));
		}else if(msg[0].equals("playerDead")) {
			control.setPlayerDead(msg[1]);
		}else if(msg[0].equals("roomOut")) {
			control.roomOut(msg[1]);
		}else if(msg[0].equals("ability")) {
			Boolean result = Boolean.parseBoolean(msg[1]);
			String user_ID = msg[2];
			int job = Integer.parseInt(msg[3]);
			if(result == true) {
				control.pro_ability(result,user_ID, job);
			}
			else {
				if(job != 3) {
					control.setPlayerDead(user_ID);
				}
				control.pro_ability(result,user_ID, job);
			}
		}else if(msg[0].equals("gameEnd")) {
			control.gameEnd(msg[1]);
		}else if(msg[0].equals("login")) {
			control.castLogin(msg[1],msg[2]);
		}else if(msg[0].equals("logout")) {
			control.castLogout(msg[1],msg[2]);
		}
	}

}
