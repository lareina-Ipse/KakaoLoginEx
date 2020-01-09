package com.example.kakaologinex.sns;

import android.app.Activity;
import android.content.Intent;

import com.example.kakaologinex.base.ICallback;
import com.example.kakaologinex.model.SnsProfile;
import com.example.kakaologinex.util.LogUtil;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;


public class KakaoAuth {

    /*
     * =============================================================================
     *                                   Variable
     * =============================================================================
     */

    private static final String TAG = KakaoAuth.class.getSimpleName();

    // sns 이름
    private static final String SNS_NAME = "kakao";

    private Activity mActivity;

    // 카카오
    private SessionCallback mSessionCallback;

    // 콜백
    private ICallback mICallback;

    // 유저정보
    private SnsProfile mSnsProfile;

    private boolean isSessionOpened;

    /*
     * =============================================================================
     *                                   System Logic
     * =============================================================================
     */

    /*
     * =============================================================================
     *                                   Custom Logic
     * =============================================================================
     */

    // 생성자
    public KakaoAuth(Activity activity, ICallback callback) {
        mActivity = activity;
        mICallback = callback;

        init();
    }

    // 객체 초기화
    public void init() {
        // 카카오 콜백 생성
        mSessionCallback = new SessionCallback();
        // 카카오 콜백 등록
        Session.getCurrentSession().addCallback(mSessionCallback);
        // 카카오 토큰 갱신처리
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    // 카카오 로그인 유무
    public boolean isLogin() {
        boolean isAvailable = Session.getCurrentSession().getTokenInfo().hasValidRefreshToken();
        return Session.getCurrentSession().isOpened() && isAvailable;
    }

    // 카카오 로그아웃
    public void logout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                LogUtil.i(TAG, "kakao logout");
                close();
            }
        });
    }

    // 카카오 연동해제
    public void unlink() {
        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onNotSignedUp() {
                LogUtil.i(TAG, "unlink() : not signedup kakao");
            }

            @Override
            public void onSuccess(Long result) {
                close();

                if (mICallback != null) {
                    mICallback.unlink();
                }
            }
        });
    }

    // 카카오 콜백 해제
    public void close() {
        Session.getCurrentSession().removeCallback(mSessionCallback);
        isSessionOpened = false;
    }

    // 카카오 로그인 결과
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        boolean isLogin = Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data);

        return isLogin;
    }

    /*
     * =============================================================================
     *                                     Listener
     * =============================================================================
     */

    /*
     * =============================================================================
     *                        Class, Handler, BroadcastReceiver
     * =============================================================================
     */

    // 카카오 세션
    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() { // 세션 연결 성공
            LogUtil.i(TAG, "onSessionOpened : " + isSessionOpened);
            if (!isSessionOpened) {
                isSessionOpened = true;

                if (mICallback != null) {
                    mICallback.login();
                }

                // 유저정보
                UserManagement.getInstance().me(new MeV2ResponseCallback() {
                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                    }

                    @Override
                    public void onSuccess(MeV2Response result) {
                        String id = "" + result.getId();
                        String email = "" + result.getKakaoAccount().getEmail();
                        mSnsProfile = new SnsProfile(SNS_NAME, id, email);
                        if (mICallback != null) {
                            mICallback.profile(mSnsProfile);
                        }
                    }
                });
            }
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) { // 세연 연결 실패
            if (exception != null) {
                Logger.e(exception);
                LogUtil.i(TAG, "onSessionOpenFailed() : kakao error " + exception.toString());
            }
        }
    }
}
