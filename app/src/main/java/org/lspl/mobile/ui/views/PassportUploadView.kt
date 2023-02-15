package org.lspl.mobile.ui.views

import org.lspl.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 31/7/17.
 */
interface PassportUploadView : MVPView {
    fun showUploadedSuccessfully()
    fun checkPermissionAndRequest()
    fun showError(msg: String?)
    fun getExternalStorageDocument()
}