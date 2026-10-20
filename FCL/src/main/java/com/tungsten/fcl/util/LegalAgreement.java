package com.tungsten.fcl.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.StringRes;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.util.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 用户协议与隐私政策：文档随安装包分发（assets/legal/&lt;语言目录&gt;/），同意状态按版本号记录。
 * <p>
 * 语言目录取自各语言 values-xx/legal.xml 中的 legal_document_dir，与界面语言走同一套资源匹配，
 * 未提供的语言自然回退到 values/legal.xml（英文）。legal.xml 不在 Weblate 的管理范围内
 * （Weblate 只管理 strings.xml），协议文本与相关文案由维护者直接维护。
 * <p>
 * 修改协议或政策的实质内容时，需同时递增 {@link #VERSION} 与各文档开头的版本号，
 * 所有用户会在下次启动时重新确认。
 */
public final class LegalAgreement {

    /** 当前协议版本，需与 assets/legal 下各文档开头标注的版本号一致 */
    public static final int VERSION = 1;

    private static final String PREFERENCES = "launcher";
    private static final String KEY_ACCEPTED_VERSION = "legal_agreement_version";
    private static final String ASSET_ROOT = "legal/";
    private static final String FALLBACK_DIR = "en";

    public enum Document {
        EULA("eula.txt", R.string.legal_eula),
        PRIVACY_POLICY("privacy.txt", R.string.legal_privacy);

        private final String fileName;
        @StringRes
        private final int titleRes;

        Document(String fileName, @StringRes int titleRes) {
            this.fileName = fileName;
            this.titleRes = titleRes;
        }

        @StringRes
        public int getTitleRes() {
            return titleRes;
        }
    }

    private LegalAgreement() {
    }

    /** 用户是否已同意当前版本的用户协议与隐私政策 */
    public static boolean isAccepted(Context context) {
        return preferences(context).getInt(KEY_ACCEPTED_VERSION, 0) >= VERSION;
    }

    public static void accept(Context context) {
        preferences(context).edit().putInt(KEY_ACCEPTED_VERSION, VERSION).apply();
    }

    /** 撤回同意：同步写入，调用方随后会直接退出进程 */
    public static void withdraw(Context context) {
        preferences(context).edit().remove(KEY_ACCEPTED_VERSION).commit();
    }

    /**
     * 读取当前界面语言的文档，该语言缺失时回退英文。读取 assets，需在后台线程调用；
     * context 须为已按启动器语言设置包装过的 Activity 上下文。
     */
    public static String load(Context context, Document document) throws IOException {
        String dir = context.getString(R.string.legal_document_dir);
        try {
            return read(context, dir, document);
        } catch (IOException e) {
            if (FALLBACK_DIR.equals(dir))
                throw e;
            return read(context, FALLBACK_DIR, document);
        }
    }

    private static String read(Context context, String dir, Document document) throws IOException {
        try (InputStream input = context.getAssets().open(ASSET_ROOT + dir + "/" + document.fileName)) {
            // Windows 检出的 CRLF 会原样打进安装包，统一换行符
            return IOUtils.readFullyAsString(input).replace("\r\n", "\n");
        }
    }

    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }
}
