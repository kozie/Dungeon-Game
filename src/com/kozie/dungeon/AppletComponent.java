package com.kozie.dungeon;

import java.applet.Applet;
import java.awt.BorderLayout;

public class AppletComponent extends Applet {

	private static final long serialVersionUID = 1L;
	private GameComponent game;

	public void init() {
		game = new GameComponent();

		setLayout(new BorderLayout());
		add(game, BorderLayout.CENTER);
	}

	public void start() {
		game.start();
	}

	public void stop() {
		game.stop();
	}

}