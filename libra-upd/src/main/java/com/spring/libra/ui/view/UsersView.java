package com.spring.libra.ui.view;

import static com.spring.libra.util.ui.GridUtils.createMenuToggle;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.spring.libra.config.security.SecurityService;
import com.spring.libra.model.entity.User;
import com.spring.libra.model.entity.UserTypes;
import com.spring.libra.repository.UserRepository;
import com.spring.libra.ui.editor.UserEditor;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
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
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.vaadin.klaudeta.PaginatedGrid;

@Route(value = "/users")
public class UsersView extends VerticalLayout {

  final PaginatedGrid<User> grid;

  final TextField filterEmail;

  private final UserRepository repo;

  private final Button addNewBtn;

  private final Button goToIssues;

  private final UserEditor editor;

  private final SecurityService securityService;

  private final Map<Column<?>, String> toggleableColumns = new HashMap<>();


  public UsersView(UserRepository repo, UserEditor editor,
      @Autowired SecurityService securityService) {
    this.securityService = securityService;
    this.repo = repo;
    this.editor = editor;
    this.grid = new PaginatedGrid<>(User.class);

    this.filterEmail = new TextField();
    this.addNewBtn = new Button("Add User", VaadinIcon.PLUS.create());
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

    this.goToIssues = new Button("Go To Issues");
    goToIssues.addClickListener(e ->
        goToIssues.getUI().ifPresent(ui ->
            ui.navigate("/issues"))
    );

    // Logo text
    VerticalLayout header = getVerticalLayoutForHeader(securityService);

    // build layout
    HorizontalLayout actions = new HorizontalLayout(filterEmail, addNewBtn, goToIssues);

    Text space = new Text("       ");
    Text text = new Text("User management");

    VerticalLayout spacing = new VerticalLayout(space, text);
    spacing.setSpacing(true);
    spacing.setHeight("50px");
    spacing.setAlignItems(Alignment.CENTER);

    add(header, spacing, toggleButton, actions, grid, editor);

    setupGrid();

    filterEmail.setPlaceholder("Filter by email");

    // Hook logic to components
        /* Replace listing with filtered content when user
          changes filter*/
    filterEmail.setValueChangeMode(ValueChangeMode.EAGER);
    filterEmail.addValueChangeListener
        (e -> listUsers(e.getValue()));


        /* Connect selected Customer to editor or hide if none
            is selected */
    grid.asSingleSelect().addValueChangeListener(showDetails());

        /* Instantiate and edit new
        Customer the new button is clicked
         */

    addNewButton(editor);

    // Listen changes made by the editor,
    // refresh data from backend
    editor.setChangeHandler(() -> {
      editor.setVisible(false);
      listUsers(filterEmail.getValue());
    });

    // Initialize listing
    listUsers(null);
  }

  private ValueChangeListener<ComponentValueChangeEvent<Grid<User>, User>> showDetails() {
    return selection -> {
      Optional<User> userOptional = Optional.ofNullable(selection.getValue());
      if (userOptional.isPresent()) {
        Dialog dialog = new Dialog();
        final User user = userOptional.get();

        dialog.add(new H4("User id # "), new Text(user.getId().toString()));
        dialog.add(new H4("Name: "), new Text(user.getName()));
        dialog.add(new H4("Email: "), new Text(user.getEmail()));
        dialog.add(new H4("Telephone: "), new Text(user.getTelephone()));
        dialog.add(new H4("Login: "), new Text(user.getLogin()));
        dialog.add(new H4("User type id: "), new Text(user.getUserTypeId().toString()));

        dialog.setWidthFull();
        dialog.setMinWidth("200px");
        dialog.setMaxWidth("500px");
        dialog.open();
      }
    };
  }

  private void addNewButton(UserEditor editor) {
    addNewBtn.addClickListener(e -> editor.editUser
        (new User(null, "", "", "", "", "", "", new UserTypes())));
  }

  private void setupGrid() {
    grid.setHeight("500px");
    grid.setColumns("id", "name", "login", "email", "telephone");
    grid.getColumnByKey("id").setAutoWidth(true).setFlexGrow(0).setFrozen(true);
    grid.getColumnByKey("login").setAutoWidth(true).setFlexGrow(0);
    grid.getColumnByKey("telephone").setAutoWidth(true).setFlexGrow(0);
    grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

    final Column<User> column = grid.addComponentColumn(t -> {
      Button editButton = new Button("Edit");
      editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
      editButton.addClickListener(click -> editor.editUser(t));
      return editButton;
    }).setWidth("auto").setFlexGrow(0);
    column.setHeader("Edit");

    grid.getColumns()
        .forEach(userColumn -> {
          final String key = userColumn.getKey();
          if (isNotBlank(key) && !key.equals("Edit")) {
            toggleableColumns.put(userColumn, key);
          }
        });
    Column<User> settingColumn = grid.addColumn(box -> "").setWidth("auto").setFlexGrow(0);
    grid.getHeaderRows().get(0).getCell(settingColumn)
        .setComponent(createMenuToggle(toggleableColumns));

    // Sets the max number of items to be rendered on the grid for each page
    grid.setPageSize(10);
    // Sets how many pages should be visible on the pagination before and/or after the current selected page
    grid.setPaginatorSize(5);
  }

  private VerticalLayout getVerticalLayoutForHeader(@Autowired SecurityService securityService) {

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

  void listUsers(String filterText) {
    if (StringUtils.isEmpty(filterText)) {
      grid.setItems(repo.findAll());
    } else {
      grid.setItems(repo.
          findByEmailStartsWithIgnoreCase(filterText));
    }
  }
}
