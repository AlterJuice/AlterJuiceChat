package com.edu.alterjuicechat.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.edu.alterjuicechat.R

abstract class BaseActivity : AppCompatActivity() {

    fun replaceFragment(fragment: Fragment, tag: String = "", addToBackStack: Boolean = false){
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fragment_slide_in, R.anim.fragment_fade_out, R.anim.fragment_fade_in, R.anim.fragment_slide_out)
            .replace(R.id.mainFragmentContainer, fragment, tag)
            .apply {
                if (addToBackStack)
                    addToBackStack(tag)
            }
            .commit()
    }
}