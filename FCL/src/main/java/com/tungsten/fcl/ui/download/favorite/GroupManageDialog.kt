package com.tungsten.fcl.ui.download.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mio.data.FavoriteManager
import com.mio.data.favorite.FavoriteGroupEntity
import com.mio.ui.adapter.SpacingItemDecoration
import com.mio.ui.applySelectableItemStyle
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
 * 行样式与渲染器选择对话框统一（DialogCard 背景 + 行间 10dp 间距）。
 */
class GroupManageDialog(context: Context) : FCLDialog(context) {

    private val binding: DialogGroupManageBinding = DialogGroupManageBinding.inflate(layoutInflater)
    private val adapter = GroupManageAdapter(context)

    init {
        setCancelable(true)
        window?.setLayout(ConvertUtils.dip2px(context, 400f), ViewGroup.LayoutParams.WRAP_CONTENT)
        setContentView(binding.root)
        binding.negative.setOnClickListener { dismiss() }
        binding.btnNew.setOnClickListener { showNewGroupDialog() }
        binding.listContainer.layoutManager = LinearLayoutManager(context)
        binding.listContainer.addItemDecoration(SpacingItemDecoration(ConvertUtils.dip2px(context, 10f)))
        binding.listContainer.adapter = adapter
        refresh()
    }

    private fun refresh() {
        adapter.notifyDataSetChanged()
        binding.hint.visibility = if (FavoriteManager.groups.value.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showNewGroupDialog() {
        val dialog = EditDialog(context, "") { name ->
            MainActivity.getInstance().lifecycleScope.launch {
                FavoriteManager.createGroup(name)
                refresh()
            }
        }
        dialog.setTitle(context.getString(R.string.favorite_group_new))
        dialog.show()
    }

    private fun showRenameDialog(group: FavoriteGroupEntity) {
        val dialog = EditDialog(context, group.name) { newName ->
            MainActivity.getInstance().lifecycleScope.launch {
                if (FavoriteManager.renameGroup(group.groupId, newName)) {
                    refresh()
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
                    refresh()
                }
            }
            .setNegativeButton(null)
            .create()
            .show()
    }

    /** 分组管理行：名称 + 重命名/删除按钮，行样式经 applySelectableItemStyle 与其他选择对话框统一 */
    private inner class GroupManageAdapter(val context: Context) :
        RecyclerView.Adapter<GroupManageAdapter.Holder>() {

        inner class Holder(val binding: DialogGroupManageItemBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(DialogGroupManageItemBinding.inflate(LayoutInflater.from(context), parent, false))

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val group = FavoriteManager.groups.value[position]
            // 行样式与渲染器选择对话框统一：DialogCard 背景（管理列表无选中态）
            applySelectableItemStyle(context, holder.binding.root, null, false, context.resources.displayMetrics.density)
            holder.binding.name.text = group.name
            holder.binding.btnRename.setOnClickListener { showRenameDialog(group) }
            holder.binding.btnDelete.setOnClickListener { showDeleteConfirm(group) }
        }

        override fun getItemCount(): Int = FavoriteManager.groups.value.size
    }
}
