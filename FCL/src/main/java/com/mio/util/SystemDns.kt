package com.mio.util

import android.net.ConnectivityManager
import com.tungsten.fcl.FCLApp

/**
 * 获取设备当前网络的真实 DNS 服务器
 */
fun getSystemDnsServerAddresses(): List<String>? {
    return runCatching {
        val connectivityManager = FCLApp.getAppContext()
            .getSystemService(ConnectivityManager::class.java) ?: return null
        val network = connectivityManager.activeNetwork ?: return null
        val linkProperties = connectivityManager.getLinkProperties(network) ?: return null

        linkProperties.dnsServers
            .mapNotNull { it.hostAddress?.takeIf(String::isNotEmpty) }
            .takeIf { it.isNotEmpty() }
    }.getOrNull()
}
