package app.cn.aiyouv.www.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/30.
 */
public class CateImp implements Serializable {
    private String id;
    private String tag;
    private ArrayList<Cate> cates;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ArrayList<Cate> getCates() {
        return cates;
    }

    public void setCates(ArrayList<Cate> cates) {
        this.cates = cates;
    }
}
