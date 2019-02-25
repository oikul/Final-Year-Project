package generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import org.joml.Vector3f;

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
		Vector3f position = new Vector3f(0, 0, 0), rotation = new Vector3f(0, 0, 0);
		Stack<Float> variableSave = new Stack<Float>();
		List<Entity> treeParts = new ArrayList<Entity>();
		for (int i = 0; i < treeString.length(); i++) {
			switch (treeString.charAt(i)) {
			case 'F':
				treeParts.add(new Entity(
						new CylinderGenerator(baseRadius, baseRadius * radiusDecrease, baseHeight, 8).getMesh()));
				treeParts.get(treeParts.size() - 1).setRotation(rotation);
				treeParts.get(treeParts.size() - 1).setPosition(position);
				position = new Vector3f(position.x, position.y + baseHeight, position.z);
				baseRadius *= radiusDecrease;
				baseHeight *= heightDecrease;
				break;
			case '+':
				rotation = new Vector3f(rotation.x + angleIncrement, rotation.y, rotation.z);
				break;
			case '-':
				rotation = new Vector3f(rotation.x - angleIncrement, rotation.y, rotation.z);
				break;
			case '[':
				variableSave.push(rotation.x);
				variableSave.push(rotation.y);
				variableSave.push(rotation.z);
				variableSave.push(baseRadius);
				variableSave.push(baseHeight);
				variableSave.push(position.x);
				variableSave.push(position.y);
				variableSave.push(position.z);
				System.out.println("pushing to stack");
				break;
			case ']':
				try {
					float z = variableSave.pop();
					float y = variableSave.pop();
					float x = variableSave.pop();
					position.set(x, y, z);
					baseHeight = variableSave.pop();
					baseRadius = variableSave.pop();
					float angleX = variableSave.pop();
					float angleY = variableSave.pop();
					float angleZ = variableSave.pop();
					rotation.set(angleX, angleY, angleZ);
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