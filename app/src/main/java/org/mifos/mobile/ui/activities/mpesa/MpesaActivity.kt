package org.mifos.mobile.ui.activities.mpesa

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityMpesaBinding

@AndroidEntryPoint
class MpesaActivity : AppCompatActivity() {
    lateinit var binding: ActivityMpesaBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mpesa)

        binding.Checkout.setOnClickListener {
            if (validate()){
                val dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                dialog.titleText = "Please Wait..."
                dialog.setCancelable(false)
                dialog.show()

                val phoneNumber = binding.PhoneNumber.editText!!.text.toString().trim().trimStart('0')
                val model = Mpesa(
                    "254$phoneNumber",
                    "1",
                    "RHHRR",
                )
                viewModel.stkpush(model)

                viewModel.mpesa_response.observe(this){
                    if (it != null){
                        dialog.dismiss()
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }else{
                        dialog.dismiss();
                    }
                }
            }

        }


    }

    fun validate(): Boolean {
        var valid = true
        if (binding.PhoneNumber.editText!!.text.toString().trim().isEmpty() || binding.PhoneNumber.editText!!.text.toString().trim().length != 10){
            binding.PhoneNumber.error = "Please input valid mobile number"
            valid = false
        }else{
            binding.PhoneNumber.error = ""
        }


        return valid
    }
}