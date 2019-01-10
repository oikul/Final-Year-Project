package game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import engine.Mesh;

public class CylinderGenerator {

	private float[] positions, textCoords, normals;
	int[] indices;
	private float angle = 0;
	private List<Float> positionsList = new ArrayList<Float>();
	private List<Integer> indicesList = new ArrayList<Integer>();
	private Mesh mesh;

	public CylinderGenerator(float radius, float height, int subdivisions) {
		float incAngle = (float) ((float) (2 * Math.PI) / (float) subdivisions);
		Vector3f v0 = new Vector3f();
		v0.x = 0;
		v0.y = 0;
		v0.z = 0;
		positionsList.add(v0.x);
		positionsList.add(v0.y);
		positionsList.add(v0.z);
		for (int i = 0; i < subdivisions; i++) {
			double incX = radius * Math.cos((double) angle);
			double incZ = radius * Math.sin((double) angle);
			v0.x = v0.x + (float) incX;
			v0.z = v0.z + (float) incZ;
			positionsList.add(v0.x);
			positionsList.add(v0.y);
			positionsList.add(v0.z);
			angle += incAngle;
		}
		v0.x = 0;
		v0.y = height;
		v0.z = 0;
		positionsList.add(v0.x);
		positionsList.add(v0.y);
		positionsList.add(v0.z);
		angle = 0;
		for (int i = 0; i < subdivisions; i++) {
			double incX = radius * Math.cos((double) angle);
			double incZ = radius * Math.sin((double) angle);
			v0.x = v0.x + (float) incX;
			v0.z = v0.z + (float) incZ;
			positionsList.add(v0.x);
			positionsList.add(v0.y);
			positionsList.add(v0.z);
			angle += incAngle;
		}
		positions = new float[positionsList.size()];
		for (int i = 0; i < positionsList.size(); i++) {
			positions[i] = positionsList.get(i);
		}
		for (int i = 0; i < positions.length; i++) {
//			if (i < positions.length / 2) {
//				indicesList.add(i);
//				indicesList.add(i + (positions.length / 2));
//				indicesList.add(i + 1);
//			} else {
//				indicesList.add(i);
//				indicesList.add(i + 1);
//				indicesList.add(i + 1 - (positions.length / 2));
//			}
			for(int j = 0; j < positions.length; j ++){
				indicesList.add(i);
				indicesList.add(j);
			}
		}
		indices = new int[indicesList.size()];
		for (int i = 0; i < indicesList.size(); i++) {
			indices[i] = indicesList.get(i);
		}
		textCoords = new float[indices.length];
		normals = new float[indices.length];
		mesh = new Mesh(positions, textCoords, normals, indices);
	}

	public Mesh getMesh() {
		return mesh;
	}

}
