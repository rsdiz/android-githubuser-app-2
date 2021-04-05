package id.rosyid.exploregithubusers.ui.explore.users

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.rosyid.exploregithubusers.data.entities.UserResponse
import id.rosyid.exploregithubusers.databinding.RowItemUserBinding

class UsersAdapter(
    private val listener: UserItemListener
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {
    interface UserItemListener {
        fun onClickedUser(username: String)
    }

    private val items = ArrayList<UserResponse>()

    fun setItems(items: ArrayList<UserResponse>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding = RowItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class UsersViewHolder(
        private val binding: RowItemUserBinding,
        private val listener: UserItemListener
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var user: UserResponse

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(item: UserResponse) {
            this.user = item
            binding.run {
                tvUsername.text = user.username
                Glide.with(binding.root)
                    .load(Uri.parse(user.avatarUrl))
                    .circleCrop()
                    .into(binding.ivAvatarUser)
            }
        }

        override fun onClick(v: View?) {
            listener.onClickedUser(user.username)
        }
    }
}
