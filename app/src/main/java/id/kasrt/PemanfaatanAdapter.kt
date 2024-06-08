package id.kasrt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.kasrt.model.DataItem

class PemanfaatanAdpater(private val pemanfaatan:MutableList<DataItem>) :
    RecyclerView.Adapter<PemanfaatanAdpater.ListViewHolder>() {
    constructor() : this(mutableListOf())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pemanfaatan, parent, false)
        return ListViewHolder(view)
    }

    fun setPemanfaatan(dataItems: List<DataItem>) {
        pemanfaatan.clear()
        pemanfaatan.addAll(dataItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = pemanfaatan.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = pemanfaatan[position]

        holder.tvPemanfaatan.text = "-" + item.kegunaan_iuran + ":"
        holder.tvTotalIuranRekap.text = item.pengeluaran_warga.toString()
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvPemanfaatan: TextView = itemView.findViewById(R.id.itemPemanfaatan)
        var tvTotalIuranRekap: TextView = itemView.findViewById(R.id.itemDanaPemanfaatan)
    }
}