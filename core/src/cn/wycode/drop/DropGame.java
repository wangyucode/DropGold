package cn.wycode.drop;

import cn.wycode.drop.screen.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 主类,负责全局属性的初始化 afgajfgak
 * Created by wangyu on 16/5/3.
 */
public class DropGame extends Game {

    public SpriteBatch batch;
    public BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        //Use LibGDX's default Arial font.
        font = new BitmapFont(Gdx.files.internal("font/text.fnt"),Gdx.files.internal("font/text.png"),false);
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();//important!
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
    }
}
