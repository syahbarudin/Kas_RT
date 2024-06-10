package id.kasrt

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.kasrt.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("unique_codes")

        binding.tvToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmailRegister.text.toString().trim()
            val password = binding.edtPasswordRegister.text.toString().trim()
            val nik = binding.edtNIKRegister.text.toString().trim()
            val uniqueCode = binding.edtUniqueCode.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                binding.edtEmailRegister.error = "Email harus diisi"
                binding.edtEmailRegister.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmailRegister.error = "Format email tidak valid"
                binding.edtEmailRegister.requestFocus()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                binding.edtPasswordRegister.error = "Password harus diisi"
                binding.edtPasswordRegister.requestFocus()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(nik)) {
                binding.edtNIKRegister.error = "NIK harus diisi"
                binding.edtNIKRegister.requestFocus()
                return@setOnClickListener
            }

            if (!isValidNIK(nik)) {
                binding.edtNIKRegister.error = "NIK tidak valid"
                binding.edtNIKRegister.requestFocus()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(uniqueCode)) {
                binding.edtUniqueCode.error = "Kode unik harus diisi"
                binding.edtUniqueCode.requestFocus()
                return@setOnClickListener
            }

            checkUniqueCodeAndRegister(uniqueCode, email, password, nik)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun checkUniqueCodeAndRegister(uniqueCode: CharSequence, email: String, password: String, nik: String) {
        databaseReference.child(uniqueCode.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.child("valid").getValue(Boolean::class.java) == true) {
                    registerUser(email, password, nik)
                } else {
                    Toast.makeText(this@RegisterActivity, "Kode unik tidak valid", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RegisterActivity, "Terjadi kesalahan: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun registerUser(email: String, password: String, nik: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = User(email, nik)
                    FirebaseDatabase.getInstance().getReference("users")
                        .child(auth.currentUser!!.uid)
                        .setValue(user)
                        .addOnCompleteListener { task1 ->
                            if (task1.isSuccessful) {
                                Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                                redirectToLogin()
                            } else {
                                Toast.makeText(this, "Registrasi gagal: ${task1.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun redirectToLogin() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isValidNIK(nik: String): Boolean {
        // Implement your NIK validation logic here
        return nik.length == 16
    }

    data class User(val email: String, val nik: String)
}
