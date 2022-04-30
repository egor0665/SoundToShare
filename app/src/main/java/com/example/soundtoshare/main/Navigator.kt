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
        val currentFragment = fragments.firstOrNull { it.isVisible }

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
    object SignIn: Screen()
    object Home: Screen()
    object Settings: Screen()
    object Map: Screen()
}

//package com.example.soundtoshare.main
//
//import android.util.Log
//import android.view.View
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import com.example.soundtoshare.R
//import com.example.soundtoshare.databinding.ActivityMainBinding
//import com.example.soundtoshare.fragments.home.HomeFragment
//import com.example.soundtoshare.fragments.home.SignInFragment
//import com.example.soundtoshare.fragments.map.MapFragment
//import com.example.soundtoshare.fragments.settings.SettingsFragment
//import com.google.android.material.bottomnavigation.BottomNavigationView
//
//class Navigator(private val supportFragmentManager: FragmentManager, binding: ActivityMainBinding) {
//    private val navView: BottomNavigationView = binding.navView
//    fun setFragment(fragment: Fragment) {
//        if (fragment is SignInFragment) {
//            toggleNavBar(View.GONE)
//            supportFragmentManager.beginTransaction().replace(
//                R.id.nav_host_fragment_activity_main,
//                fragment,
//                R.id.sign_in.toString(),
//            ).commit()
//        }
//        if (fragment is HomeFragment) {
//            toggleNavBar(View.VISIBLE)
//            supportFragmentManager.beginTransaction().replace(
//                R.id.nav_host_fragment_activity_main,
//                fragment,
//                R.id.home.toString(),
//            ).commit()
//            navView.selectedItemId = R.id.home
//        }
//    }
//
//    fun setupListener() {
//        navView.setOnItemSelectedListener { menuItem ->
//            Log.d("ScreenNavigator", menuItem.itemId.toString())
//            val currentFragment = supportFragmentManager.fragments.firstOrNull { it.isVisible }
//            Log.d("ScreenNavigator", currentFragment.toString())
//            val newFragment = supportFragmentManager.findFragmentByTag(menuItem.itemId.toString())
//            val fragmentTransaction = supportFragmentManager.beginTransaction()
//            if (currentFragment != null) {
//                if (currentFragment.tag != menuItem.itemId.toString()) {
//                    fragmentTransaction.hide(currentFragment)
//                    if (newFragment != null) {
//                        fragmentTransaction.show(newFragment)
//                    } else {
//                        when (menuItem.itemId) {
//                            R.id.home -> fragmentTransaction.add(
//                                R.id.nav_host_fragment_activity_main,
//                                HomeFragment(),
//                                R.id.home.toString()
//                            )
//                            R.id.map -> fragmentTransaction.add(
//                                R.id.nav_host_fragment_activity_main,
//                                MapFragment(),
//                                R.id.map.toString()
//                            )
//                            R.id.settings -> fragmentTransaction.add(
//                                R.id.nav_host_fragment_activity_main,
//                                SettingsFragment(),
//                                R.id.settings.toString()
//                            )
//                        }
//                    }
//                }
//            }
//            fragmentTransaction.commit()
//            Log.d("ScreenNavigator", supportFragmentManager.fragments.toString())
//            return@setOnItemSelectedListener true
//        }
//    }
//
//    private fun toggleNavBar(status: Int) {
//        navView.visibility = status
//    }
//}
