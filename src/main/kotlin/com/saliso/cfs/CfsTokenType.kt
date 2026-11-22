package com.saliso.cfs

import com.intellij.psi.tree.IElementType

class CfsTokenType(debugName: String) : IElementType(debugName, CfsLanguage)

object CfsTypes {
    @JvmField val COMMENT = CfsTokenType("COMMENT")
    @JvmField val STRING = CfsTokenType("STRING")
    @JvmField val NUMBER = CfsTokenType("NUMBER")
    @JvmField val KEYWORD = CfsTokenType("KEYWORD")
    @JvmField val IDENT = CfsTokenType("IDENT")
    @JvmField val OPERATOR = CfsTokenType("OPERATOR")
}