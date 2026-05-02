package com.example.budgettracker

import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_main)

        // --- Toolbar setup (CRITICAL FIX) ---
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        // Status bar inset handling
        val actionBarHeight = TypedValue().let { value ->
            if (theme.resolveAttribute(android.R.attr.actionBarSize, value, true)) {
                TypedValue.complexToDimensionPixelSize(value.data, resources.displayMetrics)
            } else {
                toolbar.layoutParams.height
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { view, insets ->
            val statusBarHeight =
                insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.layoutParams = view.layoutParams.apply {
                height = actionBarHeight + statusBarHeight
            }

            view.setPadding(
                view.paddingLeft,
                statusBarHeight,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        // --- Navigation setup ---
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        val bottomNav =
            findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val topLevelDestinations = setOf(
            R.id.dashboardFragment,
            R.id.expenseListFragment,
            R.id.categoryListFragment,
            R.id.debtBossFragment,
            R.id.moreFragment
        )

        bottomNav.setOnItemSelectedListener { item ->
            if (navController.currentDestination?.id == item.itemId) {
                return@setOnItemSelectedListener true
            }

            val options = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(false)
                .setPopUpTo(navController.graph.startDestinationId, false)
                .build()

            navController.navigate(item.itemId, null, options)
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in topLevelDestinations) {
                toolbar.navigationIcon = null
                toolbar.setNavigationOnClickListener(null)
            } else {
                toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                toolbar.setNavigationOnClickListener {
                    navController.navigateUp()
                }
            }

            val selectedTab = when (destination.id) {
                R.id.dashboardFragment -> R.id.dashboardFragment
                R.id.expenseListFragment,
                R.id.addExpenseFragment -> R.id.expenseListFragment
                R.id.categoryListFragment -> R.id.categoryListFragment
                R.id.debtBossFragment -> R.id.debtBossFragment
                else -> R.id.moreFragment
            }

            if (bottomNav.selectedItemId != selectedTab) {
                bottomNav.menu.findItem(selectedTab)?.isChecked = true
            }
        }

        // System bars: keep light icons (Recovery Arcade is dark)
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = false
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightNavigationBars = false
    }
}
