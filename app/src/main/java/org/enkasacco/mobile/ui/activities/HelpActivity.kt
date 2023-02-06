package org.enkasacco.mobile.ui.activities

import android.os.Bundle

import org.enkasacco.mobile.R
import org.enkasacco.mobile.ui.activities.base.BaseActivity
import org.enkasacco.mobile.ui.fragments.HelpFragment

/**
 * @author Rajan Maurya
 * On 11/03/19.
 */
class HelpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        setToolbarTitle(getString(R.string.help))
        showBackButton()
        replaceFragment(HelpFragment.newInstance(), false, R.id.container)
    }
}