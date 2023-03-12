package org.lspl.mobile.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.lspl.mobile.R
import org.lspl.mobile.ui.fragments.guarantors.CreateGuarantorFragmentOne

/* renamed from: org.techsavanna.enkasacco.ui.activities.CreateGuarantor */
class CreateGuarantorActivity : AppCompatActivity() {
    /* access modifiers changed from: protected */
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_create_guarantor)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CreateGuarantorFragmentOne()).commit()
    }

    fun stackCount(): Int {
        return supportFragmentManager.backStackEntryCount
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.container) is CreateGuarantorFragmentOne) {
            super.onBackPressed()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CreateGuarantorFragmentOne()).commit()
        }
        if (stackCount() != 0) {
            super.onBackPressed()
        }
    }
}