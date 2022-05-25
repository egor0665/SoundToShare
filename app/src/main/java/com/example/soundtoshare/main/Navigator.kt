package com.example.soundtoshare.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

open class Navigator(
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) {

    open fun setScreen(screen: Screen) = Unit

    protected fun replaceFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(containerId, fragment, fragment::class.java.simpleName)
            .commit()
    }

    protected fun selectFragment(fragment: Fragment) {
        val fragments: List<Fragment> = fragmentManager.fragments
        val currentFragment = fragments.firstOrNull { it.isVisible && it.tag != null }

        val tag = fragment::class.java.simpleName
        val newFragment: Fragment? = fragmentManager.findFragmentByTag(tag)
        if (currentFragment != null && newFragment != null && currentFragment === newFragment)
            return

        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        if (newFragment == null) {
            transaction.add(containerId, fragment, tag)
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        if (newFragment != null) {
            transaction.show(newFragment)
        }
        transaction.commitNow()
    }
}

sealed class Screen {
    object SignIn : Screen()
    object Home : Screen()
    object Settings : Screen()
    object Map : Screen()
}
