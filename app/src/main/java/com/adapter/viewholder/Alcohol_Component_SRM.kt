package com.adapter.viewholder

import android.graphics.Color
import android.view.ViewGroup
import com.base.BaseViewHolder
import com.model.alcohol_detail.AlcoholComponentData
import com.model.alcohol_detail.Srm
import com.vuforia.engine.wet.R
import com.vuforia.engine.wet.databinding.AlcoholComponentItemSrmBinding

class Alcohol_Component_SRM(val parent:ViewGroup)
    :BaseViewHolder<AlcoholComponentData,AlcoholComponentItemSrmBinding>(R.layout.alcohol_component_item_srm,parent) {

    override fun bind(data: AlcoholComponentData) {
        if(data.contents is Srm){
                binding.componentParentLayout.setBackgroundColor(Color.parseColor(data.contents.rgbHex))
                binding.componentTitle.text = "SRM"
                binding.componentSrm.text = data.contents.srm.toString()
                binding.componentParentLayout.setBackgroundColor(Color.parseColor(data.contents.rgbHex))
                binding.componentTitle.setTextColor(parent.context.resources.getColor(R.color.white,null))
                binding.componentSrm.setTextColor(parent.context.resources.getColor(R.color.white,null))
                binding.componentBorder.setBackgroundColor(parent.context.resources.getColor(R.color.white,null))

        }
        else if(data.contents is com.model.alcohol_detail.Color){
            binding.componentTitle.text = "COLOR"
            binding.componentSrm.text = data.contents.name
            binding.componentTitle.setTextColor(parent.context.getColor(R.color.white))
            binding.componentBorder.setBackgroundColor(parent.context.getColor(R.color.white))
            binding.componentSrm.setTextColor(parent.context.getColor(R.color.white))
            binding.componentParentLayout.setBackgroundColor(Color.parseColor(data.contents.rgbHex))
        }


    }
}

