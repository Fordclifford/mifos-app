package org.mifos.mobile.ui.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.mifos.mobile.R
import org.mifos.mobile.models.register.UserVerify
import org.mifos.mobile.presenters.PassportUploadPresenter
import org.mifos.mobile.presenters.RegistrationVerificationPresenter
import org.mifos.mobile.ui.activities.LoginActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.PassportUploadView
import org.mifos.mobile.utils.Toaster
import java.io.File
import java.util.*
import javax.inject.Inject


/**
 * Created by dilpreet on 31/7/17.
 */
class PassportPhotoUploadFragment : BaseFragment(), PassportUploadView {

    @kotlin.jvm.JvmField
    @BindView(R.id.buttonChoose)
    var buttonChoose: Button? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.photo)
    var imageView: ImageView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.btn_upload)
    var buttonUpload: Button? = null

    private val PICK_IMAGE_REQUEST = 1
    private val STORAGE_PERMISSION_CODE = 123
    private var bitmap: Bitmap? = null
    private var filePath: Uri? = null


    @kotlin.jvm.JvmField
    @Inject
    var presenter: PassportUploadPresenter? = null
    private var rootView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_upload_passport, container, false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        ButterKnife.bind(this, rootView!!)
        presenter?.attachView(this)
        return rootView
    }

    @OnClick(R.id.btn_upload)
    fun verifyClicked() {
     //   fileUpload()
        (activity as BaseActivity?)?.replaceFragment(ClientIdUploadFragment.newInstance(), true, R.id.container)

    }

    override fun showUploadedSuccessfully() {
        startActivity(Intent(activity, LoginActivity::class.java))
        Toast.makeText(context, getString(R.string.verified), Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    override fun showError(msg: String?) {
        Toaster.show(rootView, msg)
    }

    override fun showProgress() {
        showMifosProgressDialog(getString(R.string.uploading))
    }

    override fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
    }

    companion object {
        fun newInstance(): PassportPhotoUploadFragment {
            return PassportPhotoUploadFragment()
        }
    }

    fun fileUpload(file: File) {
        val body: MultipartBody.Part
        val mapRequestBody = LinkedHashMap<String, RequestBody>()
        val arrBody: MutableList<MultipartBody.Part> = ArrayList()
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        mapRequestBody["file\"; filename=\"" + file.name] = requestBody
        mapRequestBody["test"] =
            RequestBody.create(MediaType.parse("text/plain"), "gogogogogogogog")
        body = MultipartBody.Part.createFormData("fileName", file.name, requestBody)
        arrBody.add(body)

           //uploadFile(mapRequestBody, arrBody)

    }
}