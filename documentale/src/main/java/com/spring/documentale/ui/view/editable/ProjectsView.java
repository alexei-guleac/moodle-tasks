package com.spring.documentale.ui.view.editable;


import static com.spring.documentale.constants.ElementsSize.DEFAULT_GRID_HEIGHT;
import static com.spring.documentale.util.ui.GridUtils.createMenuToggle;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.spring.documentale.config.security.SecurityService;
import com.spring.documentale.constants.Routes;
import com.spring.documentale.model.entity.Institution;
import com.spring.documentale.model.entity.Project;
import com.spring.documentale.model.entity.User;
import com.spring.documentale.repository.ProjectRepository;
import com.spring.documentale.ui.editor.ProjectsEditor;
import com.spring.documentale.ui.view.parent.CedOperatorView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.vaadin.klaudeta.PaginatedGrid;

@Route(value = Routes.PROJECTS, layout = CedOperatorView.class)
public class ProjectsView extends VerticalLayout {

  final PaginatedGrid<Project> grid;

  final TextField filterName;

  private final ProjectRepository repo;

  private final Button addNewBtn;

  private final Button goToIndex;

  private final ProjectsEditor editor;

  private final SecurityService securityService;

  private final Map<Column<?>, String> toggleableColumns = new HashMap<>();


  public ProjectsView(ProjectRepository repo, ProjectsEditor editor,
      @Autowired SecurityService securityService) {
    this.securityService = securityService;
    this.repo = repo;
    this.editor = editor;
    this.grid = new PaginatedGrid<>(Project.class);

    this.filterName = new TextField();
    this.addNewBtn = new Button("Add Project", VaadinIcon.PLUS.create());
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
    HorizontalLayout actions = new HorizontalLayout(filterName, addNewBtn, goToIndex);

    add(toggleButton, actions, grid, editor);

    setupGrid();

    filterName.setPlaceholder("Filter by name");

    // Hook logic to components
        /* Replace listing with filtered content when user
          changes filter*/
    filterName.setValueChangeMode(ValueChangeMode.EAGER);
    filterName.addValueChangeListener
        (e -> listProjects(e.getValue()));


        /* Instantiate and edit new
        Customer the new button is clicked
         */

    addNewButton(editor);

    // Listen changes made by the editor,
    // refresh data from backend
    editor.setChangeHandler(() -> {
      editor.setVisible(false);
      listProjects(filterName.getValue());
    });

    // Initialize listing
    listProjects(null);
  }


  private void addNewButton(ProjectsEditor editor) {
    addNewBtn.addClickListener(e -> editor.editProject(
        (new Project(null, new Institution(), new User(), "", LocalDateTime.now(),
            LocalDateTime.now(), "", true))));
  }

  private void setupGrid() {
    grid.setHeight(DEFAULT_GRID_HEIGHT);
    grid.setColumns("id", "institutionId", "userCreatedId", "name", "dateFrom", "dateTill",
        "additionalInfo");
    grid.getColumnByKey("id").setAutoWidth(true).setFlexGrow(0).setFrozen(true);
    grid.getColumnByKey("institutionId").setAutoWidth(true).setFlexGrow(0);
    grid.getColumnByKey("userCreatedId").setAutoWidth(true).setFlexGrow(0);

    grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

    grid.removeColumnByKey("dateFrom");
    grid.removeColumnByKey("dateTill");
    grid.addColumn(
        new LocalDateTimeRenderer<>(Project::getDateFrom,
            DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)
        )
    ).setHeader("Date from").setAutoWidth(true).setFlexGrow(0);
    grid.addColumn(
        new LocalDateTimeRenderer<>(Project::getDateTill,
            DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)
        )
    ).setHeader("Date till").setAutoWidth(true).setFlexGrow(0);

    final String edit = "Edit";
    final Column<Project> column = grid.addComponentColumn(t -> {
      Button editButton = new Button(edit);
      editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
      editButton.addClickListener(click -> editor.editProject(t));
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
    Column<Project> settingColumn = grid.addColumn(box -> "").setWidth("auto").setFlexGrow(0);
    grid.getHeaderRows().get(0).getCell(settingColumn)
        .setComponent(createMenuToggle(toggleableColumns));

    // Sets the max number of items to be rendered on the grid for each page
    grid.setPageSize(10);
    // Sets how many pages should be visible on the pagination before and/or after the current selected page
    grid.setPaginatorSize(5);
  }

  void listProjects(String filterText) {
    if (StringUtils.isEmpty(filterText)) {
      grid.setItems(repo.findAll());
    } else {
      grid.setItems(repo.
          findByNameStartsWithIgnoreCase(filterText));
    }
  }
}
