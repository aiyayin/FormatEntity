package com.yin.swaggerformat;

import com.intellij.openapi.ui.DialogWrapper;
import com.sun.istack.Nullable;

import javax.swing.*;

public class SampleDialogWrapper extends DialogWrapper {
    FormTestSwing formTestSwing = new FormTestSwing();
    public SampleDialogWrapper() {
        super(true); // use current window as parent
        setTitle("Swager Gson format");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return  formTestSwing.initCenter();
    }


    @Override
    protected @org.jetbrains.annotations.Nullable JComponent createNorthPanel() {
        return formTestSwing.initNorth();
    }

    @Override
    protected JComponent createSouthPanel() {
        return formTestSwing.initSouth();
    }
}