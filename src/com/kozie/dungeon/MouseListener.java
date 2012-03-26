package com.kozie.dungeon;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseListener implements java.awt.event.MouseListener, MouseMotionListener {

	public boolean left = false;
	public boolean right = false;
	public boolean isDrag = false;
	public int mouseX, mouseY;
	
	public MouseListener(GameComponent game) {

		game.addMouseListener(this);
		game.addMouseMotionListener(this);
	}

	public void mouseMoved(MouseEvent e) {

		isDrag = false;
		
		mouseX = e.getX() / GameComponent.SCALE;
		mouseY = e.getY() / GameComponent.SCALE;
	}
	
	public void mouseDragged(MouseEvent e) {
		
		// Drag only while the left button is pressed
		isDrag = left;
		
		mouseX = e.getX() / GameComponent.SCALE;
		mouseY = e.getY() / GameComponent.SCALE;
	}

	public void mousePressed(MouseEvent e) {
		toggleButton(e, true);
	}

	public void mouseReleased(MouseEvent e) {
		toggleButton(e, false);
	}

	private void toggleButton(MouseEvent e, boolean state) {

		switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				left = state;
				break;
			case MouseEvent.BUTTON2:
			case MouseEvent.BUTTON3:
				right = state;
				break;
		}
	}
	
	public boolean isDrag() {
		
		return isDrag && left;
	}
	
	public void release() {
		left = false;
		right = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}