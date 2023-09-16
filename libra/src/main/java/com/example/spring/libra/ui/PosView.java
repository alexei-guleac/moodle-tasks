package com.example.spring.libra.ui;

import com.example.spring.libra.config.security.SecurityService;
import com.example.spring.libra.model.entity.City;
import com.example.spring.libra.model.entity.Pos;
import com.example.spring.libra.repository.PosRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@Route(value = "/positions")
public class PosView extends VerticalLayout {

  final Grid<Pos> grid;

  final TextField filter;

  private final PosRepository repo;

  private final Button addNewBtn;

  private final Button addNewCityBtn;

  private final Button goToIssues;

  private final PosEditor editor;

  private final CityEditor cityEditor;

  private final SecurityService securityService;

  public PosView(PosRepository repo, PosEditor editor,
      CityEditor cityEditor, @Autowired SecurityService securityService) {
    this.cityEditor = cityEditor;
    this.securityService = securityService;
    this.repo = repo;
    this.editor = editor;
    this.grid = new Grid<>(Pos.class);

    this.filter = new TextField();
    this.addNewBtn = new Button("Add Position", VaadinIcon.PLUS.create());
    this.addNewCityBtn = new Button("Add City", VaadinIcon.PLUS.create());
    addNewBtn.addThemeVariants
        (ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);

    this.goToIssues = new Button("Go To Issues");
    goToIssues.addClickListener(e ->
        goToIssues.getUI().ifPresent(ui ->
            ui.navigate("/issues"))
    );

    VerticalLayout header = getVerticalLayoutHeader(securityService);

    // build layout
    HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, addNewCityBtn, goToIssues);

    Text space = new Text("       ");
    Text text = new Text("Position management");

    VerticalLayout spacing = new VerticalLayout(space, text);
    spacing.setSpacing(true);
    spacing.setHeight("50px");
    spacing.setAlignItems(Alignment.CENTER);

    add(header, spacing, actions, grid, editor, cityEditor);

    setupGrid();

    filter.setPlaceholder("Filter by name");

    // Hook logic to components
        /* Replace listing with filtered content when user
          changes filter*/
    filter.setValueChangeMode(ValueChangeMode.EAGER);
    filter.addValueChangeListener
        (e -> listPositions(e.getValue()));


        /* Connect selected Customer to editor or hide if none
            is selected */
    grid.asSingleSelect().addValueChangeListener(e -> {
      editor.editPosition(e.getValue());
    });

        /* Instantiate and edit new
        Customer the new button is clicked
         */

    addNewButton(editor);
    addNewCityButton(cityEditor);

    // Listen changes made by the editor,
    // refresh data from backend
    editor.setChangeHandler(() -> {
      editor.setVisible(false);
      listPositions(filter.getValue());
    });
    cityEditor.setChangeHandler(() -> cityEditor.setVisible(false));

    // Initialize listing
    listPositions(null);
  }

  private void addNewButton(PosEditor editor) {
    addNewBtn.addClickListener(e -> editor.editPosition(
        (new Pos())));
  }

  private void addNewCityButton(CityEditor editor) {
    addNewCityBtn.addClickListener(e -> editor.editCity(
        (new City())));
  }

  private void setupGrid() {
    grid.setHeight("300px");
    grid.setColumns("id", "posName", "telephone", "cityId", "model", "brand",
        "daysClosed", "insertDate");
    grid.getColumnByKey("id").setWidth("60px").
        setFlexGrow(0);
    grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
  }

  private VerticalLayout getVerticalLayoutHeader(@Autowired SecurityService securityService) {

    H1 logo = new H1("Libra");
    logo.addClassName("logo");
    VerticalLayout header;
    if (securityService.getAuthenticatedUser() != null) {
      Button logout = new Button("Logout", click ->
          securityService.logout());
      header = new VerticalLayout(logo, logout);
    } else {
      header = new VerticalLayout(logo);
    }
    header.setSizeFull();
    header.setAlignItems((Alignment.CENTER));

    return header;
  }

  void listPositions(String filterText) {
    if (StringUtils.isEmpty(filterText)) {
      grid.setItems(repo.findAll());
    } else {
      grid.setItems(repo.
          findByPosNameStartsWithIgnoreCase(filterText));
    }
  }
}
