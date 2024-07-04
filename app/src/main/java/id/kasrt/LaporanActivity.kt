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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import id.kasrt.model.DataItem

@Suppress("DEPRECATION")
class LaporanActivity : AppCompatActivity() {

    // Declare UI elements
    private lateinit var etNamaDepan: EditText
    private lateinit var etNamaBelakang: EditText
    private lateinit var etEmail: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etLaporan: EditText
    private lateinit var imgPreview: ImageView
    private lateinit var btnChooseImage: Button
    private lateinit var btnSubmit: Button

    // Image URI for the selected image
    private var imageUri: Uri? = null

    // Request code for picking an image
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.laporan_warga)

        // Initialize UI elements
        etNamaDepan = findViewById(R.id.etNamaDepan)
        etNamaBelakang = findViewById(R.id.etNamaBelakang)
        etEmail = findViewById(R.id.etEmail)
        etAlamat = findViewById(R.id.etAlamat)
        etLaporan = findViewById(R.id.etLaporan)
        imgPreview = findViewById(R.id.imgPreview)
        btnChooseImage = findViewById(R.id.btnChooseImage)
        btnSubmit = findViewById(R.id.btnSubmit)

        // Set up button click listeners
        btnChooseImage.setOnClickListener {
            openFileChooser()
        }

        btnSubmit.setOnClickListener {
            submitLaporan()
        }
    }

    // Open file chooser for image selection
    private fun openFileChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Choose an image"), PICK_IMAGE_REQUEST)
    }

    // Handle the result of the image picker activity
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            imgPreview.setImageURI(imageUri)
        }
    }
    // Submit the report with or without an image
    private fun submitLaporan() {
        // Get user inputs
        val namaDepan = etNamaDepan.text.toString().trim()
        val namaBelakang = etNamaBelakang.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()
        val laporan = etLaporan.text.toString().trim()

        // Check if all fields are filled
        if (namaDepan.isEmpty() || namaBelakang.isEmpty() || email.isEmpty() || alamat.isEmpty() || laporan.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a reference for the image in Firebase Storage
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${System.currentTimeMillis()}")
        // Get Firestore instance
        val db = FirebaseFirestore.getInstance()

        if (imageUri != null) {
            // Upload the image
            storageRef.putFile(imageUri!!).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Create a data item for Firestore
                    val dataItem = DataItem(
                        namaDepan = namaDepan,
                        namaBelakang = namaBelakang,
                        email = email,
                        alamat = alamat,
                        laporan = laporan,
                        imageUrl = ""
                    )


                    // Add the report to Firestore
                    db.collection("warga").add(dataItem)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Laporan berhasil dikirim", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Gagal mengunggah laporan: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show()
            }
        } else {
            // If no image, create a data item without the image URL
            val dataItem = DataItem(
                namaDepan = namaDepan,
                namaBelakang = namaBelakang,
                email = email,
                alamat = alamat,
                laporan = laporan,
                imageUrl = ""
            )


            // Add the report to Firestore
            db.collection("warga").add(dataItem)
                .addOnSuccessListener {
                    Toast.makeText(this, "Laporan berhasil dikirim", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal mengunggah laporan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
