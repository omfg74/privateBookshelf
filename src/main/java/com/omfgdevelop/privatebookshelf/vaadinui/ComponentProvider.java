package com.omfgdevelop.privatebookshelf.vaadinui;

import com.vaadin.flow.component.button.Button;
import org.springframework.stereotype.Service;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

import java.util.function.Function;


public class ComponentProvider {

    public static <T> ConfirmDialog getConfirmDialog(String text, Function<ConfirmDialog.ConfirmEvent, T> onConfirm, Function<ConfirmDialog.CancelEvent, T> onCancel) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Cancel");
        dialog.setText(text);

        dialog.setCancelable(true);
        dialog.addCancelListener(e -> {
            onCancel.apply(e);
            dialog.close();
        });


        dialog.setConfirmText("Confirm");
        dialog.addConfirmListener(e -> {
            onConfirm.apply(e);
            dialog.close();
        });

//        Button button = new Button("Open confirm dialog");
//        button.addClickListener(event -> {
//            dialog.open();
//
//        });
        return dialog;

    }

}
