package org.lspl.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import butterknife.BindView
import butterknife.ButterKnife

import org.lspl.mobile.BuildConfig
import org.lspl.mobile.R
import org.lspl.mobile.ui.fragments.base.BaseFragment
import java.util.*

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/
class AboutUsFragment : BaseFragment() {
    private val licenseLink = "https://github.com/openMF/mifos-mobile/blob/development/LICENSE.md"
    private val sourceCodeLink = "https://github.com/openMF/mifos-mobile"
    private val websiteLink = "https://openmf.github.io/mobileapps.github.io/"

    @kotlin.jvm.JvmField
    @BindView(R.id.app_version)
    var tvAppVersion: TextView? = null


    @kotlin.jvm.JvmField
    @BindView(R.id.tv_copy_right)
    var tvCopyRight: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_about_us, container, false)
        ButterKnife.bind(this, rootView!!)
        setToolbarTitle(getString(R.string.about_us))
        tvAppVersion?.text = BuildConfig.VERSION_NAME
        tvCopyRight?.text = getString(R.string.copy_right_mifos, Calendar.getInstance()[Calendar.YEAR].toString())
        return rootView
    }


    companion object {
        @kotlin.jvm.JvmStatic
        fun newInstance(): AboutUsFragment {
            val fragment = AboutUsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}