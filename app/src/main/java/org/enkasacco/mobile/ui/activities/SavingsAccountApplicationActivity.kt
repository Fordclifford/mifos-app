package org.enkasacco.mobile.ui.activities

import android.os.Bundle

import org.enkasacco.mobile.R
import org.enkasacco.mobile.ui.activities.base.BaseActivity
import org.enkasacco.mobile.ui.enums.SavingsAccountState
import org.enkasacco.mobile.ui.fragments.SavingsAccountApplicationFragment.Companion.newInstance

/*
* Created by saksham on 30/June/2018
*/
class SavingsAccountApplicationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_savings_account_application)
        setToolbarTitle(getString(R.string.apply_savings_account))
        showBackButton()
        replaceFragment(newInstance(SavingsAccountState.CREATE,
                null), false, R.id.container)
    }
}