package com.kozie.dungeon.gfx;

public class Font {

	private SpriteCollection chars;
	private Screen screen;

	private String[] charTable = {
			"abcdefghijklmnopqrstuvwxyz",
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ",
			"1234567890!?,."};

	public Font(SpriteSheet sheet, Screen screen) {
		
		chars = new SpriteCollection();
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

		// Draw text on screen at requested position
		for (int i = 0; i < text.length(); i++) {
			
			String c = Character.toString(text.charAt(i));
			
			// Skip non existing characters and spaces
			if (c == " " || chars.get(c) == null) continue;
			
			Sprite sprite = chars.get(c);
			screen.render(sprite, x + i * sprite.width, y, colors);
		}
	}
}