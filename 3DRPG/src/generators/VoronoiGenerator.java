package generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector3f;

import engine.Mesh;
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

	public Mesh generateVoronoi(int numOfPoints, float minX, float maxX, float minZ, float maxZ, float yHeight, float offset) {
		double[] xValuesIn = new double[numOfPoints], zValuesIn = new double[numOfPoints];
		for(int i = 0; i < numOfPoints; i++){
			xValuesIn[i] = (random.nextDouble() * (minX+maxX)) - offset;
			zValuesIn[i] = (random.nextDouble() * (minZ+maxZ)) - offset;
			System.out.println(xValuesIn[i] + ", " + zValuesIn[i]);
		}
		List<GraphEdge> graphEdges = voronoi.generateVoronoi(xValuesIn, zValuesIn, minX, maxX, minZ, maxZ);
		List<Float> positionsList = new ArrayList<Float>();
		System.out.println("size: " + graphEdges.size());
		for(int i = 0; i < graphEdges.size() - 1; i++){
			positionsList.add((float) graphEdges.get(i).x1);
			positionsList.add(yHeight);
			positionsList.add((float) graphEdges.get(i).y1);
			System.out.println(graphEdges.get(i).x1 + ", " + graphEdges.get(i).y1);
			positionsList.add((float) graphEdges.get(i).x2);
			positionsList.add(yHeight);
			positionsList.add((float) graphEdges.get(i).y2);
			System.out.println(graphEdges.get(i).x2 + ", " + graphEdges.get(i).y2);
//			positionsList.add((float) graphEdges.get(i + 1).x1);
//			positionsList.add(yHeight);
//			positionsList.add((float) graphEdges.get(i + 1).y1);
//			System.out.println(graphEdges.get(i+1).x1 + ", " + graphEdges.get(i+1).y1);
		}
		positions = new float[positionsList.size()];
		for(Float p : positionsList){
			positions[positionsList.indexOf(p)] = p;
		}
		List<Integer> indicesList = new ArrayList<Integer>();
		for(int i = 0; i < positions.length / 3; i++){
			indicesList.add(i);
			indicesList.add(i + 1);
			indicesList.add(i + 2);
		}
		indices = new int[indicesList.size()];
		for(Integer i : indicesList){
			indices[indicesList.indexOf(i)] = i;
		}
		textCoords = new float[indices.length];
		normals = new float[indices.length];
		Mesh mesh = new Mesh(positions, textCoords, normals, indices);
		mesh.setColour(new Vector3f(1.0f, 0.0f, 0.0f));
		return mesh;
	}

}
