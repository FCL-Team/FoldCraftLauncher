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

(默认 FCL 客户端公共目录)  
文件: `FCLauncher/src/main/java/com/tungsten/fclauncher/utils/FCLPath.java`  
位置: `STORAGE_DIR`

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

## 背景图

文件: `FCLLibrary/src/main/res/drawable/background_` + (`dark.jpg`|`light.jpg`)

## 游戏启动加载页面背景图

文件: `FCLLibrary/src/main/res/drawable/background_loading_` + (`dark.jpg`|`light.jpg`)

## 主页面皮肤

文件: `FCL/src/main/res/layout/ui_main.xml`

删掉 `android:visibility="gone"` 即可在主页面显示 FCL 的皮肤展示

在该文件中还可以给主页面加任意组件，如果需要加点击事件  
文件: `FCL/src/main/java/com/tungsten/fcl/ui/main/MainUI.java`

## 创建账户页面

(离线登录，离线账户用户名的规则)  
文件: `FCL/src/main/java/com/tungsten/fcl/ui/account/CreateAccountDialog.java`  
位置: `USERNAME_CHECKER_PATTERN`

默认的规则是，允许中文、英文、数字、下划线

(如果玩家输入的用户名规则，将会显示的警告内容)
文件: `FCL/src/main/res/values-zh/strings.xml`
位置: `account_methods_offline_name_invalid`

(离线登录，离线账户用户名上面的红字提示)

## 游戏界面右下角小字

文件: `FCL/src/main/res/layout/view_game_menu.xml`
位置: 开头往下翻一点

## 客户端自动解压释放

路径: `FCL/src/main/assets/.minecraft`

将客户端的 `.minecraft` 文件夹复制至此，注意不要删除 `version` 文件，每次更新客户端都应该将 `version` 文件的数字加一，这样下次玩家更新客户端apk后就会提示更新客户端文件了。

更新策略是，删除原有的所有文件，再释放安装包内文件。

为了避免**安装包太大**，建议打包的客户端删除 libraries 和 assets，否则过大的安装包，会导致储存空间占用翻倍、增加使用门槛，且让玩家失去下载安装包的耐心。
