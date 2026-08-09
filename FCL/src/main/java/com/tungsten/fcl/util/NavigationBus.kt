package com.tungsten.fcl.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** 跨页面导航事件总线，统一收口左侧菜单选中态。 */
object NavigationBus {
    enum class Menu {
        HOME, MANAGE, DOWNLOAD, CONTROLLER, MULTIPLAYER, SETTING
    }

    private val _selectedMenu = MutableStateFlow<Menu?>(null)
    val selectedMenu: StateFlow<Menu?> = _selectedMenu.asStateFlow()

    @JvmStatic
    fun select(menu: Menu) {
        _selectedMenu.value = menu
    }
}
