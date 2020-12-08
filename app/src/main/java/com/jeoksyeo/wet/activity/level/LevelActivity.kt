package com.jeoksyeo.wet.activity.level

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.application.GlobalApplication
import com.vuforia.engine.wet.R
import com.vuforia.engine.wet.databinding.LevelBinding

class LevelActivity:AppCompatActivity(), View.OnClickListener, LevelContract.BaseView {
    private lateinit var binding:LevelBinding
    private lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =DataBindingUtil.setContentView(this, R.layout.level)
        binding.myLevelBasicHeader.basicHeaderWindowName.text = "나의 주류 레벨"

        presenter = Presenter().apply {
            view = this@LevelActivity
            context = this@LevelActivity.baseContext
        }

        presenter.setNetworkUtil()

        presenter.initMiniImageArray()
        presenter.getMyLevel()

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.basicHeader_backButton ->{
                finish()
                overridePendingTransition(R.anim.left_to_current,R.anim.current_to_right)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_current,R.anim.current_to_right)
    }

    override fun getView(): LevelBinding {
        return binding
    }

    override fun settingMainAlcholGIF(level: Int) {
        when(level){
            1 ->{binding.imageViewEvaluationByMeMainBottle.setAnimation(R.raw.bottle_level01)}
            2 ->{binding.imageViewEvaluationByMeMainBottle.setAnimation(R.raw.bottle_level02)}
            3 ->{binding.imageViewEvaluationByMeMainBottle.setAnimation(R.raw.bottle_level03)}
            4 ->{binding.imageViewEvaluationByMeMainBottle.setAnimation(R.raw.bottle_level04)}
            5 ->{binding.imageViewEvaluationByMeMainBottle.setAnimation(R.raw.bottle_level05)}
        }
    }

    @SuppressLint("SetTextI18n")
    override fun settingExperience(reviewCount: Int,level: Int) {
        val rest = reviewCount % 10

        binding.textViewCurrentLevel.text = GlobalApplication.instance.getLevelName(level-1) //현재 레벨
        binding.textViewEvaluationNoticeNextLevelText.text = GlobalApplication.instance.getLevelName(level) //다음레벨
        binding.textViewEvaluationNoticeNextLevelCountText.text = "까지 ${11-reviewCount}병 남았습니다." // 다음 레벨까지 남은 리뷰 개수

        for(i in 0 until rest){
            presenter.miniAlcoholList[i].setImageResource(R.mipmap.mini_bottle_full)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }
}