package org.lwjgl.openal.jsound;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.spi.MixerProvider;

/**
 * MixerProvider 入口，把 javax.sound.sampled 的 AudioSystem / Clip /
 * SourceDataLine / TargetDataLine 路由到 OpenAL 后端，修复 Android JRE 缺失
 * libjsound.so 导致的模组音乐无法播放问题。
 *
 * <p>本 jar 属于游戏库列表，类与 services 声明（META-INF/services/
 * javax.sound.sampled.spi.MixerProvider）同源，JVM 的 ServiceLoader 可直接
 * 发现并加载，不受游戏类加载器（如 Fabric knot）的类路径隔离影响。
 */
public class JSoundProvider extends MixerProvider {

    @Override
    public Mixer.Info[] getMixerInfo() {
        return new Mixer.Info[]{JSoundMixer.INFO};
    }

    @Override
    public boolean isMixerSupported(Mixer.Info info) {
        return JSoundMixer.INFO.equals(info);
    }

    @Override
    public Mixer getMixer(Mixer.Info info) {
        if (info == null || JSoundMixer.INFO.equals(info)) {
            return new JSoundMixer();
        }
        throw new IllegalArgumentException("Mixer not supported: " + info);
    }
}