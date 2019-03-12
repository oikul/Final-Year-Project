package generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import org.joml.Vector3f;
import org.joml.Vector4f;

import mesh.Entity;
import mesh.Material;
import mesh.Mesh;

public class TreeGenerator {

	private Entity[] tree;
	private CylinderGenerator cylinder;
	private LSystemGenerator lSystemGen;
	private Random random;

	public TreeGenerator(String rules, long seed) {
		random = new Random(seed);
		lSystemGen = new LSystemGenerator(rules, seed);
	}
	
	public Entity[] makeTree(int iterations, String startString, float angleIncrementZ, float angleRandZ, float angleIncrementY, float angleRandY, float baseRadius, float radiusDecrease, float baseHeight,
			float heightDecrease, float startX, float startY, float startZ){
		String treeString = lSystemGen.repeat(iterations, startString);
		cylinder = new CylinderGenerator();
		Vector3f position = new Vector3f(startX, startY, startZ), rotation = new Vector3f(0, 0, 0);
		Stack<Float> variableSave = new Stack<Float>();
		List<Entity> treeParts = new ArrayList<Entity>();
		for (int i = 0; i < treeString.length(); i++) {
			switch (treeString.charAt(i)) {
			case 'F':
				Mesh m = cylinder.makeCylinder(baseRadius, baseRadius * radiusDecrease, baseHeight, 6);
				m.setMaterial(new Material(new Vector4f(160.0f / 255.0f, 82.0f / 255.0f, 45.0f / 255.0f, 1f), 0.9f));
//				m.setMaterial(new Material(new Texture("bark"), 0.9f));
				treeParts.add(new Entity(m));
				treeParts.get(treeParts.size() - 1).setRotation(rotation);
				treeParts.get(treeParts.size() - 1).setPosition(position);
				position = new Vector3f(position.x + (float) (baseHeight * Math.sin(Math.toRadians(rotation.z)) * Math.cos(Math.toRadians(rotation.y))), position.y + baseHeight * (float) Math.cos(Math.toRadians(rotation.z)), position.z + (float) (baseHeight * Math.sin(Math.toRadians(rotation.z)) * Math.sin(Math.toRadians(rotation.y))));
				baseRadius *= radiusDecrease;
				baseHeight *= heightDecrease;
				break;
			case '+':
				rotation = new Vector3f(rotation.x, rotation.y, rotation.z + (angleIncrementZ + random.nextFloat() * angleRandZ));
				break;
			case '-':
				rotation = new Vector3f(rotation.x, rotation.y, rotation.z - (angleIncrementZ + random.nextFloat() * angleRandZ));
				break;
			case '*':
				rotation = new Vector3f(rotation.x, rotation.y + (angleIncrementY + random.nextFloat() * angleRandY), rotation.z);
				break;
			case '/':
				rotation = new Vector3f(rotation.x, rotation.y - (angleIncrementY + random.nextFloat() * angleRandY), rotation.z);
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