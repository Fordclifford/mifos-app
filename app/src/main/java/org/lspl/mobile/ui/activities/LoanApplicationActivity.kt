package org.lspl.mobile.ui.activities

import android.os.Bundle

import org.lspl.mobile.R
import org.lspl.mobile.ui.activities.base.BaseActivity
import org.lspl.mobile.ui.enums.LoanState
import org.lspl.mobile.ui.fragments.LoanApplicationFragment

class LoanApplicationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_application)
        if (savedInstanceState == null) {
            replaceFragment(LoanApplicationFragment.newInstance(LoanState.CREATE), false,
                    R.id.container)
        }
        showBackButton()
    }
}