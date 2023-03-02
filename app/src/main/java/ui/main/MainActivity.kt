package ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.jetpack.demo.R
import com.jetpack.demo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import ui.matches.fragments.MatchesFragment
import ui.teams.fragment.TeamsFragment
import utils.constant.replaceFragment
import utils.constant.showToast


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object{
        const val TeamsFragmentTAG ="teams"
        const val MatchFragmentTAG ="matches"
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        /**
         * Toolbar
         */
        val toolbar: Toolbar = binding.appBarMain.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)
        /**
         * Drawer
         */
        drawer = binding.drawerLayout
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        binding.navView.setNavigationItemSelectedListener(this)
        /**
         * Bottom navigation view
         */

        bottomNavigationView= binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.teams_menu_item -> {
                    val fragment = TeamsFragment.newInstance()
                    replaceFragment(fragment,R.id.mainContainer, TeamsFragmentTAG)
                    binding.navView.setCheckedItem(R.id.nav_news)
                    true
                }
                R.id.matches_menu_item -> {
                    val fragment = MatchesFragment.newInstance()
                    replaceFragment(fragment,R.id.mainContainer, MatchFragmentTAG)
                    binding.navView.setCheckedItem(R.id.nav_portfolio)
                    true
                }
                R.id.more_menu_item -> {
                    // If navigation drawer is not open yet, open it else close it.
                    if(!drawer.isDrawerOpen(GravityCompat.END)) {
                        drawer.openDrawer(GravityCompat.END)
                    }
                    else {
                        drawer.closeDrawer(GravityCompat.END)
                    }
                    true
                }
                else -> false
            }
        }
        val badgeMatch = bottomNavigationView.getOrCreateBadge(R.id.matches_menu_item)
        badgeMatch.isVisible = true
        badgeMatch.number = 10

        val badgeMore = bottomNavigationView.getOrCreateBadge(R.id.more_menu_item)
        badgeMore.isVisible = true

        // When we open the application first time the fragment should be shown to the user in this case it is teams fragment
        val teamsFragment = TeamsFragment.newInstance()
        replaceFragment(teamsFragment,R.id.mainContainer, TeamsFragmentTAG)

    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        val fragment: Fragment?

        when (id) {
            R.id.nav_news -> {
                fragment = TeamsFragment.newInstance()
                replaceFragment(fragment,R.id.mainContainer, TeamsFragmentTAG)
                bottomNavigationView.selectedItemId = R.id.teams_menu_item
            }

            R.id.nav_portfolio -> {
                fragment = MatchesFragment.newInstance()
                replaceFragment(fragment,R.id.mainContainer, MatchFragmentTAG)
                bottomNavigationView.selectedItemId = R.id.matches_menu_item

            }
            R.id.nav_profile -> {
                showToast(this, getString(R.string.profile), 0)

            }
            R.id.nav_logout -> {
                showToast(this, getString(R.string.logout), 0)

            }

        }
        if(!drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.openDrawer(GravityCompat.END)
        }
        else {
            drawer.closeDrawer(GravityCompat.END)
        }
        binding.navView.setCheckedItem(id)

        return true
    }

    override fun onResume() {
        super.onResume()

    }

}