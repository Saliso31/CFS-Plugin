package com.saliso.cfs

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.saliso.cfs.psi.*

class CfsAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is CfsFnName -> mark(holder, element, CfsSyntaxHighlighter.FN_NAME)

            is CfsParamName -> {
                val fn = PsiTreeUtil.getParentOfType(element, CfsFnItem::class.java)
                val key = if (fn != null && isReassigned(fn, element.text))
                    CfsSyntaxHighlighter.REASSIGNED_PARAM else CfsSyntaxHighlighter.PARAM
                mark(holder, element, key)
            }

            is CfsFieldName -> mark(holder, element, CfsSyntaxHighlighter.FIELD_DECL)
            is CfsVarName -> mark(holder, element, CfsSyntaxHighlighter.DECL_NAME)

            is CfsTypeName -> mark(holder, element, CfsSyntaxHighlighter.TYPE)
            is CfsTypeSegment -> element.node.findChildByType(CfsElementTypes.IDENT)
                ?.let { mark(holder, it.psi, CfsSyntaxHighlighter.TYPE) }

            is CfsCallExpr -> calleeName(element)?.let { mark(holder, it, CfsSyntaxHighlighter.CALL) }

            // a.name (skipped when it is the callee of a call, which CALL already colours)
            is CfsFieldExpr -> if (element.parent !is CfsCallExpr)
                element.node.findChildByType(CfsElementTypes.IDENT)
                    ?.let { mark(holder, it.psi, CfsSyntaxHighlighter.FIELD_ACCESS) }

            // S { name: 1 }
            is CfsFieldInit -> element.node.findChildByType(CfsElementTypes.IDENT)
                ?.let { mark(holder, it.psi, CfsSyntaxHighlighter.FIELD_ACCESS) }

            // use of a parameter
            is CfsPathExpr -> {
                if (element.parent is CfsCallExpr) return
                val segments = PsiTreeUtil.getChildrenOfType(element, CfsPathSegment::class.java)
                if (segments?.size != 1) return
                val ident = segments[0].node.findChildByType(CfsElementTypes.IDENT) ?: return
                val fn = PsiTreeUtil.getParentOfType(element, CfsFnItem::class.java) ?: return
                val name = ident.text
                val isParam = PsiTreeUtil.findChildrenOfType(fn, CfsParamName::class.java).any { it.text == name }
                val shadowed = PsiTreeUtil.findChildrenOfType(fn, CfsVarName::class.java).any { it.text == name }
                if (!isParam || shadowed) return
                mark(
                    holder, ident.psi,
                    if (isReassigned(fn, name)) CfsSyntaxHighlighter.REASSIGNED_PARAM
                    else CfsSyntaxHighlighter.PARAM
                )
            }
        }
    }

    private fun isReassigned(fn: CfsFnItem, name: String) =
        PsiTreeUtil.findChildrenOfType(fn, CfsAssignExpr::class.java).any {
            val lhs = it.firstChild
            lhs is CfsPathExpr && lhs.text == name
        } || PsiTreeUtil.findChildrenOfType(fn, CfsInitStmt::class.java).any {
            val target = it.expr
            target is CfsPathExpr && target.text == name
        }

    private fun calleeName(call: CfsCallExpr): PsiElement? = when (val c = call.firstChild) {
        is CfsPathExpr -> c.pathSegmentList.lastOrNull()?.node?.findChildByType(CfsElementTypes.IDENT)?.psi
        is CfsFieldExpr -> c.node.findChildByType(CfsElementTypes.IDENT)?.psi
        else -> null
    }

    private fun mark(holder: AnnotationHolder, e: PsiElement, key: TextAttributesKey) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(e).textAttributes(key).create()
    }
}