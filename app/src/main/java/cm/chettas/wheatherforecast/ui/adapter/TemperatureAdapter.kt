package cm.chettas.wheatherforecast.ui.adapter

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cm.chettas.wheatherforecast.data.HighestTempDay
import cm.chettas.wheatherforecast.databinding.StateTempCardItemLayBinding

class TemperatureAdapter : RecyclerView.Adapter<TemperatureAdapter.MyViewHolder>() {

    private lateinit var binding: StateTempCardItemLayBinding
    private val list = mutableListOf<HighestTempDay>()

    class MyViewHolder(private val binding: StateTempCardItemLayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(highestTempDay: HighestTempDay) {
            binding.model = highestTempDay
            binding.tvTemp.text = "${highestTempDay.temp}° C"
            if (highestTempDay.day>0)
             binding.tvDay.text = "Day ${highestTempDay.day}"
            else binding.tvDay.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = StateTempCardItemLayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]
        holder.bind(model)
    }

    override fun getItemCount(): Int = list.size

    fun addList(data: List<HighestTempDay>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}