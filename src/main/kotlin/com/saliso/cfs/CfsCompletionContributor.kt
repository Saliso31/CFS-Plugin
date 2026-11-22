package com.saliso.cfs

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext

class CfsCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    KEYWORDS.forEach { result.addElement(LookupElementBuilder.create(it).bold()) }
                    listOf("sizeof", "typeof").forEach { kw ->
                        result.addElement(
                            LookupElementBuilder.create(kw).bold()
                                .withTailText("(type)", true)
                                .withInsertHandler { ctx, _ ->
                                    val doc = ctx.document
                                    val at = ctx.tailOffset
                                    if (at >= doc.textLength || doc.charsSequence[at] != '(') {
                                        doc.insertString(at, "()")
                                    }
                                    ctx.editor.caretModel.moveToOffset(at + 1)
                                }
                        )
                    }
                }
            }
        )
    }

    companion object {
        private val KEYWORDS = listOf(
            "pub", "fn", "let", "var", "struct", "impl", "interface", "for", "in",
            "if", "else", "while", "loop", "return", "break", "continue",
            "self", "true", "false", "as", "assert", "static", "init"
        )
    }
}