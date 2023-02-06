package org.enkasacco.mobile.ui.views

import android.graphics.Bitmap
import org.enkasacco.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 19/6/17.
 */
interface HomeView : MVPView {
    fun showUserDetails(userName: String?)
    fun showUserImageTextDrawable()
    fun showUserImage(bitmap: Bitmap?)
    fun showNotificationCount(count: Int)
    fun showError(errorMessage: String?)
    fun showUserImageNotFound()
}