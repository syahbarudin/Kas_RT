package id.kasrt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.kasrt.R
import id.kasrt.model.CashflowItem

class CashflowAdapter(private var cashflowList: List<CashflowItem>) :
    RecyclerView.Adapter<CashflowAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cashflow, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cashflowItem = cashflowList[position]
        holder.bind(cashflowItem)
    }

    override fun getItemCount(): Int = cashflowList.size

    fun setData(newData: List<CashflowItem>) {
        cashflowList = newData
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tanggalTextView: TextView = itemView.findViewById(R.id.tanggalTextView)
        private val keteranganTextView: TextView = itemView.findViewById(R.id.keteranganTextView)
        private val pemasukanTextView: TextView = itemView.findViewById(R.id.pemasukanTextView)
        private val pengeluaranTextView: TextView = itemView.findViewById(R.id.pengeluaranTextView)
        private val saldoAkhirTextView: TextView = itemView.findViewById(R.id.saldoAkhirTextView)

        fun bind(cashflowItem: CashflowItem) {
            tanggalTextView.text = "Tanggal: ${cashflowItem.tanggal}"
            keteranganTextView.text = "Keterangan: ${cashflowItem.keterangan}"
            pemasukanTextView.text = "Pemasukan: ${cashflowItem.pemasukan}"
            pengeluaranTextView.text = "Pengeluaran: ${cashflowItem.pengeluaran}"
            saldoAkhirTextView.text = "Saldo Akhir: ${cashflowItem.saldoAkhir}"
        }
    }
}
