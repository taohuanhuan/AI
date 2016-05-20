package app.cn.aiyouv.www.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/1.
 */
public class DataBean implements Serializable {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
