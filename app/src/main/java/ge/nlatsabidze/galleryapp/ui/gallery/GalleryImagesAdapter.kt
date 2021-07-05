package ge.nlatsabidze.galleryapp.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ge.nlatsabidze.galleryapp.R

internal class GalleryImagesAdapter(private var itemsList: ArrayList<String>) : RecyclerView.Adapter<GalleryImagesAdapter.MyViewHolder>()  {

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var galleryItem: ImageView = view.findViewById(R.id.galleryImage)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_item, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        Glide.with(holder.galleryItem.context)
            .load(item)
            .into(holder.galleryItem)
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun updateStore(updatedItemList: ArrayList<String>) {
        this.itemsList = updatedItemList
        notifyDataSetChanged()
    }

    fun clearItems() {
        this.itemsList = ArrayList<String>()
        notifyDataSetChanged()
    }

    fun addImage(imageUrl: String) {
        this.itemsList.add(imageUrl)
        notifyItemInserted(itemsList.size-1)
    }
}
