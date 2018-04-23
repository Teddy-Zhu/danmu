package com.silentgo.danmu.client.douyu.model.msg;

import com.silentgo.danmu.client.douyu.model.DouyuBaseMsg;

public class BcBuyDeserveMsg extends DouyuBaseMsg {
    //房间 ID
    private String rid;
    //弹幕分组 ID
    private String gid;

    //用户等级
    private String level;
    //赠送数量
    private String cnt;

    //赠送连击次数
    private String hits;
    //酬勤等级
    private String lev;
    //用户信息序列化字符串，详见下文。注意，此处为嵌套序列化，需注 意符号的转义变换。(转义符号参见 2.2 序列化)
    private String sui;
    //扩展字段，一般不使用，可忽略
    private String sahf;

    public BcBuyDeserveMsg() {
    }

    public BcBuyDeserveMsg(DouyuBaseMsg message) {
        super(message);
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getLev() {
        return lev;
    }

    public void setLev(String lev) {
        this.lev = lev;
    }

    public String getSui() {
        return sui;
    }

    public void setSui(String sui) {
        this.sui = sui;
    }

    public String getSahf() {
        return sahf;
    }

    public void setSahf(String sahf) {
        this.sahf = sahf;
    }
}
