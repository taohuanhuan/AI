package app.cn.aiyouv.www.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/17.
 */
public class BillTableBean implements Serializable {
    private String name;
    private String time;
    private String price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
