package id.kasrt

import com.google.firebase.database.*
import id.kasrt.model.DataItem

class FirebaseDataManager {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var listener: ValueEventListener

    fun fetchData(callback: (List<DataItem>) -> Unit) {
        // Mendapatkan referensi ke Firebase Database dengan nama "warga"
        databaseReference = FirebaseDatabase.getInstance().getReference("warga")

        // Mendefinisikan listener untuk mendengarkan perubahan data
        listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<DataItem>()

                // Mengambil data dari setiap child di Firebase dan memasukkannya ke dalam list
                for (snapshot in dataSnapshot.children) {
                    val dataItem = snapshot.getValue(DataItem::class.java)
                    dataItem?.let {
                        dataList.add(it)
                    }
                }

                // Memanggil callback dengan mengirimkan data yang sudah diambil
                callback(dataList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Menangani kasus batal pengambilan data
            }
        }

        // Menambahkan listener ke databaseReference
        databaseReference.addValueEventListener(listener)
    }

    // Metode untuk menghapus listener saat tidak diperlukan lagi
    fun removeListener() {
        databaseReference.removeEventListener(listener)
    }
}