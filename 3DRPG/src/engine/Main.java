package engine;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import game.Game;

public class Main {

	private static JFrame frame;
	private static JButton runButton;
	private static int width = 600, height = 600;
	private static IGameLogic gameLogic;
	private static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	private static GameEngine gameEngine;
	private static boolean vSync = true;

	public static void main(String args[]) {
		frame = new JFrame("Input Values");
		frame.setSize(width, height);
		runButton = new JButton("Run");
		runButton.setBounds(8 * (width / 10), 17 * (height / 20), width / 10, height / 20);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(runButton);
		frame.setVisible(true);
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println("run pressed");
					gameLogic = new Game();
					gameEngine = new GameEngine("GAME", screen.width, screen.height, vSync, gameLogic);
					gameEngine.start();
				} catch (Exception excp) {
					excp.printStackTrace();
					System.exit(-1);
				}
			}
		});
		while(frame.isVisible()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
}
