package id.kasrt


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.kasrt.model.ResponseUser
import id.kasrt.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class fragment3 : Fragment() {

    private lateinit var adapter: PemanfaatanAdapter
    private lateinit var rvLaporan: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tab3, container, false)

        // Initialize RecyclerView
        rvLaporan = view.findViewById(R.id.rv_laporan)
        rvLaporan.layoutManager = LinearLayoutManager(requireContext())

        // Initialize adapter
        adapter = PemanfaatanAdapter(mutableListOf())
        rvLaporan.adapter = adapter

        // Fetch data from API
        getPemanfaatan()

        return view
    }

    private fun getPemanfaatan() {
        val apiService = ApiConfig.getApiService()
        val client = apiService.getPemanfaatan()

        client.enqueue(object : Callback<ResponseUser> {
            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                if (response.isSuccessful) {
                    val dataArray = response.body()?.data
                    if (dataArray != null) {
                        // Set data pemanfaatan pada adapter
                        adapter.setPemanfaatan(dataArray)
                    } else {
                        Toast.makeText(requireContext(), "Data not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }
}
