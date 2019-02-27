package com.qqkjbasepro.org.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/7/14.
 */
public class EventModel implements Parcelable{

    private String proId;
    private String storeId;
    private String storeName;
    private String desc;
    private String startTime;
    private String endTime;
    private String url;
    private String imgUrl;

    private String linkType;
    private String linkValue;


    String proName;//活动名称
    String proDesc;//活动内容
    String linkUrl;//活动链接
    boolean running;//正在进行中
    int type;//0、跳转网页 不能报名; 1、原生，可报名  2 跳转APP内页
    String infoImgUrl;//详情图片
    String address;//地址
    String activityApplyDesc;//报名说明

    Boolean apply;//是否报名 true 已报名false 未报名
    String applyName;//报名填写的用户名
    String applyPhone;//报名手机号


    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkValue() {
        return linkValue;
    }

    public void setLinkValue(String linkValue) {
        this.linkValue = linkValue;
    }

    public Boolean getApply() {
        return apply;
    }

    public void setApply(Boolean apply) {
        this.apply = apply;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getApplyPhone() {
        return applyPhone;
    }

    public void setApplyPhone(String applyPhone) {
        this.applyPhone = applyPhone;
    }

    public int collectId;

    public boolean collectState = false;//是否收藏

    public int getCollectId() {
        return collectId;
    }

    public void setCollectId(int collectId) {
        this.collectId = collectId;
    }

    public String getInfoImgUrl() {
        return infoImgUrl;
    }

    public void setInfoImgUrl(String infoImgUrl) {
        this.infoImgUrl = infoImgUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getActivityApplyDesc() {
        return activityApplyDesc;
    }

    public void setActivityApplyDesc(String activityApplyDesc) {
        this.activityApplyDesc = activityApplyDesc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isCollectState() {
        return collectState;
    }

    public void setCollectState(boolean collectState) {
        this.collectState = collectState;
    }

    public static Creator<EventModel> getCREATOR() {
        return CREATOR;
    }

    public boolean isCollect = false;

    public boolean isCheck = false;

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getProDesc() {
        return proDesc;
    }

    public void setProDesc(String proDesc) {
        this.proDesc = proDesc;
    }


    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public EventModel() {
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && ((EventModel)obj).getProId().equals(proId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.proId);
        dest.writeString(this.storeId);
        dest.writeString(this.storeName);
        dest.writeString(this.desc);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.url);
        dest.writeString(this.imgUrl);
        dest.writeString(this.linkType);
        dest.writeString(this.linkValue);
        dest.writeString(this.proName);
        dest.writeString(this.proDesc);
        dest.writeString(this.linkUrl);
        dest.writeByte(this.running ? (byte) 1 : (byte) 0);
        dest.writeInt(this.type);
        dest.writeString(this.infoImgUrl);
        dest.writeString(this.address);
        dest.writeString(this.activityApplyDesc);
        dest.writeValue(this.apply);
        dest.writeString(this.applyName);
        dest.writeString(this.applyPhone);
        dest.writeInt(this.collectId);
        dest.writeByte(this.collectState ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCollect ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
    }

    protected EventModel(Parcel in) {
        this.proId = in.readString();
        this.storeId = in.readString();
        this.storeName = in.readString();
        this.desc = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.url = in.readString();
        this.imgUrl = in.readString();
        this.linkType = in.readString();
        this.linkValue = in.readString();
        this.proName = in.readString();
        this.proDesc = in.readString();
        this.linkUrl = in.readString();
        this.running = in.readByte() != 0;
        this.type = in.readInt();
        this.infoImgUrl = in.readString();
        this.address = in.readString();
        this.activityApplyDesc = in.readString();
        this.apply = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.applyName = in.readString();
        this.applyPhone = in.readString();
        this.collectId = in.readInt();
        this.collectState = in.readByte() != 0;
        this.isCollect = in.readByte() != 0;
        this.isCheck = in.readByte() != 0;
    }

    public static final Creator<EventModel> CREATOR = new Creator<EventModel>() {
        @Override
        public EventModel createFromParcel(Parcel source) {
            return new EventModel(source);
        }

        @Override
        public EventModel[] newArray(int size) {
            return new EventModel[size];
        }
    };
}