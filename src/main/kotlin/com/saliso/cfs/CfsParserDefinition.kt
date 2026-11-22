package com.saliso.cfs

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.saliso.cfs.parser.CfsParser
import com.saliso.cfs.psi.CfsElementTypes

class CfsFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, CfsLanguage) {
    override fun getFileType(): FileType = CfsFileType
}

class CfsParserDefinition : ParserDefinition {
    private val file = IFileElementType(CfsLanguage)

    override fun createParser(project: Project?): PsiParser = CfsParser()
    override fun getCommentTokens(): TokenSet =
        TokenSet.create(CfsElementTypes.LINE_COMMENT, CfsElementTypes.BLOCK_COMMENT, CfsElementTypes.DOC_COMMENT)
    override fun getStringLiteralElements(): TokenSet = TokenSet.create(CfsElementTypes.STRING)
    override fun createElement(node: ASTNode): PsiElement = CfsElementTypes.Factory.createElement(node)
    override fun createLexer(project: Project?): Lexer = CfsLexerAdapter()
    override fun getFileNodeType(): IFileElementType = file
    override fun createFile(vp: FileViewProvider): PsiFile = CfsFile(vp)
}