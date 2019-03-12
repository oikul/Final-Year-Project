package generators;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector4f;

import mesh.Material;
import mesh.VMesh;
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
		mesh.setMaterial(new Material(new Vector4f(1.0f, 0.0f, 0.0f, 1f), 1f));
		return mesh;
	}
	
	public BufferedImage createVoronoiImage(int numOfPoints, float minX, float maxX, float minZ, float maxZ, float range){
		double[] xValuesIn = new double[numOfPoints], zValuesIn = new double[numOfPoints];
		for(int i = 0; i < numOfPoints; i++){
			xValuesIn[i] = (random.nextDouble() * range);
			zValuesIn[i] = (random.nextDouble() * range);
		}
		List<GraphEdge> graphEdges = voronoi.generateVoronoi(xValuesIn, zValuesIn, minX, maxX, minZ, maxZ);
		BufferedImage image = new BufferedImage((int) range, (int) range, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		for(int i = 0 ; i < graphEdges.size(); i++){
			g.setColor(new Color(0, 0, 0, 0));
			g.fillRect(0, 0, (int) range, (int) range); 
			g.setColor(new Color(60, 80, 100));
			g.setStroke(new BasicStroke(50));
			g.drawLine((int) graphEdges.get(i).x1, (int) graphEdges.get(i).y1, (int) graphEdges.get(i).x2, (int) graphEdges.get(i).y2);
		}
//		try {
//			ImageIO.write(image, "png", new File("saved.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return image;
	}
	
//	private float[] listToArrayF(List<Float> a) {
//		float[] array = new float[a.size()];
//		for (int i = 0; i < a.size(); i++) {
//			array[i] = (float) a.get(i);
//		}
//		return array;
//	}
//	
//	private int[] listToArrayI(List<Integer> a) {
//		int[] array = new int[a.size()];
//		for (int i = 0; i < a.size(); i++) {
//			array[i] = a.get(i);
//		}
//		return array;
//	}
	
}
