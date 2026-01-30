package com.example.budgettracker.ui.common

import androidx.fragment.app.Fragment
import com.example.budgettracker.R
import com.google.android.material.appbar.MaterialToolbar

fun Fragment.configureToolbar(
    title: String,
    subtitle: String? = null,
    menuRes: Int? = null,
    onMenuClick: ((Int) -> Boolean)? = null
) {
    val toolbar = requireActivity()
        .findViewById<MaterialToolbar>(R.id.topAppBar)

    toolbar.title = title
    toolbar.subtitle = subtitle

    toolbar.menu.clear()
    menuRes?.let { toolbar.inflateMenu(it) }

    toolbar.setOnMenuItemClickListener { item ->
        onMenuClick?.invoke(item.itemId) ?: false
    }
}
