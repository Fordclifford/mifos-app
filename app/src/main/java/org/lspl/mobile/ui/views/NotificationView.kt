package org.lspl.mobile.ui.views

import org.lspl.mobile.models.notification.MifosNotification
import org.lspl.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 14/9/17.
 */
interface NotificationView : MVPView {
    fun showNotifications(notifications: List<MifosNotification?>?)
    fun showError(msg: String?)
}