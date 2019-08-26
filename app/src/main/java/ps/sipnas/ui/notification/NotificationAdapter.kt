package ps.sipnas.ui.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ps.sipnas.R
import ps.sipnas.data.model.DataNotification
import ps.sipnas.databinding.FragmentNotificationItemBinding

/**
 **********************************************
 * Created by ukie on 10/21/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationHolder>() {
    private lateinit var listNotif: List<DataNotification.Data>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder =
            NotificationHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fragment_notification_item, parent, false))

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) = holder.bind(listNotif[position], position)

    override fun getItemCount() = if (::listNotif.isInitialized) listNotif.size else 0

    fun updateAdapterNotification(listNofif: List<DataNotification.Data>) {
        this.listNotif = listNofif
        notifyDataSetChanged()
    }

    open inner class NotificationHolder(private val binding: FragmentNotificationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataNotification.Data, position: Int) = with(itemView) {
            binding.notif = data
            binding.tvNotifStatus.visibility = if (position == 0) View.VISIBLE else View.GONE
            setOnClickListener {
            }
        }
    }
}