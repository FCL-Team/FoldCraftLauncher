#!/bin/sh
# 从 Maven Central 拉取 JNA 官方 Android natives（jna-<版本>.aar，与 GitHub dist 同源的发布产物），
# 提取各架构 libjnidispatch.so 按架构存放进 assets。
set -e

MAVEN_URL="https://repo1.maven.org/maven2/net/java/dev/jna/jna"
VERSIONS="5.13.0 5.14.0 5.15.0 5.16.0 5.17.0 5.18.0"
ABIS="arm64-v8a armeabi-v7a x86 x86_64"
ASSETS_DIR="$(cd "$(dirname "$0")/.." && pwd)/FCL/src/main/assets/app_runtime/jna"

rm -rf "$ASSETS_DIR"
mkdir -p "$ASSETS_DIR"

for version in $VERSIONS; do
    echo "下载 jna-$version"
    tmp="$(mktemp -d)"
    curl -sfL --retry 5 --retry-all-errors -o "$tmp/jna.aar" "$MAVEN_URL/$version/jna-$version.aar"
    for abi in $ABIS; do
        mkdir -p "$ASSETS_DIR/$version/natives/$abi"
        unzip -p "$tmp/jna.aar" "jni/$abi/libjnidispatch.so" > "$ASSETS_DIR/$version/natives/$abi/libjnidispatch.so"
    done
    rm -rf "$tmp"
done

# version 文件存所有 so 内容哈希派生的十进制数：产物不变则值不变，产物变化自动更新，
# 供 RuntimeUtils#isLatest 判断是否需要重装
hash="$(find "$ASSETS_DIR" -name '*.so' | LC_ALL=C sort | xargs cat | shasum -a 256 | cut -c1-15)"
# 不带换行写入，version 文件内容必须是纯数字（RuntimeUtils#isLatest 直接 Long.parseLong）
printf '%s' "$((16#$hash))" > "$ASSETS_DIR/version"

echo "完成: $ASSETS_DIR"
