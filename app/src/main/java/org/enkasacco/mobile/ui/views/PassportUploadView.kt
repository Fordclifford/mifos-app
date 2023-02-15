package org.enkasacco.mobile.ui.views

import org.enkasacco.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 31/7/17.
 */
interface PassportUploadView : MVPView {
    fun showUploadedSuccessfully()
    fun checkPermissionAndRequest()
    fun showError(msg: String?)
    fun getExternalStorageDocument()
}