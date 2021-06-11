package Client_CM;

import java.util.Random;

import Interface_GUI.AdminDialog;

public class OnlyforTest {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//testForGame();
		testForLogin();
	}
	private static void testForGame() {
		Controller test = new Controller("testUser1","testNickname1");
		Controller test2 = new Controller("testUser2","testNickname2");
		Controller test3 = new Controller("testUser3","testNickname3");
		Controller test4 = new Controller("testUser4","testNickname4");
		Controller test5 = new Controller("testUser5","testNickname5");
		Controller test6 = new Controller("testUser6","testNickname6");
		Controller test7 = new Controller("testUser7","testNickname7");
		Controller test8 = new Controller("testUser8","testNickname8");
	}
	private static void testForLogin() {
		AdminDialog test = new AdminDialog();
	}
}
