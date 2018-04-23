package com.silentgo.danmu.client.douyu.model.msg;

import com.silentgo.danmu.client.douyu.model.DouyuBaseMsg;

public class SpbcMsg extends DouyuBaseMsg {
	
	private String sn;
	private String dn;
	private String gn;
	private String gc;
	private String drid;
	private String gs;
	private String gb;
	private String es;
	private String gfid;
	private String eid;
	private String bgl;
	private String ifs;
	private String rid;
	private String gid;
	private String bid;
	private String sid;
	private String cl2;

	public SpbcMsg(DouyuBaseMsg message) {
		super(message);
		sn = list.get("sn");
		dn = list.get("dn");
		gn = list.get("gn");
		gc = list.get("gc");
		drid = list.get("drid");
		gs = list.get("gs");
		gb = list.get("gb");
		es = list.get("es");
		gfid = list.get("gfid");
		eid = list.get("eid");
		bgl = list.get("bgl");
		ifs = list.get("ifs");
		rid = list.get("rid");
		gid = list.get("gid");
		bid = list.get("bid");
		sid = list.get("sid");
		cl2 = list.get("cl2");
	}
	// type@=spbc/sn@=大大棒棒歌/dn@=White55开解说/gn@=火箭/gc@=1/drid@=138286/gs@=6/gb@=1/es@=1/gfid@=196/eid@=143/bgl@=3/ifs@=0/rid@=291514/gid@=0/bid@=30002_1497455820_33/sid@=73345180/cl2@=0/
//	type@=spbc/sn@=超级跳跳跳跳/dn@=木木一mango/gn@=城堡/gc@=1/drid@=1833034/gs@=6/gb@=0/es@=3/gfid@=686/eid@=139/bgl@=3/ifs@=0/rid@=291514/gid@=0/bid@=30127_1497455847_715/sid@=6181901/cl2@=0/

	@Override
	public String toString() {
		return String.format("[礼物广播][%s]->[%s] [%s]",sn,dn,gn);
	}
	
}
