package generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.joml.Vector3f;

import engine.Entity;

public class TreeGenerator {

	private Entity[] tree;
	private CylinderGenerator cylinder;
	private LSystemGenerator lSystemGen;
	private long seed;

	public TreeGenerator(String rules, long seed) {
		this.seed = seed;
		lSystemGen = new LSystemGenerator(rules, seed);
	}
	
	public Entity[] makeTree(int iterations, float angleIncrementZ, float angleIncrementY, float baseRadius, float radiusDecrease, float baseHeight,
			float heightDecrease, float startX, float startY, float startZ){
		String treeString = lSystemGen.repeat(iterations, "X");
		cylinder = new CylinderGenerator();
		Vector3f position = new Vector3f(startX, startY, startZ), rotation = new Vector3f(0, 0, 0);
		Stack<Float> variableSave = new Stack<Float>();
		List<Entity> treeParts = new ArrayList<Entity>();
		for (int i = 0; i < treeString.length(); i++) {
			switch (treeString.charAt(i)) {
			case 'F':
				treeParts.add(new Entity(
						cylinder.makeCylinder(baseRadius, baseRadius * radiusDecrease, baseHeight, 6)));
				treeParts.get(treeParts.size() - 1).setRotation(rotation);
				treeParts.get(treeParts.size() - 1).setPosition(position);
				//float phi = (float) (Math.atan(Math.sqrt(Math.pow(position.x, 2) + Math.pow(position.y, 2))/position.z));
				//position = new Vector3f(position.x + (float) (baseHeight * Math.sin(phi) * Math.sin(Math.toRadians(rotation.z))), position.y + (float) (baseHeight * Math.sin(phi) * Math.cos(Math.toRadians(rotation.z))), position.z + (float) (baseHeight * Math.cos(phi)));
				position = new Vector3f(position.x + (float) (baseHeight * Math.sin(Math.toRadians(rotation.z)) * Math.cos(Math.toRadians(rotation.y))), position.y + baseHeight * (float) Math.cos(Math.toRadians(rotation.z)), position.z + (float) (baseHeight * Math.sin(Math.toRadians(rotation.z)) * Math.sin(Math.toRadians(rotation.y))));
				baseRadius *= radiusDecrease;
				baseHeight *= heightDecrease;
				break;
			case '+':
				rotation = new Vector3f(rotation.x, rotation.y, rotation.z + angleIncrementZ);
				break;
			case '-':
				rotation = new Vector3f(rotation.x, rotation.y, rotation.z - angleIncrementZ);
				break;
			case '*':
				rotation = new Vector3f(rotation.x, rotation.y + angleIncrementY, rotation.z);
				break;
			case '/':
				rotation = new Vector3f(rotation.x, rotation.y - angleIncrementY, rotation.z);
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
				break;
			case ']':
				try {
					float z = variableSave.pop();
					float y = variableSave.pop();
					float x = variableSave.pop();
					position = new Vector3f(x, y, z);
					baseHeight = variableSave.pop();
					baseRadius = variableSave.pop();
					float angleZ = variableSave.pop();
					float angleY = variableSave.pop();
					float angleX = variableSave.pop();
					rotation = new Vector3f(angleX, angleY, angleZ);
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
		return tree;
	}

}