package com.kozie.dungeon.gfx;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SpriteCollection {
	
	private Map<String, Sprite> sprites;
	
	public SpriteCollection() {
		
		sprites = Collections.synchronizedMap(new HashMap<String, Sprite>());
	}
	
	public void set(String key, Sprite sprite) {
		
		sprites.put(key, sprite);
	}
	
	public Sprite get(String key) {
		
		if (sprites.containsKey(key)) {
			return sprites.get(key);
		}
		
		return null;
	}
}