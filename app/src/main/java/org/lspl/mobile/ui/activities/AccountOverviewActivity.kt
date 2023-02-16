package org.lspl.mobile.ui.activities

import android.os.Bundle

import org.lspl.mobile.R
import org.lspl.mobile.ui.activities.base.BaseActivity
import org.lspl.mobile.ui.fragments.AccountOverviewFragment

/**
 * @author Rajan Maurya
 * On 16/10/17.
 */
class AccountOverviewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        replaceFragment(AccountOverviewFragment.newInstance(), false, R.id.container)
        showBackButton()
        hideToolbarElevation()
    }
}