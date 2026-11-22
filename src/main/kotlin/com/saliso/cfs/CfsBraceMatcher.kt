package com.saliso.cfs

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.saliso.cfs.psi.CfsElementTypes

class CfsBraceMatcher : PairedBraceMatcher {
    override fun getPairs() = arrayOf(
        BracePair(CfsElementTypes.LBRACE, CfsElementTypes.RBRACE, true),
        BracePair(CfsElementTypes.LPAREN, CfsElementTypes.RPAREN, false),
        BracePair(CfsElementTypes.LBRACKET, CfsElementTypes.RBRACKET, false),
    )

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?) = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int) = openingBraceOffset
}