package Server_CM;
import java.util.Iterator;
import java.io.*;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMFileEvent;
import kr.ac.konkuk.ccslab.cm.event.CMInterestEvent;
import kr.ac.konkuk.ccslab.cm.event.CMMultiServerEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSNSEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.CMUserEvent;
import kr.ac.konkuk.ccslab.cm.event.CMUserEventField;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.event.mqttevent.CMMqttEvent;
import kr.ac.konkuk.ccslab.cm.event.mqttevent.CMMqttEventCONNECT;
import kr.ac.konkuk.ccslab.cm.event.mqttevent.CMMqttEventDISCONNECT;
import kr.ac.konkuk.ccslab.cm.event.mqttevent.CMMqttEventPUBACK;
import kr.ac.konkuk.ccslab.cm.event.mqttevent.CMMqttEventPUBCOMP;
import kr.ac.konkuk.ccslab.cm.event.mqttevent.CMMqttEventPUBLISH;
import kr.ac.konkuk.ccslab.cm.event.mqttevent.CMMqttEventPUBREC;
import kr.ac.konkuk.ccslab.cm.event.mqttevent.CMMqttEventPUBREL;
import kr.ac.konkuk.ccslab.cm.event.mqttevent.CMMqttEventSUBSCRIBE;
import kr.ac.konkuk.ccslab.cm.event.mqttevent.CMMqttEventUNSUBSCRIBE;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMFileTransferInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.manager.CMDBManager;
import kr.ac.konkuk.ccslab.cm.manager.CMFileTransferManager;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class CMWinServerEventHandler implements CMAppEventHandler {
	private CMWinServer m_server;
	private CMServerStub m_serverStub;
	private int m_nCheckCount;	// for internal forwarding simulation
	private boolean m_bDistFileProc;	// for distributed file processing

	// information for csc_ftp
	private boolean m_bStartCSCFTPSession;
	private String m_strFileSender;
	private String m_strFileReceiver;
	private int m_nTotalNumFilesPerSession;
	private int m_nCurNumFilesPerSession;
	
	// 새로 넣은 것들
	private int roomNum = 0;
	
	public CMWinServerEventHandler(CMServerStub serverStub, CMWinServer server)
	{
		m_server = server;
		m_serverStub = serverStub;
		m_nCheckCount = 0;
		m_bDistFileProc = false;
		
		m_bStartCSCFTPSession = false;
		m_strFileSender = null;
		m_strFileReceiver = null;
		m_nTotalNumFilesPerSession = 0;
		m_nCurNumFilesPerSession = 0;
	}
	
	@Override
	public void processEvent(CMEvent cme) {
		// TODO Auto-generated method stub
		switch(cme.getType())
		{
		case CMInfo.CM_SESSION_EVENT:
			processSessionEvent(cme);
			break;
		case CMInfo.CM_INTEREST_EVENT:
			processInterestEvent(cme);
			break;
		case CMInfo.CM_DUMMY_EVENT:
			processDummyEvent(cme);
			break;
		case CMInfo.CM_USER_EVENT:
		//	processUserEvent(cme);
			break;
		case CMInfo.CM_FILE_EVENT:
		//	processFileEvent(cme);
			break;
		case CMInfo.CM_SNS_EVENT:
		//	processSNSEvent(cme);
			break;
		case CMInfo.CM_MULTI_SERVER_EVENT:
		//	processMultiServerEvent(cme);
			break;
		case CMInfo.CM_MQTT_EVENT:
		//	processMqttEvent(cme);
			break;
		default:
			return;
		}
	}
	
	private void processSessionEvent(CMEvent cme)
	{
		CMConfigurationInfo confInfo = m_serverStub.getCMInfo().getConfigurationInfo();
		CMSessionEvent se = (CMSessionEvent) cme;
		///////////////////////////////////////
		switch(se.getID())
		{
		case CMSessionEvent.LOGIN:
			System.out.println("check1 : " + se.getPassword());
			
			int login_info = m_server.get_login_info(se.getUserName(), se.getPassword());
			
			if(login_info == -1) {
				printMessage("[PassWord Wrong]\n");
				m_serverStub.replyEvent(cme, 0);
			}
			else if(login_info == 1) {
				printMessage("1["+se.getUserName()+"] requests sign up & login.\n");
			}
			else {
				printMessage("1["+se.getUserName()+"] requests login.\n");
				m_serverStub.replyEvent(cme, 1);
			}
			break;
			///////////////////////////////
		case CMSessionEvent.LOGOUT:
			//System.out.println("["+se.getUserName()+"] logs out.");
			printMessage("["+se.getUserName()+"] logs out.\n");
			break;
		case CMSessionEvent.REQUEST_SESSION_INFO:
			//System.out.println("["+se.getUserName()+"] requests session information.");
			printMessage("["+se.getUserName()+"] requests session information.\n");
			break;
		case CMSessionEvent.CHANGE_SESSION:
			//System.out.println("["+se.getUserName()+"] changes to session("+se.getSessionName()+").");
			printMessage("["+se.getUserName()+"] changes to session("+se.getSessionName()+").\n");
			break;
		case CMSessionEvent.JOIN_SESSION:
			//System.out.println("["+se.getUserName()+"] requests to join session("+se.getSessionName()+").");
			printMessage("["+se.getUserName()+"] requests to join session("+se.getSessionName()+").\n");
			break;
		case CMSessionEvent.LEAVE_SESSION:
			//System.out.println("["+se.getUserName()+"] leaves a session("+se.getSessionName()+").");
			printMessage("["+se.getUserName()+"] leaves a session("+se.getSessionName()+").\n");
			break;
		case CMSessionEvent.ADD_NONBLOCK_SOCKET_CHANNEL:
			//System.out.println("["+se.getChannelName()+"] request to add SocketChannel with index("
			//		+se.getChannelNum()+").");
			printMessage("["+se.getChannelName()+"] request to add a nonblocking SocketChannel with key("
			+se.getChannelNum()+").\n");
			break;
		case CMSessionEvent.REGISTER_USER:
			//System.out.println("User registration requested by user["+se.getUserName()+"].");
			printMessage("User registration requested by user["+se.getUserName()+"].\n");
			break;
		case CMSessionEvent.DEREGISTER_USER:
			//System.out.println("User deregistration requested by user["+se.getUserName()+"].");
			printMessage("User deregistration requested by user["+se.getUserName()+"].\n");
			break;
		case CMSessionEvent.FIND_REGISTERED_USER:
			//System.out.println("User profile requested for user["+se.getUserName()+"].");
			printMessage("User profile requested for user["+se.getUserName()+"].\n");
			break;
		case CMSessionEvent.UNEXPECTED_SERVER_DISCONNECTION:
			m_server.printStyledMessage("Unexpected disconnection from ["
					+se.getChannelName()+"] with key["+se.getChannelNum()+"]!\n", "bold");
			break;
		case CMSessionEvent.INTENTIONALLY_DISCONNECT:
			m_server.printStyledMessage("Intentionally disconnected all channels from ["
					+se.getChannelName()+"]!\n", "bold");
			break;
		default:
			return;
		}
	}
	
	private void processInterestEvent(CMEvent cme)
	{
		CMInterestEvent ie = (CMInterestEvent) cme;
		switch(ie.getID())
		{
		case CMInterestEvent.USER_ENTER:
			//System.out.println("["+ie.getUserName()+"] enters group("+ie.getCurrentGroup()+") in session("
			//		+ie.getHandlerSession()+").");
			printMessage("["+ie.getUserName()+"] enters group("+ie.getCurrentGroup()+") in session("
					+ie.getHandlerSession()+").\n");
			break;
		case CMInterestEvent.USER_LEAVE:
			//System.out.println("["+ie.getUserName()+"] leaves group("+ie.getHandlerGroup()+") in session("
			//		+ie.getHandlerSession()+").");
			printMessage("["+ie.getUserName()+"] leaves group("+ie.getHandlerGroup()+") in session("
					+ie.getHandlerSession()+").\n");
			break;
		case CMInterestEvent.USER_TALK:
			//System.out.println("("+ie.getHandlerSession()+", "+ie.getHandlerGroup()+")");
			printMessage("("+ie.getHandlerSession()+", "+ie.getHandlerGroup()+")\n");
			//System.out.println("<"+ie.getUserName()+">: "+ie.getTalk());
			printMessage("<"+ie.getUserName()+">: "+ie.getTalk()+"\n");
			break;
		default:
			return;
		}
	}
	
	private void processDummyEvent(CMEvent cme)
	{
		CMDummyEvent due = (CMDummyEvent) cme;
		//System.out.println("session("+due.getHandlerSession()+"), group("+due.getHandlerGroup()+")");
		printMessage("session("+due.getHandlerSession()+"), group("+due.getHandlerGroup()+")\n");
		//System.out.println("dummy msg: "+due.getDummyInfo());
		printMessage("["+due.getSender()+"] sent a dummy msg: "+due.getDummyInfo()+"\n");
		
		String sender = due.getSender();
		String group = due.getHandlerGroup();
		String session = due.getHandlerSession();
		
		String[] msg = due.getDummyInfo().split("#");
		if(msg[0].equals("createRoom")) {
			// room 정보 저장 및 아이디,방이름을 전달합니다.
			m_server.makeRoom(sender,group,session,msg[1],msg[2]);
		}else if(msg[0].equals("showRoom")) {
			m_server.sendRoomInfo(sender,group,session);
		}else if(msg[0].equals("enterRoom")) {
			m_server.enterRoomResult(sender,group,session,Integer.parseInt(msg[1]),msg[2]);
		}else if(msg[0].equals("gameStart")) {
			m_server.gameStart(sender,group,session,Integer.parseInt(msg[1]));
		}else if(msg[0].equals("exitGame")) {
			m_server.castPlayerDead(Integer.parseInt(msg[1]),sender);
		}else if(msg[0].equals("exitRoom")) {
			m_server.castRoomout(Integer.parseInt(msg[1]),sender);
		}else if(msg[0].equals("vote")) {
			m_server.put_vote(Integer.parseInt(msg[1]),msg[2]);
		}else if(msg[0].equals("ability")) {
			m_server.process_ability(sender,group,session,Integer.parseInt(msg[1]),Integer.parseInt(msg[2]),msg[3]);
		}else if(msg[0].equals("login")) {
			if(msg[3].equals("false"))
				m_server.sendLoginAnounce(msg[1],msg[2]);
		}else if(msg[0].equals("logout")) {
				m_server.sendLogoutAnounce(msg[1],msg[2]);
		}
		return;
	}
	
	private void printMessage(String strText)
	{
		/*
		m_outTextArea.append(strText);
		m_outTextArea.setCaretPosition(m_outTextArea.getDocument().getLength());
		*/
		m_server.printMessage(strText);
	}
	
	/*
	private void setMessage(String strText)
	{
		m_outTextArea.setText(strText);
		m_outTextArea.setCaretPosition(m_outTextArea.getDocument().getLength());
	}
	*/

}
