package Interface_GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Logic_Basic.Room;

public class RoomPanel extends JPanel {

	
	private JLabel indexLabel,nameLabel,peopleLabel;
	private Room  room;
	
	public RoomPanel(int index, Room room,int playerNum) {
		this.room = room;
		this.setBorder(BorderFactory.createEmptyBorder(0,1,0,1));
		this.setBackground(Color.black);
		indexLabel = new JLabel("  "+index + ".");
		indexLabel.setPreferredSize(new Dimension(30,30));
		indexLabel.setForeground(Color.white);
		nameLabel = new JLabel(room.getName());
		nameLabel.setPreferredSize(new Dimension(300,30));
		nameLabel.setForeground(Color.white);
		peopleLabel = new JLabel("                              "+playerNum+"/8");
		peopleLabel.setPreferredSize(new Dimension(40,30));
		peopleLabel.setForeground(Color.white);
		
		JPanel mainPanel = new JPanel(new GridLayout(1,3,5,5));
		mainPanel .setPreferredSize(new Dimension(370,30));
		mainPanel .setBorder(BorderFactory.createLineBorder(Color.red));
		mainPanel .setBackground(Color.black);
		mainPanel.add(indexLabel);
		mainPanel.add(nameLabel);
		mainPanel.add(peopleLabel);
		
		this.add(mainPanel);
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	
	
}
