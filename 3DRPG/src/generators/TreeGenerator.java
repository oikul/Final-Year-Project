package generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import engine.Entity;

public class TreeGenerator {

	private Entity[] tree;
	private Random random;

	public TreeGenerator(float baseRadius, float radiusDecrease, float baseHeight, float heightDecrease,
			float baseSplitChance, float splitChanceIncrease, float theta, float thetaIncrease, int subdivisions, int levels, long seed) {
		random = new Random(seed);
		List<Entity> treeParts = new ArrayList<Entity>();
		float yPos = 0;
		float thetaCounter = 0, phiCounter = 0;
		for (int i = 0; i < levels; i++) {
			if (random.nextFloat() <= baseSplitChance) {
				treeParts.add(new Entity(
						new CylinderGenerator(baseRadius, baseRadius * radiusDecrease, baseHeight, subdivisions)
								.getMesh()));
				treeParts.get(i).setPosition(0, yPos, 0);
				treeParts.get(i).setRotation(theta + thetaCounter, 0, random.nextFloat() * 360);
				//thetaCounter += theta;
				//phiCounter += phi;
				treeParts.add(new Entity(
						new CylinderGenerator(baseRadius, baseRadius * radiusDecrease, baseHeight, subdivisions)
								.getMesh()));
				treeParts.get(i + 1).setPosition(0, yPos, 0);
				i++;
			} else {
				treeParts.add(new Entity(
						new CylinderGenerator(baseRadius, baseRadius * radiusDecrease, baseHeight, subdivisions)
								.getMesh()));
				treeParts.get(i).setPosition(0, yPos, 0);
			}
			yPos += baseHeight;
			baseRadius = baseRadius * radiusDecrease;
			baseHeight = baseHeight * heightDecrease;
			baseSplitChance = baseSplitChance * splitChanceIncrease;
		}
		tree = new Entity[treeParts.size()];
		for (int i = 0; i < tree.length; i++) {
			tree[i] = treeParts.get(i);
		}
	}

	public Entity[] getTree() {
		return tree;
	}

}