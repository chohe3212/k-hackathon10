package org.taehyeon.welcome_pet_khackathon.Community;

public class commentInfo {

    String cId, comment , timeStamp, uid, Name;

    public commentInfo() {
    }

    public commentInfo(String cId, String comment, String timeStamp, String uid,String name) {
        this.cId = cId;
        this.comment = comment;
        this.timeStamp = timeStamp;
        this.uid = uid;
        this.Name = name;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }
}
