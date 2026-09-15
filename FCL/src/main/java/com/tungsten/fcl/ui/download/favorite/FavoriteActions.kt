package com.tungsten.fcl.ui.download.favorite

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.mio.data.FavoriteManager
import com.mio.data.favorite.DownloadFavoriteEntity
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import kotlinx.coroutines.launch

/** 收藏星形图标：已收藏实心、未收藏描边 */
fun bindFavoriteIcon(imageView: ImageView, entityId: String) {
    imageView.setImageResource(
        if (FavoriteManager.isFavorited(entityId)) R.drawable.ic_star_filled else R.drawable.ic_star_outline
    )
}

/** 收藏按钮统一行为：已收藏 → 直接取消；未收藏 → 弹分组多选（可不选直接确认）后收藏。
 *  [onDone] 在收藏状态实际变更后回调（调用方借此刷新图标等 UI） */
@JvmOverloads
fun handleFavoriteClick(context: Context, entity: DownloadFavoriteEntity, onDone: (() -> Unit)? = null) {
    if (FavoriteManager.isFavorited(entity.id)) {
        MainActivity.getInstance().lifecycleScope.launch {
            FavoriteManager.toggle(entity)
            Toast.makeText(context, context.getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show()
            onDone?.invoke()
        }
    } else {
        GroupSelectionDialog(
            context,
            context.getString(R.string.favorite_group_select),
            emptySet()
        ) { groupIds ->
            MainActivity.getInstance().lifecycleScope.launch {
                FavoriteManager.toggle(entity, groupIds)
                Toast.makeText(context, context.getString(R.string.favorite_added), Toast.LENGTH_SHORT).show()
                onDone?.invoke()
            }
        }.show()
    }
}
