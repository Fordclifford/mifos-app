package org.lspl.mobile.ui.activities

import android.os.Bundle

import org.lspl.mobile.R
import org.lspl.mobile.ui.activities.base.BaseActivity
import org.lspl.mobile.ui.fragments.NotificationFragment

class NotificationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        replaceFragment(NotificationFragment.newInstance(), false, R.id.container)
    }
}