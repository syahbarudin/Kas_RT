package id.kasrt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.kasrt.R
import id.kasrt.model.DataItem

class WargaAdapter(private val dataItemList: MutableList<DataItem>) :
    RecyclerView.Adapter<WargaAdapter.DataItemViewHolder>() {

    fun setUsers(dataList: List<DataItem>) {
        dataItemList.clear()
        dataItemList.addAll(dataList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.data_warga, parent, false)
        return DataItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataItemViewHolder, position: Int) {
        val dataItem = dataItemList[position]
        holder.bind(dataItem)
    }

    override fun getItemCount(): Int {
        return dataItemList.size
    }

    inner class DataItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemName: TextView = itemView.findViewById(R.id.itemName)
        private val itemEmail: TextView = itemView.findViewById(R.id.itemEmail)
        private val itemAddress: TextView = itemView.findViewById(R.id.itemAddress)
        private val itemImage: ImageView = itemView.findViewById(R.id.itemAvatar)

        fun bind(dataItem: DataItem) {
            itemName.text = "${dataItem.namaDepan} ${dataItem.namaBelakang}"
            itemEmail.text = dataItem.email
            itemAddress.text = dataItem.alamat


            Glide.with(itemView.context)
                .load(dataItem.imageUrl)
                .into(itemImage)
        }
    }
}