package com.example.spring.libra.ui;

import com.example.spring.libra.config.security.SecurityService;
import com.example.spring.libra.model.entity.Issue;
import com.example.spring.libra.model.entity.IssueTypes;
import com.example.spring.libra.model.entity.Pos;
import com.example.spring.libra.model.entity.Statuses;
import com.example.spring.libra.model.entity.User;
import com.example.spring.libra.repository.IssueRepository;
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
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@Route(value = "/issues")
public class IssuesView extends VerticalLayout {

  final Grid<Issue> grid;

  final TextField filter;

  private final IssueRepository repo;

  private final Button addNewBtn;

  private final Button goToUsers;

  private final IssueEditor editor;

  private final SecurityService securityService;

  public IssuesView(IssueRepository repo, IssueEditor editor,
      @Autowired SecurityService securityService) {
    this.securityService = securityService;
    this.repo = repo;
    this.editor = editor;
    this.grid = new Grid<>(Issue.class);

    this.filter = new TextField();
    this.addNewBtn = new Button("Add Issue", VaadinIcon.PLUS.create());
    addNewBtn.addThemeVariants
        (ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);

    this.goToUsers = new Button("Go To Users");
    goToUsers.addClickListener(e ->
        goToUsers.getUI().ifPresent(ui ->
            ui.navigate("/users"))
    );

    VerticalLayout header = getVerticalLayoutHeader(securityService);

    // build layout
    HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, goToUsers);

    Text space = new Text("       ");
    Text text = new Text("Issue management");

    VerticalLayout spacing = new VerticalLayout(space, text);
    spacing.setSpacing(true);
    spacing.setHeight("50px");
    spacing.setAlignItems(Alignment.CENTER);

    add(header, spacing, actions, grid, editor);

    setupGrid();

    filter.setPlaceholder("Filter by description");

    // Hook logic to components
        /* Replace listing with filtered content when user
          changes filter*/
    filter.setValueChangeMode(ValueChangeMode.EAGER);
    filter.addValueChangeListener
        (e -> listIssues(e.getValue()));


        /* Connect selected Customer to editor or hide if none
            is selected */
    grid.asSingleSelect().addValueChangeListener(e -> {
      editor.editIssue(e.getValue());
    });

        /* Instantiate and edit new
        Customer the new button is clicked
         */

    addNewButton(editor);

    // Listen changes made by the editor,
    // refresh data from backend
    editor.setChangeHandler(() -> {
      editor.setVisible(false);
      listIssues(filter.getValue());
    });

    // Initialize listing
    listIssues(null);
  }

  private void addNewButton(IssueEditor editor) {
    addNewBtn.addClickListener(e -> editor.editIssue
        (new Issue(null, new Pos(), new IssueTypes(), 0, "", new Statuses(), "", new User(),
            new User(), "", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), "")));
  }

  private void setupGrid() {
    grid.setHeight("300px");
    grid.setColumns("id", "posId", "issueTypeId", "problemId", "priority", "assignedId",
        "description", "creationDate");
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

  void listIssues(String filterText) {
    if (StringUtils.isEmpty(filterText)) {
      grid.setItems(repo.findAll());
    } else {
      grid.setItems(repo.
          findByDescriptionStartsWithIgnoreCase(filterText));
    }
  }
}
