package Interface_GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Client_CM.Controller;
import Logic_Basic.Player;
import Logic_Basic.Timer;

public class GameFrame extends JFrame {
	
	Container frame = this.getContentPane();
	
	private JPanel northPanel = new JPanel();
	private JPanel centerPanel = new JPanel();
	private JPanel eastPanel = new JPanel();
	private JPanel southPanel = new JPanel();
	private JLabel statusLabel = new JLabel("Day 1. morning");
	private JTextArea chatArea = new JTextArea(20,40);
	private JPanel playerList = new JPanel();
	private JTextField chatfield = new JTextField(40);
	private JButton chatBtn = new JButton("전 송");
	private PlayerPanel[] players = new PlayerPanel[8];
	private int playerNum = 0;
	private JLabel jobLabel = new JLabel();
	TimerPanel timerPanel;
	
	//Attribute
	Timer timer;
	private int days = 1;
	private int status = 1;
	private Player player;
	
	// controller
	private Controller control = null;
	public GameFrame(Player player) {
		super("Mafia");	
		this.player = player;
        this.setSize(705,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.init();
        this.pushPlayer(new Player("testPlayer1","testUser1"));
        this.pushPlayer(new Player("testPlayer2","testUser2"));
        this.pushPlayer(new Player("testPlayer3","testUser3"));
        this.pushPlayer(new Player("testPlayer4","testUser4"));
        this.pushPlayer(new Player("testPlayer5","testUser5"));
        this.pushPlayer(new Player("testPlayer6","testUser6"));
        this.pushPlayer(new Player("testPlayer7","testUser7"));
        this.pushPlayer(new Player("testPlayer8","testUser8"));
        setPlayerDead("testUser1");
        this.setVisible(true);
	}
	public GameFrame(Player player, Controller control, Timer timer) {
		super("Mafia");
		this.timer = timer;
		timerPanel = new TimerPanel(this.timer);
		this.player = player;
		this.control = control;
        this.setSize(705,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.init();
        this.setVisible(true);
	}
	private void init() {
		frame.setBackground(Color.white);
		initNorth();
		initCenter();
		initEast();
		initSouth();
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				control.sendExitGameMessage();
				control.returnMain();
				super.windowClosing(e);
			}
			
		});
	}
	private void initNorth() {
		switch(this.player.getJob()) {
			case 1:
				jobLabel.setText("직업: 시민");
				chatAdminMsg("당신의 직업은 \"시민\" 입니다.");
				jobLabel.setForeground(Color.black);
				break;
			case 2:
				jobLabel.setText("직업: 의사");
				chatAdminMsg("당신의 직업은 \"의사\" 입니다.");
				jobLabel.setForeground(Color.black);
				break;
			case 3:
				jobLabel.setText("직업: 경찰");
				chatAdminMsg("당신의 직업은 \"경찰\" 입니다.");
				jobLabel.setForeground(Color.black);
				break;
			case 4:
				jobLabel.setText("직업: 마피아");
				chatAdminMsg("당신의 직업은 \"마피아\" 입니다.");
				jobLabel.setForeground(Color.red);
				break;
		}
		jobLabel.setFont(new Font("HY견고딕",Font.BOLD,16));
		statusLabel.setFont(new Font("HY견고딕",Font.BOLD,19));
		statusLabel.setForeground(new Color(80,150,220));
		northPanel.add(statusLabel);
		northPanel.setBackground(Color.white);
		northPanel.setLayout(new FlowLayout(FlowLayout.LEFT,100,10));
		northPanel.setBorder(BorderFactory.createEmptyBorder(10,50,10,10));
		northPanel.add(jobLabel);
		northPanel.add(timerPanel);
		this.add(northPanel,BorderLayout.NORTH);
		
	}
	private void initCenter() {
		JScrollPane chatScroll = new JScrollPane(chatArea);
		chatScroll.setPreferredSize(new Dimension(500,400));
		centerPanel.setBackground(Color.white);
		centerPanel.add(chatScroll);
		chatArea.setEditable(false);
		frame.add(centerPanel,BorderLayout.CENTER);
	}
	private void initEast() {
		playerList.setLayout(new GridLayout(12,1,2,2));
		JScrollPane playerScroll = new JScrollPane(playerList);
		playerScroll.setPreferredSize(new Dimension(150,400));
		eastPanel.setBackground(Color.white);
		eastPanel.add(playerScroll);
		eastPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
		frame.add(eastPanel,BorderLayout.EAST);
	}
	private void initSouth() {
		southPanel.setBackground(Color.white);
		southPanel.setBorder(BorderFactory.createEmptyBorder(0,0,50,0));
		chatfield.addKeyListener(new KeyAdapter(){

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER && GameFrame.this.player.isLive()){
					control.sendChat(chatfield.getText(),GameFrame.this.status);
					chatfield.setText("");
	            }
			}
			
		});
		southPanel.add(chatfield);
		southPanel.add(chatBtn);
		chatBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 채팅 전송
				if(GameFrame.this.player.isLive()) {
					control.sendChat(chatfield.getText(),GameFrame.this.status);
					chatfield.setText("");
				}
			}			
		});
		frame.add(southPanel,BorderLayout.SOUTH);
	}
	public void showStatus(int status, int days) {
		String statusStr = "Days " + days + ". ";
		switch(status) {
		case 0:
			statusStr += "night";
			this.changColor(false);
			break;
		default:
			statusStr += "morning";
			this.changColor(true);
			break;	
		}
		this.statusLabel.setText(statusStr);
	}
	
	public void chatAdminMsg(String str) {
		chatArea.append("[사회자]: " + str +"\n");
	}
	
	private void changColor(boolean iswhite){
		timerPanel.setColor(iswhite);
		if(iswhite) {
			northPanel.setBackground(Color.white);
			centerPanel.setBackground(Color.white);
			southPanel.setBackground(Color.white);
			eastPanel.setBackground(Color.white);
			statusLabel.setForeground(new Color(80,150,220));
			switch(this.player.getJob()) {
			case 1:
				chatBtn.setEnabled(true);
				jobLabel.setForeground(Color.black);
				break;
			case 2:
				chatBtn.setEnabled(true);
				jobLabel.setForeground(Color.black);
				break;
			case 3:
				chatBtn.setEnabled(true);
				jobLabel.setForeground(Color.black);
				break;
			case 4:
				jobLabel.setForeground(Color.red);
				break;
			}
		}else {
			northPanel.setBackground(Color.black);
			centerPanel.setBackground(Color.black);
			southPanel.setBackground(Color.black);
			eastPanel.setBackground(Color.black);
			statusLabel.setForeground(new Color(128,70,70));
			switch(this.player.getJob()) {
			case 1:
				chatBtn.setEnabled(false);
				jobLabel.setForeground(Color.white);
				break;
			case 2:
				chatBtn.setEnabled(false);
				jobLabel.setForeground(Color.white);
				break;
			case 3:
				chatBtn.setEnabled(false);
				jobLabel.setForeground(Color.white);
				break;
			case 4:
				jobLabel.setForeground(Color.red);
				break;
			}
		}
	}
	public void pushPlayer(Player player) {
		PlayerPanel set = new PlayerPanel(player,this,false);
		players[playerNum++] = set;
		playerList.add(set);
	}
	public void pushMyself(Player player) {
		PlayerPanel set = new PlayerPanel(player,this,true);
		players[playerNum++] = set;
		playerList.add(set);
	}
	public void setPlayerDead(String user_id) {
		if(user_id.equals("not_dead")) {
			chatAdminMsg("아무도 죽지 않았습니다.");
			return;
		}
		if(player.getUserID().equals(user_id)) {
			player.setLive(false);
			chatAdminMsg("플레이어 \" " + player.getNickname()+ " \"가 죽었습니다!");
			this.cantClickPlayer();
		}
		for(int i = 0; i < players.length; i++) {
			if(players[i].getPlayerID().equals(user_id)) {
				players[i].setPlayerDead();
				chatAdminMsg("플레이어 \" " + players[i].getPlayerNickname() + " \"가 죽었습니다!");
				break;
			}
		}
	}
	
	public void AbilityResult(boolean result, String content, int job) {
		if(job == 3) {
			if(result == true) {
				chatAdminMsg("플레이어 \" " + content + " \"는 마피아입니다");
			} else {
				chatAdminMsg("플레이어 \" " + content + " \"는 마피아가 아닙니다");
			}
		}
		else {
			if(result == true) {
				chatAdminMsg("의사가 플레이어를 살렸습니다");
			}
		}
	}
	public void canClickPlayer() {
		if(!this.player.isLive()) {return;}
		for(int i = 0; i < players.length; i++){
			if(players[i] != null)
				players[i].setIsClick(true);
		}
	}
	public void cantClickPlayer() {
		for(int i = 0; i < players.length; i++){
			if(players[i] != null)
				players[i].setIsClick(false);
		}
	}
	public void sendClick(String user_id) {
		// TODO 플레이어가 특정 다른 플레이어를 클릭했을을 알려줍니다. 현재의 상태(투표시간, 밤등)에 따라 Gameframe이 서버에 보내는 정보가 달라집니다. 
		System.out.println("[ClickPlayer]: "+ user_id);
		for(int i = 0; i < players.length; i++){
			players[i].setIsClick(false);
		}
			switch(status) {
				case 0:
					control.sendAbility(user_id);
					break;
				case 2:
					control.sendVote(user_id);
					break;
				default:
					break;
		}
	}
	
	//status 0 : 밤, 1 : 낮, 2 : 투표시간, 3: 최후 변론, 4 : 찬반투표
	@SuppressWarnings("deprecation")
	public void setTimer(int sec, String endEvent) {
		if(timer != null)
			timer.stop();
		timer = new Timer(sec,endEvent);
		this.timerPanel.setTimer(timer);
	}
	public void inputChat(String s) {
		chatArea.append(s);
		chatArea.setCaretPosition(chatArea.getDocument().getLength());
	}
	public void setStatus(int status,int time) {
		this.setTimer(time, "");
		if(status == 0) {
			// 밤인 경우
			chatAdminMsg("밤이 되었습니다.");
			this.status = status;
			this.showStatus(status, this.days);
			if(this.player.getJob() == 2 || this.player.getJob() == 3 || this.player.getJob() == 4)
				this.canClickPlayer();
		}else if(status == 1) {
			// 낮인 경우
			chatAdminMsg("낮이 되었습니다.");
			this.status = status;
			this.showStatus(status, ++this.days);
			this.cantClickPlayer();
		}else if(status == 2) {
			// 투표시간인 경우
			chatAdminMsg("투표시간이 되었습니다.");
			this.status = status;
			this.showStatus(status, this.days);
			this.canClickPlayer();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Player testplayer = new Player("nickname","");
		testplayer.setJob(1);
		GameFrame test = new GameFrame(testplayer);
		test.setStatus(1,20);

	}
	public void gameEnd(String winner) {
		// TODO Auto-generated method stub
		if(winner.equals("mafia")) {
			chatAdminMsg("게임의 승자는 마피아입니다.");
			if(player.getJob() == 4) {
				chatAdminMsg("축하합니다! 승리하였습니다.");
			}else {
				chatAdminMsg("아쉽게도 패배하였습니다.");
			}
		}else if(winner.equals("citizen")) {
			chatAdminMsg("게임의 승자는 시민들입니다.");
			if(!(player.getJob() == 4)) {
				chatAdminMsg("축하합니다! 승리하였습니다.");
			}else {
				chatAdminMsg("아쉽게도 패배하였습니다.");
			}
		}
		
		
	}
}
