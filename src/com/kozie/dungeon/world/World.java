package com.kozie.dungeon.world;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.kozie.dungeon.GameComponent;

public class World {
	
	private Vector<WorldBlock> tiles = new Vector<WorldBlock>();
	
	public World(int level) {
		
		InputStream levelImgSrc = GameComponent.class.getResourceAsStream("/levels/"+level+".png");
		BufferedImage levelImg = ImageIO.read(levelImgSrc);
		
		int width = levelImg.getWidth();
		int height = levelImg.getHeight();
		int[] pixels = levelImg.getRGB(0, 0, width, height, null, 0, width);
		
		tiles.setSize(pixels.length);
	}
	
	public WorldBlock getTileAt() {
		
		return null;
	}
	
	private void setTileAt(WorldBlock block, int pos) {
		
		tiles.set(pos, block);
	}
}