package id.kasrt.model

data class DataItem(
    val nama_depan: String,
    val nama_belakang: String,
    val email: String,
    val alamat: String,
    val laporan: String, // Laporan warga
    val imageUrl: String // URL gambar
)
