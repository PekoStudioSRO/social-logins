package cz.pekostudio.logins.logins

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleConfiguration
import cz.pekostudio.logins.LoginConfigurationException
import cz.pekostudio.logins.LoginElement
import cz.pekostudio.logins.SocialLogin

/**
 * @author Miroslav Hýbler
 */

class GoogleLogin(private val activity: AppCompatActivity): LoginElement<GoogleSignInAccount?> {

    private val GOOGLE_CODE = 145

    private lateinit var googleClient: GoogleSignInClient

    companion object {
        private val googleOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()

        fun config(init: GoogleLoginConfig.() -> Unit): GoogleLoginConfig = GoogleLoginConfig().also {
            it.init()

            if (it.isValid)
                googleOptions.requestIdToken(it.clientId)
            else
                throw LoginConfigurationException("clientId must not be null")
        }
    }

    init {
        init(activity)
    }

    override var onSuccess: ((GoogleSignInAccount?) -> Unit)? = null

    override fun init(activity: AppCompatActivity) {
        googleClient = GoogleSignIn.getClient(activity, googleOptions.build())
    }

    override fun login(onSuccess: (GoogleSignInAccount?) -> Unit) {
        this.onSuccess = onSuccess
        activity.startActivityForResult(googleClient.signInIntent, GOOGLE_CODE)
    }

    override fun logout() {
        googleClient.signOut()
    }

    fun withId(id: String) {
        googleOptions.requestIdToken(id)
    }

    private fun getGoogleAccount(completedTask: Task<GoogleSignInAccount?>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            onSuccess?.invoke(account)
        }
        catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GOOGLE_CODE && data != null) {
            getGoogleAccount(GoogleSignIn.getSignedInAccountFromIntent(data))
        }
    }

    class GoogleLoginConfig {
        var clientId: String? = null

        val isValid get() = !clientId.isNullOrEmpty()
    }
}