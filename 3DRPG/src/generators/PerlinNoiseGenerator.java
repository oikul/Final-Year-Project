package generators;

import java.util.Random;

public class PerlinNoiseGenerator {

	private Random random;
	private int width, height;
	
	public PerlinNoiseGenerator(long seed) {
		random = new Random(seed);
	}

	/**
	 * creates a 2D array of perlin noise recommended values for octaves are 4
	 * and 5
	 * 
	 * @param width
	 *            the number of sampling points horizontally
	 * @param height
	 *            the number of sampling points vertically
	 * @param octave1
	 *            the first octave for smoothing
	 * @param octave2
	 *            the second octave for perlin
	 * @return a 2D array of perlin noise
	 */
	public float[][] getPerlinNoise(int width, int height, int octave1, int octave2) {
		this.width = width;
		this.height = height;
		return genPerlinNoise(genSmoothNoisePerlin(genWhiteNoise(width, height), octave1), octave2);
	}
	
	private float[][] genPerlinNoise(float[][] baseNoise, int octaveCount) {
		float[][][] smoothNoise = new float[octaveCount][][];
		float persistance = 0.5f;
		for (int i = 0; i < octaveCount; i++) {
			smoothNoise[i] = genSmoothNoisePerlin(baseNoise, i);
		}
		float[][] perlinNoise = new float[width][height];
		float amplitude = 1.0f;
		float totalAmplitude = 0.0f;
		for (int octave = octaveCount - 1; octave >= 0; octave--) {
			amplitude *= persistance;
			totalAmplitude += amplitude;
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
				}
			}
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				perlinNoise[i][j] /= totalAmplitude;
			}
		}
		return perlinNoise;
	}
	
	private float[][] genSmoothNoisePerlin(float[][] baseNoise, int octave) {
		float[][] smoothNoise = new float[width][height];
		int samplePeriod = 1 << octave;
		float sampleFrequency = 1.0f / samplePeriod;
		for (int i = 0; i < width; i++) {
			int sample_i0 = (i / samplePeriod) * samplePeriod;
			int sample_i1 = (sample_i0 + samplePeriod) % width;
			float horizontal_blend = (i - sample_i0) * sampleFrequency;
			for (int j = 0; j < height; j++) {
				int sample_j0 = (j / samplePeriod) * samplePeriod;
				int sample_j1 = (sample_j0 + samplePeriod) % height;
				float vertical_blend = (j - sample_j0) * sampleFrequency;
				float top = (float) cosineInterpolate(baseNoise[sample_i0][sample_j0],
						baseNoise[sample_i1][sample_j0], horizontal_blend);
				float bottom = (float) cosineInterpolate(baseNoise[sample_i0][sample_j1],
						baseNoise[sample_i1][sample_j1], horizontal_blend);
				smoothNoise[i][j] = (float) cosineInterpolate(top, bottom, vertical_blend);
			}
		}
		return smoothNoise;
	}
	
	private float[][] genWhiteNoise(int width, int height) {
		float[][] noise = new float[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				noise[i][j] = random.nextFloat();
			}
		}
		return noise;
	}

	public static double cosineInterpolate(double x0, double x1, double mu) {
		double mu2;
		mu2 = (1 - Math.cos(mu * Math.PI)) / 2;
		return (x0 * (1 - mu2) + x1 * mu2);
	}

}