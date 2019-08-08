package com.xhttp.org.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 成就
 * Created by lixingxing on 2019/5/28.
 */
public class AchievnmentModel implements Parcelable {
    /**
     * id : 1
     * activityId : e6d4f743bde14248b8f0b761218a08a6
     * interactId : null
     * status : 0
     * nameCn : 中文名称
     * nameEn : English name
     * coverImg : http://baidu.com
     * rewardCn : 中文奖励
     * rewardEn : English Reword
     * points : 1000
     * positionCn : null
     * positionEn : null
     * taskNum : 4
     * taskCompletedNum : 1
     * ruleCn : 中文规则1
     * ruleEn : Enlish Rule
     * introductionCn : null
     * introductionEn : null
     * removed : false
     * show : true
     * created : 2019-05-08 11:52:46
     * updated : 2019-05-08 14:15:32
     * operator : admin
     */

    public int count = 0;

    public int id;
    public String activityId;
    public String interactId;
    public int status;
    public String nameCn;
    public String nameEn;
    public String coverImg;
    public String rewardCn;
    public String rewardEn;
    public int points;
    public String positionCn;
    public String positionEn;
    public int taskNum;
    public int taskCompletedNum;
    public String ruleCn;
    public String ruleEn;
    public String introductionCn;
    public String introductionEn;
    public boolean removed;
    public boolean show;
    public String created;
    public String updated;
    public String operator;

    public AchievnmentModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeInt(this.id);
        dest.writeString(this.activityId);
        dest.writeString(this.interactId);
        dest.writeInt(this.status);
        dest.writeString(this.nameCn);
        dest.writeString(this.nameEn);
        dest.writeString(this.coverImg);
        dest.writeString(this.rewardCn);
        dest.writeString(this.rewardEn);
        dest.writeInt(this.points);
        dest.writeString(this.positionCn);
        dest.writeString(this.positionEn);
        dest.writeInt(this.taskNum);
        dest.writeInt(this.taskCompletedNum);
        dest.writeString(this.ruleCn);
        dest.writeString(this.ruleEn);
        dest.writeString(this.introductionCn);
        dest.writeString(this.introductionEn);
        dest.writeByte(this.removed ? (byte) 1 : (byte) 0);
        dest.writeByte(this.show ? (byte) 1 : (byte) 0);
        dest.writeString(this.created);
        dest.writeString(this.updated);
        dest.writeString(this.operator);
    }

    protected AchievnmentModel(Parcel in) {
        this.count = in.readInt();
        this.id = in.readInt();
        this.activityId = in.readString();
        this.interactId = in.readString();
        this.status = in.readInt();
        this.nameCn = in.readString();
        this.nameEn = in.readString();
        this.coverImg = in.readString();
        this.rewardCn = in.readString();
        this.rewardEn = in.readString();
        this.points = in.readInt();
        this.positionCn = in.readString();
        this.positionEn = in.readString();
        this.taskNum = in.readInt();
        this.taskCompletedNum = in.readInt();
        this.ruleCn = in.readString();
        this.ruleEn = in.readString();
        this.introductionCn = in.readString();
        this.introductionEn = in.readString();
        this.removed = in.readByte() != 0;
        this.show = in.readByte() != 0;
        this.created = in.readString();
        this.updated = in.readString();
        this.operator = in.readString();
    }

    public static final Creator<AchievnmentModel> CREATOR = new Creator<AchievnmentModel>() {
        @Override
        public AchievnmentModel createFromParcel(Parcel source) {
            return new AchievnmentModel(source);
        }

        @Override
        public AchievnmentModel[] newArray(int size) {
            return new AchievnmentModel[size];
        }
    };
}
