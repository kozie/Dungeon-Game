package com.kozie.dungeon;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.MemoryImageSource;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.kozie.dungeon.gfx.Colors;
import com.kozie.dungeon.gfx.Font;
import com.kozie.dungeon.gfx.Screen;
import com.kozie.dungeon.gfx.Sprite;
import com.kozie.dungeon.gfx.SpriteSheet;
import com.kozie.dungeon.world.World;

public class GameComponent extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 400;
	public static final int HEIGHT = 320;
	public static final int SCALE = 2;
	public static final String TITLE = "Dungeon";
	
	protected static GameComponent instance;

	public boolean running = true;
	private double framerate = 60;
	private int fps;
	public int deltaTime;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	public int[] colors = new int[6 * 6 * 6];
	
	public Screen screen;
	public SpriteSheet spritesheet;
	public Font font;
	
	private Sprite cursor;
	private int cursorColors;
	private int cursorDragColors;

	public KeyboardListener keyListener;
	public MouseListener mouseListener;
	
	public World world;
	public GameConfig config;

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

		// Initialize new screen for managing screen pixels and
		// a main spritesheet manager
		screen = new Screen(WIDTH, HEIGHT, this);
		spritesheet = new SpriteSheet(GameComponent.class.getResourceAsStream("/main.png"), 16);
		font = new Font(spritesheet, screen);
		
		// Hide the cursor
		int[] empty = new int[16 * 16];
		Image cursorImg = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, empty, 0 ,16));
		Cursor emptyCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "cursor");
		setCursor(emptyCursor);
		
		// Set custom cursor
		cursor = new Sprite(spritesheet, 36 * 40);
		cursorColors = Colors.get(-1, 444, 111, 555);
		cursorDragColors = Colors.get(-1, 444, 500, 555);
		
		requestFocus();
		
		// Initiate first level (world)
		world = new World(1);
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

			// Pause the game if not focused
			if (!hasFocus()) {
				keyListener.release();
				mouseListener.release();

				/*lastRun = System.nanoTime();
				lastTimer = System.currentTimeMillis();
				lastTickTime = lastTimer;

				continue;*/
			}
			
			// TODO Exit if esc is pressed
			if (keyListener.esc.pressed) {
				stop();
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

			// Get or initialize the buffer strategy
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
				//System.out.println(fps + " " + deltaTime);
				frames = 0;
			}
		}
		
		// Close when done
		System.exit(0);
	}

	public void tick() {

	}

	public void render(Graphics2D g) {

		// Temporary for benchmark
		screen.test(this);
		
		// Render some info / Statistics
		int col = Colors.get(-1, -1, 111, 555);
		font.draw(fps + "fps", 1, 1, col);
		font.draw(deltaTime + "ms", 38, 1, col);
		/*font.draw("x:" + mouseListener.mouseX + ", y:" + mouseListener.mouseY, 2, 10, col);
		if (mouseListener.left && mouseListener.right) {
			font.draw("left and right pressed", 2, 18, col);
		} else if (mouseListener.left) {
			font.draw("left pressed", 2, 18, col);
		} else if (mouseListener.right) {
			font.draw("right pressed", 2, 18, col);
		} else {
			font.draw("none pressed", 2, 18, col);
		}*/
		
		// TODO Add duck for testing
		Sprite duck = new Sprite(spritesheet, 0, 2, 2);
		screen.render(duck, 40, 40);
		
		// Render the custom cursor
		int cursorCol = (mouseListener.isDrag()) ? cursorDragColors : cursorColors;
		screen.render(cursor, mouseListener.mouseX, mouseListener.mouseY, cursorCol);
		
		// Draw screen info onto the buffered image
		for (int i = 0; i < screen.pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}

		// Draw the graphics to canvas
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}

	public GameComponent() {
		config = new GameConfig();
		
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		keyListener = new KeyboardListener(this);
		mouseListener = new MouseListener(this);
	}
	
	public static GameComponent getInstance() {
		if (instance == null) {
			instance = new GameComponent();
		}
		
		return instance;
	}

	public static void main(String[] args) {

		GameComponent game = getInstance();
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