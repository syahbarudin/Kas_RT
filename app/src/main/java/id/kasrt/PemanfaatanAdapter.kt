package id.kasrt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.kasrt.model.DataItem

class PemanfaatanAdapter(private val dataList: MutableList<DataItem>) : RecyclerView.Adapter<PemanfaatanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pemanfaatan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setPemanfaatan(data: List<DataItem>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTotalIuran: TextView = itemView.findViewById(R.id.totalIuranTextView)
        private val tvPengeluaran: TextView = itemView.findViewById(R.id.tvPengeluaran)
        private val tvKegunaanIuran: TextView = itemView.findViewById(R.id.tvKegunaanIuran)

        fun bind(dataItem: DataItem) {
            tvTotalIuran.text = "Total Iuran: ${dataItem.total_iuran_individu}"
            tvPengeluaran.text = "Pengeluaran: ${dataItem.pengeluaran_warga}"
            tvKegunaanIuran.text = "Kegunaan Iuran: ${dataItem.kegunaan_iuran}"
        }
    }
}
