package com.saliso.cfs

import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import com.saliso.cfs.psi.CfsElementTypes.*
class CfsFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(context: FormattingContext): FormattingModel {
        val spacing = SpacingBuilder(context.codeStyleSettings, CfsLanguage)
            .before(BLOCK).spacing(0, 0, 1, false, 0)        // fn f() \n {
            .before(STRUCT_BODY).spacing(0, 0, 1, false, 0)
            .before(LBRACE).spacing(0, 0, 1, false, 0)       // interface/impl braces
            .before(KW_ELSE).spacing(0, 0, 1, false, 0)      // } \n else
            .after(LBRACE).spacing(0, 0, 1, false, 0)
            .before(RBRACE).spacing(0, 0, 1, false, 0)
        return FormattingModelProvider.createFormattingModelForPsiFile(
            context.containingFile,
            CfsBlock(context.node, spacing),
            context.codeStyleSettings
        )
    }
}

class CfsBlock(node: ASTNode, private val spacing: SpacingBuilder) :
    AbstractBlock(node, null, null) {

    override fun buildChildren(): List<Block> {
        val result = ArrayList<Block>()
        var child = myNode.firstChildNode
        while (child != null) {
            if (child.elementType != TokenType.WHITE_SPACE) result.add(CfsBlock(child, spacing))
            child = child.treeNext
        }
        return result
    }

    override fun getIndent(): Indent? {
        val type = myNode.elementType
        if (type == LBRACE || type == RBRACE) return Indent.getNoneIndent()
        val parent = myNode.treeParent?.elementType
        return when {
            parent == BLOCK || parent == STRUCT_BODY -> Indent.getNormalIndent()
            type == FN_ITEM && (parent == INTERFACE_ITEM || parent == IMPL_ITEM) -> Indent.getNormalIndent()
            else -> Indent.getNoneIndent()
        }
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val t = myNode.elementType
        val indent = if (t == BLOCK || t == STRUCT_BODY || t == INTERFACE_ITEM || t == IMPL_ITEM)
            Indent.getNormalIndent() else Indent.getNoneIndent()
        return ChildAttributes(indent, null)
    }

    override fun getSpacing(child1: Block?, child2: Block) = spacing.getSpacing(this, child1, child2)

    override fun isLeaf() = myNode.firstChildNode == null
}