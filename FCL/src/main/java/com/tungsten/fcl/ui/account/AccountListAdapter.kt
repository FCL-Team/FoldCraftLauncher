package com.tungsten.fcl.ui.account

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mio.util.copyToClipBoard
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemAccountBinding
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.ui.UIManager.Companion.instance
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccount
import com.tungsten.fclcore.auth.offline.OfflineAccount
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import java.util.Objects
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException

class AccountListAdapter(
    private val context: Context,
    private val list: MutableList<AccountListItem>
) : RecyclerView.Adapter<AccountListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_account, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = list[position]
        val binding = ItemAccountBinding.bind(holder.itemView)
        binding.radio.isChecked = item.account == Accounts.getSelectedAccount()
        binding.avatar.imageProperty().unbind()
        binding.avatar.imageProperty().bind(item.imageProperty())
        binding.name.stringProperty().unbind()
        binding.name.stringProperty().bind(item.titleProperty())
        binding.name.setSelected(true)
        binding.type.stringProperty().unbind()
        binding.type.stringProperty().bind(item.subtitleProperty())
        binding.type.setSelected(true)
        binding.skin.setVisibility(
            if (item.canUploadSkin().get()) View.VISIBLE else View.INVISIBLE
        )
        binding.radio.setOnClickListener {
            Accounts.setSelectedAccount(item.account)
            instance.accountUI.refresh().start()
        }
        binding.refresh.setOnClickListener {
            binding.refresh.setVisibility(View.INVISIBLE)
            binding.refreshProgress.visibility = View.VISIBLE
            item.refreshAsync()
                .whenComplete(Schedulers.androidUIThread()) { ex: Exception? ->
                    binding.refresh.setVisibility(View.VISIBLE)
                    binding.refreshProgress.visibility = View.INVISIBLE
                    if (ex != null) {
                        val builder1 = FCLAlertDialog.Builder(context)
                        builder1.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                        builder1.setMessage(Accounts.localizeErrorMessage(context, ex))
                        builder1.setNegativeButton(
                            context.getString(com.tungsten.fcllibrary.R.string.dialog_positive),
                            null
                        )
                        builder1.create().show()
                    }
                    item.refreshSkinBinding()
                    instance.accountUI.refresh().start()
                }.start()
        }
        binding.skin.setOnClickListener {
            try {
                if (item.account is AuthlibInjectorAccount) {
                    Thread {
                        try {
                            val uploadTask =
                                Objects.requireNonNull<CompletableFuture<Task<*>?>>(item.uploadSkin())
                                    .get()
                            Schedulers.androidUIThread().execute {
                                if (uploadTask != null) {
                                    binding.skin.setVisibility(View.INVISIBLE)
                                    binding.skinProgress.visibility = View.VISIBLE
                                    uploadTask
                                        .whenComplete(
                                            Schedulers.androidUIThread()
                                        ) {
                                            binding.skin.setVisibility(View.VISIBLE)
                                            binding.skinProgress.visibility = View.INVISIBLE
                                            item.refreshSkinBinding()
                                        }
                                        .start()
                                }
                            }
                        } catch (e: ExecutionException) {
                            e.printStackTrace()
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }.start()
                } else if (item.account is OfflineAccount) {
                    val dialog = OfflineAccountSkinDialog(context, item)
                    dialog.show()
                } else {
                    val uploadTask =
                        Objects.requireNonNull<CompletableFuture<Task<*>?>>(item.uploadSkin()).get()
                    if (uploadTask != null) {
                        binding.skin.setVisibility(View.INVISIBLE)
                        binding.skinProgress.visibility = View.VISIBLE
                        uploadTask
                            .whenComplete(
                                Schedulers.androidUIThread()
                            ) {
                                binding.skin.setVisibility(View.VISIBLE)
                                binding.skinProgress.visibility = View.INVISIBLE
                                item.refreshSkinBinding()
                            }
                            .start()
                    }
                }
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        binding.copyUuid.setOnClickListener {
            copyToClipBoard(context, item.account.uuid.toString())
            Toast.makeText(context, R.string.message_copy, Toast.LENGTH_SHORT).show()
        }
        binding.delete.setOnClickListener {
            val builder = FCLAlertDialog.Builder(context)
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
            builder.setMessage(
                String.format(
                    context.getString(R.string.version_manage_remove_confirm),
                    item.title
                )
            )
            builder.setPositiveButton {
                item.remove()
                instance.accountUI.refresh().start()
            }
            builder.setNegativeButton(null)
            builder.create().show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh(list: List<AccountListItem>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}