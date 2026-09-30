# Qualcomm native-Vulkan hand depth workaround

Related: [hand report #1887](https://github.com/FCL-Team/FoldCraftLauncher/issues/1887).
The earlier externally enabled profiler crash is separate
([#1886](https://github.com/FCL-Team/FoldCraftLauncher/issues/1886)).

## Source integration

The off-by-default **Qualcomm Vulkan hand depth workaround** switch is in
the version/directory rendering settings. `vulkanHandDepthFix` is saved,
cloned and passed into launch options like the other version settings.

`DefaultLauncher` enables it only when explicitly opted in, the backend is
explicitly `vulkan`, the system driver is selected, Java is 25, and the
entry point is vanilla `net.minecraft.client.main.Main`. GL, custom drivers,
other Java versions and mod-loader entry points are skipped.

The [VulkanCompat](../../VulkanCompat/) source module builds a self-contained
Java-17-targeted startup transformer. Gradle packages it as an APK asset;
the launcher extracts it atomically to its private cache and adds its JVM
options automatically. No manually built/copied agent or Java 25 build
toolchain is required. Its ASM uses an isolated loader so another agent's
older ASM cannot parse the snapshot's Java 25 classes by mistake.

The transformer accepts only this inspected GameRenderer class SHA256:

```text
e31c63dfe463b191845c5403e62cd5462a875d9f7337eb1a5d07c27bfe26f376
```

It replaces exactly one `OptionalDouble.empty()` in `renderItemInHand`
with `OptionalDouble.of(0.0)`: hand depth attachment LOAD becomes CLEAR.
Existing separate clears, other passes and world depth behavior stay intact.
Changed classes/snapshots are skipped. Startup logs distinguish requested,
active, applied and skipped states; extraction failure leaves a normal launch.

The client is Mojang-signed. A class-only overlay was considered but rejected
because mixing an unsigned replacement into a signed package could fail.
Instrumentation instead preserves the original code source/signers. The
original JAR, signatures, driver properties and KGSL hooks are untouched.
Switching the setting off restores the normal launch on the next start.
Remove an old manually installed hand-agent argument when migrating to this
built-in option. No Minecraft classes, proprietary drivers or built JARs
are committed.

## Evidence and limitations

Verified environment: FCL 1.3.3.6; Lenovo TB323FU; Android 16; arm64-v8a;
Adreno 840; Minecraft 26.4 Snapshot 2; Java 25; native system Qualcomm Vulkan
1.4.295, build `9ba3e037dd, If4edbf9452`. KGSL hooks stayed active, so this
is not a clean-stock-kernel comparison.

The game separately clears D32_FLOAT depth to 0.0, emits broad barriers and
then uses legacy render-pass-2 with hand depth LOAD. The
[standalone probe](depth-probe/) reproduces rejected hand-like fragments
for that sequence, while attachment CLEAR passes. At 3040 x 1904, four
separate-clear + LOAD rounds had 632000 / 635648 / 628224 / 632000 incorrect
pixels out of 5788160; attachment-clear controls had zero incorrect pixels.
Keeping the separate clear before attachment CLEAR passed two further rounds.

Rejected pixels have depth **0.0** but retain world color. Clear-only readback
is correct. Stale accelerated-depth state/metadata is suspected, not proven
to be specifically LRZ. The probe passes with host llvmpipe and API/sync
validation. Kernel customization remains a limitation of the attribution.
The user confirmed the same hand attachment change fixes the near-block
glitch via the original temporary agent.

Source tests cover launch gates, atomic extraction/failure recovery, executed
synthetic bytecode/frames, unique-anchor checks, unsupported hashes and
private ASM/transformer loading. The new transformer was also verified
against the exact client with Java 25 class-file verification and was loaded
as a real startup agent with signed GameRenderer and a signed package sibling;
the signers remained intact. Other clears and original client SHA256 match.

The source-built ARM64 `com.tungsten.fcl.debug` APK was assembled in
[fork CI](https://github.com/zxcccssssssas-eng/FoldCraftLauncher/actions/runs/36747972892)
and installed alongside the original app. The UI toggle, persistence across
restart, automatic asset extraction (matching hash), and automatic injection
without manual Java arguments were verified. The game log confirms native
Qualcomm Vulkan and the bundled transformer's Applied message; the original
signed client JAR hash is unchanged. Seven focused tests pass. Two unrelated
control-layout golden tests fail identically on the unmodified baseline;
the full suite is not claimed to pass.

The first fresh-world test failed with the vendor profiler's duplicate-submit
path still active despite `debug.vulkan.profiler=false`: 1767 wrapper calls,
3534 core submissions, followed by device lost. Matching the original startup
renderer did not remove it. The HAL gate at `0x24ccf0` separately checks
`debug.graphics.gpu.profiler.perfetto`, then `ro.debuggable` or a positive
`prctl(PR_GET_DUMPABLE)` result. The test APK is debuggable; the original is not.

Temporarily disabling only that Perfetto profiling property and restarting
the test app produced 41982 core entries and 41982 successful returns, with
zero defective profiler-submit hits and no increase in its fault counter
(14). The user confirmed fresh-world entry, near-block hand rendering and
menu scrolling all work in this source-built APK under that condition. Native
Qualcomm Vulkan and the KGSL hooks stayed active. The property was restored
after saving/quitting; no global-property mutation is part of the source fix.
The hand patch does not fix the vendor profiler or validate gameplay with
that profiling route enabled. Additional device coverage and performance
remain untested. Historical
[#1498](https://github.com/FCL-Team/FoldCraftLauncher/issues/1498) and
[#1674](https://github.com/FCL-Team/FoldCraftLauncher/pull/1674) use other
backends and are not assumed to share this cause.

This source patch does not address #1886's externally enabled
`debug.vulkan.profiler=true` crash or change global profiling properties.
That separate profiler-off comparison had 4346 successful core submissions
without the defective profiler entry, with modules/hooks retained.

References: [depth clear](https://docs.vulkan.org/refpages/latest/refpages/source/vkCmdClearDepthStencilImage.html),
[attachment load/clear](https://docs.vulkan.org/refpages/latest/refpages/source/VkAttachmentDescription2.html).
