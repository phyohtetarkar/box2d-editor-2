package com.phyohtet.bodyeditor;

import aurelienribon.bodyeditor.Ctx;
import aurelienribon.bodyeditor.canvas.Canvas;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.phyohtet.bodyeditor.PhysicsBodyEditor;

import java.io.File;
import java.io.IOException;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Physics Body Editor");
		config.setWindowedMode(1080, 720);
		new Lwjgl3Application(new PhysicsBodyEditor(), config);
	}

	private static void parseArgs(String[] args) {
		for (int i = 1; i < args.length; i++) {
			if (args[i - 1].equals("-f")) {
				try {
					File file = new File(args[i]).getCanonicalFile();
					Ctx.io.setProjectFile(file);
					Ctx.io.importFromFile();
				} catch (IOException ignored) {
				}
			}
		}
	}
}
