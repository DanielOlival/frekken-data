package io.danito.tekken7.views.configuration;

import com.vaadin.flow.component.select.Select;
import io.danito.tekken7.backend.BackendService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.danito.tekken7.views.main.MainView;

@Route(value = "configuration", layout = MainView.class)
@PageTitle("Configuration")
@CssImport("styles/views/configuration/configuration-view.css")
public class ConfigurationView extends Div {

    private BackendService backendService = BackendService.getInstance();
    private Select<String> selectCharacter = new Select<>();

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    public ConfigurationView() {
        setId("configuration-view");

        VerticalLayout wrapper = createWrapper();
        //createTitle(wrapper);
        //createFormLayout(wrapper);
        //createButtonLayout(wrapper);

        selectCharacter.setItems(backendService.findCharacters());
        selectCharacter.setEmptySelectionAllowed(false);
        selectCharacter.setPlaceholder("Choose fighter");
        selectCharacter.setLabel("Choose your main character");
        selectCharacter.setValue(backendService.findMainCharacter() );
        selectCharacter.addValueChangeListener(e -> {
            backendService.saveMainCharacter(selectCharacter.getValue());
                });
        wrapper.add(selectCharacter);

        add(wrapper);
    }

    private void createTitle(VerticalLayout wrapper) {
        H1 h1 = new H1("Configuration");
        wrapper.add(h1);
    }

    private VerticalLayout createWrapper() {
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setId("wrapper");
        wrapper.setSpacing(false);
        wrapper.setSizeFull();
        return wrapper;
    }

    private void createFormLayout(VerticalLayout wrapper) {
        FormLayout formLayout = new FormLayout();
        formLayout.setSizeFull();
        addFormItem(wrapper, formLayout, selectCharacter, "Main character?");
    }

    private void createButtonLayout(VerticalLayout wrapper) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        //buttonLayout.addClassName("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(cancel);
        buttonLayout.add(save);
        wrapper.add(buttonLayout);
    }

    private FormLayout.FormItem addFormItem(VerticalLayout wrapper,
            FormLayout formLayout, Component field, String fieldName) {
        FormLayout.FormItem formItem = formLayout.addFormItem(field, fieldName);
        wrapper.add(formLayout);
        //field.getElement().getClassList().add("full-width");
        return formItem;
    }

}
