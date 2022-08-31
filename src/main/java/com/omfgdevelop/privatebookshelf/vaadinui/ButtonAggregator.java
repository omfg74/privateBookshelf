package com.omfgdevelop.privatebookshelf.vaadinui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import java.util.function.Supplier;


public class ButtonAggregator extends VerticalLayout {


    public ButtonAggregator addButton(String btnName, String attr, Supplier<StreamResource> supplier) {
        var button = new Anchor();
        button.setHref(supplier.get());

        if (attr != null) {
            button.getElement().setAttribute(attr, true);
        }
        var style = new Button(btnName);
        style.setIcon(new Icon(VaadinIcon.DOWNLOAD_ALT));
        button.add(style);
        add(button);
        return this;
    }
}
