package hud;

import mesh.Entity;

public interface IHud {
	
	Entity[] getEntities();
	
	default void cleanup() {
		Entity[] entities = getEntities();
		for(Entity entity : entities) {
			entity.getMesh().cleanUp();
		}
	}

}
