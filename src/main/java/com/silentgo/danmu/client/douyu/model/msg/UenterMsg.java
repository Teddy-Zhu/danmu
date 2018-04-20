package com.silentgo.danmu.client.douyu.model.msg;

import com.silentgo.danmu.client.douyu.model.DouyuBaseMsg;

public class UenterMsg extends DouyuBaseMsg {

	private String rid;
	private String uid;
	private String nn;
	private String level;
	private String ic;
	private String rni;
	private String el;
	private String sahf;
	private String wgei;
	
	public UenterMsg(DouyuBaseMsg message) {
		super(message);
		type = list.get("type");
		rid = list.get("rid");
		uid = list.get("uid");
		nn = list.get("nn");
		level = list.get("level");
		ic = list.get("ic");
		rni = list.get("rni");
		el = list.get("el");
		sahf = list.get("sahf");
		wgei = list.get("wgei");
	}
//type@=uenter/rid@=291514/uid@=5990581/nn@=木亦哥哥/level@=45/ic@=avanew@Sface@S201704@S08@S02@S40b2d71d6918cdfc6fbe17c89362903b/rni@=0/el@=/sahf@=0/wgei@=0/

	@Override
	public String toString() {
		return String.format("[用户入场][%s(L%s)] 进入房间(%s)",nn,level,rid);
	}
}
