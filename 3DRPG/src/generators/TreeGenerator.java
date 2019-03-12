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
	private float[] positions, textCoords, normals;
	private int[] indices;

	public TreeGenerator(String rules, long seed) {
		random = new Random(seed);
		lSystemGen = new LSystemGenerator(rules, seed);
	}

	public Entity[] makeTree(int iterations, String startString, float angleIncrementZ, float angleRandZ,
			float angleIncrementY, float angleRandY, float baseRadius, float radiusDecrease, float baseHeight,
			float heightDecrease, int subdivisions, float startX, float startY, float startZ) {
		String treeString = lSystemGen.repeat(iterations, startString);
		cylinder = new CylinderGenerator();
		Vector3f position = new Vector3f(startX, startY, startZ), rotation = new Vector3f(0, 0, 0);
		Stack<Float> variableSave = new Stack<Float>();
		List<Entity> treeParts = new ArrayList<Entity>();
		for (int i = 0; i < treeString.length(); i++) {
			switch (treeString.charAt(i)) {
			case 'F':
				Mesh m = cylinder.makeCylinder(baseRadius, baseRadius * radiusDecrease, baseHeight, subdivisions);
				m.setMaterial(new Material(new Vector4f(160.0f / 255.0f, 82.0f / 255.0f, 45.0f / 255.0f, 1f), 0.9f));
				// m.setMaterial(new Material(new Texture("bark"), 0.9f));
				treeParts.add(new Entity(m));
				treeParts.get(treeParts.size() - 1).setRotation(rotation);
				treeParts.get(treeParts.size() - 1).setPosition(position);
				position = new Vector3f(
						position.x + (float) (baseHeight * Math.sin(Math.toRadians(rotation.z))
								* Math.cos(Math.toRadians(rotation.y))),
						position.y + baseHeight * (float) Math.cos(Math.toRadians(rotation.z)),
						position.z + (float) (baseHeight * Math.sin(Math.toRadians(rotation.z))
								* Math.sin(Math.toRadians(rotation.y))));
				baseRadius *= radiusDecrease;
				baseHeight *= heightDecrease;
				break;
			case '+':
				rotation = new Vector3f(rotation.x, rotation.y,
						rotation.z + (angleIncrementZ + random.nextFloat() * angleRandZ));
				break;
			case '-':
				rotation = new Vector3f(rotation.x, rotation.y,
						rotation.z - (angleIncrementZ + random.nextFloat() * angleRandZ));
				break;
			case '*':
				rotation = new Vector3f(rotation.x, rotation.y + (angleIncrementY + random.nextFloat() * angleRandY),
						rotation.z);
				break;
			case '/':
				rotation = new Vector3f(rotation.x, rotation.y - (angleIncrementY + random.nextFloat() * angleRandY),
						rotation.z);
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

	public Entity makeTreeFast(int iterations, String startString, float angleIncrementZ, float angleRandZ,
			float angleIncrementY, float angleRandY, float baseRadius, float radiusDecrease, float baseHeight,
			float heightDecrease, int subdivisions, float startX, float startY, float startZ) {
		String treeString = lSystemGen.repeat(iterations, startString);
		Entity tree;
		Vector3f position = new Vector3f(startX, startY, startZ), rotation = new Vector3f(0, 0, 0);
		Stack<Float> variableSave = new Stack<Float>();
		List<Float> positionsList = new ArrayList<Float>(), textCoordsList = new ArrayList<Float>();
		List<Integer> indicesList = new ArrayList<Integer>();
		boolean doneFirstPoly = false;
		float angle = 0;
		float incAngle = (float) ((float) (2 * Math.PI) / (float) subdivisions);
		for (int i = 0; i < treeString.length(); i++) {
			switch (treeString.charAt(i)) {
			case 'F':
				positionsList.add(position.x);
				positionsList.add(position.y);
				positionsList.add(position.z);
				Vector3f v0 = position;
				for (int j = 0; j < subdivisions; j++) {
					double incX = baseRadius * Math.cos((double) angle);
					double incZ = baseRadius * Math.sin((double) angle);
					v0.x = v0.x + (float) incX;
					v0.z = v0.z + (float) incZ;
					positionsList.add(v0.x);
					positionsList.add(v0.y);
					positionsList.add(v0.z);
					angle += incAngle;
					v0 = position;
				}
				for (int j = (positionsList.size() / 3) - (subdivisions + 1); j < positionsList.size() / 3 - 1; j++) {
					indicesList.add((positionsList.size() / 3) - (subdivisions + 1));
					indicesList.add(j);
					indicesList.add(j + 1);
					indicesList.add(j + 1);
					indicesList.add(j);
					indicesList.add((positionsList.size() / 3) - (subdivisions + 1));
				}
				if (doneFirstPoly) {
					for (int j = (positionsList.size() / 3) - 2 * (subdivisions + 1); j < positionsList.size() / 3
							- (subdivisions + 2); j++) {
						// connect bottom vertices to top
						indicesList.add(j);
						indicesList.add(j + 1);
						indicesList.add(j + (subdivisions + 1));

						// connect top vertices to bottom
						indicesList.add(j + 1);
						indicesList.add(j + (subdivisions + 2));
						indicesList.add(j + (subdivisions + 1));
					}
				}
				doneFirstPoly = true;
				position = new Vector3f(
						position.x + (float) (baseHeight * Math.sin(Math.toRadians(rotation.z))
								* Math.cos(Math.toRadians(rotation.y))),
						position.y + baseHeight * (float) Math.cos(Math.toRadians(rotation.z)),
						position.z + (float) (baseHeight * Math.sin(Math.toRadians(rotation.z))
								* Math.sin(Math.toRadians(rotation.y))));
				baseRadius *= radiusDecrease;
				baseHeight *= heightDecrease;
				break;
			case '+':
				rotation = new Vector3f(rotation.x, rotation.y,
						rotation.z + (angleIncrementZ + random.nextFloat() * angleRandZ));
				break;
			case '-':
				rotation = new Vector3f(rotation.x, rotation.y,
						rotation.z - (angleIncrementZ + random.nextFloat() * angleRandZ));
				break;
			case '*':
				rotation = new Vector3f(rotation.x, rotation.y + (angleIncrementY + random.nextFloat() * angleRandY),
						rotation.z);
				break;
			case '/':
				rotation = new Vector3f(rotation.x, rotation.y - (angleIncrementY + random.nextFloat() * angleRandY),
						rotation.z);
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
					doneFirstPoly = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
		positions = listToArray(positionsList);
		textCoords = listToArray(textCoordsList);
		indices = listToArrayI(indicesList);
		normals = calculateNormals(positions, indices);
		Mesh mesh = new Mesh(positions, textCoords, normals, indices);
		mesh.setMaterial(new Material(new Vector4f(160.0f / 255.0f, 82.0f / 255.0f, 45.0f / 255.0f, 1f), 0.2f));
		tree = new Entity(mesh);
		return tree;
	}

	private int[] listToArrayI(List<Integer> a) {
		int[] array = new int[a.size()];
		for (int i = 0; i < a.size(); i++) {
			array[i] = a.get(i);
		}
		return array;
	}

	private float[] listToArray(List<Float> a) {
		float[] array = new float[a.size()];
		for (int i = 0; i < a.size(); i++) {
			array[i] = (float) a.get(i);
		}
		return array;
	}

	private float[] calculateNormals(float[] positions, int[] indices) {
		ArrayList<Float> normalsList = new ArrayList<Float>();
		for (int i = 0; i < indices.length; i += 3) {
			Vector3f v1 = new Vector3f(positions[indices[i]], positions[indices[i] + 1], positions[indices[i] + 2]);
			Vector3f v2 = new Vector3f(positions[indices[i + 1]], positions[indices[i + 1] + 1],
					positions[indices[i + 1] + 2]);
			Vector3f v3 = new Vector3f(positions[indices[i + 2]], positions[indices[i + 2] + 1],
					positions[indices[i + 2] + 2]);
			// ((v2 - v1).cross(v3 - v1)).normalize();
			Vector3f v21 = v2.sub(v1);
			Vector3f v31 = v3.sub(v1);
			Vector3f normal = v21.cross(v31);
			normal = normal.normalize();
			normalsList.add(normal.x);
			normalsList.add(normal.y);
			normalsList.add(normal.z);
		}
		float[] normals = listToArray(normalsList);
		return normals;
	}

}