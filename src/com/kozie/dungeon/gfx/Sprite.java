package com.kozie.dungeon.gfx;

public class Sprite {

	public int width, height;
	public int[] pixels;
	
	public Sprite(SpriteSheet sheet, int offset, int xTiles, int yTiles) {
		
		width = xTiles * sheet.tileSize;
		height = yTiles * sheet.tileSize;
		pixels = sheet.getTile(offset, xTiles, yTiles);
	}
	
	public Sprite(SpriteSheet sheet, int offset) {
		
		this(sheet, offset, 1, 1);
	}
}