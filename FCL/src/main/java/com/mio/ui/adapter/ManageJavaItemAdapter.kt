package com.mio.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mio.ui.applySelectableItemStyle
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemManageJavaBinding
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.game.JavaVersion
import java.io.File

/**
 * 已安装 Java 列表适配器，条目外观与渲染器选择弹窗一致：
 * 卡片背景 + 当前选中的 Java 主题色高亮，右侧为删除按钮。
 */
class ManageJavaItemAdapter(
    val context: Context,
    val versions: List<JavaVersion>,
    val currentName: String?,
    val action: (JavaVersion, Boolean) -> Unit
) :
    RecyclerView.Adapter<ViewHolder>() {

    private val density = context.resources.displayMetrics.density

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_manage_java, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val binding = ItemManageJavaBinding.bind(holder.itemView)
        val data = versions[position]
        // 选中态统一样式：当前 Java 主题色底，其余普通卡片（无勾选图标，右侧为删除按钮）
        applySelectableItemStyle(context, binding.root, null, data.name == currentName, density)
        binding.javaName.text = data.name
        binding.javaVersion.text = data.versionName
        if (File(FCLPath.JAVA_PATH, data.name).resolve("version").exists()) {
            binding.delete.visibility = View.INVISIBLE
            binding.javaName.text = "${data.name} (${context.getString(R.string.internal)})"
        } else {
            binding.delete.visibility = View.VISIBLE
            binding.delete.setOnClickListener {
                action.invoke(data, true)
            }
        }
        binding.root.setOnClickListener {
            action.invoke(data, false)
        }
    }

    override fun getItemCount(): Int {
        return versions.size
    }
}