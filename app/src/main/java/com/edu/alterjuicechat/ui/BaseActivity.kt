package com.edu.alterjuicechat.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.edu.alterjuicechat.R

open class BaseActivity : AppCompatActivity() {

    fun replaceFragment(fragment: Fragment, tag: String = "", addToBackStack: Boolean = false){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragmentContainer, fragment, tag)
            .apply {
                if (addToBackStack)
                    addToBackStack(tag)
            }
            .commit()
    }
}