package com.vitorhilarioapps.mystock.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.squareup.picasso.Picasso
import com.vitorhilarioapps.mystock.R
import com.vitorhilarioapps.mystock.databinding.ActivityHomeBinding
import com.vitorhilarioapps.mystock.databinding.HeaderNavViewBinding
import com.vitorhilarioapps.mystock.ui.auth.AuthActivity
import com.vitorhilarioapps.mystock.ui.home.viewmodel.FirestoreViewModel

class HomeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val viewModel: FirestoreViewModel by viewModels()

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupUI()
        setupClickListeners()
    }

    /*-----------------
    |    Handle UI    |
    -----------------*/

    private fun setupUI() {
        setupNavController()
        setupHeaderNavView()
    }

    private fun setupHeaderNavView() {
        val headerView = binding.navView.getHeaderView(0)
        val headerBinding: HeaderNavViewBinding = HeaderNavViewBinding.bind(headerView)
        val user = viewModel.getUser()

        headerBinding.tvEmailHeader.text = user?.email
        headerBinding.tvUsernameHeader.text = user?.displayName

        Picasso.get()
            .load("file:///android_asset/banner.png")
            .into(headerBinding.bannerHeader)

        user?.photoUrl?.let { uri ->
            Picasso.get()
                .load(uri)
                .error(R.drawable.user_avatar)
                .into(headerBinding.ivPerson)
        }
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        NavigationUI.setupWithNavController(binding.toolbar, navController, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupClickListeners() {
        val headerView = binding.navView.getHeaderView(0)
        val headerBinding: HeaderNavViewBinding = HeaderNavViewBinding.bind(headerView)
        headerBinding.btnLogoutHeader.setOnClickListener { logout() }
    }

    /*-----------------------
    |    Firebase Logout    |
    -----------------------*/

    private fun logout() {
        viewModel.getAuth().signOut()
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}