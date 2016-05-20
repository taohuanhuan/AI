package app.cn.aiyouv.www.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/27.
 */
public class LoginStatus implements Serializable {
    private String name;
    private String fensi;
    private String dianzan;
    private String guanzhu;
    private String img;
    private String rank;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFensi() {
        return fensi;
    }

    public void setFensi(String fensi) {
        this.fensi = fensi;
    }

    public String getDianzan() {
        return dianzan;
    }

    public void setDianzan(String dianzan) {
        this.dianzan = dianzan;
    }

    public String getGuanzhu() {
        return guanzhu;
    }

    public void setGuanzhu(String guanzhu) {
        this.guanzhu = guanzhu;
    }
}
