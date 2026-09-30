# Qualcomm native Vulkan: a tested hand workaround and profiler findings

Thank you to the FCL maintainers for supporting native Vulkan and recent
Minecraft snapshots. These notes share a device-side investigation that may
be useful for similar reports. They do not establish a general FCL defect or
recommend enabling a workaround for every device.

Two independent problems were reproduced on one rooted tablet. The user
confirmed that both tested workarounds resolved their respective reproduction.
The hand workaround remains **opt-in, Java-25-only and snapshot-specific**.
Nothing here changes the launcher's default behavior or bundles a patched
Qualcomm driver, a Java runtime, or any Minecraft classes.

## Tested environment and limits

- Lenovo TB323FU, Android 16, arm64-v8a, Adreno 840.
- FCL 1.3.3.6 as reported by the tested installation; Minecraft **26.4 Snapshot 2**.
- Java 25; Minecraft native Vulkan backend, using the **system** driver.
- Qualcomm Vulkan 1.4.295, driver build `9ba3e037dd, If4edbf9452`.
- HAL build ID `161035f15a6861099941ffa455ead4b4`; SHA256
  `f787722f948d942bdb83053fec049cecc5907cb975d2acef1950258f3257f1fb`.
- Rooted, customized environment with KGSL low-latency hooks. Those hooks
  stayed loaded and unchanged during the reproducer/workaround comparison.
  This is **not** a clean-stock-kernel test and does not exclude every
  possible kernel/driver interaction.

The installed renderer plugin's presence should not be confused with the
active game backend: game logs and loaded libraries confirmed native system
Vulkan, not OpenGL, Zink, Turnip, or ANGLE. A separately installed Adreno
upgrade module was disabled and was not the active driver.

Related historical reports include [#1498](https://github.com/FCL-Team/FoldCraftLauncher/issues/1498)
and the ANGLE fix in [#1674](https://github.com/FCL-Team/FoldCraftLauncher/pull/1674).
Those involve different backends/devices; visual similarity is not proof
that they have the same cause as this native-Vulkan reproduction.

## 1. Hand corruption after a separate depth clear

### Symptoms and game-side sequence

When standing near a block, patches/stripes in the first-person hand were
missing and the world showed through. There was no new GPU fault associated
with this rendering issue.

Inspection of the exact client bytecode and short driver-entry traces showed:

1. `GameRenderer.render3dHud` selects HUD/main depth and clears it to 0.0.
2. The clear reaches Qualcomm's `vkCmdClearDepthStencilImage` and its internal
   clear helper: GENERAL layout, depth aspect, mip/layer 0, one level/layer.
3. The encoder emits a broad ALL_COMMANDS, MEMORY_READ|MEMORY_WRITE barrier.
4. `renderItemInHand` begins a legacy render-pass-2 pass with depth **LOAD**
   (`OptionalDouble.empty()`), then draws the hand.

Main-target formats are RGBA8_UNORM and D32_FLOAT. The viewport depth range
was 0.0 to 1.0. The captured path did not use dynamic rendering.

### Standalone pixel-readback reproduction

Source is in [depth-probe](depth-probe/). It renders red world-like depth
(approximately 0.65–0.75), then a green hand-like full-screen triangle at
depth 0.5 with depth test/write enabled and comparison GREATER. It uses
GENERAL layouts, legacy render-pass-2 and the game's broad barrier pattern.

Each result checks every color and depth pixel after a completed fence wait
and coherent CPU readback. Remaining profiler-wrapped device commands are
resolved directly to existing Qualcomm core exports within the probe; the
installed driver is never edited. This isolates the already-known profiler
double-submit from this test, but does not remove global kernel hooks.

| Sequence | Expected result | Tested Qualcomm result |
| --- | --- | --- |
| Separate depth clear 0.0, hand pass LOAD | Green; depth 0.5 | Some hand fragments incorrectly rejected |
| Hand attachment CLEAR 0.0 | Green; depth 0.5 | Every pixel correct |
| No clear, hand pass LOAD | Red; world depth retained | Every pixel correct (negative control) |
| Separate clear, immediate readback without hand | Red; depth 0.0 | Every pixel correct |
| Separate clear, then hand attachment CLEAR 0.0 | Green; depth 0.5 | Every pixel correct |

At 512 x 256, a direct-core four-round run had 6,528 / 7,936 / 9,600 / 8,960
incorrect hand pixels out of 131,072. At 3040 x 1904, a four-round run had
632,000 / 635,648 / 628,224 / 632,000 incorrect pixels out of 5,788,160.
Other modes in those runs had zero incorrect color/depth pixels.
The fifth mode, added to match the actual patch's retained separate clear,
had zero incorrect pixels in two additional full-resolution rounds.

Crucially, rejected hand pixels still contained depth **0.0**, while color
remained red. A clear-only readback also returned 0.0 at every pixel.
This suggests stale accelerated-depth state/metadata or a driver transition
problem, rather than an absent API clear. **LRZ involvement is a hypothesis;
the exact internal faulty instruction/state has not been proven.**

The same source/shaders passed on host Mesa llvmpipe with Khronos API and
synchronization validation enabled, without validation errors. This checks
the reproducer, not every Minecraft call on Android. A run under a different
UID/GID also failed, avoiding the FCL-specific priority boost; global hooks
remained active, so that is not a stock-kernel comparison.

### Tested, reversible hand workaround

[HandDepthClear.java](hand-workaround/HandDepthClear.java) is a small startup
agent. It changes exactly one call in `GameRenderer.renderItemInHand`:

```java
// Original depth attachment: preserve/load the separately cleared image
OptionalDouble.empty()

// Workaround: clear depth when the hand render pass begins
OptionalDouble.of(0.0)
```

The original separate clear remains for other HUD/screen-effect paths. The
agent changes no hand geometry, world depth testing, shaders, submissions,
driver binaries, system properties, or KGSL hooks. It transforms the class
in memory; the original client JAR is untouched.

Safety boundaries:

- Requires Java 25 and the exact inspected GameRenderer class SHA256
  `e31c63dfe463b191845c5403e62cd5462a875d9f7337eb1a5d07c27bfe26f376`.
- Matches the exact method descriptor and requires exactly one replacement.
- Uses resource-parsing class hierarchy resolution through the game's loader.
- Verifies the transformed bytecode; changed classes/snapshots/mods are
  rejected with a `NOT APPLIED` warning.
- Does not silently enable itself; a user must explicitly add the agent option.
- Remove it before switching to an older Java runtime. The guard is not a
  promise of compatibility with future snapshots or other launchers.

Build/test instructions are in [hand-workaround](hand-workaround/README.md).
After applying it, startup logs confirmed Qualcomm system Vulkan and:

```text
[HandDepthClear] patched GameRenderer.renderItemInHand: depth LOAD -> CLEAR(0.0)
```

The user repeated the near-block reproduction and confirmed: "this fix works."
The system driver and KGSL hook policy were unchanged. No new GPU faults
were observed during these tests. Broader stability/performance is untested.

## 2. Earlier scrolling crash with externally enabled Qualcomm profiling

This was a separate device-loss problem, resolved before the hand test.
A third-party module enabled `debug.vulkan.profiler=true`; we did **not**
establish that FCL itself enables this property.

The driver-provided submit pointer reached Qualcomm's
`libVkLayer_ADRENO_qprofiler.so`, build ID
`fa3193d9b9ffebfc89b12fe99b7ef6e1`. Disassembly and live entry/return probes
showed a profiler replacement calling the underlying driver submission,
then calling a WithDcvs/WithoutDcvs submission path again.

In the inspected profiler binary:

- `0x1cdd60` calls the underlying submit function (`blr x28`).
- The first result is not returned/checked before the later path.
- `0x1cdd8c` or `0x1cdd94` calls the next submission helper.
- Live core-call return addresses in the profiler were `0x1cdd64` and
  `0x1ce52c` within one profiler call.

A follow-up capture found **3,337 complete pairs** matching submit counts,
first command-buffer handles from the first two batches, and captured
signal semaphore/value. It was not a byte-for-byte capture of all submit
structures. The game allocates those command buffers with ONE_TIME_SUBMIT.
This is concrete duplicate-submission evidence, not simply two unrelated
profiling batches.

The scrolling crash coincided with HFI error 609 (`GMU_CP_GPC_ERROR`), KGSL
GPU_COMMAND returning EPROTO, subsequent recovery errors, driver VkResult -4,
then the game's five-second semaphore timeout.

### Tested crash workaround and persistence

After saving/closing the game, profiling was disabled with the rooted-device
property `debug.vulkan.profiler=false`, and Minecraft was restarted.
The offending `debug.vulkan.profiler=true` line was also removed from the
module's `system.prop`, with a backup, to avoid that module setting it again.
Only that property line was removed; **no module or KGSL hooks were removed**.
A reboot persistence test was not part of the original comparison.

A live game capture afterward had **4,346 core submit entries and 4,346
successful returns**, with no hits on the defective profiler entry.
The fault counter stayed unchanged and the user confirmed menu scrolling
worked. A standalone direct-HAL resolver could still return profiler wrapper
addresses even with the property false, so pointer lookup alone must not be
used to judge the running game's path.

If investigating a similar case, please check the actual driver/profiler
path first and save the world before any restart. Do not change global
profiling settings during an intended profiling session without considering
other apps, and do not remove unrelated module settings/hooks.

## What would be useful to review

We would appreciate feedback on whether these notes/reproducers belong here,
or whether part should be reported to Mojang/Qualcomm instead. An eventual
launcher-side workaround should be explicitly gated and tested on additional
devices; this contribution deliberately changes no production defaults.
An in-render-pass hand depth clear is the tested direction, not a claim to
repair the proprietary driver binary.

The included sources are diagnostics/workaround code only. No proprietary
driver binaries, Minecraft class files, account details, or raw personal logs
are included.

Specification references:

- [vkCmdClearDepthStencilImage](https://docs.vulkan.org/refpages/latest/refpages/source/vkCmdClearDepthStencilImage.html)
- [VkAttachmentDescription2](https://docs.vulkan.org/refpages/latest/refpages/source/VkAttachmentDescription2.html)
- [vkQueueSubmit2](https://docs.vulkan.org/refpages/latest/refpages/source/vkQueueSubmit2.html)
