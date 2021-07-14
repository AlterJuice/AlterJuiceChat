package com.edu.alterjuicechat.ui.base

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    fun replaceFragment(fragment: Fragment, tag: String = "", addToBackStack: Boolean = false){
        with(requireActivity() as BaseActivity){
            this.replaceFragment(fragment, tag, addToBackStack)
        }
    }
}