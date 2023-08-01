package com.example.serfinsaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.serfinsaapp.commerce.AddCommerceFragment
import com.example.serfinsaapp.commerce.list.CommerceListFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(CommerceListFragment())

        drawerLayout = findViewById(R.id.dlMain)
        navigationView = findViewById(R.id.nav_view)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        navigationView.bringToFront()

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer_content_desc, R.string.close_drawer_content_desc)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_home)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            return
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.nav_home -> {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
                replaceFragment(CommerceListFragment())
            }
            R.id.nav_afiliate -> {
                replaceFragment(AddCommerceFragment())
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}