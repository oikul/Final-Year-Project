package generators;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import mesh.Mesh;

public class CylinderGenerator {

	private float[] positions, textCoords, normals;
	int[] indices;
	private float angle;
	private List<Float> positionsList;
	private List<Integer> indicesList;
	private Mesh mesh;

	public Mesh makeCylinder(float radiusBottom, float radiusTop, float height, int subdivisions) {
		float incAngle = (float) ((float) (2 * Math.PI) / (float) subdivisions);
		Vector3f v0 = new Vector3f();
		v0.x = 0;
		v0.y = 0;
		v0.z = 0;
		angle = 0;
		positionsList = new ArrayList<Float>();
		indicesList = new ArrayList<Integer>();
		positionsList.add(v0.x);
		positionsList.add(v0.y);
		positionsList.add(v0.z);
		for (int i = 0; i < subdivisions; i++) {
			double incX = radiusBottom * Math.cos((double) angle);
			double incZ = radiusBottom * Math.sin((double) angle);
			v0.x = v0.x + (float) incX;
			v0.z = v0.z + (float) incZ;
			positionsList.add(v0.x);
			positionsList.add(v0.y);
			positionsList.add(v0.z);
			angle += incAngle;
			v0.x = 0;
			v0.y = 0;
			v0.z = 0;
		}
		v0.x = 0;
		v0.y = height;
		v0.z = 0;
		positionsList.add(v0.x);
		positionsList.add(v0.y);
		positionsList.add(v0.z);
		angle = 0;
		for (int i = 0; i < subdivisions; i++) {
			double incX = radiusTop * Math.cos((double) angle);
			double incZ = radiusTop * Math.sin((double) angle);
			v0.x = v0.x + (float) incX;
			v0.z = v0.z + (float) incZ;
			positionsList.add(v0.x);
			positionsList.add(v0.y);
			positionsList.add(v0.z);
			angle += incAngle;
			v0.x = 0;
			v0.y = height;
			v0.z = 0;
		}
		positions = new float[positionsList.size()];
		for (int i = 0; i < positionsList.size(); i++) {
			positions[i] = positionsList.get(i);
		}
		for (int i = 0; i < positions.length / 3; i++) {
			if (i == 0) {
				for (int j = 1; j < positions.length / 6; j++) {
					if (j < positions.length / 6 - 1) {
						indicesList.add(j + 1);
					} else {
						indicesList.add(1);
					}
					indicesList.add(j);
					indicesList.add(i);
				}
			} else if (i == positions.length / 6) {
				for (int j = positions.length / 6; j < positions.length / 3; j++) {
					indicesList.add(i);
					indicesList.add(j);
					if (j < positions.length / 3 - 1) {
						indicesList.add(j + 1);
					} else {
						indicesList.add(positions.length / 6 + 1);
					}
				}
			} else if (i > 0 && i < positions.length / 6) {
				indicesList.add(i);
				if (i + 1 == positions.length / 6) {
					indicesList.add(1);
				} else {
					indicesList.add(i + 1);
				}
				indicesList.add(i + positions.length / 6);
			} else if (i > positions.length / 6 && i < positions.length / 3) {
				if (i - positions.length / 6 + 1 == positions.length / 6) {
					indicesList.add(1);
				} else {
					indicesList.add(i - positions.length / 6 + 1);
				}
				if (i + 1 == positions.length / 3) {
					indicesList.add(positions.length / 6 + 1);
				} else {
					indicesList.add(i + 1);
				}

				indicesList.add(i);
			}
		}
		indices = new int[indicesList.size()];
		for (int i = 0; i < indicesList.size(); i++) {
			indices[i] = indicesList.get(i);
		}
		textCoords = new float[indices.length];
		normals = calculateNormals(positions, indices);
		mesh = new Mesh(positions, textCoords, normals, indices);
		return mesh;
	}

	public float[] getPositions() {
		return positions;
	}

	public float[] getTextCoords() {
		return textCoords;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
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
			Vector3f v2 = new Vector3f(positions[indices[i + 1]], positions[indices[i + 1] + 1], positions[indices[i + 1] + 2]);
			Vector3f v3 = new Vector3f(positions[indices[i + 2]], positions[indices[i + 2] + 1], positions[indices[i + 2] + 2]);
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
