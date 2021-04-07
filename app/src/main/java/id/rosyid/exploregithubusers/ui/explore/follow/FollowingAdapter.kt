package id.rosyid.exploregithubusers.ui.explore.follow

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.rosyid.exploregithubusers.data.entities.FollowingResponse
import id.rosyid.exploregithubusers.databinding.RowItemUserBinding

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.UsersViewHolder>() {
    private var items: List<FollowingResponse> = listOf()

    fun setItems(items: ArrayList<FollowingResponse>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding = RowItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class UsersViewHolder(
        private val binding: RowItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var user: FollowingResponse

        fun bind(item: FollowingResponse) {
            this.user = item
            binding.run {
                tvUsername.text = user.username
                Glide.with(binding.root)
                    .load(Uri.parse(user.avatarUrl))
                    .circleCrop()
                    .into(binding.ivAvatarUser)
            }
        }
    }
}
