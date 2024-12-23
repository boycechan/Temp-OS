package com.touchgfx.mvvm.base.config

abstract class FrameConfigModule : AppliesOptions {
    /**
     * 是否启用解析配置
     * @return 默认返回{@code true}
     */
    fun isManifestParsingEnabled(): Boolean = true
}