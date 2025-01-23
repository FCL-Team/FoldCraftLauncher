#!/system/bin/sh
theme_file="${FCL_PATH_INTERNAL}/shared_prefs/theme.xml"

theme_begin="<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n<map>\n"
int_item="    <int name=\""
boolean_item="    <boolean name=\""
value_begin="\" value=\""
value_end="\" />\n"
theme_end="</map>\n"

echo -en "${theme_begin}" >"${theme_file}"

if [[ "${FCL_CONF_THEME_THEME_COLOR}" != "" ]]; then
  echo -en "${int_item}" >>"${theme_file}"
  echo -en "theme_color" >>"${theme_file}"
  echo -en "${value_begin}" >>"${theme_file}"
  echo -en "${FCL_CONF_THEME_THEME_COLOR}" >>"${theme_file}"
  echo -en "${value_end}" >>"${theme_file}"
fi

if [[ "${FCL_CONF_THEME_THEME_COLOR2}" != "" ]]; then
  echo -en "${int_item}" >>"${theme_file}"
  echo -en "theme_color2" >>"${theme_file}"
  echo -en "${value_begin}" >>"${theme_file}"
  echo -en "${FCL_CONF_THEME_THEME_COLOR2}" >>"${theme_file}"
  echo -en "${value_end}" >>"${theme_file}"
fi

if [[ "${FCL_CONF_THEME_ANIMATION_SPEED}" != "" ]]; then
  echo -en "${int_item}" >>"${theme_file}"
  echo -en "animation_speed" >>"${theme_file}"
  echo -en "${value_begin}" >>"${theme_file}"
  echo -en "${FCL_CONF_THEME_ANIMATION_SPEED}" >>"${theme_file}"
  echo -en "${value_end}" >>"${theme_file}"
fi

if [[ "${FCL_CONF_THEME_CLOSE_SKIN_MODEL}" != "" ]]; then
  echo -en "${boolean_item}" >>"${theme_file}"
  echo -en "close_skin_model" >>"${theme_file}"
  echo -en "${value_begin}" >>"${theme_file}"
  echo -en "${FCL_CONF_THEME_CLOSE_SKIN_MODEL}" >>"${theme_file}"
  echo -en "${value_end}" >>"${theme_file}"
fi

if [[ "${FCL_CONF_THEME_FULLSCREEN}" != "" ]]; then
  echo -en "${boolean_item}" >>"${theme_file}"
  echo -en "fullscreen" >>"${theme_file}"
  echo -en "${value_begin}" >>"${theme_file}"
  echo -en "${FCL_CONF_THEME_FULLSCREEN}" >>"${theme_file}"
  echo -en "${value_end}" >>"${theme_file}"
fi

if [[ "${FCL_CONF_THEME_MODIFIED}" != "" ]]; then
  echo -en "${boolean_item}" >>"${theme_file}"
  echo -en "modified" >>"${theme_file}"
  echo -en "${value_begin}" >>"${theme_file}"
  echo -en "${FCL_CONF_THEME_MODIFIED}" >>"${theme_file}"
  echo -en "${value_end}" >>"${theme_file}"
fi

echo -en "${theme_end}" >>"${theme_file}"

unset theme_file
unset theme_begin
unset int_item
unset boolean_item
unset value_begin
unset value_end
unset theme_end