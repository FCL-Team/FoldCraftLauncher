# 修改手册

按下 `Ctrl+F` 搜索你想修改的内容。

正在施工中...

## 应用名称

文件: `FCL/src/main/res/values/strings.xml`  
位置: `app_name`

## 与 FCL 原版共存

现在已经是共存的了，避免与本仓库主的服务器客户端冲突，建议修改

(应用包名)  
文件: `FCL/build.gradle`  
位置: `applicationId`

(文件提供器)  
文件: `FCLLibrary/src/main/res/values/strings.xml`  
位置: `file_browser_provider`

## 应用版本号

(实际版本号)  
文件: `FCL/build.gradle`  
位置: `versionCode` 和 `versionName`

(显示版本号)  
文件: `FCL/src/main/res/values/strings.xml`  
位置: `app_version`

## 应用图标

文件: `FCL/src/main/res/drawable/ic_launcher` + (`.png`|`_round.png`)  
文件: `FCL/src/main/res/drawable/img_app.png`

## 首次启动的 EULA 页面

文件: `FCL/src/main/java/com/tungsten/fcl/fragment/EulaFragment.java`  
位置: `EULA_URL`

## 默认主题颜色

文件: `FCLLibrary/src/main/res/values/colors.xml`  
位置: `default_theme_color`

文件: `FCL/src/main/res/values/colors.xml`  
位置: `ui_bg_color`

## 删除多余的 Java

路径: `FCL/src/main/assets/app_runtime/java/`  
不需要哪个就删哪个，只留自己客户端需要的那个即可

## 默认 FCL 客户端公共目录

文件: `FCLauncher/src/main/java/com/tungsten/fclauncher/utils/FCLPath.java`  
位置: `SHARED_COMMON_DIR`

## 背景图

文件: `FCLLibrary/src/main/res/drawable/background_` + (`dark.jpg`|`light.jpg`)

## 加载页面背景图

文件: `FCLLibrary/src/main/res/drawable/background_loading_` + (`dark.jpg`|`light.jpg`)

## 游戏界面右下角小字

文件: `FCL/src/main/res/layout/view_game_menu.xml`
位置: 接近结尾部分
