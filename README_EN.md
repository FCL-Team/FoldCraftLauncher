<div align="center">
    <img width="75" src="/FCL/src/main/res/drawable/img_app.png"></img>
</div>

<h1 align="center">Fold Craft Launcher</h1>

<div align="center">

[![Android CI](https://github.com/FCL-Team/FoldCraftLauncher/actions/workflows/build.yml/badge.svg)](https://github.com/FCL-Team/FoldCraftLauncher/actions/workflows/build.yml)
![Downloads](https://img.shields.io/github/downloads/FCL-Team/FoldCraftLauncher/total?style=flat-square&color=f18cb9)
![Release](https://img.shields.io/github/v/release/FCL-Team/FoldCraftLauncher?style=flat-square&color=f18cb9)

[![Discord](https://img.shields.io/badge/Discord-red?logo=discord&logoColor=white)](https://discord.gg/ffhvuXTwyV)
[![QQ](https://img.shields.io/badge/QQ-green)](http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=LwxydGEvBZJnn09sXOjkQo9tuuLcYwx5&authKey=seyY5pPUCIHMWS5FqVryq926T0G2GarSXetpxxV9DJxBVt%2FPcg1vxN%2F%2FXpsCowyk&noverify=0&group_code=762054349)
[![Sponsor](https://img.shields.io/badge/sponsor-blue?logo=GitHub-Sponsors)](https://afdian.com/@tungs)

</div>

🌍 **Language**  
[简体中文](./README.md) | English | [Русский язык](./README_RU.md)

> ✨ **Project Introduction**  
> 「Fold Craft Launcher」 is a Minecraft: Java Edition launcher for Android platforms developed by the FCL Team. Built upon the core functionalities of [HMCL](https://github.com/HMCL-dev/HMCL) and using the [Amethyst-Android](https://github.com/AngelAuraMC/Amethyst-Android) backend, it enables users to enjoy Java Edition MC on mobile devices, supporting mod loading and operation across all versions.

---

## 🚀 Core Features

✅ **Full Version Support**  
- Native support for all Minecraft versions (including latest snapshots)
- Mod loader support: Forge/NeoForge/LiteLoader/OptiFine/Fabric/Quilt/Cleanroom...

⚙️ **Key Highlights**  
- Built-in multi-version Java runtimes (Java 8/17/21/25) with custom Java import support
- Virtual mouse and customizable key mapping
- Shaders support (requires VirGL/Zink/MG renderers)
- Dynamic resource management (mods/modpacks/textures/shaders/saves)
- Personalized theme customization (background/color schemes)
- Supports [renderer pluginization](https://github.com/ShirosakiMio/FCLRendererPlugin)

---

## 🎮 Screenshots

<div align="center">
  <img src="/.github/images/ui_main_light.jpg" width="30%" alt="Light Theme">
  <img src="/.github/images/ui_main_dark.jpg" width="30%" alt="Dark Theme">
  <img src="/.github/images/game.jpg" width="30%" alt="Gameplay Preview">
</div>

---

## 📜 License

This project is licensed under **[GPL-3.0 License](https://www.gnu.org/licenses/gpl-3.0.html)**

---

## 🌍 Translation

We'd love your help with translations! The project's localization is managed on [Weblate](https://hosted.weblate.org/projects/foldcraftlauncher/) — no tools needed, just translate in your browser.

---

## 🤝 Contributions & Acknowledgements
### Contributors Wall
<a href="https://github.com/FCL-Team/FoldCraftLauncher/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=FCL-Team/FoldCraftLauncher" />
</a>

### Related Projects
- [HMCL](https://github.com/HMCL-dev/HMCL): source of core functionality (fclcore ported from `org.jackhuang.hmcl`)
- [Boat and related projects](https://github.com/AOF-Dev/Boat)
- [Amethyst-Android](https://github.com/AngelAuraMC/Amethyst-Android) (PojavLauncher's Android fork): JVM launching and rendering backend
- [authlib-injector](https://github.com/yushijinhun/authlib-injector)
- [EasyTier](https://github.com/EasyTier/EasyTier): underlying mesh networking for LAN play
- [Terracotta](https://github.com/burningtnt/Terracotta): LAN play solution based on EasyTier (Terracotta module JNI wrapper)
- [TouchController](https://github.com/TouchController/TouchController): touch controller dependency
- [NG-GL4ES](https://github.com/ShirosakiMio/NG-GL4ES): gl4es fork renderer (prebuilt aar shipped with FCL)
- [FCLRendererPlugin](https://github.com/ShirosakiMio/FCLRendererPlugin): renderer plugin extension
- [FCLDriverPlugin](https://github.com/FCL-Team/FCLDriverPlugin): driver (Turnip etc.) plugin extension

### Dependencies

- [Amethyst-Android](https://github.com/AngelAuraMC/Amethyst-Android) (PojavLauncher's Android fork): [GPL-3.0]
- Android Support
  Libraries: [Apache License 2.0](https://android.googlesource.com/platform/prebuilts/maven_repo/android/+/master/NOTICE.txt)
- [GL4ES](https://github.com/ptitSeb/gl4es): [MIT License](https://github.com/ptitSeb/gl4es/blob/master/LICENSE)
- [NG-GL4ES](https://github.com/ShirosakiMio/NG-GL4ES) (gl4es fork derived from Krypton Wrapper, used as a prebuilt aar)
- [ANGLE](https://chromium.googlesource.com/angle/angle): [BSD-3 License](https://chromium.googlesource.com/angle/angle/+/refs/heads/main/LICENSE)
- [OpenJDK](https://github.com/AngelAuraMC/openjdk-multiarch-jdk8u): [GNU GPLv2 License](https://openjdk.java.net/legal/gplv2+ce.html) (runtimes built and released by FCL-Team)
- [LWJGL3](https://github.com/LWJGL/lwjgl3) (official jars + Android source patches): [BSD-3 License](https://github.com/LWJGL/lwjgl3/blob/master/LICENSE.md)
- [LWJGLX](https://github.com/AngelAuraMC/lwjglx) (LWJGL2 API compatibility layer for LWJGL3):
  unknown license
- [Mesa 3D Graphics Library](https://gitlab.freedesktop.org/mesa/mesa): [MIT License](https://docs.mesa3d.org/license.html)
- [SPIRV-Cross](https://github.com/KhronosGroup/SPIRV-Cross) (SPIR-V reflection/translation, natives packaged as aar): [Apache License 2.0](https://github.com/KhronosGroup/SPIRV-Cross/blob/master/LICENSE)
- [bhook](https://github.com/bytedance/bhook) (Used for exit code
  trapping): [MIT license](https://github.com/bytedance/bhook/blob/main/LICENSE).
- [libepoxy](https://github.com/anholt/libepoxy): [MIT License](https://github.com/anholt/libepoxy/blob/master/COPYING).
- [virglrenderer](https://github.com/AngelAuraMC/virglrenderer): [MIT License](https://gitlab.freedesktop.org/virgl/virglrenderer/-/blob/master/COPYING).
- [OpenAL-Soft](https://github.com/kcat/openal-soft): [GNU LGPLv2.1](https://github.com/kcat/openal-soft/blob/master/COPYING)
    - [oboe](https://github.com/google/oboe): [Apache License 2.0](https://github.com/google/oboe/blob/main/LICENSE).
    - [pfffft](https://bitbucket.org/jpommier/pffft/src/master/): [ARR]
- [EasyTier](https://github.com/EasyTier/EasyTier) (embedded networking layer in the Terracotta module): [LGPL-3.0](https://github.com/EasyTier/EasyTier/blob/main/LICENSE)
- [Terracotta](https://github.com/burningtnt/Terracotta) (`net.burningtnt.terracotta` JNI wrapper): [AGPL-3.0](https://github.com/burningtnt/Terracotta/blob/main/LICENSE)
- [TouchController](https://github.com/TouchController/TouchController) (touch controller): [LGPL-3.0](https://github.com/TouchController/TouchController/blob/main/LICENSE)
- [discord-rpc](https://github.com/discord/discord-rpc) (libdiscord-rpc.so): [MIT License](https://github.com/discord/discord-rpc/blob/master/LICENSE)
- [control-converter](https://github.com/NingZeStudio/control-converter) (FCL→ZL2 control layout converter, Go implementation, packaged as `libcc.so`): [MIT License](https://opensource.org/licenses/MIT)
