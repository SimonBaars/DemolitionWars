package com.demolitionwars.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.demolitionwars.DemolitionWarsGame;

/**
 * Desktop entry point for local development and headless-friendly testing.
 */
public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle(DemolitionWarsGame.TITLE);
        config.setForegroundFPS(60);
        config.useVsync(true);
        config.setWindowedMode(1280, 720);
        new Lwjgl3Application(new DemolitionWarsGame(), config);
    }
}
