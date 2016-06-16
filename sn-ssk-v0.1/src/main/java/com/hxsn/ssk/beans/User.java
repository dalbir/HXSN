package com.hxsn.ssk.beans;

/**
 * "username":"jiwl",	//用户名
 * "realname":"",	//姓名
 * "nickname":"",	//昵称
 * "email":"",	//Email地址
 * "phone":"",	//手机号码
 * "address":""	//地址
 */
public class User {
    String id;
    String name;
    String realName;
    String nickName;
    String email;
    String phone;
    String address;
    String urlHeadImage;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrlHeadImage() {
        return urlHeadImage;
    }

    public void setUrlHeadImage(String urlHeadImage) {
        this.urlHeadImage = urlHeadImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
