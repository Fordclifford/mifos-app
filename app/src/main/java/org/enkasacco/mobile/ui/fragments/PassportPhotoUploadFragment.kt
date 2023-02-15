package org.enkasacco.mobile.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_upload_passport.*
import org.enkasacco.mobile.R
import org.enkasacco.mobile.api.BaseApiManager
import org.enkasacco.mobile.api.BaseURL
import org.enkasacco.mobile.api.RequiredFieldException
import org.enkasacco.mobile.api.SelfServiceInterceptor
import org.enkasacco.mobile.presenters.PassportUploadPresenter
import org.enkasacco.mobile.ui.activities.LoginActivity
import org.enkasacco.mobile.ui.activities.base.BaseActivity
import org.enkasacco.mobile.ui.fragments.base.BaseFragment
import org.enkasacco.mobile.ui.views.PassportUploadView
import org.enkasacco.mobile.utils.*
import org.enkasacco.mobile.utils.FileUtils.getMimeType
import java.io.File
import javax.inject.Inject


/**
 * Created by dilpreet on 31/7/17.
 */
class PassportPhotoUploadFragment(clientId: Long) : BaseFragment(), PassportUploadView {

    @JvmField
    @BindView(R.id.buttonChoose)
    var buttonChoose: Button? = null

    @JvmField
    @BindView(R.id.photo)
    var imageView: ImageView? = null

    @JvmField
    @BindView(R.id.btn_upload)
    var buttonUpload: Button? = null

    private var fileChoosen: File? = null

    private val FILE_SELECT_CODE = 9544
    private var uri: Uri? = null
    private var filePath: String? = null
    var clientId: Long? = clientId


    @JvmField
    @Inject
    var presenter: PassportUploadPresenter? = null
    private var rootView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_upload_passport, container, false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        ButterKnife.bind(this, rootView!!)
        presenter?.attachView(this)
        buttonUpload!!.isEnabled = false
        BaseApiManager.createService(
            BaseURL.PROTOCOL_HTTPS + BaseURL.API_ENDPOINT,
            SelfServiceInterceptor.DEFAULT_TENANT,
            SelfServiceInterceptor.DEFAULT_TOKEN
        )
        return rootView
    }

    @OnClick(R.id.btn_upload)
    fun oploadClicked() {
        //   fileUpload()
        try {
            validateInput()
        } catch (e: RequiredFieldException) {
            e.notifyUserWithToast(activity)
        }

    }

    @OnClick(R.id.buttonChoose)
    fun openFilePicker() {
        checkPermissionAndRequest()
    }

    @Throws(RequiredFieldException::class)
    fun validateInput() {
        presenter?.createImage(
            clientId!!, fileChoosen
        )
    }

    override fun showUploadedSuccessfully() {
        (activity as BaseActivity?)?.replaceFragment(
            ClientIdUploadFragment.newInstance(
                clientId!!
            ), true, R.id.container
        )
        Toast.makeText(context, getString(R.string.photo_uploaded), Toast.LENGTH_SHORT).show()
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
        fun newInstance(clientId: Long): PassportPhotoUploadFragment {
            return PassportPhotoUploadFragment(clientId)
        }
    }

    override fun checkPermissionAndRequest() {
        if (CheckSelfPermissionAndRequest.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            getExternalStorageDocument()
        } else {
            requestPermission()
        }
    }

    /**
     * This Method is Requesting the Permission
     */
    fun requestPermission() {
        (activity as BaseActivity?)?.let {
            CheckSelfPermissionAndRequest.requestPermission(
                it,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Constants.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE,
                resources.getString(
                    R.string.dialog_message_read_external_storage_permission_denied
                ),
                resources.getString(R.string.dialog_message_permission_never_ask_again_read),
                Constants.READ_EXTERNAL_STORAGE_STATUS
            )
        }
    }

    /**
     * This Method getting the Response after User Grant or denied the Permission
     *
     * @param requestCode  Request Code
     * @param permissions  Permission
     * @param grantResults GrantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    // permission was granted, yay! Do the
                    getExternalStorageDocument()
                } else {

                    // permission denied, boo! Disable the
                    Toaster.show(
                        rootView, resources
                            .getString(R.string.permission_denied_to_read_external_document)
                    )
                }
            }
        }
    }

    /**
     * This method is to start an intent(getExternal Storage Document).
     * If Android Version is Kitkat or greater then start intent with ACTION_OPEN_DOCUMENT,
     * otherwise with ACTION_GET_CONTENT
     */


    override fun getExternalStorageDocument() {
        val gallery = Intent(Intent.ACTION_PICK, Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(gallery, "Open Gallery"), FILE_SELECT_CODE);
    }

    /**
     * This Method will be called when any document will be selected from the External Storage.
     *
     * @param requestCode Request Code
     * @param resultCode  Result Code ok or Cancel
     * @param data        Intent Data
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FILE_SELECT_CODE -> if (resultCode == Activity.RESULT_OK) {
                // Get the Uri of the selected file
                uri = data!!.data
                filePath = FileUtils.getPathReal(activity, uri)
                if (filePath != null) {
                    Log.d("FIle Url", filePath!!)
                    fileChoosen = File(filePath!!)
                }
                if (fileChoosen != null) {
                    imageView?.setImageURI(uri)
                }
                btn_upload.isEnabled = true
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}