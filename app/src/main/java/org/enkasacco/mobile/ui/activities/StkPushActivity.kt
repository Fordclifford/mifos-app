package org.enkasacco.mobile.ui.activities

import android.os.Bundle
import org.enkasacco.mobile.R
import org.enkasacco.mobile.ui.activities.base.BaseActivity
import org.enkasacco.mobile.ui.fragments.StkPushFragment

class StkPushActivity : BaseActivity() {
    var amount = 0.0
    var clientId: Long = 0

    /* access modifiers changed from: protected */
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        val intent2 = intent
        intent = intent2
        clientId = intent2.extras!!.getLong("clientId")
        println("client ids" + clientId)
        amount = 200.0
        setContentView(R.layout.activity_stk_push)
        replaceFragment(StkPushFragment.newInstance("R#" + clientId, amount), true, R.id.container)
    }
}