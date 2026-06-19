package com.tungsten.fcl.ui.setting.compose

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.tungsten.fcl.R
import com.tungsten.fcllibrary.component.theme.ThemePreset

/**
 * 液态玻璃主题预览，严格遵循 Backdrop 官方教程实现。
 */
@Composable
fun LiquidGlassThemePreview(
    preset: ThemePreset,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    MaterialTheme(
        colorScheme = if (isDark) darkColorScheme() else lightColorScheme()
    ) {
        CompositionLocalProvider(
            LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Ltr
        ) {
            val backgroundColor = if (isDark) Color(0xFF1A1A2E) else Color(0xFFF0F4F8)
            val tintColor = Color(preset.getColor())

            BoxWithConstraints(
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(1.6f)
                    .background(backgroundColor)
            ) {
                val backdrop = rememberLayerBackdrop {
                    drawRect(backgroundColor)
                    drawContent()
                }

                // 背景内容，被 layerBackdrop 捕获到 backdrop 中
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .layerBackdrop(backdrop)
                        .padding(16.dp)
                ) {
                    BackgroundContent(tintColor = tintColor, isDark = isDark)
                }

                // 玻璃底部栏
                GlassBottomBar(
                    backdrop = backdrop,
                    tintColor = tintColor,
                    modifier = Modifier
                        .safeContentPadding()
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
private fun BackgroundContent(
    tintColor: Color,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            DecorativeCard(
                color = tintColor.copy(alpha = 0.25f),
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
            )
            DecorativeCard(
                color = if (isDark) Color(0xFF2A2A3E) else Color(0xFFE0E7EF),
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
            )
        }
        DecorativeCard(
            color = if (isDark) Color(0xFF252536) else Color(0xFFE8EEF5),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
private fun DecorativeCard(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color, RoundedCornerShape(16.dp))
    )
}

@Composable
private fun GlassBottomBar(
    backdrop: com.kyant.backdrop.Backdrop,
    tintColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .height(80.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 普通玻璃按钮：白色半透明表面层
        GlassButton(
            backdrop = backdrop,
            tintColor = null,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        )

        // 有色玻璃按钮：蓝色 Hue blend + 75% alpha 叠加
        GlassButton(
            backdrop = backdrop,
            tintColor = tintColor,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
        )
    }
}

@Composable
private fun GlassButton(
    backdrop: com.kyant.backdrop.Backdrop,
    tintColor: Color?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .drawBackdrop(
                backdrop = backdrop,
                shape = { CircleShape },
                effects = {
                    vibrancy()
                    blur(4f.dp.toPx())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        lens(16f.dp.toPx(), 32f.dp.toPx())
                    }
                },
                onDrawSurface = {
                    if (tintColor != null) {
                        drawRect(tintColor, blendMode = androidx.compose.ui.graphics.BlendMode.Hue)
                        drawRect(tintColor.copy(alpha = 0.75f))
                    } else {
                        drawRect(Color.White.copy(alpha = 0.5f))
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (tintColor != null) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_build_24),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = "FCL",
                color = Color(0xFF0E0E0E),
                fontSize = 16.sp
            )
        }
    }
}
