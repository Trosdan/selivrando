package com.exemplo.jordan.selivrando

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceManager
import android.support.annotation.NonNull
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.internal.FirebaseAppHelper.getUid
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId


class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                Log.d("FirebaseLogin", "onAuthStateChanged:signed_in:" + user.uid)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                // User is signed out
                Log.d("FirebaseLogin", "onAuthStateChanged:signed_out")
            }
            // ...
        }

        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("ManterConectado", false)){
            mAuth?.signInWithCustomToken(PreferenceManager.getDefaultSharedPreferences(this).getString("UserToken", ""))
                    ?.addOnCompleteListener(this) { task ->
                        Log.d("FirebaseLogin", "signInWithEmail:onComplete:" + task.isSuccessful)

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful) {
                            Log.w("FirebaseLogin", "signInWithEmail:failed", task.exception)
                            Toast.makeText(this@LoginActivity, "Falha no login",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        }

    }

    override fun onStart() {
        super.onStart()
        mAuth?.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth?.removeAuthStateListener(mAuthListener!!);
        }
    }


    public fun btn_Login(view: View){
        mAuth?.signInWithEmailAndPassword(login.text.toString(), password.text.toString())
                ?.addOnCompleteListener(this) { task ->
                    Log.d("FirebaseLogin", "signInWithEmail:onComplete:" + task.isSuccessful)

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        Log.w("FirebaseLogin", "signInWithEmail:failed", task.exception)
                        Toast.makeText(this@LoginActivity, "Falha no login",
                                Toast.LENGTH_SHORT).show()
                    }
                    if(manterConect.isChecked) {
                        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("ManterConectado", true).commit()
                        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("UserUID", task.getResult().user.uid).commit()
                        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("UserToken", FirebaseInstanceId.getInstance().token).commit()
                    }
                }
    }

    public fun btn_CadasteSe(view: View) {
        startActivity(Intent(this@LoginActivity, CadastroUsuario::class.java))
    }

}
