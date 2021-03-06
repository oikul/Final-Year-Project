package mesh;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import generators.PerlinNoiseGenerator;
import generators.ValueNoiseGenerator;
import generators.VoronoiGenerator;

public class HeightMapMesh {

	private static final float startX = -0.5f, startZ = -0.5f;
	private Mesh mesh;
	private float[] positions, textCoords, normals;
	private int[] indices;

	public HeightMapMesh(int width, int height, int textInc, long seed, float amplitude, float roughness, int voctaves, int poctave1, int poctave2, boolean perlinorvalue, int voronoiSize, int voronoiPoints) {
		PerlinNoiseGenerator pGenerator = null;
		ValueNoiseGenerator vGenerator = null;
		if(perlinorvalue){
			pGenerator = new PerlinNoiseGenerator(seed);
		}else{
			vGenerator = new ValueNoiseGenerator(seed, amplitude, roughness, voctaves);
		}
		float incx = Math.abs(startX * 2) / (width - 1);
		float incz = Math.abs(startZ * 2) / (height - 1);
		List<Float> positionsList = new ArrayList<Float>();
		List<Float> textCoordsList = new ArrayList<Float>();
		List<Integer> indicesList = new ArrayList<Integer>();
		float[][] noise = new float[0][0];
		if(perlinorvalue){
			noise = pGenerator.getPerlinNoise(width, height, poctave1, poctave2);
		}
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				positionsList.add(startX + col + incx);
				if(perlinorvalue){
					positionsList.add(noise[col][row] * amplitude);	
				}else{
					positionsList.add(vGenerator.generateHeight(col, row) - (amplitude * voctaves / 2));
				}
				positionsList.add(startZ + row + incz);

				textCoordsList.add((float) textInc * (float) col / (float) width);
				textCoordsList.add((float) textInc * (float) row / (float) height);

				if (col < width - 1 && row < height - 1) {
					int leftTop = row * width + col;
					int leftBottom = (row + 1) * width + col;
					int rightBottom = (row + 1) * width + col + 1;
					int rightTop = row * width + col + 1;
					indicesList.add(rightTop);
					indicesList.add(leftBottom);
					indicesList.add(leftTop);
					indicesList.add(rightBottom);
					indicesList.add(leftBottom);
					indicesList.add(rightTop);
				}
			}
		}
		positions = listToArray(positionsList);
		textCoords = listToArray(textCoordsList);
		indices = indicesList.stream().mapToInt(i -> i).toArray();
		normals = calcNormals(positions, width, height);
		this.mesh = new Mesh(positions, textCoords, normals, indices);
		Material m = new Material(new Texture("grass"), 0f);
		VoronoiGenerator voronoi = new VoronoiGenerator(0, seed);
		long beforeTime = System.currentTimeMillis();
		Texture t = new Texture();
		t.setImage(voronoi.createVoronoiImage(voronoiPoints, 0f, voronoiSize, 0f, voronoiSize, voronoiSize));
		t.loadTexture();
		m.setSecondaryTexture(t);
		long afterTime = System.currentTimeMillis();
		//System.out.println("time taken to generate roads: " + (afterTime - beforeTime) + " ms");
		this.mesh.setMaterial(m);
	}

	public Mesh getMesh() {
		return this.mesh;
	}

	private float[] listToArray(List<Float> a) {
		float[] array = new float[a.size()];
		for (int i = 0; i < a.size(); i++) {
			array[i] = (float) a.get(i);
		}
		return array;
	}

	private float[] calcNormals(float[] positions, int width, int height) {
		Vector3f v0 = new Vector3f();
		Vector3f v1 = new Vector3f();
		Vector3f v2 = new Vector3f();
		Vector3f v3 = new Vector3f();
		Vector3f v4 = new Vector3f();
		Vector3f v12 = new Vector3f();
		Vector3f v23 = new Vector3f();
		Vector3f v34 = new Vector3f();
		Vector3f v41 = new Vector3f();
		List<Float> normals = new ArrayList<Float>();
		Vector3f normal = new Vector3f();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (row > 0 && row < height - 1 && col > 0 && col < width - 1) {
					int i0 = row * width * 3 + col * 3;
					v0.x = positions[i0];
					v0.y = positions[i0 + 1];
					v0.z = positions[i0 + 2];

					int i1 = row * width * 3 + (col - 1) * 3;
					v1.x = positions[i1];
					v1.y = positions[i1 + 1];
					v1.z = positions[i1 + 2];
					v1 = v1.sub(v0);

					int i2 = (row + 1) * width * 3 + col * 3;
					v2.x = positions[i2];
					v2.y = positions[i2 + 1];
					v2.z = positions[i2 + 2];
					v2 = v2.sub(v1);

					int i3 = row * width * 3 + (col + 1) * 3;
					v3.x = positions[i3];
					v3.y = positions[i3 + 1];
					v3.z = positions[i3 + 2];
					v3 = v3.sub(v2);

					int i4 = (row - 1) * width * 3 + col * 3;
					v4.x = positions[i4];
					v4.y = positions[i4 + 1];
					v4.z = positions[i4 + 2];
					v4 = v4.sub(v3);

					v1.cross(v2, v12);
					v12.normalize();

					v2.cross(v3, v23);
					v23.normalize();

					v3.cross(v4, v34);
					v34.normalize();

					v4.cross(v1, v41);
					v41.normalize();

					normal = v12.add(v23).add(v34).add(v41);
					normal.normalize();
				} else {
					normal.x = 0;
					normal.y = 1;
					normal.z = 0;
				}
				normal.normalize();
				normals.add(normal.x);
				normals.add(normal.y);
				normals.add(normal.z);
			}
		}
		return listToArray(normals);
	}

	public float[] getPositions() {
		return positions;
	}

	public void setPositions(float[] positions) {
		this.positions = positions;
	}

	public float[] getTextCoords() {
		return textCoords;
	}

	public void setTextCoords(float[] textCoords) {
		this.textCoords = textCoords;
	}

	public float[] getNormals() {
		return normals;
	}

	public void setNormals(float[] normals) {
		this.normals = normals;
	}

	public int[] getIndices() {
		return indices;
	}

	public void setIndices(int[] indices) {
		this.indices = indices;
	}

}
