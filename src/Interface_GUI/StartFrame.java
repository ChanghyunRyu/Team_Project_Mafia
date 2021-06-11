package Interface_GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Client_CM.Controller;

public class StartFrame extends JFrame{
	
	private Controller controller = null;
	private Container frame = this.getContentPane();
	
	private JPanel centerPanel = new JPanel();
	private JPanel northPanel = new JPanel();
	private JTextArea playerList = new JTextArea(30,8);
	
	ImageIcon start_img,start_img_2;

	public StartFrame(String roomname,Controller controller,boolean isMaster){
		super(roomname);		
        this.setSize(250,400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.controller = controller;
        init(isMaster);
        this.setVisible(true);
     
	}
	private void init(boolean isMaster) {
		JScrollPane playerScroll = new JScrollPane(playerList);
		playerScroll.setPreferredSize(new Dimension(200,300));
		centerPanel.setBackground(new Color(123,78,78));
		centerPanel.add(playerScroll);
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
		playerList.setEditable(false);
		frame.add(centerPanel,BorderLayout.CENTER);
		if(isMaster) {initStartBtn();}
		playerList.append("====== 플레이어 리스트 =====\n");
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				controller.sendExitRoomMessage();
				controller.returnMain();
				super.windowClosing(e);
			}
			
		});
	}
	public void initStartBtn() {
		start_img = new ImageIcon("img/gamestart.png");
		Image changeImg = start_img.getImage().getScaledInstance(190,30, Image.SCALE_SMOOTH);
		start_img = new ImageIcon(changeImg);
		
		start_img_2 = new ImageIcon("img/gamestart_2.png");
		changeImg = start_img_2.getImage().getScaledInstance(190,30, Image.SCALE_SMOOTH);
		start_img_2 = new ImageIcon(changeImg);
		
		JLabel start_btn = new JLabel(start_img);
		start_btn.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				controller.sendGameStart();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseEntered(e);
				start_btn.setIcon(start_img_2);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseExited(e);
				start_btn.setIcon(start_img);
			}
			
		});
		northPanel.setBackground(new Color(123,78,78));
		northPanel.add(start_btn);
		frame.add(northPanel,BorderLayout.SOUTH);
	}
	public void clearPlayerList() {
		this.playerList.setText("====== 플레이어 리스트 =====\n");
	}
	public void initPlayer(String username) {
		playerList.append(" "+username+"\n");
	}
}
