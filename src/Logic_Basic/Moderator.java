package Logic_Basic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


import java.util.Random;
import java.util.Vector;

public class Moderator {
	final int NUM = 8;
	private Vector<String> voteResult = new Vector<String>();
	private String policeDecision;
	private String doctorDecision = "";
	private String mafiatarget = "";
	Room room;
	
	public Moderator(Room room) {
		this.room = room;
	}	
	//1 : 시민, 2 : 의사, 3: 경찰, 4: 마피아
	public void assignJob(Room room, int playerNumber){
		Player[] players = new Player[playerNumber];
		int index = 0;
		HashMap<String, Player> playerList = room.getPlayerList();
		Iterator iterator = playerList.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry entry = (Entry)iterator.next();
			Player playerdummy = (Player)entry.getValue();
			players[index++] = playerdummy;
		}
		Random random = new Random();
		int policeNum = random.nextInt(playerNumber);
		players[policeNum].setJob(3);
		
		int doctorNum;
		while(true) {
			doctorNum = random.nextInt(playerNumber);
			if(doctorNum != policeNum)
				break;
		}
		players[doctorNum].setJob(2);
		
		int[] mafiaNum = new int[2];
		while(true) {
			mafiaNum[0] = random.nextInt(playerNumber);
			if(mafiaNum[0] != policeNum && mafiaNum[0] != doctorNum) {
				break;
			}
		}
		while(true) {
			mafiaNum[1] = random.nextInt(playerNumber);
			if(mafiaNum[1] != policeNum && mafiaNum[1] != doctorNum && mafiaNum[1] != mafiaNum[0]) {
				break;
			}
		}
		players[mafiaNum[0]].setJob(4);
		players[mafiaNum[1]].setJob(4);
	}
	public void putVote(String user_id) {
		voteResult.add(user_id);
	}
	public String decision() {
		Vector<Candidate> candidates = new Vector<Candidate>();
		Candidate can = null;
		votes:for(int i = 0; i < voteResult.size(); i++) {
			String target_id = voteResult.elementAt(i);
			for(int j = 0; j < candidates.size(); j++) {
				if((candidates.elementAt(j).id).equals(target_id)) {
					candidates.elementAt(j).addVotes();
					continue votes;
				}
			}
			candidates.add(new Candidate(target_id));
		}
		Collections.sort(candidates);
		voteResult.clear();
		if(candidates.size() > 1 && candidates.elementAt(0).votes == candidates.elementAt(1).votes) {
			
			return "not_dead";
		}
		return candidates.elementAt(0).id;
	}
	public String calAblibity() {
		return "";
	}
	
	public void put_mafia_target(String target_id) {
		if(mafiatarget.equals("")) {
			mafiatarget = target_id;
		}
	}
	public void put_docter_target(String target_id) {
		if(doctorDecision.equals("")) {
			doctorDecision = target_id;
		}
	}
	//1 : 시민, 2 : 의사, 3: 경찰, 4: 마피아
	public boolean resultPolice(String suspect_id, Room room) {
		HashMap<String, Player> playerList = room.getPlayerList();
		Player player = playerList.get(suspect_id);
		if(player.getJob() == 4)
			return true;
		else
			return false;
	}
	public String isGameEnd() {
		int mafianum = 0;
		int live = 0;
		HashMap<String, Player> playerList = room.getPlayerList();
        Iterator iterator = playerList.entrySet().iterator();
        while(iterator.hasNext()) {
           Entry entry = (Entry)iterator.next();
           Player playerdummy = (Player)entry.getValue();
           if(playerdummy.isLive()) {
        	   live++;
        	   if(playerdummy.getJob() == 4) {
        		   mafianum++;
        	   }
           }
        }
        if(mafianum == 0)
        	return "citizen";
        if(mafianum == live-mafianum)
        	return "mafia";
		return "not_end";
	}
	public String getMafiaResult() {
		if(mafiatarget.equals("") || mafiatarget.equals(doctorDecision)) {
			return "not_dead";
		}else {
			return mafiatarget;
		}	
	}
	public void resetAbility() {
		doctorDecision = "";
		mafiatarget = "";
	}
	public class Candidate implements Comparable<Candidate>{
		String id;
		int votes = 1;
		public Candidate(String id) {
			this.id = id;
		}
		
		public void addVotes() {
			this.votes++;
		}

		@Override
		public int compareTo(Candidate o) {
			// TODO Auto-generated method stub
			return o.votes - this.votes;
		}
	}
}
