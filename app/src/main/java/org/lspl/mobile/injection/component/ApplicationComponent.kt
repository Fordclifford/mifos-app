package org.lspl.mobile.injection.component

import android.app.Application
import android.content.Context

import dagger.Component

import org.lspl.mobile.api.BaseApiManager
import org.lspl.mobile.api.DataManager
import org.lspl.mobile.api.local.DatabaseHelper
import org.lspl.mobile.api.local.PreferencesHelper
import org.lspl.mobile.injection.ApplicationContext
import org.lspl.mobile.injection.module.ApplicationModule

import javax.inject.Singleton

/**
 * @author ishan
 * @since 08/07/16
 */
@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    @ApplicationContext
    fun context(): Context?
    fun application(): Application?
    fun dataManager(): DataManager?
    fun prefManager(): PreferencesHelper?
    fun baseApiManager(): BaseApiManager?
    fun databaseHelper(): DatabaseHelper?
}