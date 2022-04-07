package com.example.buyonmars

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.buyonmars.base.BaseApplication
import com.example.buyonmars.ui.marslist.MarsListFragment
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.navigation)

        val onBoardingFragment: PaperOnboardingFragment = PaperOnboardingFragment.newInstance(getDataForOnboarding())
        val marsListFragment = MarsListFragment()

        if (!BaseApplication.instance.serviceManager.slideWasShown()) {
            navigateToSlide(onBoardingFragment,marsListFragment)
        } else {
            navigateToMars(marsListFragment)
        }

        val navController = navHostFragment.navController
        navController.setGraph(graph, intent.extras)
    }

    private fun navigateToMars(marsListFragment: MarsListFragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.nav_host_fragment, marsListFragment
            )
            .commit()
    }

    private fun navigateToSlide(onBoardingFragment: PaperOnboardingFragment, marsListFragment: MarsListFragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.nav_host_fragment, onBoardingFragment
            )
            .commit()
        onBoardingFragment.setOnRightOutListener {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.nav_host_fragment, marsListFragment
                )
                .commit()
            BaseApplication.instance.serviceManager.setSlideWasShown(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.mars_list_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.all_view_option -> {
                true
            }
//            R.id.buy_view_option -> {
//                true
//            }
//            R.id.rent_view_option -> {
//                true
//            }
//            R.id.favorites_view_option -> {
//                true
//            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getDataForOnboarding(): ArrayList<PaperOnboardingPage>? {
        val scr1 = PaperOnboardingPage(
            "Welcome", "All mars lands that you want =)",
            Color.parseColor("#F27F79"), R.drawable.ic_planet2, R.drawable.ic_planet_icon
        )
        val scr2 = PaperOnboardingPage(
            "Doble TAP!", "Make a double tap to add the land to your favorite list",
            Color.parseColor("#FFFF4A"), R.drawable.ic_love_big, R.drawable.ic_love_unmark
        )
        val scr3 = PaperOnboardingPage(
            "Buy & Rent", "Enjoy!",
            Color.parseColor("#FFD03B"), R.drawable.ic_enjoy, R.drawable.ic_enjoy_icon
        )
        val elements: ArrayList<PaperOnboardingPage> = ArrayList()
        elements.add(scr1)
        elements.add(scr2)
        elements.add(scr3)
        return elements
    }
}

