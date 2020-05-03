package io.danito.tekken7.backend.utils;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class UtilsUI {
    public static VerticalLayout createVerticalWrapper(String id) {
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setId(id);
        wrapper.setMargin(false);
        wrapper.setPadding(false);
        wrapper.setSizeFull();
        return wrapper;
    }

    public static HorizontalLayout createHorizontalWrapper(String id) {
        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setId(id);
        wrapper.setMargin(false);
        wrapper.setPadding(false);
        //wrapper.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        wrapper.setWidthFull();
        return wrapper;
    }

}
