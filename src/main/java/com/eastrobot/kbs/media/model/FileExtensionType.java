package com.eastrobot.kbs.media.model;

/**
 * 文件类型
 *
 * @author Yogurt_lei
 * @date 2018-04-18 17:57
 */
public enum FileExtensionType {
    PCM("pcm", ".pcm"),
    JPG("jpg", ".jpg"),
    TXT("txt", ".txt"),
    AAC("aac", ".aac"),
    ZIP("zip", ".zip"),
    /**
     * parse result file extension
     */
    RS("rs", ".rs");

    String ext;
    String pExt;


    FileExtensionType(String ext, String pExt) {
        this.ext = ext;
        this.pExt = pExt;
    }

    /**
     * 获取文件类型后缀, e.g: jpg, png
     *
     * @return ext
     */
    public String ext() {
        return ext;
    }

    /**
     * 获取文件类型后缀(带有.(点)), e.g: .jpg, .png
     *
     * @return .ext
     */
    public String pExt() {
        return pExt;
    }
}
