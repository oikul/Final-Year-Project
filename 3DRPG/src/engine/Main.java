package engine;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import game.Game;

public class Main {

	private static JFrame frame;
	private static JButton runButton;
	private static JLabel terrainLabel, widthLabel, heightLabel, scaleLabel, amplitudeLabel, roughnessLabel,
			octavesLabel, pOctave1Label, pOctave2Label, voronoiLabel, terrainSeedLabel, perlinOrValueLabel, treeLabel,
			rulesLabel, iterationsLabel, angleIncrementLabel, zLabel, zRandLabel, yLabel, yRandLabel, baseRadiusLabel,
			radiusDecreaseLabel, baseHeightLabel, heightDecreaseLabel, yStartLabel, treeSeedLabel;
	private static JTextField widthField, heightField, scaleField, amplitudeField, roughnessField, octavesField,
			pOctave1Field, pOctave2Field, voronoiField, terrainSeedField, rulesField, iterationsField, zField,
			zRandField, yField, yRandField, baseRadiusField, radiusDecreaseField, baseHeightField, heightDecreaseField,
			yStartField, treeSeedField;
	private static JRadioButton perlinButton, valueButton;
	private static ButtonGroup group;
	private static int width = 600, height = 600;
	private static IGameLogic game;
	private static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	private static GameEngine gameEngine;
	private static boolean vSync = true;

	public static void main(String args[]) {
		frame = new JFrame("Input Values");
		frame.setSize(width, height);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		terrainLabel = new JLabel("Terrain Options:");
		terrainLabel.setBounds(width / 100, height / 100, width / 6, height / 30);
		frame.add(terrainLabel);

		widthLabel = new JLabel("    Terrain Size (x-direction)");
		widthLabel.setBounds(width / 100, height / 100 + height / 30, width / 3, height / 30);
		frame.add(widthLabel);
		widthField = new JTextField();
		widthField.setText("256");
		widthField.setBounds(width / 100 + width / 3, height / 100 + height / 30, width / 3, height / 30);
		frame.add(widthField);

		heightLabel = new JLabel("    Terrain Size (z-direction)");
		heightLabel.setBounds(width / 100, height / 100 + 2 * height / 30, width / 3, height / 30);
		frame.add(heightLabel);
		heightField = new JTextField();
		heightField.setText("256");
		heightField.setBounds(width / 100 + width / 3, height / 100 + 2 * height / 30, width / 3, height / 30);
		frame.add(heightField);

		scaleLabel = new JLabel("    Scale");
		scaleLabel.setBounds(width / 100, height / 100 + 3 * height / 30, width / 3, height / 30);
		frame.add(scaleLabel);
		scaleField = new JTextField();
		scaleField.setText("1");
		scaleField.setBounds(width / 100 + width / 3, height / 100 + 3 * height / 30, width / 3, height / 30);
		frame.add(scaleField);

		amplitudeLabel = new JLabel("    Amplitude");
		amplitudeLabel.setBounds(width / 100, height / 100 + 4 * height / 30, width / 3, height / 30);
		frame.add(amplitudeLabel);
		amplitudeField = new JTextField();
		amplitudeField.setText("6");
		amplitudeField.setBounds(width / 100 + width / 3, height / 100 + 4 * height / 30, width / 3, height / 30);
		frame.add(amplitudeField);

		roughnessLabel = new JLabel("    Roughness");
		roughnessLabel.setBounds(width / 100, height / 100 + 5 * height / 30, width / 3, height / 30);
		frame.add(roughnessLabel);
		roughnessField = new JTextField();
		roughnessField.setText("1");
		roughnessField.setBounds(width / 100 + width / 3, height / 100 + 5 * height / 30, width / 3, height / 30);
		frame.add(roughnessField);

		octavesLabel = new JLabel("    Octave Count (for value noise)");
		octavesLabel.setBounds(width / 100, height / 100 + 6 * height / 30, width / 3, height / 30);
		frame.add(octavesLabel);
		octavesField = new JTextField();
		octavesField.setText("4");
		octavesField.setBounds(width / 100 + width / 3, height / 100 + 6 * height / 30, width / 3, height / 30);
		frame.add(octavesField);

		pOctave1Label = new JLabel("    Perlin Octave 1");
		pOctave1Label.setBounds(width / 100, height / 100 + 7 * height / 30, width / 3, height / 30);
		frame.add(pOctave1Label);
		pOctave1Field = new JTextField();
		pOctave1Field.setText("4");
		pOctave1Field.setBounds(width / 100 + width / 3, height / 100 + 7 * height / 30, width / 3, height / 30);
		frame.add(pOctave1Field);

		pOctave2Label = new JLabel("    Perlin Octave 2");
		pOctave2Label.setBounds(width / 100, height / 100 + 8 * height / 30, width / 3, height / 30);
		frame.add(pOctave2Label);
		pOctave2Field = new JTextField();
		pOctave2Field.setText("5");
		pOctave2Field.setBounds(width / 100 + width / 3, height / 100 + 8 * height / 30, width / 3, height / 30);
		frame.add(pOctave2Field);

		voronoiLabel = new JLabel("    Voronoi Graph Image Size");
		voronoiLabel.setBounds(width / 100, height / 100 + 9 * height / 30, width / 3, height / 30);
		frame.add(voronoiLabel);
		voronoiField = new JTextField();
		voronoiField.setText("4096");
		voronoiField.setBounds(width / 100 + width / 3, height / 100 + 9 * height / 30, width / 3, height / 30);
		frame.add(voronoiField);

		terrainSeedLabel = new JLabel("    Seed Value for Terrain");
		terrainSeedLabel.setBounds(width / 100, height / 100 + 10 * height / 30, width / 3, height / 30);
		frame.add(terrainSeedLabel);
		terrainSeedField = new JTextField();
		terrainSeedField.setText("0");
		terrainSeedField.setBounds(width / 100 + width / 3, height / 100 + 10 * height / 30, width / 3, height / 30);
		frame.add(terrainSeedField);

		perlinOrValueLabel = new JLabel("    Use Perlin or Value Noise?");
		perlinOrValueLabel.setBounds(width / 100, height / 100 + 11 * height / 30, width / 3, height / 30);
		frame.add(perlinOrValueLabel);
		perlinButton = new JRadioButton("Perlin", true);
		perlinButton.setBounds(width / 100 + width / 3, height / 100 + 11 * height / 30, width / 6, height / 30);
		valueButton = new JRadioButton("Value");
		valueButton.setBounds(width / 100 + width / 3 + width / 6, height / 100 + 11 * height / 30, width / 6,
				height / 30);
		group = new ButtonGroup();
		group.add(perlinButton);
		group.add(valueButton);
		frame.add(perlinButton);
		frame.add(valueButton);

		treeLabel = new JLabel("Tree Options:");
		treeLabel.setBounds(width / 100, height / 100 + 12 * height / 30, width / 3, height / 30);
		frame.add(treeLabel);

		rulesLabel = new JLabel("    Rules for L-Systems");
		rulesLabel.setBounds(width / 100, height / 100 + 13 * height / 30, width / 3, height / 30);
		frame.add(rulesLabel);
		rulesField = new JTextField();
		rulesField.setText(
				"X0.4>F+[[X]-X]-F[-FX]+X,X0.4>F*[[X]/X]/F[/FX]*X,X0.4>F/[[X]*X]*F[*FX]/X,X0.4>F-[[X]+X]+F[+FX]-X,F0.6>FF");
		rulesField.setBounds(2 * width / 9, height / 100 + 13 * height / 30, width / 2 + width / 4, height / 30);
		frame.add(rulesField);

		iterationsLabel = new JLabel("    Tree Generator Iterations");
		iterationsLabel.setBounds(width / 100, height / 100 + 14 * height / 30, width / 3, height / 30);
		frame.add(iterationsLabel);
		iterationsField = new JTextField();
		iterationsField.setText("4");
		iterationsField.setBounds(width / 100 + width / 3, height / 100 + 14 * height / 30, width / 3, height / 30);
		frame.add(iterationsField);

		angleIncrementLabel = new JLabel("    Angle Increments:");
		angleIncrementLabel.setBounds(width / 100, height / 100 + 15 * height / 30, width / 3, height / 30);
		frame.add(angleIncrementLabel);
		zLabel = new JLabel("        Z Angle Increment");
		zLabel.setBounds(width / 100, height / 100 + 16 * height / 30, width / 3, height / 30);
		frame.add(zLabel);
		zField = new JTextField();
		zField.setText("30");
		zField.setBounds(width / 100 + width / 3, height / 100 + 16 * height / 30, width / 3, height / 30);
		frame.add(zField);
		zRandLabel = new JLabel("        Z Angle Randomness");
		zRandLabel.setBounds(width / 100, height / 100 + 17 * height / 30, width / 3, height / 30);
		frame.add(zRandLabel);
		zRandField = new JTextField();
		zRandField.setText("30");
		zRandField.setBounds(width / 100 + width / 3, height / 100 + 17 * height / 30, width / 3, height / 30);
		frame.add(zRandField);
		yLabel = new JLabel("        Y Angle Increment");
		yLabel.setBounds(width / 100, height / 100 + 18 * height / 30, width / 3, height / 30);
		frame.add(yLabel);
		yField = new JTextField();
		yField.setText("30");
		yField.setBounds(width / 100 + width / 3, height / 100 + 18 * height / 30, width / 3, height / 30);
		frame.add(yField);
		yRandLabel = new JLabel("        Y Angle Randomness");
		yRandLabel.setBounds(width / 100, height / 100 + 19 * height / 30, width / 3, height / 30);
		frame.add(yRandLabel);
		yRandField = new JTextField();
		yRandField.setText("30");
		yRandField.setBounds(width / 100 + width / 3, height / 100 + 19 * height / 30, width / 3, height / 30);
		frame.add(yRandField);

		baseRadiusLabel = new JLabel("    Tree Base Radius");
		baseRadiusLabel.setBounds(width / 100, height / 100 + 20 * height / 30, width / 3, height / 30);
		frame.add(baseRadiusLabel);
		baseRadiusField = new JTextField();
		baseRadiusField.setText("0.4");
		baseRadiusField.setBounds(width / 100 + width / 3, height / 100 + 20 * height / 30, width / 3, height / 30);
		frame.add(baseRadiusField);
		radiusDecreaseLabel = new JLabel("    Tree Radius Decrease");
		radiusDecreaseLabel.setBounds(width / 100, height / 100 + 21 * height / 30, width / 3, height / 30);
		frame.add(radiusDecreaseLabel);
		radiusDecreaseField = new JTextField();
		radiusDecreaseField.setText("0.95");
		radiusDecreaseField.setBounds(width / 100 + width / 3, height / 100 + 21 * height / 30, width / 3, height / 30);
		frame.add(radiusDecreaseField);

		baseHeightLabel = new JLabel("    Tree Base Height");
		baseHeightLabel.setBounds(width / 100, height / 100 + 22 * height / 30, width / 3, height / 30);
		frame.add(baseHeightLabel);
		baseHeightField = new JTextField();
		baseHeightField.setText("0.8");
		baseHeightField.setBounds(width / 100 + width / 3, height / 100 + 22 * height / 30, width / 3, height / 30);
		frame.add(baseHeightField);
		heightDecreaseLabel = new JLabel("    Tree Height Decrease");
		heightDecreaseLabel.setBounds(width / 100, height / 100 + 23 * height / 30, width / 3, height / 30);
		frame.add(heightDecreaseLabel);
		heightDecreaseField = new JTextField();
		heightDecreaseField.setText("0.95");
		heightDecreaseField.setBounds(width / 100 + width / 3, height / 100 + 23 * height / 30, width / 3, height / 30);
		frame.add(heightDecreaseField);

		yStartLabel = new JLabel("    Starting Y Coordinate");
		yStartLabel.setBounds(width / 100, height / 100 + 24 * height / 30, width / 3, height / 30);
		frame.add(yStartLabel);
		yStartField = new JTextField();
		yStartField.setText("-3");
		yStartField.setBounds(width / 100 + width / 3, height / 100 + 24 * height / 30, width / 3, height / 30);
		frame.add(yStartField);

		treeSeedLabel = new JLabel("    Tree Seed");
		treeSeedLabel.setBounds(width / 100, height / 100 + 25 * height / 30, width / 3, height / 30);
		frame.add(treeSeedLabel);
		treeSeedField = new JTextField();
		treeSeedField.setText("0");
		treeSeedField.setBounds(width / 100 + width / 3, height / 100 + 25 * height / 30, width / 3, height / 30);
		frame.add(treeSeedField);

		runButton = new JButton("Run");
		runButton.setBounds(8 * (width / 10), 26 * (height / 30), width / 10, height / 30);
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println("run pressed");
					boolean perlinOrValue;
					if (perlinButton.isSelected()) {
						perlinOrValue = true;
					} else {
						perlinOrValue = false;
					}
					game = new Game(Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()),
							Integer.parseInt(octavesField.getText()), Integer.parseInt(pOctave1Field.getText()),
							Integer.parseInt(pOctave2Field.getText()), Integer.parseInt(voronoiField.getText()),
							Float.parseFloat(scaleField.getText()), Float.parseFloat(amplitudeField.getText()),
							Float.parseFloat(roughnessField.getText()), Long.parseLong(terrainSeedField.getText()),
							perlinOrValue, rulesField.getText(), Long.parseLong(treeSeedField.getText()),
							Integer.parseInt(iterationsField.getText()), Float.parseFloat(zField.getText()),
							Float.parseFloat(zRandField.getText()), Float.parseFloat(yField.getText()),
							Float.parseFloat(yRandField.getText()), Float.parseFloat(baseRadiusField.getText()),
							Float.parseFloat(radiusDecreaseField.getText()),
							Float.parseFloat(baseHeightField.getText()),
							Float.parseFloat(heightDecreaseField.getText()), Float.parseFloat(yStartField.getText()));
					gameEngine = new GameEngine("GAME", screen.width, screen.height, vSync, game);
					gameEngine.start();
				} catch (Exception excp) {
					excp.printStackTrace();
					System.exit(-1);
				}
			}
		});
		frame.add(runButton);

		frame.setVisible(true);

		while (frame.isVisible()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
}
