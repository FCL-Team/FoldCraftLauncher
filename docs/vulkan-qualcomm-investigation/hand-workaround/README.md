# Opt-in hand depth-clear agent (Java 25)

This is the tested snapshot-specific workaround, not a default launcher
patch. Please read the [investigation and limitations](../README.md) first.

Requirements: JDK/runtime 25, the exact inspected Minecraft 26.4 Snapshot 2
client, and its JOML dependency for the host verification test. No Minecraft
classes or Java runtime are redistributed here.

From this directory:

```sh
mkdir -p build/classes
javac -Xlint:all -Werror -d build/classes HandDepthClear.java
jar --create --file build/fcl-vulkan-hand-depth.jar \
  --manifest MANIFEST.MF -C build/classes .
java -cp build/fcl-vulkan-hand-depth.jar fcl.compat.HandDepthClear \
  /path/to/client.jar /path/to/joml.jar
```

The self-test checks bytecode validity, exactly one attachment replacement,
preservation of existing depth clears and rejection of a changed class.

Save/close the game and back up launch settings. Copy the built JAR to a
location readable by the launcher, then add to the affected Java-25 profile:

```text
-javaagent:/absolute/readable/path/fcl-vulkan-hand-depth.jar
```

Keep native Vulkan/system-driver selection unchanged for the comparison.
At startup, confirm both the Qualcomm Vulkan backend and the agent's
`patched GameRenderer.renderItemInHand` log before checking the near-block
hand reproduction. `NOT APPLIED` means the workaround is not active.

Rollback: save/close the game, remove only the agent launch option and
relaunch. Do not overwrite newer settings with an older full backup.
Remove the option before changing to Java older than 25. The agent is not
automatically enabled and is not promised to work on other client classes.
