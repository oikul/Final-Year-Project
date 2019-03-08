package hud;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import mesh.Entity;
import mesh.Material;
import mesh.Mesh;
import mesh.Texture;

public class TextItem extends Entity {

	private static final float ZPOS = 0.0f;
	private static final int VERTICES_PER_QUAD = 4;
	private String text;
	private final int numCols;
	private final int numRows;

	public TextItem(String text, String fontFileName, int numCols, int numRows) throws Exception {
		super();
		this.text = text;
		this.numCols = numCols;
		this.numRows = numRows;
		Texture texture = new Texture(fontFileName);
		this.setMesh(buildMesh(texture, numCols, numRows));
		this.getMesh().setMaterial(new Material(texture));
	}

	private Mesh buildMesh(Texture texture, int numCols, int numRows) {
		byte[] chars = text.getBytes(Charset.forName("ISO-8859-1"));
		int numChars = chars.length;
		List<Float> positions = new ArrayList<Float>();
		List<Float> textCoords = new ArrayList<Float>();
		float[] normals = new float[0];
		List<Integer> indices = new ArrayList<Integer>();
		float tileWidth = (float) texture.getWidth() / (float) numCols;
		float tileHeight = (float) texture.getHeight() / (float) numRows;
		for (int i = 0; i < numChars; i++) {
			byte currChar = chars[i];
			int col = currChar % numCols;
			int row = currChar / numCols;

			// Build a character tile composed by two triangles

			// Left Top vertex
			positions.add((float) i * tileWidth); // x
			positions.add(0.0f); // y
			positions.add(ZPOS); // z
			textCoords.add((float) col / (float) numCols);
			textCoords.add((float) row / (float) numRows);
			indices.add(i * VERTICES_PER_QUAD);

			// Left Bottom vertex
			positions.add((float) i * tileWidth); // x
			positions.add(tileHeight); // y
			positions.add(ZPOS); // z
			textCoords.add((float) col / (float) numCols);
			textCoords.add((float) (row + 1) / (float) numRows);
			indices.add(i * VERTICES_PER_QUAD + 1);

			// Right Bottom vertex
			positions.add((float) i * tileWidth + tileWidth); // x
			positions.add(tileHeight); // y
			positions.add(ZPOS); // z
			textCoords.add((float) (col + 1) / (float) numCols);
			textCoords.add((float) (row + 1) / (float) numRows);
			indices.add(i * VERTICES_PER_QUAD + 2);

			// Right Top vertex
			positions.add((float) i * tileWidth + tileWidth); // x
			positions.add(0.0f); // y
			positions.add(ZPOS); // z
			textCoords.add((float) (col + 1) / (float) numCols);
			textCoords.add((float) row / (float) numRows);
			indices.add(i * VERTICES_PER_QUAD + 3);

			// Add indices por left top and bottom right vertices
			indices.add(i * VERTICES_PER_QUAD);
			indices.add(i * VERTICES_PER_QUAD + 2);
		}
		return new Mesh(listToArrayF(positions), listToArrayF(textCoords), normals, listToArrayI(indices));
	}
	
	private float[] listToArrayF(List<Float> a) {
		float[] array = new float[a.size()];
		for (int i = 0; i < a.size(); i++) {
			array[i] = (float) a.get(i);
		}
		return array;
	}
	
	private int[] listToArrayI(List<Integer> a) {
		int[] array = new int[a.size()];
		for (int i = 0; i < a.size(); i++) {
			array[i] = a.get(i);
		}
		return array;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		Texture texture = this.getMesh().getMaterial().getTexture();
		this.getMesh().cleanUp();
		this.setMesh(buildMesh(texture, numCols, numRows));
	}

}
