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
    //TODO 添加金币代码
    private Texture coin;
    //TODO 添加元宝代码
    private Texture yGold;
    private Texture dropImage;
    private Texture bucketImage;
    //TODO 炸弹
    private Texture blast;

    private Music rainMusic;
    private Sound dropSound;

    private OrthographicCamera camera;
    //桶的位置
    private Rectangle bucketRect;
    //矢量
    private Vector3 touchPos;
   //里面存放了所有下落物体的 位置属性
    private Array<DropGold> rainDrops;
    private long lastDropTime;

    private int score;

    //文字相关的array
    private Array<GetScore> getScoreDrops;
    public GameScreen(DropGame game) {

        this.game = game;

        dropImage = new Texture(Gdx.files.internal("img/gold.png"));
        //TODO 添加金币代码
        coin=new Texture(Gdx.files.internal("img/gold2.png"));
        //TODO 添加元宝代码
        yGold=new Texture(Gdx.files.internal("img/gold1.png"));
        bucketImage = new Texture(Gdx.files.internal("img/bucket.png"));
        blast=new Texture(Gdx.files.internal("img/blast.png"));

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

        rainDrops = new Array<DropGold>();
        getScoreDrops=new Array<GetScore>();
        doRainDrop();

        score = 0;
    }

    private void doRainDrop() {
        Rectangle rainRect = new Rectangle();
        rainRect.x = MathUtils.random(0, GameSettings.SCREEN_WIDTH - 64);
        rainRect.y = GameSettings.SCREEN_HEIGHT;
        rainRect.width = 64;
        rainRect.height = 64;
        rainDrops.add(new DropGold(rainRect,0));
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

        for (DropGold rainRect : rainDrops) {
            //TODO 添加金币代码
            if(rainRect.rainRect.x%5==0){
                game.batch.draw(coin, rainRect.rainRect.x, rainRect.rainRect.y);
                rainRect.score=1;
            }else if(rainRect.rainRect.x%3==0){
                game.batch.draw(yGold, rainRect.rainRect.x, rainRect.rainRect.y);
                rainRect.score=2;
            } else if(rainRect.rainRect.x%7==0){
                game.batch.draw(dropImage, rainRect.rainRect.x, rainRect.rainRect.y);
                rainRect.score=3;
            }else{
                game.batch.draw(blast, rainRect.rainRect.x, rainRect.rainRect.y);
                rainRect.score=-50;
            }

        }
        //TODO 展示文字代码
        for(GetScore score:getScoreDrops){
            game.font.draw(game.batch, score.getScore , score.rainRect.x, score.rainRect.y);
        }
        //TODO 判断是否移除获得分数
        for(int i=0;i<getScoreDrops.size;i++){
            if(TimeUtils.nanoTime() - getScoreDrops.get(i).createTime > 400000000){
                getScoreDrops.removeIndex(i);
            }
        }
        game.font.draw(game.batch, "分数" + score , 20, GameSettings.SCREEN_HEIGHT - 20);
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

        Iterator<DropGold> iterator = rainDrops.iterator();
        while (iterator.hasNext()) {
            DropGold rainDrop = iterator.next();
            rainDrop.rainRect.y -= 200 * Gdx.graphics.getDeltaTime();
            if (rainDrop.rainRect.y + 64 < 0) {
                iterator.remove();
            }
            if (rainDrop.rainRect.overlaps(bucketRect)) {
                dropSound.play();
                //TODO 分数展示
                Rectangle rainRect = new Rectangle();
                rainRect.x = bucketRect.x;
                rainRect.y = 94;
                rainRect.width = 64;
                rainRect.height = 20;
                //TODO 此处需要区分是多少分的数据
                getScoreDrops.add(new GetScore(rainRect,(rainDrop.score>0?"+":"")+rainDrop.score,TimeUtils.nanoTime()));
                score += rainDrop.score;
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
