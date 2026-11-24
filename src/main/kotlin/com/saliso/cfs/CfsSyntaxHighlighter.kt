package com.saliso.cfs

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.saliso.cfs.psi.CfsElementTypes
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as D

class CfsSyntaxHighlighter : SyntaxHighlighterBase() {
    companion object {
        val LINE_COMMENT = createTextAttributesKey("CFS_LINE_COMMENT", D.LINE_COMMENT)
        val BLOCK_COMMENT = createTextAttributesKey("CFS_BLOCK_COMMENT", D.BLOCK_COMMENT)
        val DOC_COMMENT = createTextAttributesKey("CFS_DOC_COMMENT", D.DOC_COMMENT)
        val STRING = createTextAttributesKey("CFS_STRING", D.STRING)
        val NUMBER = createTextAttributesKey("CFS_NUMBER", D.NUMBER)
        val KEYWORD = createTextAttributesKey("CFS_KEYWORD", D.KEYWORD)
        val BOOL = createTextAttributesKey("CFS_BOOL", D.KEYWORD)
        val TYPE = createTextAttributesKey("CFS_TYPE", D.CLASS_NAME)
        val FN_NAME = createTextAttributesKey("CFS_FN_NAME", D.FUNCTION_DECLARATION)
        val CALL = createTextAttributesKey("CFS_CALL", D.FUNCTION_CALL)
        val DECL_NAME = createTextAttributesKey("CFS_DECL_NAME", D.LOCAL_VARIABLE)
        val IDENT = createTextAttributesKey("CFS_IDENT", D.IDENTIFIER)
        val OPERATOR = createTextAttributesKey("CFS_OPERATOR", D.OPERATION_SIGN)
        val BRACES = createTextAttributesKey("CFS_BRACES", D.BRACES)
        val BRACKETS = createTextAttributesKey("CFS_BRACKETS", D.BRACKETS)
        val PARENS = createTextAttributesKey("CFS_PARENS", D.PARENTHESES)
        val COMMA = createTextAttributesKey("CFS_COMMA", D.COMMA)
        val SEMICOLON = createTextAttributesKey("CFS_SEMICOLON", D.SEMICOLON)
        val DOT = createTextAttributesKey("CFS_DOT", D.DOT)
        val FIELD_ACCESS = createTextAttributesKey("CFS_FIELD_ACCESS", D.INSTANCE_FIELD)
        val IDENTIFIER  = createTextAttributesKey("CFS_IDENTIFIER ", D.IDENTIFIER)
        val PARAM = createTextAttributesKey("CFS_PARAM", D.PARAMETER)
        val REASSIGNED_PARAM = createTextAttributesKey("CFS_REASSIGNED_PARAM", D.REASSIGNED_PARAMETER)
        val FIELD_DECL = createTextAttributesKey("CFS_FIELD_DECL", D.INSTANCE_FIELD)
        val NAMESPACE = createTextAttributesKey("CFS_NAMESPACE", D.IDENTIFIER)
        val BAD = createTextAttributesKey("CFS_BAD", HighlighterColors.BAD_CHARACTER)

        private val ATTRS: Map<IElementType, Array<TextAttributesKey>> = buildMap {
            fun bind(key: TextAttributesKey, vararg types: IElementType) =
                types.forEach { put(it, arrayOf(key)) }

            bind(LINE_COMMENT, CfsElementTypes.LINE_COMMENT)
            bind(BLOCK_COMMENT, CfsElementTypes.BLOCK_COMMENT)
            bind(DOC_COMMENT, CfsElementTypes.DOC_COMMENT)
            bind(STRING, CfsElementTypes.STRING)
            bind(NUMBER, CfsElementTypes.NUMBER)

            bind(
                KEYWORD,
                CfsElementTypes.KW_BREAK, CfsElementTypes.KW_CONTINUE, CfsElementTypes.KW_ELSE,
                CfsElementTypes.KW_FALSE, CfsElementTypes.KW_FN, CfsElementTypes.KW_FOR,
                CfsElementTypes.KW_IF, CfsElementTypes.KW_IMPL, CfsElementTypes.KW_IN,
                CfsElementTypes.KW_INTERFACE, CfsElementTypes.KW_LET, CfsElementTypes.KW_LOOP,
                CfsElementTypes.KW_PUB, CfsElementTypes.KW_RETURN, CfsElementTypes.KW_SELF,
                CfsElementTypes.KW_STRUCT, CfsElementTypes.KW_TRUE, CfsElementTypes.KW_VAR,
                CfsElementTypes.KW_WHILE
            )

            bind(KEYWORD, CfsElementTypes.KW_AS, CfsElementTypes.KW_AS_BANG, CfsElementTypes.KW_ASSERT,
                CfsElementTypes.KW_STATIC, CfsElementTypes.KW_INIT)

            bind(KEYWORD, CfsElementTypes.KW_SIZEOF, CfsElementTypes.KW_TYPEOF)

            bind(FIELD_ACCESS, CfsElementTypes.FIELD_OFFSET, CfsElementTypes.FIELD_INDEX)

            bind(
                OPERATOR,
                CfsElementTypes.AMP, CfsElementTypes.ANDAND, CfsElementTypes.BANG,
                CfsElementTypes.EQ, CfsElementTypes.EQEQ, CfsElementTypes.GT,
                CfsElementTypes.LE, CfsElementTypes.LT, CfsElementTypes.MINUS,
                CfsElementTypes.NEQ, CfsElementTypes.OROR, CfsElementTypes.PERCENT,
                CfsElementTypes.PLUS, CfsElementTypes.SLASH, CfsElementTypes.STAR
            )

            bind(BRACES, CfsElementTypes.LBRACE, CfsElementTypes.RBRACE)
            bind(BRACKETS, CfsElementTypes.LBRACKET, CfsElementTypes.RBRACKET)
            bind(PARENS, CfsElementTypes.LPAREN, CfsElementTypes.RPAREN)
            bind(COMMA, CfsElementTypes.COMMA)
            bind(SEMICOLON, CfsElementTypes.SEMICOLON)
            bind(DOT, CfsElementTypes.DOT)

            bind(IDENTIFIER, CfsElementTypes.IDENT)
            bind(OPERATOR, CfsElementTypes.COLON, CfsElementTypes.COLONCOLON)

            bind(BAD, TokenType.BAD_CHARACTER)
        }
    }
    override fun getHighlightingLexer(): Lexer = CfsLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> =
        ATTRS[tokenType] ?: emptyArray()
}

class CfsSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, file: VirtualFile?): SyntaxHighlighter =
        CfsSyntaxHighlighter()
}