package cn.wycode.drop.screen;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by huangyi on 16/5/4.
 */
public class GetScore {
    public GetScore(Rectangle rainRect, String getScore, long createTime){
        this.rainRect=rainRect;
        this.getScore=getScore;
        this.createTime=createTime;
    }
    public   Rectangle rainRect;//位置
    public  String getScore;//获得分数
    public long createTime;//创建时间
}
