package org.lspl.mobile.ui.activities

import android.os.Bundle

import org.lspl.mobile.R
import org.lspl.mobile.ui.activities.base.BaseActivity
import org.lspl.mobile.ui.fragments.SavingAccountsDetailFragment
import org.lspl.mobile.utils.Constants

/**
 * Created by Rajan Maurya on 05/03/17.
 */
class SavingsAccountContainerActivity : BaseActivity() {

    private var savingsId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        savingsId = intent?.extras?.getLong(Constants.SAVINGS_ID)!!
        replaceFragment(SavingAccountsDetailFragment.newInstance(savingsId), false, R.id.container)
        showBackButton()
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 2 && transferSuccess) {
            supportFragmentManager.popBackStack()
            supportFragmentManager.popBackStack()
            transferSuccess = false
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        var transferSuccess = false
    }
}