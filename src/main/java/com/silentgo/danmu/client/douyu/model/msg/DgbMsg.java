package com.silentgo.danmu.client.douyu.model.msg;

import com.silentgo.danmu.client.douyu.model.DouyuBaseMsg;

public class DgbMsg extends DouyuBaseMsg {

	private String rid;
	/**
	 * 礼物 id
	 */
	private String gfid;
	/**
	 * 礼物显示样式
	 */
	private String gs;
	private String uid;
	private String nn;
	private String ic;
	private String eid;
	private String level;
	/**
	 * 主播体重
	 */
	private String dw;
	/**
	 * 礼物连击次数
	 */
	private String hits;
	private String ct;
	private String cm;
	private String bnn;
	private String bl;
	private String brid;
	private String hc;
	private String sahf;
	
//	type@=dgb/rid@=291514/gfid@=713/gs@=2/uid@=37276862/nn@=逍遥侠士/ic@=avatar@Sdefault@S13/eid@=0/level@=13/dw@=81872024/ct@=0/cm@=0/bnn@=/bl@=0/brid@=0/hc@=/sahf@=0/
	
	public DgbMsg(DouyuBaseMsg message) {
		super(message);
		rid = list.get("rid");
		gfid = list.get("gfid");
		gs = list.get("gs");
		uid = list.get("uid");
		nn = list.get("nn");
		ic = list.get("ic");
		eid = list.get("eid");
		level = list.get("level");
		dw = list.get("dw");
		hits = list.get("hits");
		ct = list.get("ct");
		cm = list.get("cm");
		bnn = list.get("bnn");
		bl = list.get("bl");
		brid = list.get("brid");
		hc = list.get("hc");
		sahf = list.get("sahf");
	}

	@Override
	public String toString() {
		return String.format("[礼物][%s(L%s)]:gs(%s):gfid(%s)x %s",nn,level,gs,gfid,hits==null ?"1":hits);
	}

}
