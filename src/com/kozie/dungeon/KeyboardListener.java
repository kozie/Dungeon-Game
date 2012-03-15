package com.kozie.dungeon;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class KeyboardListener implements KeyListener {

	public class Key {

		public boolean pressed = false;

		public Key() {
			keys.add(this);
		}

		public void isPressed(boolean state) {
			pressed = state;
		}
	}

	public List<Key> keys = new ArrayList<Key>();
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key tab = new Key();
	public Key qKey = new Key();
	public Key eKey = new Key();
	public Key shift = new Key();
	public Key space = new Key();

	public KeyboardListener(GameComponent game) {
		game.addKeyListener(this);
	}

	public void release() {
		for (int i = 0; i < keys.size(); i++) {
			keys.get(i).isPressed(false);
		}
	}

	public void keyPressed(KeyEvent e) {
		toggleKey(e, false);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e, false);
	}

	private void toggleKey(KeyEvent e, boolean state) {

		switch (e.getKeyCode()) {

			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				up.isPressed(state);
				break;
	
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				down.isPressed(state);
				break;
	
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				left.isPressed(state);
				break;
	
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				right.isPressed(state);
				break;
	
			case KeyEvent.VK_TAB:
				tab.isPressed(state);
				break;
	
			case KeyEvent.VK_Q:
				qKey.isPressed(state);
				break;
	
			case KeyEvent.VK_E:
				eKey.isPressed(state);
				break;
	
			case KeyEvent.VK_SHIFT:
				shift.isPressed(state);
				break;
	
			case KeyEvent.VK_SPACE:
				space.isPressed(state);
				break;
		}
	}

	public void keyTyped(KeyEvent e) {
	}

}