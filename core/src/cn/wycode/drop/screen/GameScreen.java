package cn.wycode.drop.screen;

import cn.wycode.drop.DropGame;
import cn.wycode.drop.GameSettings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {

    private final DropGame game;

    private Texture dropImage;
    private Texture bucketImage;

    private Music rainMusic;
    private Sound dropSound;

    private OrthographicCamera camera;

    private Rectangle bucketRect;
    private Vector3 touchPos;

    private Array<Rectangle> rainDrops;
    private long lastDropTime;

    private int score;

    public GameScreen(DropGame game) {

        this.game = game;

        dropImage = new Texture(Gdx.files.internal("img/droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("img/bucket.png"));

        // load the drop sound effect and the rain background "music"
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/rain.mp3"));
        dropSound = Gdx.audio.newSound(Gdx.files.internal("sound/drop.wav"));

        rainMusic.setLooping(true);
        rainMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);


        bucketRect = new Rectangle();
        bucketRect.x = GameSettings.SCREEN_WIDTH / 2 - 64 / 2;
        bucketRect.y = 20;
        bucketRect.width = 64;
        bucketRect.height = 64;

        touchPos = new Vector3();

        rainDrops = new Array<Rectangle>();
        doRainDrop();

        score = 0;
    }

    private void doRainDrop() {
        Rectangle rainRect = new Rectangle();
        rainRect.x = MathUtils.random(0, GameSettings.SCREEN_WIDTH - 64);
        rainRect.y = GameSettings.SCREEN_HEIGHT;
        rainRect.width = 64;
        rainRect.height = 64;
        rainDrops.add(rainRect);
        lastDropTime = TimeUtils.nanoTime();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);


        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();

        game.batch.draw(bucketImage, bucketRect.x, bucketRect.y);

        for (Rectangle rainRect : rainDrops) {
            game.batch.draw(dropImage, rainRect.x, rainRect.y);
        }

        game.font.draw(game.batch, "共接到" + score + "滴", 20, GameSettings.SCREEN_HEIGHT - 20);

        game.batch.end();

        //Touch input
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucketRect.x = touchPos.x - 64 / 2;
        }

        //DeskTop & HTML5 input
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            bucketRect.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            bucketRect.x += 200 * Gdx.graphics.getDeltaTime();

        //make sure our bucket stays within the screen limits
        if (bucketRect.x < 0)
            bucketRect.x = 0;
        if (bucketRect.x > GameSettings.SCREEN_WIDTH - 64)
            bucketRect.x = GameSettings.SCREEN_WIDTH - 64;

        if (TimeUtils.nanoTime() - lastDropTime > 200000000)
            doRainDrop();

        Iterator<Rectangle> iterator = rainDrops.iterator();
        while (iterator.hasNext()) {
            Rectangle rainDrop = iterator.next();
            rainDrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (rainDrop.y + 64 < 0) {
                iterator.remove();
            }
            if (rainDrop.overlaps(bucketRect)) {
                dropSound.play();
                score += 1;
                iterator.remove();
            }
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
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
    }
}
