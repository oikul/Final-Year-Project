package game;

import mesh.Entity;
import mesh.HeightMapMesh;

public class Terrain {

	private Entity[] chunks;

	public Terrain(int blocksPerRow, float scale, int width, int height, int textInc, long seed, float amplitude, float roughness, int octaves, int poctave1, int poctave2, boolean perlinorvalue, int voronoiSize, int voronoiPoints) {
		chunks = new Entity[blocksPerRow * blocksPerRow];
		HeightMapMesh mesh = new HeightMapMesh(width, height, textInc, seed, amplitude, roughness, octaves, poctave1, poctave2, perlinorvalue, voronoiSize, voronoiPoints);
		for (int row = 0; row < blocksPerRow; row++) {
			for (int col = 0; col < blocksPerRow; col++) {
				float xDisplacement = -width / 2;// (col - ((float) blocksPerRow
													// - 1) / (float) 2) * scale
													// * width;
				float zDisplacement = -height / 2;// (row - ((float)
													// blocksPerRow - 1) /
													// (float) 2) * scale *
													// height;
				Entity terrainChunk = new Entity(mesh.getMesh());
				terrainChunk.setScale(scale);
				terrainChunk.setPosition(xDisplacement, -amplitude, zDisplacement);
				chunks[row * blocksPerRow + col] = terrainChunk;
			}
		}
	}

	public Entity[] getChunks() {
		return chunks;
	}

}
