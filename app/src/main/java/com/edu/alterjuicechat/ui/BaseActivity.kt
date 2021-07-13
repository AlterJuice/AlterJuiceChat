package com.edu.alterjuicechat.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.edu.alterjuicechat.R

abstract class BaseActivity : AppCompatActivity() {

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

// base fragment +
// viewmodel delete user with arguments
// fix !!
// start Loop in init view model

// lifecycle scope in MianActivity
// job to init
// delete include
