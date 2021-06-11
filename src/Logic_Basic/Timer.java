package Logic_Basic;

import Interface_GUI.TimerPanel;
import Server_CM.CMWinServer;

public class Timer extends Thread{
	
	private String endEvent;
	private int sec;
	private TimerPanel panel;
	CMWinServer server;
	
	public Timer(int sec, String endEvent) {
		this.endEvent = endEvent;
		this.sec = sec;
		this.start();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(this.sec >= 0) {
			if(panel != null) {
				panel.setTime();
			}
			this.sec--;
			if(this.sec == 0) {
				String[] event = endEvent.split("#");
				switch(event[0]) {
				case "chgstatus":
					if(this.server != null) {
						server.changeStatus(Integer.parseInt(event[1]),Integer.parseInt(event[2]),Integer.parseInt(event[3]));
					}
					break;
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public int getSec() {
		return this.sec;
	}
	public void setTimerPanel(TimerPanel panel) {
		this.panel = panel;
	}
	public void setServer(CMWinServer server) {
		this.server = server;
	}
}
