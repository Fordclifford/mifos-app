package org.enkasacco.mobile.ui.views

import android.graphics.Bitmap
import org.enkasacco.mobile.models.client.Client
import org.enkasacco.mobile.ui.views.base.MVPView

/**
 * Created by naman on 07/04/17.
 */
interface UserDetailsView : MVPView {
    fun showUserDetails(client: Client?)
    fun showUserImage(bitmap: Bitmap?)
    fun showError(message: String?)
}