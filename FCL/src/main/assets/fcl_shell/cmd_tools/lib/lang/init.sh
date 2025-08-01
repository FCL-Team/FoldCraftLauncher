#!/system/bin/sh
if [[ -f "${FCL_PATH_INTERNAL}/shared_prefs/launcher.xml" ]]; then
  fclLang="$(cat "${FCL_PATH_INTERNAL}/shared_prefs/launcher.xml" | grep "name=\"lang\"")"
fi
if [[ "${fclLang}" != "" ]]; then
  fclLang=${fclLang/#    <int name=\"lang\" value=\"/}
  fclLang=${fclLang/%\" \/>/}
  if [[ "${fclLang}" == "0" ]]; then
    FCL_CONF_LANG="$(getprop persist.sys.locale )"
    FCL_CONF_LANG="${FCL_CONF_LANG:0:2}"
  elif [[ "${fclLang}" == "1" ]]; then
    FCL_CONF_LANG="en"
  elif [[ "${fclLang}" == "2" ]]; then
    FCL_CONF_LANG="zh"
  elif [[ "${fclLang}" == "3" ]]; then
    FCL_CONF_LANG="ru"
  elif [[ "${fclLang}" == "4" ]]; then
    FCL_CONF_LANG="pt"
  elif [[ "${fclLang}" == "3" ]]; then
    FCL_CONF_LANG="fa"
  fi
else
  FCL_CONF_LANG="$(getprop persist.sys.locale )"
  FCL_CONF_LANG="${FCL_CONF_LANG:0:2}"
fi
export FCL_CONF_LANG="${FCL_CONF_LANG}"