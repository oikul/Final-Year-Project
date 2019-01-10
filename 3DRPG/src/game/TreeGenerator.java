package game;

import java.util.ArrayList;
import java.util.List;

import engine.Entity;

public class TreeGenerator {
	
	private Entity[] tree;
	
	public TreeGenerator(float baseRadius, float radiusDecrease, float baseHeight, float heightDecrease, float baseSplitChance, float splitChanceIncrease, int subdivisions, int levels){
		List<Entity> treeParts = new ArrayList<Entity>();
		float yPos = 0;
		for(int i = 0; i < levels; i++){
			treeParts.add(new Entity(new CylinderGenerator(baseRadius, baseRadius * radiusDecrease, baseHeight, subdivisions).getMesh()));
			treeParts.get(i).setPosition(0, yPos, 0);
			yPos += baseHeight;
			baseRadius = baseRadius * radiusDecrease;
			baseHeight = baseHeight * heightDecrease;
			baseSplitChance = baseSplitChance * splitChanceIncrease;
		}
		tree = new Entity[treeParts.size()];
		for(int i = 0; i < tree.length; i ++){
			tree[i] = treeParts.get(i);
		}
	}
	
	public Entity[] getTree(){
		return tree;
	}
	
}