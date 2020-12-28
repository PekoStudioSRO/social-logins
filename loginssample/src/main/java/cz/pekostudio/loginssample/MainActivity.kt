package cz.pekostudio.loginssample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import cz.pekostudio.logins.SocialLogin
import cz.pekostudio.logins.logins.AppleLogin
import cz.pekostudio.logins.logins.FacebookLogin
import cz.pekostudio.logins.logins.GoogleLogin
import cz.pekostudio.logins.multiple
import cz.pekostudio.uiutils.toast

class MainActivity : AppCompatActivity() {

    lateinit var googleButton: MaterialButton
    lateinit var facebookButton: MaterialButton
    lateinit var appleButton: MaterialButton

    lateinit var facebookLogin: FacebookLogin
    lateinit var googleLogin: GoogleLogin
    lateinit var appleLogin: AppleLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        googleButton = findViewById(R.id.google_login)
        facebookButton = findViewById(R.id.facebook_login)
        appleButton = findViewById(R.id.apple_login)

        facebookLogin = FacebookLogin(this)
        googleLogin = GoogleLogin(this)
        appleLogin = AppleLogin(this)

        /**
         * apple and google login needs initialization in some activity even if you
         * want to use SocialLogin object
         */
        AppleLogin.config {
            clientId = packageName
            redirectUri = "todo uri"
            scope = "email"
        }

        GoogleLogin.config {
            clientId = "todo token"
        }

        simpleLogin()
    }

    override fun onDestroy() {
        super.onDestroy()
        SocialLogin.logout()

    }

    private fun simpleLogin() {
        SocialLogin.init(this)

        googleButton.setOnClickListener {
            SocialLogin.login(SocialLogin.Type.GOOGLE) {
                toast("Google login success")
            }
        }

        facebookButton.setOnClickListener {
            SocialLogin.login(SocialLogin.Type.FACEBOOK) {
                toast("Facebook login success")
            }
        }

        appleButton.setOnClickListener {
            SocialLogin.login(SocialLogin.Type.APPLE) {
                toast("Apple login success")
            }
        }
    }

    private fun customLogin() {


        multiple(facebookLogin, googleLogin, appleLogin) {
            it.init(this)
        }

        googleButton.setOnClickListener {
            googleLogin.login {
                toast("Google login success")
            }
        }

        facebookButton.setOnClickListener {
            facebookLogin.login {
                toast("Facebook login success")
            }
        }

        appleButton.setOnClickListener {
            appleLogin.login {
                toast("Apple login success")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        SocialLogin.onActivityResult(requestCode, resultCode, data)
        
        //or
        facebookLogin.onActivityResult(requestCode, resultCode, data)
        googleLogin.onActivityResult(requestCode, resultCode, data)
    }
}