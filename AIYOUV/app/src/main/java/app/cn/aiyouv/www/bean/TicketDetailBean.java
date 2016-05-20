package app.cn.aiyouv.www.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/14.
 */
public class TicketDetailBean implements Serializable {
    private String price;
    private String time;
    private String code;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
