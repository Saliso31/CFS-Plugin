package com.saliso.cfs

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager

class CfsEnterHandler : EnterHandlerDelegateAdapter() {
    override fun postProcessEnter(
        file: PsiFile,
        editor: Editor,
        dataContext: DataContext
    ): EnterHandlerDelegate.Result {
        if (file.language != CfsLanguage) return EnterHandlerDelegate.Result.Continue

        val doc = editor.document
        val text = doc.charsSequence
        var i = editor.caretModel.offset - 1
        while (i >= 0 && text[i].isWhitespace()) i--
        if (i < 0 || text[i] != '{') return EnterHandlerDelegate.Result.Continue

        val lineStart = doc.getLineStartOffset(doc.getLineNumber(i))
        if (text.subSequence(lineStart, i).isBlank()) return EnterHandlerDelegate.Result.Continue // already alone

        PsiDocumentManager.getInstance(file.project).commitDocument(doc)
        CodeStyleManager.getInstance(file.project).reformatText(file, lineStart, i + 1)
        return EnterHandlerDelegate.Result.Continue
    }
}