package id.kasrt

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import id.kasrt.databinding.ActivityLoginBinding
import id.kasrt.face.FaceRecognitionActivity
import id.kasrt.ui.fragmentmain

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.forgotPassword.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.tvToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.tvToFace.setOnClickListener {
            val intent = Intent(this, FaceRecognitionActivity::class.java)
            intent.putExtra("ACTION_TYPE", "LOGIN")
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailLogin.text.toString()
            val password = binding.edtPasswordLogin.text.toString()

            // Validasi email
            if (email.isEmpty()) {
                binding.edtEmailLogin.error = "Email harus diisi"
                binding.edtEmailLogin.requestFocus()
                return@setOnClickListener
            }

            // Validasi email tidak sesuai format
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmailLogin.error = "Format email tidak valid"
                binding.edtEmailLogin.requestFocus()
                return@setOnClickListener
            }

            // Validasi password
            if (password.isEmpty()) {
                binding.edtPasswordLogin.error = "Password harus diisi"
                binding.edtPasswordLogin.requestFocus()
                return@setOnClickListener
            }

            // Lakukan proses login
            loginUserWithEmail(email, password)
        }
    }

    private fun loginUserWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Selamat datang, ${user?.email}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, fragmentmain::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            if (::backToast.isInitialized) {
                backToast.cancel()
            }
            super.onBackPressed()
            finishAffinity() // This will close the app
        } else {
            backToast = Toast.makeText(this, "Tekan kembali lagi untuk keluar", Toast.LENGTH_SHORT)
            backToast.show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
