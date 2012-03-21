package com.kozie.dungeon.gfx;

import java.util.Random;

import com.kozie.dungeon.GameComponent;

public class Screen {

	public int width, height;
	public int[] pixels;
	
	private GameComponent game;

	public Screen(int width, int height, GameComponent game) {

		this.width = width;
		this.height = height;
		this.game = game;
		
		pixels = new int[width * height];
	}
	
	public void render(Sprite sprite, int x, int y, int colors) {
		
		for (int yy = 0; yy < sprite.height; yy++) {
			for (int xx = 0; xx < sprite.width; xx++) {
				//System.out.println(colors >> 24);
				int col = (colors >> (sprite.pixels[yy * sprite.width + xx] * 8)) & 0xFF;
				if (col < 255) {
					
					int i = y * width + yy * width + x + xx;
					if (i > 0 && i < pixels.length) {
						pixels[i] = 0xFF << 24 | game.colors[col];
					}
				}
			}
		}
	}

	public void test(GameComponent game) {

		// Temp for benchmarking
		Random r = new Random();
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0xFF << 24 | game.colors[r.nextInt(game.colors.length)];
		}
	}
}