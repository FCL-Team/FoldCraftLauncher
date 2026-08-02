# observable 库冒烟测试

`SmokeTest.java` 是 `com.tungsten.fclcore.observable`（fakefx 移除阶段 2a 产物）的语义冒烟测试，
68 条断言覆盖触发时机、bind/双向绑定防回环、extractor 冒泡、Change 事件形态、弱监听 GC 等。
工程无测试源集与 junit 依赖，故以普通 Java main 形式存放（不参与 gradle 编译）。

复跑方式（Git Bash，需 JDK）：

```bash
cd E:/project/FoldCraftLauncher
mkdir -p /tmp/obs-classes /tmp/obs-test
javac -encoding UTF-8 -d /tmp/obs-classes $(find FCLCore/src/main/java/com/tungsten/fclcore/observable -name "*.java")
javac -encoding UTF-8 -cp "$(cygpath -w /tmp/obs-classes)" -d /tmp/obs-test docs/migration/observable-smoke/SmokeTest.java
java -cp "$(cygpath -w /tmp/obs-classes);$(cygpath -w /tmp/obs-test)" SmokeTest
```

期望输出结尾：`== 68 passed, 0 failed ==`（2026-08-02 实测通过）。
