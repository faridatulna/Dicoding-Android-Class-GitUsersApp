package gitusersapp.app.com.gitusersapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import gitusersapp.app.com.gitusersapp.DetailActivity
import gitusersapp.app.com.gitusersapp.R
import gitusersapp.app.com.gitusersapp.databinding.ItemRowUserBinding
import gitusersapp.app.com.gitusersapp.model.UserModel


class UserAdapter(private val data: List<UserModel>) : RecyclerView.Adapter<UserAdapter.CardViewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        //connecting to item layout
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return CardViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        //give data to ViewHolder that corresponding to its position
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //match the data with its component
        private val binding = ItemRowUserBinding.bind(itemView)
        fun bind(user: UserModel){
            Glide.with(itemView.context).load(user.avatar)
                .apply(RequestOptions().override(350, 350))
                .into(binding.userAvatar)
            binding.tvUsername.text = user.login

            //to detail
            itemView.setOnClickListener {
                val detailIntent = Intent(itemView.context, DetailActivity::class.java)
                detailIntent.putExtra("userlist", user)
                itemView.context.startActivity(detailIntent)
            }
        }
    }

}