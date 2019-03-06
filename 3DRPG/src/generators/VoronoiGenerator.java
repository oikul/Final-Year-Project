package generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector3f;

import game.VMesh;
import voronoi.GraphEdge;
import voronoi.Voronoi;

public class VoronoiGenerator {

	private float[] positions, textCoords, normals;
	private int[] indices;
	private Voronoi voronoi;
	private Random random;

	public VoronoiGenerator(float minDist, long seed) {
		voronoi = new Voronoi((double) minDist);
		random = new Random(seed);
	}

	public VMesh generateVoronoi(int numOfPoints, float minX, float maxX, float minZ, float maxZ, float yHeight, float range) {
		double[] xValuesIn = new double[numOfPoints], zValuesIn = new double[numOfPoints];
		for(int i = 0; i < numOfPoints; i++){
			xValuesIn[i] = (random.nextDouble() * range) - (range/2);
			zValuesIn[i] = (random.nextDouble() * range) - (range/2);
		}
		List<GraphEdge> graphEdges = voronoi.generateVoronoi(xValuesIn, zValuesIn, minX, maxX, minZ, maxZ);
		List<Float> positionsList = new ArrayList<Float>();
		for(int i = 0; i < graphEdges.size(); i++){
			positionsList.add((float) graphEdges.get(i).x1);
			positionsList.add(yHeight);
			positionsList.add((float) graphEdges.get(i).y1);
			positionsList.add((float) graphEdges.get(i).x2);
			positionsList.add(yHeight);
			positionsList.add((float) graphEdges.get(i).y2);
		}
		positions = new float[positionsList.size()];
		for(int i = 0; i < positionsList.size(); i++){
			positions[i] = positionsList.get(i);
		}
		List<Integer> indicesList = new ArrayList<Integer>();
		for(int i = 0; i < positions.length / 3 - 1; i+=2){
			indicesList.add(i);
			indicesList.add(i+1);
		}
		indices = new int[indicesList.size()];
		for(int i = 0; i < indicesList.size(); i++){
			indices[i] = indicesList.get(i);
		}
		textCoords = new float[indices.length];
		normals = new float[indices.length];
		VMesh mesh = new VMesh(positions, textCoords, normals, indices);
		mesh.setColour(new Vector3f(1.0f, 0.0f, 0.0f));
		return mesh;
	}
	
}
