package cn.wycode.drop.screen;

import cn.wycode.drop.DropGame;
import cn.wycode.drop.GameSettings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * 主菜单
 * Created by wangyu on 16/5/3.
 */
public class MainMenuScreen implements Screen {

    final DropGame game;
    OrthographicCamera camera;

    public MainMenuScreen(final DropGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "接元宝", GameSettings.SCREEN_WIDTH / 2 - 50, GameSettings.SCREEN_HEIGHT / 2 + 100);
        game.font.draw(game.batch, "点击任意位置开始", GameSettings.SCREEN_WIDTH / 2 - 120, GameSettings.SCREEN_HEIGHT / 2);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
