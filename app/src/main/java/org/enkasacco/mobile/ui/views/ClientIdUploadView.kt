package org.enkasacco.mobile.ui.views

import org.enkasacco.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 31/7/17.
 */
interface ClientIdUploadView : MVPView {
    fun showUploadedSuccessfully()
    fun showError(msg: String?)
    fun checkPermissionAndRequestBack()
    fun checkPermissionAndRequestFront()
    fun getExternalStorageDocumentFront()
    fun getExternalStorageDocumentBack()
}