package id.kasrt

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import id.kasrt.model.DataItem

class LaporanActivity : AppCompatActivity() {

    private lateinit var etNamaDepan: EditText
    private lateinit var etNamaBelakang: EditText
    private lateinit var etEmail: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etLaporan: EditText
    private lateinit var imgPreview: ImageView
    private lateinit var btnChooseImage: Button
    private lateinit var btnSubmit: Button
    private var imageUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.laporan_warga)

        etNamaDepan = findViewById(R.id.etNamaDepan)
        etNamaBelakang = findViewById(R.id.etNamaBelakang)
        etEmail = findViewById(R.id.etEmail)
        etAlamat = findViewById(R.id.etAlamat)
        etLaporan = findViewById(R.id.etLaporan)
        imgPreview = findViewById(R.id.imgPreview)
        btnChooseImage = findViewById(R.id.btnChooseImage)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnChooseImage.setOnClickListener {
            openFileChooser()
        }

        btnSubmit.setOnClickListener {
            submitLaporan()
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            imageUri = data.data
            imgPreview.setImageURI(imageUri)
        }
    }

    private fun submitLaporan() {
        val namaDepan = etNamaDepan.text.toString().trim()
        val namaBelakang = etNamaBelakang.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()
        val laporan = etLaporan.text.toString().trim()

        if (namaDepan.isEmpty() || namaBelakang.isEmpty() || email.isEmpty() || alamat.isEmpty() || laporan.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val databaseRef = FirebaseDatabase.getInstance().getReference("warga")
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${System.currentTimeMillis()}")

        if (imageUri != null) {
            storageRef.putFile(imageUri!!).addOnSuccessListener { taskSnapshot ->
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val dataItem = DataItem(
                        namaDepan,
                        namaBelakang,
                        email,
                        alamat,
                        laporan,
                        uri.toString()
                    )

                    val uploadId = databaseRef.push().key
                    if (uploadId != null) {
                        databaseRef.child(uploadId).setValue(dataItem)
                        Toast.makeText(this, "Laporan berhasil dikirim", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show()
            }
        } else {
            val dataItem = DataItem(
                namaDepan,
                namaBelakang,
                email,
                alamat,
                laporan,
                ""
            )

            val uploadId = databaseRef.push().key
            if (uploadId != null) {
                databaseRef.child(uploadId).setValue(dataItem)
                Toast.makeText(this, "Laporan berhasil dikirim", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
