package com.silentgo.danmu.client.douyu.model;

import com.silentgo.danmu.base.BaseMsg;

import java.util.LinkedHashMap;


public class DouyuBaseMsg extends BaseMsg {

    protected LinkedHashMap<String, String> list;

    //(1) 多个键值对数据:key1@=value1/key2@=value2/key3@=value3/
    //(2) 数组数据:value1/value2/value3/
    public DouyuBaseMsg() {
    }

    public DouyuBaseMsg(String message) {
//        1 键 key 和值 value 直接采用‘@=’分割
//        2 数组采用‘/’分割
//        3 如果 key 或者 value 中含有字符‘/’，则使用‘@S’转义
//         4 如果 key 或者 value 中含有字符‘@’，使用‘@A’转义
        this(message, "/", "@=");
    }

    public DouyuBaseMsg(DouyuBaseMsg message) {
        this.content = message.content;
        this.type = message.type;
        this.list = new LinkedHashMap<>(message.list);
    }

    public DouyuBaseMsg(String message, String split1, String split2) {
        this.content = message;
        this.list = parser(message, split1, split2);
        this.type = list.get("type");
    }

    public static LinkedHashMap<String, String> parser(String message, String split1, String split2) {
        LinkedHashMap<String, String> list = new LinkedHashMap<>();
        if (message != null) {
            message = message.trim();

            String[] splits1 = message.split(split1);
            for (String s : splits1) {
                String[] splits2 = s.split(split2);
                if (splits2.length == 2) {
                    list.put(splits2[0], splits2[1]);
                }
            }
        }
        return list;
    }

    public LinkedHashMap<String, String> getList() {
        return list;
    }

    public void setList(LinkedHashMap<String, String> list) {
        this.list = list;
    }
}
