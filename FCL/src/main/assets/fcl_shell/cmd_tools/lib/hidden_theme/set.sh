#!/system/bin/sh
${fclshell_load_lang} hidden_theme

. "${FCL_PATH_SHELL}/lib/launcher_theme/load.sh"
export FCL_CONF_THEME_THEME_COLOR="2136981471"
export FCL_CONF_THEME_THEME_COLOR2="-32897"
export FCL_CONF_THEME_ANIMATION_SPEED="8"
export FCL_CONF_THEME_CLOSE_SKIN_MODEL="true"
export FCL_CONF_THEME_MODIFIED="true"
. "${FCL_PATH_SHELL}/lib/launcher_theme/set.sh"
. "${FCL_PATH_SHELL}/lib/launcher_theme/unload.sh"

rm -rf "${FCL_PATH_INTERNAL}/files/background" >"/dev/null" 2>&1
rm -rf "${FCL_PATH_INTERNAL}/files/cursor.png" >"/dev/null" 2>&1
rm -rf "${FCL_PATH_INTERNAL}/files/menu_icon.png" >"/dev/null" 2>&1
rm -rf "${FCL_PATH_INTERNAL}/files/menu_icon.gif" >"/dev/null" 2>&1
mkdir -p "${FCL_PATH_INTERNAL}/files/background"
cp -f "${FCL_PATH_SHELL}/res/hidden_theme/background.png" "${FCL_PATH_INTERNAL}/files/background/lt.png"
cp -f "${FCL_PATH_SHELL}/res/hidden_theme/background.png" "${FCL_PATH_INTERNAL}/files/background/dk.png"
cp -f "${FCL_PATH_SHELL}/res/hidden_theme/cursor.png" "${FCL_PATH_INTERNAL}/files/cursor.png"
cp -f "${FCL_PATH_SHELL}/res/hidden_theme/menu_icon.gif" "${FCL_PATH_INTERNAL}/files/menu_icon.gif"

echo "${lang_hidden_theme_enabled}"