package org.lspl.mobile.ui.activities

import android.os.Bundle

import org.lspl.mobile.R
import org.lspl.mobile.ui.activities.base.BaseActivity
import org.lspl.mobile.ui.fragments.BeneficiaryAddOptionsFragment

/**
 * @author Rajan Maurya
 * On 04/06/18.
 */
class AddBeneficiaryActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        showBackButton()
        replaceFragment(BeneficiaryAddOptionsFragment.newInstance(), false, R.id.container)
    }
}