package com.jeoksyeo.wet.activity.login.apple

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.OAuthProvider
import com.jeoksyeo.wet.activity.login.Login
import com.error.ErrorManager
import com.google.android.gms.tasks.OnCompleteListener
import java.lang.Exception
import java.lang.StringBuilder

class AppleLogin(private val activity: Activity) {
    private val provider = OAuthProvider.newBuilder("apple.com")
    private val pending = FirebaseAuth.getInstance().pendingAuthResult
    private var user: FirebaseUser? = null

    init {
        provider.addCustomParameter("locale", "ko_KR")
        provider.scopes = mutableListOf("email", "name")
    }

    fun loginExecute() {
        user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            //동일한 작업을 하고있는지 확인
            if (pending != null) {
                pending.addOnSuccessListener { authResult ->
                    //동일한 작업을 하고 있다면 여기서 정보들을 얻을 수 있음.
                    Log.d(ErrorManager.Apple_TAG, "checkPending:onSuccess:$authResult")

                }.addOnFailureListener { e ->
                    Log.w(ErrorManager.Apple_TAG, "checkPending:onFailure", e)
                }
            } else {
                //동일한 작업을 하고 있지 않을 때.
                // 본격적인 회원가입 절차 진행하면 됨.
                Log.d(ErrorManager.Apple_TAG, "pending: null")

                //firebase에 자신이 지정한 소셜 회사의 로그인 api를 진행
                FirebaseAuth.getInstance()
                    .startActivityForSignInWithProvider(activity, provider.build())
                    .addOnSuccessListener { authResult ->
                        try {
                            user = authResult.user!!
                            //access token
                            user!!.getIdToken(true) //   FirebaseAuth.getInstance().getAccessToken()와 동일
                                .addOnCompleteListener(activity) { task ->
                                    if (task.isSuccessful) {
                                        Log.e(
                                            ErrorManager.Apple_TAG,
                                            "accessToken: " + task.result?.token.toString()
                                        )

                                        Login.loginObj.setUserInfo("APPLE", task.result?.token)

                                    }
                                }
                        } catch (e: Exception) {
                            Log.e(ErrorManager.Apple_TAG, "user object is null")
                        }

                    }
            }.addOnFailureListener { e ->
                Log.e(ErrorManager.Apple_TAG, "activitySignIn:onFailure", e)
            }
        }
    }

    fun appleSignOut() {

        Log.e("애플로그아웃",FirebaseAuth.getInstance().currentUser?.email.toString())
        FirebaseAuth.getInstance().signOut()
    }

    fun appleDelete() {
        FirebaseAuth.getInstance().currentUser?.delete()?.addOnCompleteListener(activity,
            OnCompleteListener {
                if (it.isSuccessful) {
                    //삭제후 핸들링
                    Log.e("애플삭제",FirebaseAuth.getInstance().currentUser?.email.toString())
                }

            })?.addOnFailureListener {
            it.stackTrace
        }
    }
}

