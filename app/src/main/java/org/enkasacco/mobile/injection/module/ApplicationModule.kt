package org.enkasacco.mobile.injection.module

import android.app.Application
import android.content.Context

import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck

import org.enkasacco.mobile.api.BaseApiManager
import org.enkasacco.mobile.api.local.PreferencesHelper
import org.enkasacco.mobile.injection.ApplicationContext

import javax.inject.Singleton

/**
 * @author ishan
 * @since 08/07/16
 */
@Module
@DisableInstallInCheck
class ApplicationModule(private val application: Application) {
    @Provides
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun providePrefManager(@ApplicationContext context: Context?): PreferencesHelper {
        return PreferencesHelper(context)
    }

    @Provides
    @Singleton
    fun provideBaseApiManager(preferencesHelper: PreferencesHelper?): BaseApiManager {
        return BaseApiManager(preferencesHelper!!)
    }
}