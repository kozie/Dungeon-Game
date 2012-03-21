package com.kozie.dungeon.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class SpriteSheet {

	private int[] pixels;
	public int width, height;
	public int tileSize;

	public SpriteSheet(InputStream img, int tileSize) {
		
		// Read image data
		try {
			BufferedImage imgData = ImageIO.read(img);

			width = imgData.getWidth();
			height = imgData.getHeight();
			pixels = imgData.getRGB(0, 0, width, height, null, 0, width);

			// Read out pixels and convert pixels into color locations
			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = (pixels[i] & 0xFF) / 64;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.tileSize = tileSize;
	}
	
	public int[] getTile(int tileOffset, int xTiles, int yTiles) {
		
		int[] sprite = new int[(xTiles * tileSize) * (yTiles * tileSize)];
		
		int offsetX = (tileOffset % (width / tileSize)) * tileSize;
		int offsetY = (tileOffset / (width / tileSize)) * width * tileSize;
		int offset = offsetX + offsetY;
		
		for (int y = 0; y < yTiles * tileSize; y++) {
			for (int x = 0; x < xTiles * tileSize; x++) {
				
				sprite[y * (xTiles * tileSize) + x] = pixels[offset + y * width + x];
			}
		}
		
		return sprite;
	}
	
	public int[] getTile(int offset) {
		
		return getTile(offset, 1, 1);
	}
}