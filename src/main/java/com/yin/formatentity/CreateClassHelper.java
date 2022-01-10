package com.yin.formatentity;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.kotlin.psi.KtClass;

import java.io.File;
import java.util.List;

public class CreateClassHelper {
    private Editor editor;
    private PsiElementFactory factory;
    private int offset;
    private Project project;
    private PsiFile psiFile;
    private PsiClass targetClass;

    public CreateClassHelper(Project project, AnActionEvent anActionEvent) {
        this.project = project;
        psiFile = anActionEvent.getData(LangDataKeys.PSI_FILE);
        editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        offset = editor.getCaretModel().getOffset();
        PsiElement element = psiFile.findElementAt(offset);
        factory = JavaPsiFacade.getElementFactory(project);
        targetClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
    }

    public boolean checkNull() {
        return psiFile == null || editor == null || project == null;
    }

    public void createClassAndField(String className, String nameStr, List<String> fieldList) {
        PsiClass innerClass = null;
        if (targetClass == null) {
            PsiDirectory psiDirectory = psiFile.getContainingDirectory();
            File file = new File(psiDirectory.getVirtualFile().getCanonicalPath().concat("/")
                    .concat(className.trim().replace(".", "/")).concat(".java"));
            if (!file.exists()) {
                targetClass = JavaDirectoryService.getInstance().createClass(psiDirectory, className);
            }else {
                PsiFile psiFile1 = psiDirectory.findFile(className + ".java");
                if ((psiFile1 instanceof PsiJavaFile) && ((PsiJavaFile) psiFile1).getClasses().length > 0) {
                    targetClass = ((PsiJavaFile) psiFile1).getClasses()[0];
                }
            }
            FileEditorManager manager = FileEditorManager.getInstance(project);
            manager.openFile(targetClass.getContainingFile().getVirtualFile(), true, true);
            innerClass = targetClass;
        } else {
            PsiClass classFromText = factory.createClassFromText(nameStr, targetClass);
            PsiClass[] classFromTextInnerClasses = classFromText.getInnerClasses();
            if (classFromTextInnerClasses.length > 0) {
                innerClass = classFromTextInnerClasses[0];
            }
        }

        if (innerClass != null && fieldList.size() > 0)
            for (String fieldString : fieldList) {
                innerClass.add(factory.createFieldFromText(fieldString, innerClass));
            }
        if (innerClass != null && innerClass != targetClass) {
            targetClass.add(innerClass);
        }
        importClass();

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
        int lineNumber = document.getLineNumber(offset);
        for (String fieldString : fieldList) {
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
