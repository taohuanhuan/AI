package app.cn.aiyouv.www.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/27.
 */
public class Article  implements Serializable{
    private String id;
    private String title;
    private String time;
    private String content;
    private String author;
    private String hits;
    private double latitude;
    private double longitude;
    private ArrayList<DetailPics> detailPicses;

    public ArrayList<DetailPics> getDetailPicses() {
        return detailPicses;
    }

    public void setDetailPicses(ArrayList<DetailPics> detailPicses) {
        this.detailPicses = detailPicses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
