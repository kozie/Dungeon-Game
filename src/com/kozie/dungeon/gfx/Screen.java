package com.kozie.dungeon.gfx;

import java.util.Random;

import com.kozie.dungeon.GameComponent;

public class Screen {

	public int width, height;
	public int[] pixels;

	public Screen(int width, int height) {

		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}
	
	public void render(Sprite sprite, int x, int y) {
		
	}

	public void test(GameComponent game) {

		// Temp for benchmarking
		Random r = new Random();
		for (int i = 0; i < pixels.length; i++) {
			// pixels[i] = 0xCC << 24 | r.nextInt(255) << 16 | r.nextInt(255) <<
			// 8 | r.nextInt(255);
			pixels[i] = 0xFF << 24 | game.colors[r.nextInt(game.colors.length)];
		}
	}
}