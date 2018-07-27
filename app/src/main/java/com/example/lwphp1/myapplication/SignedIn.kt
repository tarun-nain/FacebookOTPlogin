package com.example.lwphp1.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.facebook.accountkit.Account

import com.facebook.accountkit.AccountKit
import com.facebook.accountkit.AccountKitCallback
import com.facebook.accountkit.AccountKitError

class SignedIn : AppCompatActivity() {

    private lateinit var signout: Button
    private lateinit var tvPhoneNumber: TextView
    private lateinit var tvEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signed_in)

        signout = findViewById(R.id.btn_logout)
        tvPhoneNumber = findViewById(R.id.et_phonenumber)
        tvEmail = findViewById(R.id.et_email)

        signout.setOnClickListener { AccountKit.logOut() }

        AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
            override fun onSuccess(account: Account) {
                // Get Account Kit ID
                val accountKitId = account.getId()

                // Get phone number
                val phoneNumber = account.getPhoneNumber()
                if (phoneNumber != null) {
                    val phoneNumberString = phoneNumber.toString()
                    tvPhoneNumber.setText(phoneNumberString)
                }

                // Get email
                val email = account.getEmail()


                tvEmail.setText(email)

            }

            override fun onError(error: AccountKitError) {
                // Handle Error
            }
        })



    }


}
