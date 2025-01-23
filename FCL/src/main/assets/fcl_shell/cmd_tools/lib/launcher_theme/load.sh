#!/system/bin/sh
if [[ -f "${FCL_PATH_INTERNAL}/shared_prefs/theme.xml" ]]; then
  theme_data="$(cat "${FCL_PATH_INTERNAL}/shared_prefs/theme.xml")"
fi

theme_color="$(echo -n "${theme_data}" | grep "<int name=\"theme_color\"")"
if [[ "${theme_color}" != "" ]]; then
  theme_color=${theme_color/#    <int name=\"theme_color\" value=\"/}
  theme_color=${theme_color/%\" \/>/}
else
  theme_color=""
fi

theme_color2="$(echo -n "${theme_data}" | grep "<int name=\"theme_color2\"")"
if [[ "${theme_color2}" != "" ]]; then
  theme_color2=${theme_color2/#    <int name=\"theme_color2\" value=\"/}
  theme_color2=${theme_color2/%\" \/>/}
else
  theme_color2=""
fi

animation_speed="$(echo -n "${theme_data}" | grep "<int name=\"animation_speed\"")"
if [[ "${animation_speed}" != "" ]]; then
  animation_speed=${animation_speed/#    <int name=\"animation_speed\" value=\"/}
  animation_speed=${animation_speed/%\" \/>/}
else
  animation_speed=""
fi

close_skin_model="$(echo -n "${theme_data}" | grep "<boolean name=\"close_skin_model\"")"
if [[ "${close_skin_model}" != "" ]]; then
  close_skin_model=${close_skin_model/#    <boolean name=\"close_skin_model\" value=\"/}
  close_skin_model=${close_skin_model/%\" \/>/}
else
  close_skin_model=""
fi

fullscreen="$(echo -n "${theme_data}" | grep "<boolean name=\"fullscreen\"")"
if [[ "${fullscreen}" != "" ]]; then
  fullscreen=${fullscreen/#    <boolean name=\"fullscreen\" value=\"/}
  fullscreen=${fullscreen/%\" \/>/}
else
  fullscreen=""
fi

modified="$(echo -n "${theme_data}" | grep "<boolean name=\"modified\"")"
if [[ "${modified}" != "" ]]; then
  modified=${modified/#    <boolean name=\"modified\" value=\"/}
  modified=${modified/%\" \/>/}
else
  modified=""
fi

export FCL_CONF_THEME_THEME_COLOR="${theme_color}"
export FCL_CONF_THEME_THEME_COLOR2="${theme_color2}"
export FCL_CONF_THEME_ANIMATION_SPEED="${animation_speed}"
export FCL_CONF_THEME_CLOSE_SKIN_MODEL="${close_skin_model}"
export FCL_CONF_THEME_FULLSCREEN="${fullscreen}"
export FCL_CONF_THEME_MODIFIED="${modified}"

unset theme_data
unset theme_color
unset theme_color2
unset animation_speed
unset close_skin_model
unset fullscreen
unset modified