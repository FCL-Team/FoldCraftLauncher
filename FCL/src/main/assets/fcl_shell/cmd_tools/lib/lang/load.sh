#!/system/bin/sh
target="$1"
fallback="$2"
lang="${FCL_CONF_LANG}"
lang_res="${FCL_PATH_SHELL}/res/lang"

if [[ "${fallback}" == "" ]]; then
  fallback="en"
fi

if [[ $1 == "" ]]; then
  if [[ -f "${lang_res}/${lang}.sh" ]]; then
    . "${lang_res}/${lang}.sh"
  else
    . "${lang_res}/${fallback}.sh"
  fi
elif [[ -d "${lang_res}/${target}" ]]; then
  if [[ -f "${lang_res}/${target}/${lang}.sh" ]]; then
    . "${lang_res}/${target}/${lang}.sh"
  else
    . "${lang_res}/${target}/${fallback}.sh"
  fi
else
  echo "error: invalid argument"
fi

unset target
unset fallback
unset lang
unset lang_res