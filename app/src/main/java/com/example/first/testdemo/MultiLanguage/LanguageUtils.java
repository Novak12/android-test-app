package com.example.first.testdemo.MultiLanguage;

import java.util.Locale;

public class LanguageUtils {
    /**
     * 语言的缩写 如：zh_CN、zh_TW、en
     *
     * @param language
     * @return
     */
    public static Locale getLocal(String language) {
        switch (language) {
            case "zh_CN":
                //中文
                return Locale.CHINA;
            case "zh_TW":
                //繁体中文
                return Locale.TRADITIONAL_CHINESE;
            case "en":
                return Locale.ENGLISH;
            default:
                //默认中文
                return Locale.ENGLISH;
        }
    }
}
