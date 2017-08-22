package com.huotu.mall.wenslimall.partnermall.entity;

/**
 * Created by Shoon on 2017/5/5.
 */

public class GiftPathEntity {

    /**
     * id : 1
     * name : test gift longlonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglong
     * introduction : Introduction longlonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglong
     * description : 礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情礼品详情
     * ar_type : video
     * created_at : 2017-04-19 11:50:53
     * updated_at : 2017-04-19 11:50:53
     * img_url : http://tocode.oss-cn-shanghai.aliyuncs.com/2017/04/08/bff2254ef1da71076334cc93e672dd7f.Minutes to Midnight.jpg
     * ar_url : http://tocode.oss-cn-shanghai.aliyuncs.com/2017/04/08/bff2254ef1da71076334cc93e672dd7f.Minutes to Midnight.jpg
     * h5_url : http://localhost/mobile/gift/1
     */

    public GiftGridData getGiftData() {
        GiftGridData data = new GiftGridData();
        data.setGiftTitle(name);
        data.setType(ar_type);
        data.setWebUrl(h5_url);
        data.setImageUrl(img_url);
        data.setDownloadUrl(ar_url);
        data.setId(id);
        data.setAr_name(ar_name);
        return data;
    }


    private int id;
    private String name;
    private String introduction;
    private String description;
    private String ar_type;
    private String created_at;
    private String updated_at;
    private String img_url;
    private String ar_url;
    private String h5_url;
    private String ar_name;

    public String getAr_name() {
        return ar_name;
    }

    public void setAr_name(String ar_name) {
        this.ar_name = ar_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAr_type() {
        return ar_type;
    }

    public void setAr_type(String ar_type) {
        this.ar_type = ar_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getAr_url() {
        return ar_url;
    }

    public void setAr_url(String ar_url) {
        this.ar_url = ar_url;
    }

    public String getH5_url() {
        return h5_url;
    }

    public void setH5_url(String h5_url) {
        this.h5_url = h5_url;
    }
}
