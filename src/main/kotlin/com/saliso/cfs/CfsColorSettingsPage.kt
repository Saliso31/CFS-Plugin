package com.saliso.cfs

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class CfsColorSettingsPage : ColorSettingsPage {
    override fun getDisplayName() = "CFS"
    override fun getIcon(): Icon = CfsFileType.icon
    override fun getHighlighter(): SyntaxHighlighter = CfsSyntaxHighlighter()
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = arrayOf(
        AttributesDescriptor("Comments//Line comment", CfsSyntaxHighlighter.LINE_COMMENT),
        AttributesDescriptor("Comments//Block comment", CfsSyntaxHighlighter.BLOCK_COMMENT),
        AttributesDescriptor("Comments//Doc comment", CfsSyntaxHighlighter.DOC_COMMENT),
        AttributesDescriptor("Literals//String and Char", CfsSyntaxHighlighter.STRING),
        AttributesDescriptor("Literals//Number", CfsSyntaxHighlighter.NUMBER),
        AttributesDescriptor("Literals//Boolean", CfsSyntaxHighlighter.BOOL),
        AttributesDescriptor("Keyword", CfsSyntaxHighlighter.KEYWORD),
        AttributesDescriptor("Type", CfsSyntaxHighlighter.TYPE),
        AttributesDescriptor("Function//Declaration", CfsSyntaxHighlighter.FN_NAME),
        AttributesDescriptor("Function//Call", CfsSyntaxHighlighter.CALL),
        AttributesDescriptor("Parameter//Normal", CfsSyntaxHighlighter.PARAM),
        AttributesDescriptor("Parameter//Reassigned", CfsSyntaxHighlighter.REASSIGNED_PARAM),
        AttributesDescriptor("Field//Access (name, offset, index)", CfsSyntaxHighlighter.FIELD_ACCESS),
        AttributesDescriptor("Variable declaration", CfsSyntaxHighlighter.DECL_NAME),
        AttributesDescriptor("Field//Declaration", CfsSyntaxHighlighter.FIELD_DECL),
        AttributesDescriptor("Identifier", CfsSyntaxHighlighter.IDENT),
        AttributesDescriptor("Operator", CfsSyntaxHighlighter.OPERATOR),
        AttributesDescriptor("Punctuation//Braces", CfsSyntaxHighlighter.BRACES),
        AttributesDescriptor("Punctuation//Brackets", CfsSyntaxHighlighter.BRACKETS),
        AttributesDescriptor("Punctuation//Parentheses", CfsSyntaxHighlighter.PARENS),
        AttributesDescriptor("Punctuation//Comma", CfsSyntaxHighlighter.COMMA),
        AttributesDescriptor("Punctuation//Semicolon", CfsSyntaxHighlighter.SEMICOLON),
        AttributesDescriptor("Punctuation//Dot", CfsSyntaxHighlighter.DOT),
    )

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey> =
        mutableMapOf(
            "type" to CfsSyntaxHighlighter.TYPE,
            "fn" to CfsSyntaxHighlighter.FN_NAME,
            "call" to CfsSyntaxHighlighter.CALL,
            "decl" to CfsSyntaxHighlighter.DECL_NAME,
            "param" to CfsSyntaxHighlighter.PARAM,
            "reassigned" to CfsSyntaxHighlighter.REASSIGNED_PARAM,
            "field" to CfsSyntaxHighlighter.FIELD_ACCESS,
            "fielddecl" to CfsSyntaxHighlighter.FIELD_DECL,
        )

    override fun getDemoText() = """
/// A point in 2D space
pub struct <type>Point</type> {
    <fielddecl>x</fielddecl>: <type>f64</type>,
    <fielddecl>y</fielddecl>: <type>f64</type>,
}

static assert sizeof(<type>Point</type>) == 16;

interface <type>Shape</type> {
    fn <fn>area</fn>(self) <type>f64</type>;
}

impl <type>Shape</type> for <type>Point</type> {
    fn <fn>area</fn>(self) <type>f64</type> { 0.0 }
}

pub fn <fn>sum</fn>(<param>p</param>: <type>Point</type>) <type>f64</type> {
    return <param>p</param>.<field>x</field> + <param>p</param>.@8 + <param>p</param>.#1;
}

pub fn <fn>largest</fn>(<param>items</param>: <type>Vec</type><<type>i32</type>>, <reassigned>limit</reassigned>: <type>i32</type>) <type>i32</type> {
    init <decl>best</decl>: <type>i32</type>;
    <reassigned>limit</reassigned> = <reassigned>limit</reassigned> - 1;
    let <decl>names</decl> = Vec::<<type>String</type>>::<call>new</call>();
    for <decl>item</decl> in <param>items</param> {
        if item > best { best = item; }
    }
    let <decl>wide</decl> = best as <type>u64</type>;
    let <decl>bits</decl> = best as! <type>f32</type>;
    assert best > 0;
    return best;
}
""".trimIndent()
}