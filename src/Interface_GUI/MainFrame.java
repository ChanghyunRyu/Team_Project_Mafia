package Interface_GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Client_CM.Controller;
import Logic_Basic.Room;
import Logic_Basic.Timer;


public class MainFrame extends JFrame{
	
	Container frame = this.getContentPane();
	JPanel northPanel = new JPanel();
	JPanel centerPanel = new JPanel();
	JPanel southPanel = new JPanel();
	
	//버튼 이미지들
	ImageIcon make_room_img,room_in_img,exit_img;
	ImageIcon make_room_img_2,room_in_img_2,exit_img_2;
	
	// 컴포넌트
	JScrollPane listScroll;
	JPanel roomListPanel = new JPanel();
	
	// Attribute
	private int roomNumber = 0;
	// Controller
	Controller controller = null;
	private JTextArea anounceArea = new JTextArea(10,5);
	
	
	public MainFrame(Controller controller){
		super("Mafia");		
        this.setSize(500,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.controller = controller;
        setFrame();
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				MainFrame.this.controller.sendLogoutMsg();
				super.windowClosing(e);
			}
			
		});
	}
	private void setFrame() {
		frame.setBackground(Color.black);
		this.setNorthBeginning();
		this.setCenterBeginning();
		this.setSouthBeginning();
		frame.add(centerPanel,BorderLayout.CENTER);
	}
	private void setSouthBeginning() {
		anounceArea.setEditable(false);
		JScrollPane anounceScroll = new JScrollPane(anounceArea);
		anounceScroll.setPreferredSize(new Dimension(450,45));
		southPanel.add(anounceScroll);
		frame.add(southPanel,BorderLayout.SOUTH);
	}
	private void setNorthBeginning() {
		// nouthPanel 기본 조정
		northPanel.setBackground(Color.black);
		northPanel.setBorder(BorderFactory.createEmptyBorder(50,0,0,60));
		// 타이틀 이미지 조정
		ImageIcon title_img = new ImageIcon("img/title.png");
		Image changeImg = title_img.getImage().getScaledInstance(215,81, Image.SCALE_SMOOTH);
		title_img = new ImageIcon(changeImg);
		JLabel title = new JLabel(title_img);		
		title.setForeground(Color.red);
		
		// 혈흔 이미지 조정
		ImageIcon blood_img = new ImageIcon("img/blood.png");
		changeImg = blood_img.getImage().getScaledInstance(70,91, Image.SCALE_SMOOTH);
		blood_img = new ImageIcon(changeImg);
		JLabel blood = new JLabel(blood_img);
		
		
		northPanel.add(blood);
		northPanel.add(title);
		frame.add(northPanel,BorderLayout.NORTH);
	}
	private void setCenterBeginning() {
		centerPanel.removeAll();
		centerPanel.setLayout(new GridLayout(3,1,5,5));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(130,0,220,0));
		centerPanel.setBackground(Color.black);
		make_room_img = new ImageIcon("img/btn1.png");
		Image changeImg = make_room_img.getImage().getScaledInstance(190,30, Image.SCALE_SMOOTH);
		make_room_img = new ImageIcon(changeImg);
		room_in_img = new ImageIcon("img/btn2.png");
		changeImg = room_in_img.getImage().getScaledInstance(190,30, Image.SCALE_SMOOTH);
		room_in_img = new ImageIcon(changeImg);
		exit_img = new ImageIcon("img/btn3.png");
		changeImg = exit_img.getImage().getScaledInstance(190,30, Image.SCALE_SMOOTH);
		exit_img = new ImageIcon(changeImg);
		
		make_room_img_2 = new ImageIcon("img/btn1_2.png");
		changeImg = make_room_img_2.getImage().getScaledInstance(190,30, Image.SCALE_SMOOTH);
		make_room_img_2 = new ImageIcon(changeImg);
		room_in_img_2 = new ImageIcon("img/btn2_2.png");
		changeImg = room_in_img_2.getImage().getScaledInstance(190,30, Image.SCALE_SMOOTH);
		room_in_img_2 = new ImageIcon(changeImg);
		exit_img_2 = new ImageIcon("img/btn3_2.png");
		changeImg = exit_img_2.getImage().getScaledInstance(190,30, Image.SCALE_SMOOTH);
		exit_img_2 = new ImageIcon(changeImg);
		
		JLabel make_room_btn = new JLabel(make_room_img);
		make_room_btn.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				roomMakeDialog make = new roomMakeDialog(MainFrame.this);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseEntered(e);
				make_room_btn.setIcon(make_room_img_2);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseExited(e);
				make_room_btn.setIcon(make_room_img);
			}
			
		});
		JLabel room_in_btn = new JLabel(room_in_img);
		room_in_btn.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				MainFrame.this.setCenterRoomin();
				MainFrame.this.controller.showRoom();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseEntered(e);
				room_in_btn.setIcon(room_in_img_2);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseExited(e);
				room_in_btn.setIcon(room_in_img);
			}
			
		});
		JLabel exit_btn = new JLabel(exit_img);
		exit_btn.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				MainFrame.this.controller.exit();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseEntered(e);
				exit_btn.setIcon(exit_img_2);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseExited(e);
				exit_btn.setIcon(exit_img);
			}
			
		});
		centerPanel.add(make_room_btn);
		centerPanel.add(room_in_btn);
		centerPanel.add(exit_btn);
		this.repaint();
		this.validate();
	}
	private void setCenterRoomin() {
		centerPanel.removeAll();
		roomListPanel.removeAll();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBackground(Color.black);
		centerPanel.setBorder(BorderFactory.createEmptyBorder(50,40,100,40));
		centerPanel.setPreferredSize(new Dimension(400,420));
		JPanel cancelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		ImageIcon cancel_img = new ImageIcon("img/delete-2.png");
		Image changeImg = cancel_img.getImage().getScaledInstance(20,20, Image.SCALE_SMOOTH);
		cancel_img  = new ImageIcon(changeImg);
		JLabel cancelBtn = new JLabel(cancel_img);
		cancelBtn.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				MainFrame.this.setCenterBeginning();
			}
			
		});
		cancelPanel.setPreferredSize(new Dimension(400,27));
		cancelPanel.setBackground(Color.black);
		cancelPanel.add(cancelBtn);
		centerPanel.add(cancelPanel,BorderLayout.NORTH);
		
		roomListPanel.setLayout(new GridLayout(100,1,5,5));
		roomListPanel.setBackground(Color.black);
		listScroll = new JScrollPane(roomListPanel);
		listScroll.setBackground(Color.black);
		listScroll.setPreferredSize(new Dimension(400,400));
		centerPanel.add(listScroll,BorderLayout.CENTER);
		this.repaint();
		this.validate();
	}
	public void initRoom(Room room, int playerNum) {
		this.roomNumber++;
		RoomPanel roomPanel = new RoomPanel(this.roomNumber,room,playerNum);
		roomPanel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				controller.enterRoom(roomPanel.getRoom().getID());
			}
			
		});
		roomListPanel.add(roomPanel);
		repaint();
		validate();
	}
	public void makeRoom(String name) {
		controller.createRoom(name);
	}
	public class roomMakeDialog extends JDialog{
		
		MainFrame parent;
		private JTextField roomName = new JTextField(30);
		
		public roomMakeDialog(MainFrame parent) {
			super(parent);
			this.parent = parent;
			this.setBackground(Color.black);
			this.setSize(450,80);
			this.setLocationRelativeTo(null);
			init();
			this.setResizable(false);
			this.setVisible(true);
		}
		private void init() {
			JPanel panel = new JPanel();
			panel.setBackground(Color.black);
			panel.add(roomName);
			JButton btn = new JButton("만들기");
			btn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(roomName.getText().equals("")) {
						
					}else {
						parent.makeRoom(roomName.getText());
						dispose();
					}
				}
				
			});
			panel.add(btn);
			this.add(panel);
		}
	}
	public void showLogin(String user_id, String user_nickname) {
		// TODO Auto-generated method stub
		anounceArea.setEditable(true);
		anounceArea.append("[알림]: " + user_nickname + "( " + user_id +" )" + "님이 로그인하셨습니다.\n");
		anounceArea.setEditable(false);
	}
	public void showLogout(String user_id, String user_nickname) {
		// TODO Auto-generated method stub
		anounceArea.setEditable(true);
		anounceArea.append("[알림]: " + user_nickname + "( " + user_id +" )" + "님이 로그아웃하셨습니다.\n");
		anounceArea.setEditable(false);
	}
}
