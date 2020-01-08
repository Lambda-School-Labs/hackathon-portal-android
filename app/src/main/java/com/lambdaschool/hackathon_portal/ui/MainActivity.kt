package com.lambdaschool.hackathon_portal.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.auth0.android.Auth0Exception
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.provider.VoidCallback
import com.auth0.android.provider.WebAuthProvider
import com.google.android.material.navigation.NavigationView
import com.lambdaschool.hackathon_portal.App
import com.lambdaschool.hackathon_portal.R
import com.lambdaschool.hackathon_portal.model.wipeCurrentUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NavDrawerInterface {

    @Inject
    lateinit var webAuthProviderLogout: WebAuthProvider.LogoutBuilder
    @Inject
    lateinit var credentialsManager: SecureCredentialsManager

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }
    val navigationView by lazy {
        findViewById<NavigationView>(R.id.nav_view)
    }
    val headerView by lazy {
        navigationView.getHeaderView(0)
    }
    val textviewTitle by lazy {
        headerView.findViewById<TextView>(R.id.nav_header_title)
    }
    val textviewSubtitle by lazy {
        headerView.findViewById<TextView>(R.id.nav_header_subtitle)
    }
    val imageviewHeader by lazy {
        headerView.findViewById<ImageView>(R.id.nav_header_image)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App)
            .appComponent
            .injectMainActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        nav_view.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when (menuItem.itemId) {

                R.id.nav_drawer_logout -> {
                    webAuthProviderLogout.start(this, object : VoidCallback {
                        override fun onSuccess(payload: Void?) {
                            Log.i("Nav Drawer", "Success")
                            credentialsManager.clearCredentials()
                            wipeCurrentUser()
                            nav_host_fragment.findNavController().navigate(R.id.loginFragment)
                        }

                        override fun onFailure(error: Auth0Exception?) {
                            Log.i("Nav Drawer", "Failure ${error?.message}")
                        }
                    })
                }

                R.id.nav_drawer_account -> {
                    nav_host_fragment.findNavController().navigate(R.id.accountFragment)
                    title = "Account Details"
                }

                R.id.nav_drawer_settings -> {
                    nav_host_fragment.findNavController().navigate(R.id.settingsFragment)
                    title = "Settings"
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun lockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toggle.isDrawerIndicatorEnabled = false
    }

    override fun unlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toggle.isDrawerIndicatorEnabled = true
    }

    override fun setUserImage(imageUrl: String) {
        GlobalScope.launch(Main) { Picasso.get().load(imageUrl).into(imageviewHeader) }
    }

    override fun setUsername(username: String) {
        GlobalScope.launch(Main) { textviewTitle.text = username }
    }

    override fun setUserEmail(email: String) {
        GlobalScope.launch(Main) { textviewSubtitle.text = email }
    }
}