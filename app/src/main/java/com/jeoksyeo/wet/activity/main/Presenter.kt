package com.jeoksyeo.wet.activity.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.adapter.main.AlcholRankAdapter
import com.adapter.main.BannerAdapter
import com.adapter.main.RecommendAlcholAdapter
import com.adapter.navigation.NavigationAdpater
import com.application.GlobalApplication
import com.bumptech.glide.Glide
import com.custom.ViewPagerTransformer
import com.error.ErrorManager
import com.model.navigation.NavigationItem
import com.service.ApiGenerator
import com.service.ApiService
import com.vuforia.engine.wet.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.drawer_navigation.view.*
import kotlinx.android.synthetic.main.fragment_signup_request.view.*
import kotlinx.android.synthetic.main.main.view.*
import kotlinx.android.synthetic.main.main.view.basic_header
import kotlinx.android.synthetic.main.navigation_header.view.*

@SuppressLint("SetTextI18n")
class Presenter : MainContract.BasePresenter {

    override lateinit var view: MainContract.BaseView
    private  var compositeDisposable:CompositeDisposable = CompositeDisposable()
    private val handler = Handler()
    private val bundle = Bundle()
    private val slideRunnable = Runnable {
        view.getView().mainBanner.currentItem  +=1
    }

    override fun initCarouselViewPager(context: Context) {
        val lst = mutableListOf<Int>()
        lst.add(R.mipmap.nuggi)
        lst.add(R.mipmap.alchol_img)
        view.getView().mainBanner.adapter = BannerAdapter(context, lst, view.getView().mainBanner)
        view.getView().mainBanner.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(slideRunnable)
                handler.postDelayed(slideRunnable, 4000)
                view.getView().bannerCount.text = "$position / ${lst.size}"
            }
        })
    }

    override fun initRecommendViewPager(context: Context)  {
        compositeDisposable.add(ApiGenerator.retrofit.create(ApiService::class.java)
            .getRecommendAlchol(
                GlobalApplication.userBuilder.createUUID,
                GlobalApplication.userInfo.getAccessToken()
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.getView().activityMainRecommendViewPager2.adapter = RecommendAlcholAdapter(
                    context,
                    it.data?.alcholList?.toMutableList()!!

                )
            }, { t -> Log.e(ErrorManager.ALCHOL_RECOMMEND,t.message.toString()) }))

        view.getView().activityMainRecommendViewPager2.setPageTransformer(
            ViewPagerTransformer(
                context
            )
        )
        view.getView().activityMainRecommendViewPager2.offscreenPageLimit =3
        view.getView().activityMainRecommendViewPager2.clipToPadding =false
        view.getView().activityMainRecommendViewPager2.clipChildren =false
    }

    override fun initNavigationItemSet(context: Context,activity:Activity,provider: String?) {
        val lst = mutableListOf<NavigationItem>()
        lst.add(NavigationItem(R.mipmap.btn_top_setting, "설정"))
        lst.add(NavigationItem(R.mipmap.nv_profile, "내 프로필"))
        lst.add(NavigationItem(R.mipmap.navigation1_img, "내가 평가한 주류"))
        lst.add(NavigationItem(R.mipmap.navigation2_img, "나의 주류 레벨"))
        lst.add(NavigationItem(R.mipmap.navigation3_img, "내가 찜한 주류"))
        lst.add(NavigationItem(R.mipmap.qna_img, "문의사항"))
        lst.add(provider?.let { NavigationItem(R.mipmap.navigation5_img, "로그아웃") }
            ?: NavigationItem(R.mipmap.navigation5_img, "로그인"))

        view.getView().mainNavigation.navigationContainer.setHasFixedSize(true)
        view.getView().mainNavigation.navigationContainer.layoutManager = LinearLayoutManager(context)
        view.getView().mainNavigation.navigationContainer.adapter = NavigationAdpater(context,activity,lst
            ,GlobalApplication.userInfo.getProvider(),GlobalApplication.ACTIVITY_HANDLING_MAIN)
    }

    override fun initAlcholRanking(context: Context) {
        view.getView().monthlyRecylcerView.setHasFixedSize(true)
        view.getView().monthlyRecylcerView.layoutManager = LinearLayoutManager(context)

        compositeDisposable.add(ApiGenerator.retrofit.create(ApiService::class.java).getAlcholRanking(
            GlobalApplication.userBuilder.createUUID,GlobalApplication.userInfo.getAccessToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              view.getView().monthlyRecylcerView.adapter = AlcholRankAdapter(context,it.data?.alcholList?.toMutableList()!!)

            },{t ->Log.e(ErrorManager.ALCHOL_RANKING,t.message.toString())}))

    }

    override fun checkLogin(context: Context,provider: String?) {
        provider?.let {
            //유저 프로필 설정하는 화면 필요함

            view.getView().mainDrawerLayout.main_navigation.navigation_header_Name.text=
                GlobalApplication.userInfo.getNickName() + "님 안녕하세요" +"\n" //레벨을 적어야함.
        }
    }

    override fun detachView() {
        compositeDisposable.dispose()
    }

}