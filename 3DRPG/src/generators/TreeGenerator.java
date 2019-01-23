package generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import engine.Entity;

public class TreeGenerator {

	private Entity[] tree;
	private Random random;

	public TreeGenerator(float baseRadius, float radiusDecrease, float baseHeight, float heightDecrease,
			float baseSplitChance, float splitChanceIncrease, float theta, float thetaIncrease, int subdivisions,
			int levels, long seed) {
		random = new Random(seed);
		List<Entity> treeParts = new ArrayList<Entity>();
		float yPos = 0;
		float thetaCounter = 0, phiCounter = 0;
		for (int i = 0; i < levels; i++) {
			if (random.nextFloat() <= baseSplitChance) {
				treeParts.add(new Entity(
						new CylinderGenerator(baseRadius, baseRadius * radiusDecrease, baseHeight, subdivisions)
								.getMesh()));
				treeParts.get(i).setPosition(0, yPos, 0);
				treeParts.get(i).setRotation(theta + thetaCounter, 0, random.nextFloat() * 360);
				// thetaCounter += theta;
				// phiCounter += phi;
				treeParts.add(new Entity(
						new CylinderGenerator(baseRadius, baseRadius * radiusDecrease, baseHeight, subdivisions)
								.getMesh()));
				treeParts.get(i + 1).setPosition(0, yPos, 0);
				i++;
			} else {
				treeParts.add(new Entity(
						new CylinderGenerator(baseRadius, baseRadius * radiusDecrease, baseHeight, subdivisions)
								.getMesh()));
				treeParts.get(i).setPosition(0, yPos, 0);
			}
			yPos += baseHeight;
			baseRadius = baseRadius * radiusDecrease;
			baseHeight = baseHeight * heightDecrease;
			baseSplitChance = baseSplitChance * splitChanceIncrease;
		}
		tree = new Entity[treeParts.size()];
		for (int i = 0; i < tree.length; i++) {
			tree[i] = treeParts.get(i);
		}
	}

	public TreeGenerator(int iterations, float angleIncrement, float baseRadius, float radiusDecrease, float baseHeight,
			float heightDecrease) {
		LSystemGenerator gen = new LSystemGenerator("XF", "+-[]", "X>F+[[X]-X]-F[-FX]+X,F>FF");
		String treeString = gen.repeat(iterations, "X");
		System.out.println(treeString);
		float angleX = 0, angleY = 0, angleZ = 0, xPos = 0, yPos = 0, zPos = 0;
		Stack<Float> variableSave = new Stack<Float>();
		List<Entity> treeParts = new ArrayList<Entity>();
		for (int i = 0; i < treeString.length(); i++) {
			switch (treeString.charAt(i)) {
			case 'F':
				treeParts.add(new Entity(
						new CylinderGenerator(baseRadius, baseRadius * radiusDecrease, baseHeight, 8).getMesh()));
				treeParts.get(treeParts.size() - 1).setRotation(angleX, angleY, angleZ);
				treeParts.get(treeParts.size() - 1).setPosition(xPos, yPos, zPos);
				yPos += baseHeight;
				baseRadius *= radiusDecrease;
				baseHeight *= heightDecrease;
				break;
			case '+':
				angleX += angleIncrement;
				break;
			case '-':
				angleX -= angleIncrement;
				break;
			case '[':
				variableSave.push(angleX);
				variableSave.push(angleY);
				variableSave.push(angleZ);
				variableSave.push(baseRadius);
				variableSave.push(baseHeight);
				variableSave.push(xPos);
				variableSave.push(yPos);
				variableSave.push(zPos);
				System.out.println("pushing to stack");
				break;
			case ']':
				try {
					zPos = variableSave.pop();
					yPos = variableSave.pop();
					xPos = variableSave.pop();
					baseHeight = variableSave.pop();
					baseRadius = variableSave.pop();
					angleX = variableSave.pop();
					angleY = variableSave.pop();
					angleZ = variableSave.pop();
					System.out.println("popping from stack");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
		tree = new Entity[treeParts.size()];
		for (int i = 0; i < tree.length; i++) {
			tree[i] = treeParts.get(i);
		}
	}

	public Entity[] getTree() {
		return tree;
	}

}