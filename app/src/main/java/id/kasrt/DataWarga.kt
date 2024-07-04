package id.kasrt


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.kasrt.adapter.WargaAdapter
import id.kasrt.model.DataItem

class DataWarga : AppCompatActivity() {

    private lateinit var adapter: WargaAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var firebaseDataManager: FirebaseDataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_warga)

        recyclerView = findViewById(R.id.rv_users)
        adapter = WargaAdapter(mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        firebaseDataManager = FirebaseDataManager()

        firebaseDataManager.fetchData { dataList ->
            adapter.setUsers(dataList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        firebaseDataManager.removeListener()
    }
}
