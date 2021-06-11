package Interface_GUI;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Logic_Basic.Timer;

public class TimerPanel extends JPanel{
	
	ImageIcon timer_img,timer_img_w;
	JLabel imgLabel,timeLabel;
	Timer timer;
	
	public TimerPanel(Timer timer) {
		this.setBackground(Color.white);
		this.timer = timer;
		timer.setTimerPanel(this);
		this.setLayout(new GridLayout(1,2,5,5));
		timer_img = new ImageIcon("img/Clock.png");
		Image changeImg = timer_img.getImage().getScaledInstance(21,28, Image.SCALE_SMOOTH);
		timer_img = new ImageIcon(changeImg);
		
		timer_img_w = new ImageIcon("img/Clock_white.png");
		changeImg = timer_img_w.getImage().getScaledInstance(21,28, Image.SCALE_SMOOTH);
		timer_img_w = new ImageIcon(changeImg);
		
		imgLabel = new JLabel(timer_img);
		timeLabel = new JLabel(String.format("  %02d", timer.getSec()));
		this.add(imgLabel);
		this.add(timeLabel);
	}
	public void setColor(boolean isWhite) {
		if(isWhite) {
			this.setBackground(Color.white);
			imgLabel.setIcon(timer_img);
		}else {
			this.setBackground(Color.black);
			imgLabel.setIcon(timer_img_w);
		}
	}
	public void setTime() {
		timeLabel.setText(String.format("  %02d", timer.getSec()));
		if(timer.getSec() < 10){
			timeLabel.setForeground(Color.red);
		}
	}
	public void setTimer(Timer timer) {
		this.timer = timer;
		timer.setTimerPanel(this);
		this.setTime();
	}
}
