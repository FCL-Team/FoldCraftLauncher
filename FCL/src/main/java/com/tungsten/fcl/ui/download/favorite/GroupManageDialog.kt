package com.tungsten.fcl.ui.download.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.mio.data.FavoriteManager
import com.mio.data.favorite.FavoriteGroupEntity
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.DialogGroupManageBinding
import com.tungsten.fcl.databinding.DialogGroupManageItemBinding
import com.tungsten.fcllibrary.component.dialog.EditDialog
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.util.ConvertUtils
import kotlinx.coroutines.launch

/**
 * 分组管理对话框：新建、重命名、删除分组；删除仅摘除收藏条目上的分组标记，不取消收藏。
 */
class GroupManageDialog(context: Context) : FCLDialog(context) {

    private lateinit var binding: DialogGroupManageBinding

    init {
        setCancelable(true)
        window?.setLayout(ConvertUtils.dip2px(context, 400f), ViewGroup.LayoutParams.WRAP_CONTENT)
        binding = DialogGroupManageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.negative.setOnClickListener { dismiss() }
        binding.btnNew.setOnClickListener { showNewGroupDialog() }
        rebuild()
    }

    private fun rebuild() {
        val groups = FavoriteManager.groups.value
        val inflater = LayoutInflater.from(context)
        binding.listContainer.removeAllViews()
        for (group in groups) {
            val item = DialogGroupManageItemBinding.inflate(inflater, binding.listContainer, false)
            item.name.text = group.name
            item.btnRename.setOnClickListener { showRenameDialog(group) }
            item.btnDelete.setOnClickListener { showDeleteConfirm(group) }
            binding.listContainer.addView(item.root)
        }
        binding.hint.visibility = if (groups.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showNewGroupDialog() {
        val dialog = EditDialog(context, "") { name ->
            MainActivity.getInstance().lifecycleScope.launch {
                FavoriteManager.createGroup(name)
                rebuild()
            }
        }
        dialog.setTitle(context.getString(R.string.favorite_group_new))
        dialog.show()
    }

    private fun showRenameDialog(group: FavoriteGroupEntity) {
        val dialog = EditDialog(context, group.name) { newName ->
            MainActivity.getInstance().lifecycleScope.launch {
                if (FavoriteManager.renameGroup(group.groupId, newName)) {
                    rebuild()
                } else {
                    Toast.makeText(context, R.string.favorite_group_exists, Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.setTitle(context.getString(R.string.favorite_group_rename))
        dialog.show()
    }

    private fun showDeleteConfirm(group: FavoriteGroupEntity) {
        FCLAlertDialog.Builder(context)
            .setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
            .setMessage(context.getString(R.string.favorite_group_delete_confirm, group.name))
            .setPositiveButton {
                MainActivity.getInstance().lifecycleScope.launch {
                    FavoriteManager.deleteGroup(group.groupId)
                    rebuild()
                }
            }
            .setNegativeButton(null)
            .create()
            .show()
    }
}
