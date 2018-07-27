package com.example.lwphp1.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.facebook.accountkit.AccountKitLoginResult
import com.facebook.accountkit.ui.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {
    private lateinit var btnOTP: Button
    private lateinit var btnEmail: Button
    private lateinit var uiManager: UIManager

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        printHashKey(this)
        // Skin is CLASSIC, CONTEMPORARY, or TRANSLUCEN

        //       uiManager=new  SkinManager(SkinManager.Skin.TRANSLUCENT,
        //               ContextCompat.getColor(this,R.color.colorAccent));

        uiManager = SkinManager(SkinManager.Skin.TRANSLUCENT,
                ContextCompat.getColor(this, R.color.colorAccent)



        btnOTP = findViewById(R.id.btn_otp)
        btnEmail = findViewById(R.id.btn_email)

        btnOTP.setOnClickListener { StartLoginPage(LoginType.PHONE) }

        btnEmail.setOnClickListener { StartLoginPage(LoginType.EMAIL) }
    }

    private fun StartLoginPage(loginType: LoginType) {
        if (loginType == LoginType.EMAIL) {
            val intent = Intent(this, AccountKitActivity::class.java)
            val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
                    LoginType.EMAIL,
                    AccountKitActivity.ResponseType.CODE) // Use token when 'Enable client Access Token Flow' is YES
            intent.putExtra(
                    AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                    configurationBuilder.build())
            startActivityForResult(intent, REQUEST_CODE)


        } else if (loginType == LoginType.PHONE) {
            val intent = Intent(this, AccountKitActivity::class.java)
            val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
                    LoginType.PHONE,
                    AccountKitActivity.ResponseType.CODE) // Use token when 'Enable client Access Token Flow' is YES
            intent.putExtra(
                    AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                    configurationBuilder.build())
            configurationBuilder.setUIManager(uiManager)
            startActivityForResult(intent, REQUEST_CODE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) { // confirm that this response matches your request
            val loginResult = data.getParcelableExtra<AccountKitLoginResult>(AccountKitLoginResult.RESULT_KEY)
            val toastMessage: String
            if (loginResult.error != null) {
                toastMessage = loginResult.error!!.errorType.message

                return

            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled"
                return
            } else {
                if (loginResult.accessToken != null) {
                    toastMessage = "Success:" + loginResult.accessToken!!.accountId
                } else {


                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.authorizationCode!!.substring(0, 10))
                }

                // Success! Start your next activity...
            }
            Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show()
        }
    }


    fun printHashKey(pContext: Context) {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = Base64.encodeToString(md.digest(), 0)

                Log.i(TAG, "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(TAG, "printHashKey()", e)
        }

    }

    companion object {

        val TAG = "OTP Activity"
        var REQUEST_CODE = 999
    }
}




