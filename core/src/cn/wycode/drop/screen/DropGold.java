package cn.wycode.drop.screen;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by huangyi on 16/5/4.
 */
public class DropGold {
    public DropGold(Rectangle rainRec,int score){
        this.rainRect=rainRec;
        this.score=score;
    }
    public Rectangle rainRect;//位置
    public  int score;//分数
}
