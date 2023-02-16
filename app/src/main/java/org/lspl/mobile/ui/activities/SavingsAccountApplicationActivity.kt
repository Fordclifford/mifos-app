package org.lspl.mobile.ui.activities

import android.os.Bundle

import org.lspl.mobile.R
import org.lspl.mobile.ui.activities.base.BaseActivity
import org.lspl.mobile.ui.enums.SavingsAccountState
import org.lspl.mobile.ui.fragments.SavingsAccountApplicationFragment.Companion.newInstance

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