package com.example.spring.vaadin.ui;

import static javax.swing.text.StyleConstants.setFontSize;

import com.example.spring.vaadin.config.security.SecurityService;
import com.example.spring.vaadin.entity.Customer;
import com.example.spring.vaadin.repository.CustomerRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@Route(value = "/")
public class Index extends VerticalLayout {

  final Grid<Customer> grid;

  final TextField filter;

  private final CustomerRepository repo;

  private SecurityService securityService;

  private final Button addNewBtn;

  private final UserEditor editor;

  public Index(CustomerRepository repo, UserEditor editor, @Autowired SecurityService securityService) {
    this.securityService = securityService;
    this.repo = repo;
    this.editor = editor;
    this.grid = new Grid<>(Customer.class);

    this.filter = new TextField();
    this.addNewBtn = new Button
        ("Add Customer", VaadinIcon.PLUS.create());
    addNewBtn.addThemeVariants
        (ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);

    H1 logo = new H1("Vaadin CRM");
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

    // build layout
    HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);

    Text space = new Text("       ");
    Text text = new Text("CRM - Customer Relationship Manager");

    VerticalLayout spacing = new VerticalLayout(space, text);
    spacing.setSpacing(true);
    spacing.setHeight("50px");
    spacing.setAlignItems(Alignment.CENTER);

    add(header, spacing, actions, grid, editor);
    grid.setHeight("300px");
    grid.setColumns("id", "firstName", "lastName", "email");
    grid.getColumnByKey("id").setWidth("60px").
        setFlexGrow(0);
    filter.setPlaceholder("Filter by email");

    // Hook logic to components
        /* Replace listing with filtered content when user
          changes filter*/
    filter.setValueChangeMode(ValueChangeMode.EAGER);
    filter.addValueChangeListener
        (e -> listUsers(e.getValue()));

        /* Connect selected Customer to editor or hide if none
            is selected */
    grid.asSingleSelect().addValueChangeListener(e -> {
      editor.editUser(e.getValue());
    });

        /* Instantiate and edit new
        Customer the new button is clicked
         */
    addNewBtn.addClickListener(e -> editor.editUser
        (new Customer("", "", "")));

    // Listen changes made by the editor,
    // refresh data from backend
    editor.setChangeHandler(() -> {
      editor.setVisible(false);
      listUsers(filter.getValue());
    });

    // Initialize listing
    listUsers(null);
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
