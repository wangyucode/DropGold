package cn.wycode.drop.desktop;

import cn.wycode.drop.DropGame;
import cn.wycode.drop.GameSettings;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "接元宝";
		config.width = GameSettings.SCREEN_WIDTH;
		config.height = GameSettings.SCREEN_HEIGHT;
		new LwjglApplication(new DropGame(), config);
	}
}
