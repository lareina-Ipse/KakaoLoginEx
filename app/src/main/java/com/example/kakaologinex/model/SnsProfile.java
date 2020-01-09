package com.example.kakaologinex.model;

public class SnsProfile {

    // sns 종류 : kakao, naver, google, facebook
    private String sns;

    // sns 사용자 고유 id
    private String id;

    // sns 이메일
    private String email;

    public SnsProfile(String sns, String id, String email) {
        this.sns = sns;
        this.id = id;
        this.email = email;

    }

    public String getSns() {
        return sns;
    }

    public void setSns(String sns) {
        this.sns = sns;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
