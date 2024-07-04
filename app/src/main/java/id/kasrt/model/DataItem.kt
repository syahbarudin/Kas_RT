package id.kasrt.model

data class DataItem(
    val namaDepan: String,
    val namaBelakang: String,
    val email: String,
    val alamat: String,
    val laporan: String, // Laporan warga
    val imageUrl: String // URL gambar
)
