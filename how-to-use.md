# 修改手册

按下 `Ctrl+F` 搜索你想修改的内容。

正在施工中...

## 应用名称

文件: `FCL/src/main/res/values/strings.xml`  
位置: `app_name`

## 应用包名

文件: `FCL/build.gradle`  
位置: `applicationId`

## 应用版本号

(实际版本号)  
文件: `FCL/build.gradle`  
位置: `versionCode` 和 `versionName`

(显示版本号)  
文件: `FCL/src/main/res/values/strings.xml`  
位置: `app_version`

## 应用图标

文件: `FCL/src/main/res/mipmap-anydpi-v26/ic_launcher` + (`.png`|`_foreground.png`|`_round.png`)

## 默认主题颜色

文件: `FCL/src/main/res/values/colors.xml`  
位置: `default_theme_color` 和 `ui_bg_color`

## 删除多余的 Java

路径: `FCL/src/main/assets/app_runtime/java`  
不需要哪个就删哪个，只留自己客户端需要的那个即可
