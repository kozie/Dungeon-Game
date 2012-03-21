package com.kozie.dungeon.gfx;

public class Colors {
	
	public static int get(int a, int b, int c, int d) {
		
		a = get(a);
		b = get(b);
		c = get(c);
		d = get(d);
		
		return (d << 24) | (c << 16) | (b << 8) | a;
	}
	
	public static int get(int c) {
		
		if (c < 0) return 255;
		
		int r = c / 100;
		int g = c / 10 % 10;
		int b = c % 10;
		
		return r * 36 + g * 6 + b;
	}
}