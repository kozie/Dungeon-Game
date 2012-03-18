package com.kozie.dungeon.gfx;

public class Font {

	private SpriteCollection chars;
	private Screen screen;
	private SpriteSheet sheet;

	private String[] charTable = {
			"abcdefghijklmnopqrstuvwxyz",
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ",
			"1234567890!?,."};

	public Font(SpriteSheet sheet, Screen screen) {
		
		chars = new SpriteCollection();
		
		this.sheet = sheet;
		this.screen = screen;
		
		// Set starting offset of font collection in the sprite.
		// These are located in the last three rows
		int xTiles = sheet.width / sheet.tileSize;
		int yTiles = sheet.height / sheet.tileSize;
		int offset = (yTiles - 3) * xTiles;
		
		for (int y = 0; y < charTable.length; y++) {
			for (int x = 0; x < charTable[y].length(); x++) {
				
				chars.set(Character.toString(charTable[y].charAt(x)), new Sprite(sheet, offset + y * xTiles + x));
			}
		}
	}

	public void draw(String text, int x, int y, int colors) {

	}
}