package app.cn.aiyouv.www.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/21.
 */
public class Fs_Bean implements Serializable {
    private String name;
    private String id;
    private String pic;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
