package com.tungsten.fcl.ui.download.favorite

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.mio.data.FavoriteManager
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.DialogGroupSelectionBinding
import com.tungsten.fcllibrary.component.dialog.EditDialog
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.view.FCLCheckBox
import com.tungsten.fcllibrary.util.ConvertUtils
import kotlinx.coroutines.launch

/**
 * 分组多选对话框：收藏或修改收藏条目时勾选所属分组（可多选、可空），支持在对话框内直接新建分组。
 */
class GroupSelectionDialog(
    context: Context,
    title: String,
    checkedGroupIds: Set<String>,
    private val onConfirm: (List<String>) -> Unit,
) : FCLDialog(context) {

    private lateinit var binding: DialogGroupSelectionBinding
    private val checked = checkedGroupIds.toMutableSet()

    init {
        setCancelable(true)
        window?.setLayout(ConvertUtils.dip2px(context, 400f), ViewGroup.LayoutParams.WRAP_CONTENT)
        binding = DialogGroupSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.title.text = title
        binding.negative.setOnClickListener { dismiss() }
        binding.positive.setOnClickListener {
            val selected = FavoriteManager.groups.value
                .filter { it.groupId in checked }
                .map { it.groupId }
            dismiss()
            onConfirm(selected)
        }
        binding.btnNew.setOnClickListener { showNewGroupDialog() }
        rebuild()
    }

    private fun rebuild() {
        val groups = FavoriteManager.groups.value
        binding.listContainer.removeAllViews()
        for (group in groups) {
            val check = FCLCheckBox(context)
            check.text = group.name
            check.isChecked = group.groupId in checked
            check.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) checked.add(group.groupId) else checked.remove(group.groupId)
            }
            binding.listContainer.addView(check)
        }
        binding.hint.visibility = if (groups.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showNewGroupDialog() {
        val dialog = EditDialog(context, "") { name ->
            MainActivity.getInstance().lifecycleScope.launch {
                val group = FavoriteManager.createGroup(name)
                checked.add(group.groupId)
                rebuild()
            }
        }
        dialog.setTitle(context.getString(R.string.favorite_group_new))
        dialog.show()
    }
}
