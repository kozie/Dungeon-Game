package com.kozie.dungeon;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.kozie.dungeon.gfx.Screen;

public class GameComponent extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 400;
	public static final int HEIGHT = 320;
	public static final int SCALE = 2;
	public static final String TITLE = "Dungeon";

	public boolean running = true;
	private double framerate = 60;
	private int fps;
	public int deltaTime;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	public int[] colors = new int[6 * 6 * 6];
	public Screen screen;

	public KeyboardListener keyListener;
	public MouseListener mouseListener;

	public void init() {

		int i = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = r * (255 / 5);
					int gg = g * (255 / 5);
					int bb = b * (255 / 5);

					// Cast to mid tones
					int mid = (rr * 30 + gg * 59 + bb * 11) / 100;
					rr = (rr + mid) / 2;
					gg = (gg + mid) / 2;
					bb = (bb + mid) / 2;

					colors[i++] = rr << 16 | gg << 8 | bb;
				}
			}
		}

		screen = new Screen(WIDTH, HEIGHT);

		requestFocus();
	}

	public void start() {

		running = true;

		Thread thread = new Thread(this);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}

	public void stop() {

		running = false;
	}

	public void run() {

		// Keep track of some times
		long lastRun = System.nanoTime();
		long lastTimer = System.currentTimeMillis();
		long lastTickTime = System.currentTimeMillis();

		double nsPerTick = 1000000000.0 / framerate;
		double unprocessed = 0;
		int toTick = 0;
		int frames = 0;

		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		while (running) {

			// Pause the game if not focussed
			if (!hasFocus()) {
				keyListener.release();

				lastRun = System.nanoTime();
				lastTimer = System.currentTimeMillis();
				lastTickTime = lastTimer;

				continue;
			}

			// Set ticks that are unprocessed
			boolean shouldRender = false;
			while (unprocessed >= 1) {
				toTick++;
				unprocessed -= 1;
			}

			// Set correct amount of ticks to do
			int tickCount = toTick;

			// Just tick once if between 0 and 3 for stability
			if (toTick > 0 && toTick < 3) tickCount = 1;

			// Keep remaining ticks at 20 for stability
			if (toTick > 20) toTick = 20;

			// Go tick some ;)
			for (int i = 0; i < tickCount; i++) {

				long currentTickTime = System.currentTimeMillis();
				deltaTime = (int) (currentTickTime - lastTickTime);

				tick();
				toTick--;

				lastTickTime = currentTickTime;
				shouldRender = true;
			}

			// Get or init the buffer strategy
			BufferStrategy bs = getBufferStrategy();
			if (bs == null) {
				createBufferStrategy(3);
				continue;
			}

			// Render frames if needed
			if (shouldRender) {
				frames++;

				Graphics2D g = (Graphics2D) bs.getDrawGraphics();
				render(g);
			}

			// Check how many ticks are unprocessed after
			// all the ticks and renderings
			long now = System.nanoTime();
			unprocessed += (now - lastRun) / nsPerTick;
			lastRun = now;

			// Sleep for a short while
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Render to canvas if needed
			if (shouldRender && bs != null) {
				bs.show();
			}

			// Update fps stats
			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				fps = frames;
				System.out.println(fps + " " + deltaTime);
				frames = 0;
			}
		}
	}

	public void tick() {

	}

	public void render(Graphics2D g) {

		// Temp for benchmark
		screen.test(this);
		for (int i = 0; i < screen.pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

		// Render FPS on canvas
		g.setColor(Color.YELLOW);
		g.drawString("FPS " + fps, 10, 20);
		g.drawString("X: " + mouseListener.mouseX + ", Y: " + mouseListener.mouseY, 10, 30);

		if (mouseListener.left && mouseListener.right) {
			g.drawString("Left and right pressed", 10, 40);
		} else if (mouseListener.left) {
			g.drawString("Left pressed", 10, 40);
		} else if (mouseListener.right) {
			g.drawString("Right pressed", 10, 40);
		} else {
			g.drawString("None pressed", 10, 40);
		}

	}

	public GameComponent() {

		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

		keyListener = new KeyboardListener(this);
		mouseListener = new MouseListener(this);
	}

	public static void main(String[] args) {

		GameComponent game = new GameComponent();
		JFrame frame = new JFrame();
		JPanel panel = new JPanel(new BorderLayout());

		panel.add(game);
		frame.setContentPane(panel);
		frame.pack();
		frame.setTitle(TITLE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		game.start();
	}

}