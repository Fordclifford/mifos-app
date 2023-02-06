package org.enkasacco.mobile.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import org.enkasacco.mobile.R
import org.enkasacco.mobile.api.RequiredFieldException
import org.enkasacco.mobile.presenters.ClientIdUploadPresenter
import org.enkasacco.mobile.ui.activities.LoginActivity
import org.enkasacco.mobile.ui.activities.base.BaseActivity
import org.enkasacco.mobile.ui.fragments.base.BaseFragment
import org.enkasacco.mobile.ui.views.ClientIdUploadView
import org.enkasacco.mobile.utils.CheckSelfPermissionAndRequest
import org.enkasacco.mobile.utils.Constants
import org.enkasacco.mobile.utils.FileUtils
import org.enkasacco.mobile.utils.Toaster
import java.io.File
import javax.inject.Inject

/**
 * Created by dilpreet on 31/7/17.
 */
class NextOfKinIdUploadFragment(clientId: Long) : BaseFragment(), ClientIdUploadView {

    @JvmField
    @BindView(R.id.id_front_choose_nok)
    var buttonChooseFront: Button? = null


    @JvmField
    @BindView(R.id.choose_id_back_nok)
    var buttonChooseBack: Button? = null


    @JvmField
    @BindView(R.id.id_front_view_nok)
    var imageViewFront: ImageView? = null

    @JvmField
    @BindView(R.id.id_back_view_nok)
    var imageViewBack: ImageView? = null

    @JvmField
    @BindView(R.id.next_of_kin_id_upload)
    var buttonUpload: Button? = null

    private var idBackFile: File? = null
    private var idFrontFile: File? = null

    private val FILE_FRONT_SELECT_CODE = 9544
    private val FILE_BACK_SELECT_CODE = 100
    private var uri: Uri? = null
    private var filePath: String? = null
    var backSelected: Boolean = false
    var frontSelected: Boolean = false
    var clientId: Long? = clientId

    @kotlin.jvm.JvmField
    @Inject
    var presenter: ClientIdUploadPresenter? = null
    private var rootView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_upload_next_of_kin_id, container, false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        ButterKnife.bind(this, rootView!!)
        presenter?.attachView(this)
        return rootView
    }

    @OnClick(R.id.next_of_kin_id_upload)
    fun verifyClicked() {
        //   fileUpload()
        try {
            validateInput()
        } catch (e: RequiredFieldException) {
            e.notifyUserWithToast(activity)
        }
    }

    @OnClick(R.id.id_front_choose_nok)
    fun openFilePicker() {
        checkPermissionAndRequestFront()
    }

    @OnClick(R.id.choose_id_back_nok)
    fun openFilePickerBack() {
        checkPermissionAndRequestBack()
    }


    @Throws(RequiredFieldException::class)
    fun validateInput() {
        presenter?.uploadDocument(
            clientId!!, idFrontFile, idBackFile, "Next of Kin"
        )
    }

    override fun showUploadedSuccessfully() {
        startActivity(Intent(activity, LoginActivity::class.java))
        Toast.makeText(context, getString(R.string.successful_login_now), Toast.LENGTH_SHORT).show()
    }

    override fun showError(msg: String?) {
        Toaster.show(rootView, msg)
    }

    override fun showProgress() {
        showMifosProgressDialog(getString(R.string.verifying))
    }

    override fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
    }

    companion object {
        fun newInstance(clientId: Long): NextOfKinIdUploadFragment {
            return NextOfKinIdUploadFragment(clientId)
        }
    }

    override fun checkPermissionAndRequestFront() {
        if (CheckSelfPermissionAndRequest.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            getExternalStorageDocumentFront()
        } else {
            requestPermission()
        }
    }

    override fun checkPermissionAndRequestBack() {
        if (CheckSelfPermissionAndRequest.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            getExternalStorageDocumentBack()
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
            FILE_FRONT_SELECT_CODE -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    // permission was granted, yay! Do the
                    checkPermissionAndRequestFront()
                } else {

                    // permission denied, boo! Disable the
                    Toaster.show(
                        rootView, resources
                            .getString(R.string.permission_denied_to_read_external_document)
                    )
                }
            }
            FILE_BACK_SELECT_CODE -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    // permission was granted, yay! Do the
                    checkPermissionAndRequestBack()
                } else {

                    // permission denied, boo! Disable the
                    Toaster.show(
                        rootView, resources
                            .getString(R.string.permission_denied_to_read_external_document)
                    )
                }
            }
            Constants.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    // permission was granted, yay! Do the
                    checkPermissionAndRequestBack()
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


    override fun getExternalStorageDocumentBack() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(
            Intent.createChooser(gallery, "Open Gallery"),
            FILE_BACK_SELECT_CODE
        );
    }


    override fun getExternalStorageDocumentFront() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(
            Intent.createChooser(gallery, "Open Gallery"),
            FILE_FRONT_SELECT_CODE
        );
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
            FILE_FRONT_SELECT_CODE -> if (resultCode == Activity.RESULT_OK) {
                // Get the Uri of the selected file
                uri = data!!.data
                filePath = FileUtils.getPathReal(activity, uri)
                if (filePath != null) {
                    Log.d("FIle Url", filePath!!)
                    idFrontFile = File(filePath!!)
                }
                if (idFrontFile != null) {
                    imageViewFront?.setImageURI(uri)
                }
                frontSelected = true
                if (backSelected) {
                    buttonUpload!!.isEnabled = true
                }

            }
            FILE_BACK_SELECT_CODE -> if (resultCode == Activity.RESULT_OK) {
                // Get the Uri of the selected file
                uri = data!!.data
                filePath = FileUtils.getPathReal(activity, uri)
                if (filePath != null) {
                    Log.d("FIle Url", filePath!!)
                    idBackFile = File(filePath!!)
                }
                if (idBackFile != null) {
                    imageViewBack?.setImageURI(uri)
                }
                backSelected = true
                if (frontSelected) {
                    buttonUpload!!.isEnabled = true
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}