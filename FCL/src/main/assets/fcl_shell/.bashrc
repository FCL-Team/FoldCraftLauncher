#!/system/bin/sh
export FCL_PATH_SHELL="$(dirname "$(readlink -f "${0}")")/cmd_tools"
export FCL_PATH_INTERNAL="${HOME}"
chmod +x "${FCL_PATH_SHELL}/bin/"*
export PATH="${FCL_PATH_SHELL}/bin":${PATH}
. ${FCL_PATH_SHELL}/init.sh