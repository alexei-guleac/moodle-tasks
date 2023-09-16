package com.example.spring.libra.ui;

import com.example.spring.libra.config.security.SecurityService;
import com.example.spring.libra.model.entity.User;
import com.example.spring.libra.model.entity.UserTypes;
import com.example.spring.libra.repository.UserRepository;
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

@Route(value = "/users")
public class UsersView extends VerticalLayout {

  final Grid<User> grid;

  final TextField filterEmail;

  private final UserRepository repo;

  private final Button addNewBtn;

  private final Button goToIssues;

  private final UserEditor editor;

  private final SecurityService securityService;

  public UsersView(UserRepository repo, UserEditor editor,
      @Autowired SecurityService securityService) {
    this.securityService = securityService;
    this.repo = repo;
    this.editor = editor;
    this.grid = new Grid<>(User.class);

    this.filterEmail = new TextField();
    this.addNewBtn = new Button("Add User", VaadinIcon.PLUS.create());
    addNewBtn.addThemeVariants
        (ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);

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

    add(header, spacing, actions, grid, editor);

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
    grid.asSingleSelect().addValueChangeListener(e -> {
      editor.editUser(e.getValue());
    });

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

  private void addNewButton(UserEditor editor) {
    addNewBtn.addClickListener(e -> editor.editUser
        (new User(null, "", "", "", "", "", "", new UserTypes())));
  }

  private void setupGrid() {
    grid.setHeight("300px");
    grid.setColumns("id", "name", "login", "email", "telephone");
    grid.getColumnByKey("id").setWidth("60px").
        setFlexGrow(0);
    grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
  }

  private VerticalLayout getVerticalLayoutForHeader(@Autowired SecurityService securityService) {

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

  void listUsers(String filterText) {
    if (StringUtils.isEmpty(filterText)) {
      grid.setItems(repo.findAll());
    } else {
      grid.setItems(repo.
          findByEmailStartsWithIgnoreCase(filterText));
    }
  }
}
