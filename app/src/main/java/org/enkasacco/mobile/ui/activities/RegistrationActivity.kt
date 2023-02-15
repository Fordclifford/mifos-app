package org.enkasacco.mobile.ui.activities

import android.content.DialogInterface
import android.os.Bundle

import org.enkasacco.mobile.R
import org.enkasacco.mobile.ui.activities.base.BaseActivity
import org.enkasacco.mobile.ui.fragments.ClientIdUploadFragment
import org.enkasacco.mobile.ui.fragments.NextOfKinRegistrationFragment
import org.enkasacco.mobile.ui.fragments.PassportPhotoUploadFragment
import org.enkasacco.mobile.ui.fragments.RegistrationFragment
import org.enkasacco.mobile.utils.MaterialDialog

class RegistrationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        replaceFragment(RegistrationFragment.newInstance(), false, R.id.container)
    }

    override fun onBackPressed() {
        MaterialDialog.Builder().init(this)
                .setTitle(getString(R.string.dialog_cancel_registration_title))
                .setMessage(getString(R.string.dialog_cancel_registration_message))
                .setPositiveButton(getString(R.string.cancel),
                        DialogInterface.OnClickListener { _, _ -> super.onBackPressed() })
                .setNegativeButton(R.string.continue_str,
                        DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                .createMaterialDialog()
                .show()
    }
}