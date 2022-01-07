package com.yin.formatentity;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.kotlin.psi.KtClass;
import org.jetbrains.kotlin.psi.KtPsiFactory;
import org.jetbrains.kotlin.psi.KtPsiUtil;
import org.jetbrains.kotlin.resolve.jvm.KotlinJavaPsiFacade;

import java.util.List;

public class CreateKotlinClassHelper {
    private Editor editor;
    private PsiElement element;
    private PsiElementFactory factory;
    private int offset;
    private Project project;
    private PsiFile psiFile;
    private KtClass targetClass;

    public CreateKotlinClassHelper(Project project, AnActionEvent anActionEvent) {
        this.project = project;
        psiFile = anActionEvent.getData(LangDataKeys.PSI_FILE);
        editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        offset = editor.getCaretModel().getOffset();
        element = psiFile.findElementAt(offset);
        factory = JavaPsiFacade.getElementFactory(project);
        targetClass = PsiTreeUtil.getParentOfType(element, KtClass.class);
    }

    public boolean checkNull() {
        return psiFile == null || editor == null || project == null;
    }

    public void createClassAndField(String name, List<String> fieldList) {
        insertString(name);
        insertField(fieldList);
    }

    public void importClass() {
        PsiDocumentManager mPsiDocumentManager = PsiDocumentManager.getInstance(project);
        Document document = mPsiDocumentManager.getCachedDocument(psiFile);
        if (document != null) {
            mPsiDocumentManager.commitDocument(document);
        }
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        styleManager.optimizeImports(psiFile);
        styleManager.shortenClassReferences(targetClass);
    }

    public void insertField(List<String> fieldList) {
        Document document = editor.getDocument();
        int lineNumber = document.getLineNumber(offset) + 1;
        for (int i = 0; i < fieldList.size(); i++) {
            String fieldString = fieldList.get(i);
            if (i == fieldList.size() - 1) {
                fieldString = fieldString.substring(0, fieldString.length() - 2);
            }
            int nextLineStartOffset = document.getLineStartOffset(lineNumber + 1);
            document.insertString(nextLineStartOffset, "\t" + fieldString);
            lineNumber = lineNumber + 4;

        }
        importClass();
    }

    public void insertString(String s) {
        Document document = editor.getDocument();
        int lineNumber = document.getLineNumber(offset);

        int nextLineStartOffset = document.getLineStartOffset(lineNumber + 1);
        document.insertString(nextLineStartOffset, "\t" + s);

        importClass();
    }
}
