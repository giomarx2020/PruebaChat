package com.giosoto.chatpruebaradar.views.activitys

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.giosoto.chatpruebaradar.R
import com.giosoto.chatpruebaradar.databinding.ActivityMainBinding
import com.giosoto.chatpruebaradar.views.fragments.InicioFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding : ActivityMainBinding
    lateinit var drawerLayout : DrawerLayout
    private lateinit var navController: NavController
    private lateinit var navigationView : NavigationView
    private lateinit var toggle : ActionBarDrawerToggle
    var sharedPreferences: SharedPreferences? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)



        sharedPreferences =
            this.getSharedPreferences("chat", Context.MODE_PRIVATE)
        drawerLayout = binding.drawer

        setupNavigation()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcvInicio) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navview.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        val navView: NavigationView = binding.navview

        navView.itemIconTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
        navView.itemTextColor = ColorStateList.valueOf(resources.getColor(R.color.black))

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.hide()
        navView.setNavigationItemSelectedListener(this)

        val view = binding.root
        setContentView(view)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.inicioFragment -> {
                FirebaseAuth.getInstance().signOut()
                replaceFragment(InicioFragment())
                Toast.makeText(this@MainActivity,"Has cerrado Sesion", Toast.LENGTH_LONG).show()
            }

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer)
        drawerLayout.closeDrawer(Gravity.LEFT)

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.e("Entro a ","Cerrar Sesion")
        if (toggle.onOptionsItemSelected(item)){
            item.iconTintList = ColorStateList.valueOf(resources.getColor(R.color.azul))

            if(item.itemId == R.id.inicioFragment){
                FirebaseAuth.getInstance().signOut()
                val intent  = Intent(this@MainActivity, InicioFragment::class.java)
                Toast.makeText(this@MainActivity,"Has cerrado Sesion", Toast.LENGTH_LONG).show()
                startActivity(intent)
                return true
            }

            drawerLayout.closeDrawer(Gravity.LEFT)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fcvInicio, fragment)
        fragmentTransaction.commit()
    }

    private fun setupNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcvInicio) as NavHostFragment
        navController = navHostFragment.navController
        drawerLayout.closeDrawers()
    }


}


