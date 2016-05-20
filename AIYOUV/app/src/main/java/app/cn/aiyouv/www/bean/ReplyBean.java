package app.cn.aiyouv.www.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/30.
 */
public class ReplyBean  implements Serializable{
    private String replyId;
    private String name;
    private String ico;
    private String time;
    private String content;

    private ArrayList<ReplyBean> replyBeans;

    public ArrayList<ReplyBean> getReplyBeans() {
        return replyBeans;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReplyBeans(ArrayList<ReplyBean> replyBeans) {
        this.replyBeans = replyBeans;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
