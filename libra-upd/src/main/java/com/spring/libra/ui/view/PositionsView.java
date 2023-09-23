package com.spring.libra.ui.view;

import com.spring.libra.config.security.SecurityService;
import com.spring.libra.model.entity.City;
import com.spring.libra.model.entity.ConnectionTypes;
import com.spring.libra.model.entity.Pos;
import com.spring.libra.repository.PosRepository;
import com.spring.libra.ui.editor.CityEditor;
import com.spring.libra.ui.editor.PosEditor;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.vaadin.klaudeta.PaginatedGrid;

@Route(value = "/positions")
public class PositionsView extends VerticalLayout {

  final PaginatedGrid<Pos> grid;

  final TextField filter;

  private final PosRepository repo;

  private final Button addNewBtn;

  private final Button addNewCityBtn;

  private final Button goToIssues;

  private final PosEditor editor;

  private final CityEditor cityEditor;

  private final SecurityService securityService;

  public PositionsView(PosRepository repo, PosEditor editor,
      CityEditor cityEditor, @Autowired SecurityService securityService) {
    this.cityEditor = cityEditor;
    this.securityService = securityService;
    this.repo = repo;
    this.editor = editor;
    this.grid = new PaginatedGrid<>(Pos.class);

    this.filter = new TextField();
    this.addNewBtn = new Button("Add Position", VaadinIcon.PLUS.create());
    this.addNewCityBtn = new Button("Add City", VaadinIcon.PLUS.create());
    addNewBtn.addThemeVariants
        (ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
    addNewCityBtn.addThemeVariants
        (ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);

    Button toggleButton = new Button("Toggle theme variant", click -> {
      ThemeList themeList = UI.getCurrent().getElement().getThemeList();
      if (themeList.contains(Lumo.DARK)) {
        themeList.remove(Lumo.DARK);
      } else {
        themeList.add(Lumo.DARK);
      }
    });

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

    add(header, spacing, toggleButton, actions, grid, editor, cityEditor);

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
        (new Pos(null, "", "", "", "", new City(), "", "",
            new ConnectionTypes(),
            false, false, false, false, 0,
            LocalDateTime.now()))));
  }

  private void addNewCityButton(CityEditor editor) {
    addNewCityBtn.addClickListener(e -> editor.editCity(
        (new City(null, ""))));
  }

  private void setupGrid() {
    grid.setHeight("500px");
    grid.setColumns("id", "posName", "telephone", "cityId", "model", "brand",
        "daysClosed", "insertDate");
    grid.getColumnByKey("id").setAutoWidth(true).setFlexGrow(0).setFrozen(true);
    grid.getColumnByKey("telephone").setAutoWidth(true).setFlexGrow(0);
    grid.getColumnByKey("daysClosed").setAutoWidth(true).setFlexGrow(0);
    grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

    // Sets the max number of items to be rendered on the grid for each page
    grid.setPageSize(10);
    // Sets how many pages should be visible on the pagination before and/or after the current selected page
    grid.setPaginatorSize(5);
  }

  private VerticalLayout getVerticalLayoutHeader(@Autowired SecurityService securityService) {

    H1 logo = new H1("Libra");
    logo.addClassName("logo");
    VerticalLayout header;
    if (securityService.getAuthenticatedUser() != null) {

      ConfirmDialog dialog = new ConfirmDialog();
      dialog.setHeader("Logout");
      dialog.setText("Do you want to log out from the system now?");
      dialog.setCancelable(true);

      dialog.setConfirmText("Logout");
      dialog.addConfirmListener(event -> securityService.logout());

      Button logout = new Button("Logout", click ->
          dialog.open());
      VerticalLayout verticalLayout = new VerticalLayout(logout);
      verticalLayout.setJustifyContentMode(JustifyContentMode.END);
      verticalLayout.setAlignItems((Alignment.END));
      verticalLayout.setAlignSelf(Alignment.END);
      verticalLayout.setSpacing(false);
      verticalLayout.setPadding(false);
      verticalLayout.setHeight("40px");

      header = new VerticalLayout(verticalLayout, logo);
      header.setSpacing(false);
      header.setPadding(false);
      header.setHeight("20px");
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
