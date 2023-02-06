package org.enkasacco.mobile.ui.activities

import android.os.Bundle

import org.enkasacco.mobile.R
import org.enkasacco.mobile.ui.activities.base.BaseActivity
import org.enkasacco.mobile.ui.fragments.LoanAccountsDetailFragment
import org.enkasacco.mobile.utils.Constants

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/
class LoanAccountContainerActivity : BaseActivity() {

    private var loanId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        loanId = intent?.extras?.getLong(Constants.LOAN_ID)!!
        replaceFragment(LoanAccountsDetailFragment.newInstance(loanId), false, R.id.container)
        showBackButton()
    }
}