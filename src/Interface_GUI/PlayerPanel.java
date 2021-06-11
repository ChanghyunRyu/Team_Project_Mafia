package Interface_GUI;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Logic_Basic.Player;

public class PlayerPanel extends JPanel {
	
	private Player player;
	private JLabel playerLabel;
	private boolean isClick = false;
	private GameFrame gameframe;
	
	public PlayerPanel(Player player,GameFrame gameframe,boolean isMe) {
		this.gameframe = gameframe;
		this.player = player;
		if(isMe)
			playerLabel = new JLabel(player.getNickname()+"(본인)");
		else
			playerLabel = new JLabel(player.getNickname());
		this.add(playerLabel);
		addAction();
	}
	private void addAction() {
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				if(isClick && player.isLive()) {
					gameframe.sendClick(PlayerPanel.this.player.getUserID());
					playerLabel.setForeground(Color.black);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseEntered(e);
				if(isClick && player.isLive()) {
					playerLabel.setForeground(Color.red);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseExited(e);
				if(isClick && player.isLive()) {
					playerLabel.setForeground(Color.black);
				}
			}
			
		});
	}
	public void setPlayerDead() {
		player.setLive(false);
		playerLabel.setForeground(Color.gray);
	}
	public String getPlayerNickname() {
		return player.getNickname();
	}
	public String getPlayerID() {
		return player.getUserID();
	}
	public void setIsClick(boolean isClick) {
		this.isClick = isClick;
	}
}
