package com.lx.red

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.activity.result.contract.ActivityResultContracts
import com.lx.red.databinding.ActivityAgreeBinding
import com.lx.red.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_agree.*

class AgreeActivity : AppCompatActivity() {

    lateinit var binding: ActivityAgreeBinding
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        infocheckBox.setOnClickListener { onCheckChanged(infocheckBox) }
        localcheckBox.setOnClickListener { onCheckChanged(localcheckBox) }
        emercheckBox.setOnClickListener { onCheckChanged(emercheckBox) }
        bodycheckBox.setOnClickListener { onCheckChanged(bodycheckBox) }
        allcheckBox.setOnClickListener { onCheckChanged(allcheckBox) }


        //약관동의-> 회원가입
        binding.agreeButton.setOnClickListener {
            if (infocheckBox.isChecked){
                if(localcheckBox.isChecked){
                    launcher.launch(Intent(applicationContext, RegisterActivity::class.java))
//                        if(emercheckBox.isChecked){
//                            if(bodycheckBox.isChecked){
//                                launcher.launch(Intent(applicationContext, RegisterActivity::class.java))
//                            }
//                        }
                }
            }else{
                return@setOnClickListener
            }



        }
        binding.allcheckBox.setOnCheckedChangeListener{compoundButton, checked ->
            binding.agreeButton.isEnabled = allcheckBox.isChecked
        }
    }





    private fun onCheckChanged(compoundButton: CompoundButton) {
        when (compoundButton.id) {
            R.id.allcheckBox -> {
                if (allcheckBox.isChecked) {
                    infocheckBox.isChecked = true
                    localcheckBox.isChecked = true
                    emercheckBox.isChecked = true
                    bodycheckBox.isChecked = true
                } else {
                    infocheckBox.isChecked = false
                    localcheckBox.isChecked = false
                    emercheckBox.isChecked = false
                    bodycheckBox.isChecked = false
                }
            }
            else -> {
                allcheckBox.isChecked = (
                        infocheckBox.isChecked
                                && localcheckBox.isChecked
                                && emercheckBox.isChecked
                                && bodycheckBox.isChecked)
            }
        }
    }
}

