package io.danito.tekken7.views.framedata;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import io.danito.tekken7.backend.*;
import io.danito.tekken7.backend.dao.AdditionalNotes;
import io.danito.tekken7.backend.dao.Move;
import io.danito.tekken7.backend.utils.UtilsUI;
import io.danito.tekken7.views.main.MainView;

import java.util.*;

@Route(value = "framedata", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Framedata")
@CssImport(value = "/styles/grid-style.css", themeFor = "vaadin-grid")
@CssImport("styles/views/framedata/framedata-view.css")
public class FramedataView extends Div {

    // Backend service access
    private BackendService backendService = BackendService.getInstance();

    // Layout components
    private Select<String> selectCharacter = new Select<>();
    private Select<String> selectYourCharacter = new Select<>();
    private Grid<Move> gridMoveList = new Grid<>(Move.class);
    private TextField textFilter = new TextField();
    private Checkbox checkboxAntiData = new Checkbox();
    private Label whereToSideStep = new Label();
    private List<Move> currentMoveList = new ArrayList<>();

    // Layout required properties
    List<String> characterNames = new ArrayList<>();
    private String mainCharacterName;

    public FramedataView() {
        setId("anticharacter-view");

        // Main layout that will contain the selection and the horizontal layout
        VerticalLayout verticalWrapper = UtilsUI.createVerticalWrapper("verticalWrapper");
        // Layout that will contain the grid
        HorizontalLayout horizontalWrapper = UtilsUI.createHorizontalWrapper("horizontalWrapper");

        // Initialize character names to fill select component
        characterNames = backendService.findCharacters();

        // Initialize main character if configured
        mainCharacterName = backendService.findMainCharacter();

        // Create your character select dropdown
        createSelectYourCharacter();

        // Create character select dropdown
        createSelectCharacter();

        createTextFieldFilter();

        // Create an anti information checkbox
        createCheckboxAntiInformation();

        // Create frame data grid
        createGridMoves();

        // Main layout that will contain the selection and the horizontal layout
        VerticalLayout verticalWrapper2 = UtilsUI.createVerticalWrapper("verticalWrapper2");
        verticalWrapper2.add(checkboxAntiData);
        verticalWrapper2.add(whereToSideStep);

        // Activated when anti information enabled
        whereToSideStep.setVisible(false);

        horizontalWrapper.add(selectCharacter, selectYourCharacter, textFilter, verticalWrapper2);

        // Add character select dropdown to vertical wrapper
        verticalWrapper.add(horizontalWrapper);

        // Add horizontal to main layout
        verticalWrapper.add(gridMoveList);

        // Set vertical layout to full size to occupy full  screen size
        verticalWrapper.setSizeFull();

        // Add main layout to page
        add(verticalWrapper);

        // Set page to full screen size
        setSizeFull();

    }

    /**
     * Create a character select option to view corresponding moves
     */
    private void createSelectCharacter() {
        selectCharacter.setLabel("Choose fighter");;
        selectCharacter.setId("characterSelect");
        selectCharacter.setItems(characterNames);
        selectCharacter.setEmptySelectionAllowed(false);
        selectCharacter.setPlaceholder("Choose fighter");
        selectCharacter.addValueChangeListener( e -> {
            currentMoveList = backendService.findMoves(selectCharacter.getValue(), selectYourCharacter.getValue());
            gridMoveList.setItems(currentMoveList);
            AdditionalNotes additionalNotes = backendService.findAdditionalNote(selectCharacter.getValue());
            whereToSideStep.setText(additionalNotes.getWeakSide());
        });
    }

    /**
     * Create a your character select option to view corresponding moves
     */
    private void createSelectYourCharacter() {
        selectYourCharacter.setLabel("Choose your fighter");;
        selectYourCharacter.setId("yourCharacterSelect");
        selectYourCharacter.setItems(characterNames);
        selectYourCharacter.setEmptySelectionAllowed(false);
        selectYourCharacter.setPlaceholder("Choose your fighter");
        selectYourCharacter.setReadOnly(true);
        if ( mainCharacterName != null)
            selectYourCharacter.setValue(mainCharacterName);
        selectYourCharacter.addValueChangeListener( e -> {
            currentMoveList = backendService.findMoves(selectCharacter.getValue(), selectYourCharacter.getValue());
            gridMoveList.setItems(currentMoveList);
            AdditionalNotes additionalNotes = backendService.findAdditionalNote(selectCharacter.getValue());
            whereToSideStep.setText(additionalNotes.getWeakSide());
        });
    }

    /**
     * Create a checkbox option to view anti information
     */
    private void createCheckboxAntiInformation() {
        checkboxAntiData.setLabel("Anti data");
        checkboxAntiData.setValue(false);
        checkboxAntiData.addValueChangeListener( e -> {
            changeLayout(checkboxAntiData.getValue());
        });

    }

    /**
     * Create a textField to filter information
     */
    private void createTextFieldFilter() {
        textFilter.setLabel("Filter:");
        textFilter.setValueChangeMode(ValueChangeMode.EAGER);
        textFilter.addValueChangeListener( e -> {
            filterMoves();
        });

    }

    /**
     * Create a grid for the selected character moves
     */
    private void createGridMoves() {

        Binder<Move> binder = new Binder<>(Move.class);
        gridMoveList.getEditor().setBinder(binder);

        // Set class name to styled so defined CSS properties apply
        gridMoveList.addClassName("styled");
        gridMoveList.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS,GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES);
        gridMoveList.setSizeFull();

        // Set columns to display from the select character move list
        gridMoveList.setColumns("move", "hitLevel", "damage", "startup", "onBlock", "onHit", "onCounterHit", "notes");

        TextField punishmentField = new TextField();

        // Close the editor in case of backward between components
        punishmentField.getElement()
                .addEventListener("keydown",
                        event -> gridMoveList.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");

        // If the main character is defined, add corresponding column for punishment
        //if (mainCharacterName != null) {
        gridMoveList.addColumn("punishment");
        //    gridMoveList.getColumnByKey("punishment").setHeader(mainCharacterName);
        gridMoveList.getColumnByKey("punishment").setEditorComponent(punishmentField);
        gridMoveList.getColumnByKey("punishment").setVisible(false);
        binder.forField(punishmentField).bind("punishment");
        //}

        // Go through all columns and remove sort and add auto width
        for (Grid.Column column : gridMoveList.getColumns()) {
            column.setSortable(false);
            column.setAutoWidth(true);
        }

        gridMoveList.addItemDoubleClickListener(event -> {
            gridMoveList.getEditor().editItem(event.getItem());
            punishmentField.focus();
        });

        gridMoveList.getEditor().addCloseListener(event -> {
            if (binder.getBean() != null)
                binder.getBean().setAdditionalNote(true);
            backendService.saveAntiCharacter(selectCharacter.getValue(), selectYourCharacter.getValue());
        });
    }

    private void  changeLayout(boolean antiInformation) {
        gridMoveList.getColumnByKey("damage").setVisible(!antiInformation);
        gridMoveList.getColumnByKey("startup").setVisible(!antiInformation);
        gridMoveList.getColumnByKey("onHit").setVisible(!antiInformation);
        gridMoveList.getColumnByKey("onCounterHit").setVisible(!antiInformation);
        gridMoveList.getColumnByKey("notes").setVisible(!antiInformation);
        gridMoveList.getColumnByKey("punishment").setVisible(antiInformation);
        selectYourCharacter.setReadOnly(!antiInformation);
        whereToSideStep.setVisible(checkboxAntiData.getValue());
    }

    private void filterMoves() {
        List<Move> moveList = new ArrayList<>();
        String moveToString;
        for (Move move : currentMoveList) {
            if (checkboxAntiData.getValue())
                moveToString = move.toString(true, false, false,
                        false, true, false, false,
                        false, true);
            else
                moveToString = move.toString(true, true, true,
                        true, true, true, true,
                        true, false);

            boolean passesFilter = (textFilter.getValue() == null || textFilter.getValue().isEmpty())
                    || moveToString.toLowerCase().contains(textFilter.getValue().toLowerCase());
            if (passesFilter) {
                moveList.add(moveList.size(), move);
            }
        }

        gridMoveList.setItems(moveList);
    }
}