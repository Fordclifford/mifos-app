package org.lspl.mobile.ui.fragments

import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import org.lspl.mobile.R
import org.lspl.mobile.api.BaseApiManager
import org.lspl.mobile.api.BaseURL
import org.lspl.mobile.api.RequiredFieldException
import org.lspl.mobile.api.SelfServiceInterceptor
import org.lspl.mobile.exceptions.InvalidTextInputException
import org.lspl.mobile.models.client.NextOfKinPayload
import org.lspl.mobile.models.templates.client.FamilyMemberOptions
import org.lspl.mobile.presenters.RegistrationVerificationPresenter
import org.lspl.mobile.ui.activities.base.BaseActivity
import org.lspl.mobile.ui.fragments.base.BaseFragment
import org.lspl.mobile.ui.helpers.MFDatePicker
import org.lspl.mobile.ui.views.RegistrationVerificationView
import org.lspl.mobile.utils.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by dilpreet on 31/7/17.
 */
class NextOfKinRegistrationFragment(clientId: Long) : BaseFragment(),
    MFDatePicker.OnDatePickListener, RegistrationVerificationView,
    AdapterView.OnItemSelectedListener {

    @JvmField
    @BindView(R.id.et_id_number_nok)
    var etIdNumber: EditText? = null


    @JvmField
    @BindView(R.id.et_first_name_nok)
    var etFirstName: EditText? = null

    @JvmField
    @BindView(R.id.et_middle_name_nok)
    var etMiddleName: EditText? = null

    @JvmField
    @BindView(R.id.et_last_name_nok)
    var etLastName: EditText? = null

    @JvmField
    @BindView(R.id.et_phone_number_nok)
    var etPhoneNumber: EditText? = null


    @JvmField
    @BindView(R.id.gender_options)
    var spGender: MaterialAutoCompleteTextView? = null


    @JvmField
    @BindView(R.id.marital_status)
    var spMarital: MaterialAutoCompleteTextView? = null

    @JvmField
    @BindView(R.id.relationship)
    var spRelationship: MaterialAutoCompleteTextView? = null

    @JvmField
    @BindView(R.id.occupation)
    var spOccupation: MaterialAutoCompleteTextView? = null

    @JvmField
    @BindView(R.id.tv_dateofbirth)
    var tvDateOfBirth: TextView? = null

    @JvmField
    @BindView(R.id.is_dependent)
    var cbIsDependent: CheckBox? = null

    @JvmField
    var datePickerDateOfBirth: DialogFragment? = null

    private var mCurrentDateView // the view whose click opened the date picker
            : View? = null

    private var genderId = 0
    private var relationshipId = 0
    private var occupationId = 0
    private var maritalStatusId = 0
    private var result = true
    private var dateOfBirthString: String? = null


    var clientId: Long? = clientId
    private var genderIdOptions: MutableList<String>? = ArrayList()
    private var maritalStatusIdOptions: MutableList<String>? = ArrayList()
    private var professionIdOptions: MutableList<String>? = ArrayList()
    private var relationshipIdOptions: MutableList<String>? = ArrayList()
    private var familyMembersTemplate: FamilyMemberOptions? = null
    private var selectedDob: Instant = Instant.now()
    private val datePickerDialog by lazy {
        getDatePickerDialog(selectedDob, DatePickerConstrainType.ONLY_PAST_DAYS) {
            val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(it)
            tvDateOfBirth?.text = formattedDate
            dateOfBirthString = formattedDate

            setSubmissionDisburseDate()
        }
    }


    @JvmField
    @Inject
    var presenter: RegistrationVerificationPresenter? = null
    private var rootView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_next_of_kin_registration, container, false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        ButterKnife.bind(this, rootView!!)
        presenter?.attachView(this)
        showUserInterface()
        presenter!!.locaFamilyTemplate(clientId)
        return rootView
    }

    private fun showUserInterface() {

        spGender?.setSimpleItems(genderIdOptions!!.toTypedArray())
        spGender?.setSimpleItems(genderIdOptions!!.toTypedArray())

        spGender?.setOnItemClickListener { _, _, position, _ ->
            familyMembersTemplate?.genderIdOptions?.let {
                if (it.size > position) {
                    genderId = it[position].id
                }
            }
        }

        //Relationship
        spRelationship?.setSimpleItems(relationshipIdOptions!!.toTypedArray())
        spRelationship?.setSimpleItems(relationshipIdOptions!!.toTypedArray())

        spRelationship?.setOnItemClickListener { _, _, position, _ ->
            familyMembersTemplate?.relationshipIdOptions?.let {
                if (it.size > position) {
                    relationshipId = it[position].id
                }
            }
        }

        //Occupation
        spOccupation?.setSimpleItems(professionIdOptions!!.toTypedArray())
        spOccupation?.setSimpleItems(professionIdOptions!!.toTypedArray())

        spOccupation?.setOnItemClickListener { _, _, position, _ ->
            familyMembersTemplate?.professionIdOptions?.let {
                if (it.size > position) {
                    occupationId = it[position].id
                }
            }
        }

        //Marital Status
        spMarital?.setSimpleItems(maritalStatusIdOptions!!.toTypedArray())
        spMarital?.setSimpleItems(maritalStatusIdOptions!!.toTypedArray())

        spMarital?.setOnItemClickListener { _, _, position, _ ->
            println("loan_purpose_field clicked")
            familyMembersTemplate?.maritalStatusIdOptions?.let {
                if (it.size > position) {
                    maritalStatusId = it[position].id
                }
            }
        }
    }

    @OnClick(R.id.dob_edit)
    fun setTvDisbursementOnDate() {
        datePickerDialog.show(requireActivity().supportFragmentManager, Constants.DFRAG_DATE_PICKER)
    }

    override fun onDatePicked(date: String) {
        if (mCurrentDateView != null && mCurrentDateView === tvDateOfBirth) {
            tvDateOfBirth!!.text = date
        }
    }

    @OnClick(R.id.next_of_kin_register)
    fun verifyClicked() {
        val payload = NextOfKinPayload()

        if (tvDateOfBirth!!.text.toString()!!.isNotEmpty()) {
            dateOfBirthString = tvDateOfBirth!!.text.toString()
            dateOfBirthString =
                DateHelper.getDateAsStringUsedForDateofBirth(dateOfBirthString)
                    ?.replace("-", " ")
            payload.dateOfBirth = dateOfBirthString
        }


        payload.firstName = etFirstName?.text.toString()
        payload.middleName = etMiddleName?.text.toString()
        payload.lastName = etLastName?.text.toString()
        payload.mobileNumber = etPhoneNumber?.text.toString()
        payload.isDependent = cbIsDependent!!.isChecked

        if (!TextUtils.isEmpty(etMiddleName!!.editableText.toString())) {
            payload.middleName = etMiddleName!!.editableText.toString()
        }
        if (PhoneNumberUtils.isGlobalPhoneNumber(etPhoneNumber!!.editableText.toString())) {
            payload.mobileNumber = etPhoneNumber!!.editableText.toString()
        }
        if (!TextUtils.isEmpty(etIdNumber!!.editableText.toString())) {
            payload.qualification = etIdNumber!!.editableText.toString()
        }
        if (genderIdOptions!!.isNotEmpty() && genderId != 0) {
            payload.genderId = genderId
        }

        if (maritalStatusIdOptions!!.isNotEmpty() && maritalStatusId != 0) {
            payload.maritalStatusId = maritalStatusId
        }

        if (professionIdOptions!!.isNotEmpty() && occupationId != 0) {
            payload.professionId = occupationId
        }
        if (relationshipIdOptions!!.isNotEmpty() && relationshipId != 0) {
            payload.relationshipId = relationshipId
        }


        if (!isFirstNameValid) {
            return
        }
        if (!isValidMobile) {
            return
        }
        if (!isValidRelationship) {
            return
        }
        if (!isIdValid) {
            return
        }
        if (!isMiddleNameValid) {
            return
        }
        if (isLastNameValid) {
            if (Network.isConnected(context)) {
                presenter?.createNok(payload, clientId)
            } else {
                Toaster.show(rootView, getString(R.string.no_internet_connection))
            }
        }
    }

    fun setSubmissionDisburseDate() {
        dateOfBirthString = tvDateOfBirth?.text.toString()
        dateOfBirthString = DateHelper.getSpecificFormat(
            DateHelper.FORMAT_dd_MMMM_yyyy, dateOfBirthString
        )
    }

    override fun onItemSelected(
        parent: AdapterView<*>,
        view: View,
        position: Int,
        id: Long
    ) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
    val isFirstNameValid: Boolean
        get() {
            result = true
            try {
                if (TextUtils.isEmpty(etFirstName!!.editableText.toString())) {
                    throw RequiredFieldException(
                        resources.getString(R.string.first_name),
                        resources.getString(R.string.error_cannot_be_empty)
                    )
                }
                if (!ValidationUtil.isNameValid(etFirstName!!.editableText.toString())) {
                    throw InvalidTextInputException(
                        resources.getString(R.string.first_name),
                        resources.getString(R.string.error_should_contain_only),
                        InvalidTextInputException.TYPE_ALPHABETS
                    )
                }
            } catch (e: InvalidTextInputException) {
                e.notifyUserWithToast(activity)
                result = false
            } catch (e: RequiredFieldException) {
                e.notifyUserWithToast(activity)
                result = false
            }
            return result
        }

    val isMiddleNameValid: Boolean
        get() {
            result = true
            try {
                if (!TextUtils.isEmpty(etMiddleName!!.editableText.toString())
                    && !ValidationUtil.isNameValid(
                        etMiddleName!!.editableText
                            .toString()
                    )
                ) {
                    throw InvalidTextInputException(
                        resources.getString(R.string.middle_name),
                        resources.getString(R.string.error_should_contain_only),
                        InvalidTextInputException.TYPE_ALPHABETS
                    )
                }
            } catch (e: InvalidTextInputException) {
                e.notifyUserWithToast(activity)
                result = false
            }
            return result
        }

    val isLastNameValid: Boolean
        get() {
            result = true
            try {
                if (TextUtils.isEmpty(etLastName!!.editableText.toString())) {
                    throw RequiredFieldException(
                        resources.getString(R.string.last_name),
                        resources.getString(R.string.error_cannot_be_empty)
                    )
                }
                if (!ValidationUtil.isNameValid(etLastName!!.editableText.toString())) {
                    throw InvalidTextInputException(
                        resources.getString(R.string.last_name),
                        resources.getString(R.string.error_should_contain_only),
                        InvalidTextInputException.TYPE_ALPHABETS
                    )
                }
            } catch (e: InvalidTextInputException) {
                e.notifyUserWithToast(activity)
                result = false
            } catch (e: RequiredFieldException) {
                e.notifyUserWithToast(activity)
                result = false
            }
            return result
        }

    val isIdValid: Boolean
        get() {
            result = true
            try {
                if (TextUtils.isEmpty(etIdNumber!!.editableText.toString())) {
                    throw RequiredFieldException(
                        resources.getString(R.string.id_number),
                        resources.getString(R.string.error_cannot_be_empty)
                    )
                }
            } catch (e: RequiredFieldException) {
                e.notifyUserWithToast(activity)
                result = false
            }
            return result
        }


    val isValidRelationship: Boolean
        get() {
            result = true
            try {
                if (TextUtils.isEmpty(relationshipId.toString()) || relationshipId==0) {
                    throw RequiredFieldException(
                        resources.getString(R.string.relationship),
                        resources.getString(R.string.error_cannot_be_empty)
                    )
                }
            } catch (e: RequiredFieldException) {
                e.notifyUserWithToast(activity)
                result = false
            }
            return result
        }
    val isValidMobile: Boolean
        get() {
            result = true
            try {
                if (TextUtils.isEmpty(etPhoneNumber?.text.toString())) {
                    throw RequiredFieldException(
                        resources.getString(R.string.mobile_no),
                        resources.getString(R.string.error_cannot_be_empty)
                    )
                }
            } catch (e: RequiredFieldException) {
                e.notifyUserWithToast(activity)
                result = false
            }
            return result
        }
    override fun showVerifiedSuccessfully() {
        (activity as BaseActivity?)?.replaceFragment(
            NextOfKinIdUploadFragment.newInstance(clientId!!),
            true,
            R.id.container
        )

        Toast.makeText(
            context,
            getString(R.string.successful),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showClientTemplate(clientsTemplate: FamilyMemberOptions?) {
        Log.d("We passed this", clientsTemplate.toString())
        this.familyMembersTemplate = clientsTemplate

        if (familyMembersTemplate?.genderIdOptions != null) {
            for ((_, name) in familyMembersTemplate!!.genderIdOptions!!) {
                genderIdOptions!!.add(name!!)
            }
            spGender?.setSimpleItems(genderIdOptions!!.toTypedArray())
        }
        //Marital Status

        if (familyMembersTemplate?.maritalStatusIdOptions != null) {
            for ((_, name) in familyMembersTemplate!!.maritalStatusIdOptions!!) {
                maritalStatusIdOptions!!.add(name!!)
            }
            spMarital?.setSimpleItems(maritalStatusIdOptions!!.toTypedArray())

        }
        //Profession

        if (familyMembersTemplate?.professionIdOptions != null) {
            for ((_, name) in familyMembersTemplate!!.professionIdOptions!!) {
                professionIdOptions!!.add(name!!)
            }
            spOccupation?.setSimpleItems(professionIdOptions!!.toTypedArray())
        }

        //Repationship


        if (familyMembersTemplate?.relationshipIdOptions != null) {
            for ((_, name) in familyMembersTemplate!!.relationshipIdOptions!!) {
                relationshipIdOptions!!.add(name!!)
            }
            spRelationship?.setSimpleItems(relationshipIdOptions!!.toTypedArray())
        }

    }

    override fun showError(msg: String?) {
        Toaster.show(rootView, msg)
    }

    override fun showProgress() {
        showMifosProgressDialog(getString(R.string.processing))
    }

    override fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
    }

    companion object {
        fun newInstance(clientId: Long): NextOfKinRegistrationFragment {
            return NextOfKinRegistrationFragment(clientId)
        }
    }
}