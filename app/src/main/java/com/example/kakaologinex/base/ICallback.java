package com.example.kakaologinex.base;

import kr.co.chience.snslogin.model.SnsProfile;

public interface ICallback {

    // 로그인
    void login();

    // 로그아웃
    void logout();

    // 연동해제
    void unlink();

    // 소셜 프로필 정보
    void profile(SnsProfile profile);
}
