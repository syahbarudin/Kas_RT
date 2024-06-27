package id.kasrt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.kasrt.R
import id.kasrt.adapter.CashflowAdapter
import id.kasrt.model.CashflowItem
import id.kasrt.model.LaporanKeuangan
import id.kasrt.model.ResponseUser
import id.kasrt.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Tab3 : Fragment() {

        private lateinit var rvCashflow: RecyclerView
        private lateinit var sisaSaldoMaretTextView: TextView
        private lateinit var masukTextView: TextView
        private lateinit var keluarTextView: TextView
        private lateinit var sisaSaldoAprilTextView: TextView
        private lateinit var adapter: CashflowAdapter

        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
                val rootView = inflater.inflate(R.layout.fragment_tab3, container, false)

                sisaSaldoMaretTextView = rootView.findViewById(R.id.sisaSaldoMaretTextView)
                masukTextView = rootView.findViewById(R.id.masukTextView)
                keluarTextView = rootView.findViewById(R.id.keluarTextView)
                sisaSaldoAprilTextView = rootView.findViewById(R.id.sisaSaldoAprilTextView)
                rvCashflow = rootView.findViewById(R.id.rv_cashflow)

                adapter = CashflowAdapter(emptyList())
                rvCashflow.layoutManager = LinearLayoutManager(requireContext())
                rvCashflow.adapter = adapter

                getCashflow()
                getLaporanKeuangan()

                return rootView
        }

        private fun getCashflow() {
                ApiConfig.getApiService().getCashflow().enqueue(object : Callback<ResponseUser<List<CashflowItem>>> {
                        override fun onResponse(call: Call<ResponseUser<List<CashflowItem>>>, response: Response<ResponseUser<List<CashflowItem>>>) {
                                if (response.isSuccessful) {
                                        val responseBody = response.body()
                                        if (responseBody != null) {
                                                val dataList = responseBody.data

                                                adapter.setData(dataList)
                                        } else {
                                                Toast.makeText(requireContext(), "Cashflow data not found", Toast.LENGTH_SHORT).show()
                                        }
                                } else {
                                        Toast.makeText(requireContext(), "Failed to retrieve cashflow data", Toast.LENGTH_SHORT).show()
                                }
                        }

                        override fun onFailure(call: Call<ResponseUser<List<CashflowItem>>>, t: Throwable) {
                                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                t.printStackTrace()
                        }
                })
        }


        private fun getLaporanKeuangan() {
                ApiConfig.getApiService().getLaporanKeuangan().enqueue(object : Callback<ResponseUser<LaporanKeuangan>> {
                        override fun onResponse(call: Call<ResponseUser<LaporanKeuangan>>, response: Response<ResponseUser<LaporanKeuangan>>) {
                                if (response.isSuccessful) {
                                        val responseBody = response.body()
                                        if (responseBody != null) {
                                                val laporanKeuangan = responseBody.data

                                                sisaSaldoMaretTextView.text = "Sisa Saldo Maret: ${laporanKeuangan.sisaSaldomaret}"
                                                masukTextView.text = "Masuk: ${laporanKeuangan.masuk}"
                                                keluarTextView.text = "Keluar: ${laporanKeuangan.keluar}"
                                                sisaSaldoAprilTextView.text = "Sisa Saldo April: ${laporanKeuangan.sisaSaldoApril}"
                                        } else {
                                                Toast.makeText(requireContext(), "Laporan Keuangan data not found", Toast.LENGTH_SHORT).show()
                                        }
                                } else {
                                        Toast.makeText(requireContext(), "Failed to retrieve Laporan Keuangan data", Toast.LENGTH_SHORT).show()
                                }
                        }

                        override fun onFailure(call: Call<ResponseUser<LaporanKeuangan>>, t: Throwable) {
                                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                t.printStackTrace()
                        }
                })
        }
}
