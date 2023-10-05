package com.spring.documentale.ui.view;


import static com.spring.documentale.constants.ElementsSize.DEFAULT_GRID_HEIGHT;
import static com.spring.documentale.util.ui.GridUtils.createMenuToggle;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.spring.documentale.config.security.SecurityService;
import com.spring.documentale.constants.Routes;
import com.spring.documentale.model.entity.Institution;
import com.spring.documentale.repository.InstitutionRepository;
import com.spring.documentale.ui.editor.InstitutionEditor;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.vaadin.klaudeta.PaginatedGrid;

@Route(value = Routes.INSTITUTIONS, layout = AdministratorView.class)
public class InstitutionsView extends VerticalLayout {

  final PaginatedGrid<Institution> grid;

  final TextField filterEmail;

  private final InstitutionRepository repo;

  private final Button addNewBtn;

  private final Button goToIndex;

  private final InstitutionEditor editor;

  private final SecurityService securityService;

  private final Map<Column<?>, String> toggleableColumns = new HashMap<>();


  public InstitutionsView(InstitutionRepository repo, InstitutionEditor editor,
      @Autowired SecurityService securityService) {
    this.securityService = securityService;
    this.repo = repo;
    this.editor = editor;
    this.grid = new PaginatedGrid<>(Institution.class);

    this.filterEmail = new TextField();
    this.addNewBtn = new Button("Add Institution", VaadinIcon.PLUS.create());
    addNewBtn.addThemeVariants
        (ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);

    Button toggleButton = new Button("Toggle theme variant", click -> {
      ThemeList themeList = UI.getCurrent().getElement().getThemeList();
      if (themeList.contains(Lumo.DARK)) {
        themeList.remove(Lumo.DARK);
      } else {
        themeList.add(Lumo.DARK);
      }
    });

    this.goToIndex = new Button("./");
    goToIndex.addClickListener(e ->
        goToIndex.getUI().ifPresent(ui ->
            ui.navigate(Routes.INDEX))
    );

    // build layout
    HorizontalLayout actions = new HorizontalLayout(filterEmail, addNewBtn, goToIndex);

    add(toggleButton, actions, grid, editor);

    setupGrid();

    filterEmail.setPlaceholder("Filter by name");

    // Hook logic to components
        /* Replace listing with filtered content when user
          changes filter*/
    filterEmail.setValueChangeMode(ValueChangeMode.EAGER);
    filterEmail.addValueChangeListener
        (e -> listInstitutions(e.getValue()));


        /* Instantiate and edit new
        Customer the new button is clicked
         */

    addNewButton(editor);

    // Listen changes made by the editor,
    // refresh data from backend
    editor.setChangeHandler(() -> {
      editor.setVisible(false);
      listInstitutions(filterEmail.getValue());
    });

    // Initialize listing
    listInstitutions(null);
  }


  private void addNewButton(InstitutionEditor editor) {
    addNewBtn.addClickListener(e -> editor.editInstitution(
        (new Institution(null, "", "", ""))));
  }

  private void setupGrid() {
    grid.setHeight(DEFAULT_GRID_HEIGHT);
    grid.setColumns("id", "name", "code", "additionalInfo");
    grid.getColumnByKey("id").setAutoWidth(true).setFlexGrow(0).setFrozen(true);
    grid.getColumnByKey("code").setAutoWidth(true).setFlexGrow(0);

    grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

    final String edit = "Edit";
    final Column<Institution> column = grid.addComponentColumn(t -> {
      Button editButton = new Button(edit);
      editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
      editButton.addClickListener(click -> editor.editInstitution(t));
      return editButton;
    }).setWidth("auto").setFlexGrow(0);
    column.setHeader(edit);

    grid.getColumns()
        .forEach(userColumn -> {
          final String key = userColumn.getKey();
          if (isNotBlank(key) && !key.equals(edit)) {
            toggleableColumns.put(userColumn, key);
          }
        });
    Column<Institution> settingColumn = grid.addColumn(box -> "").setWidth("auto").setFlexGrow(0);
    grid.getHeaderRows().get(0).getCell(settingColumn)
        .setComponent(createMenuToggle(toggleableColumns));

    // Sets the max number of items to be rendered on the grid for each page
    grid.setPageSize(10);
    // Sets how many pages should be visible on the pagination before and/or after the current selected page
    grid.setPaginatorSize(5);
  }

  void listInstitutions(String filterText) {
    if (StringUtils.isEmpty(filterText)) {
      grid.setItems(repo.findAll());
    } else {
      grid.setItems(repo.
          findByNameStartsWithIgnoreCase(filterText));
    }
  }
}
