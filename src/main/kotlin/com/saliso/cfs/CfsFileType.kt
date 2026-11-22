package com.saliso.cfs

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.icons.AllIcons
import javax.swing.Icon

object CfsFileType : LanguageFileType(CfsLanguage) {
    override fun getName(): String = "CFS File"
    override fun getDescription(): String = "CFS language file"
    override fun getDefaultExtension(): String = "cfs"
    override fun getIcon(): Icon = AllIcons.FileTypes.Text
}