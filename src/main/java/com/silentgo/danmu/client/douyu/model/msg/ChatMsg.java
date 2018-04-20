package com.silentgo.danmu.client.douyu.model.msg;

import com.silentgo.danmu.client.douyu.model.DouyuBaseMsg;

public class ChatMsg extends DouyuBaseMsg {

    /**
     * 房间 ID
     */
    private String rid;

    /**
     * 客户端类型：默认值 0（表示 web 用户）
     */
    private String ct;

    /**
     * 用户 ID
     */
    private String uid;
    /**
     * 用户昵称
     */
    private String nn;
    /**
     * 弹幕文本内容
     */
    private String txt;
    /**
     * 弹幕唯一 ID
     */
    private String cid;

    /**
     * 图标
     * avanew@Sface@S201706@S08@S21@S05 e00080744c2fb42121d17f0ee68bfe
     * <p>
     * avatar@Sdefault@S13
     */
    private String ic;
    /**
     * 用户等级
     */
    private String level;

    /**
     * 0
     */
    private String sahf;
    /**
     * 粉丝勋章
     */
    private String bnn;
    /**
     * 粉丝勋章等级
     */
    private String bl;
    /**
     * 粉丝勋章所在的房间号
     */
    private String brid;

    /**
     * 32位的字符，hash
     */
    private String hc;
    private String el;

    public ChatMsg(DouyuBaseMsg message) {
        super(message);
        rid = list.get("rid");
        ct = list.get("ct");
        uid = list.get("uid");
        nn = list.get("nn");
        txt = list.get("txt");
        cid = list.get("cid");
        ic = list.get("ic");
        level = list.get("level");
        sahf = list.get("sahf");
        bnn = list.get("bnn");
        bl = list.get("bl");
        brid = list.get("brid");
        hc = list.get("hc");
        el = list.get("el");

        user = nn;
        if (txt != null) {
            // @S --> /
            // @A --> @
            // \\ --> \
            txt = txt.replace("@S", "/").replace("@A", "@").replace("\\\\", "\\");
        } else {
            txt = "";
        }
    }

    @Override
    public String toString() {
        return String.format("[弹幕][%s(L%s)]:%s", nn, level, txt);
    }

}
