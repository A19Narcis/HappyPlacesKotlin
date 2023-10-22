package test.narcis.happyplaces.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import test.narcis.happyplaces.MainActivity
import test.narcis.happyplaces.R
import test.narcis.happyplaces.activities.AddHappyPlaceActivity
import test.narcis.happyplaces.database.DatabaseHandler
import test.narcis.happyplaces.models.HappyPlaceModel

open class HappyPlacesAdapter(private val context: Context, private var list: ArrayList<HappyPlaceModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onclickListener : onClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_happy_place, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model : HappyPlaceModel = list[position]
        if (holder is MyViewHolder) {

            val image : ImageView = holder.itemView.findViewById(R.id.iv_place_image)
            image.setImageURI(Uri.parse(model.image))

            val title : TextView = holder.itemView.findViewById(R.id.tvTitle)
            title.text = model.title

            val description : TextView = holder.itemView.findViewById(R.id.tvDescription)
            description.text = model.description

            holder.itemView.setOnClickListener{
                onclickListener?.onClick(position, model)
            }
        }
    }

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int){
        val intent = Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(intent, requestCode)
        notifyItemChanged(position)
    }

    fun removeAt(position: Int){
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteHappyPlace(list[position])

        if (isDeleted > 0){
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface onClickListener{
        fun onClick(position: Int, model: HappyPlaceModel){}
    }

    fun setOnClickListener(onClickListener: onClickListener){
        this.onclickListener = onClickListener
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}