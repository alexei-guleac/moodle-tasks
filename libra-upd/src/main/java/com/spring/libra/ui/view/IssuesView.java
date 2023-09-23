package com.spring.libra.ui.view;

import com.spring.libra.config.security.SecurityService;
import com.spring.libra.model.entity.Issue;
import com.spring.libra.model.entity.IssueTypes;
import com.spring.libra.model.entity.Pos;
import com.spring.libra.model.entity.Statuses;
import com.spring.libra.model.entity.User;
import com.spring.libra.repository.IssueRepository;
import com.spring.libra.ui.editor.IssueEditor;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.vaadin.klaudeta.PaginatedGrid;

@Route(value = "/issues")
public class IssuesView extends VerticalLayout {

  final PaginatedGrid<Issue> grid;

  final TextField filter;

  private final IssueRepository repo;

  private final Button addNewBtn;

  private final Button goToUsers;

  private final Button goToPositions;

  private final IssueEditor editor;

  private final SecurityService securityService;

  public IssuesView(IssueRepository repo, IssueEditor editor,
      @Autowired SecurityService securityService) {
    this.securityService = securityService;
    this.repo = repo;
    this.editor = editor;
    this.grid = new PaginatedGrid<>(Issue.class);

    this.filter = new TextField();
    this.addNewBtn = new Button("Add Issue", VaadinIcon.PLUS.create());
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

    this.goToUsers = new Button("Go To Users");
    goToUsers.addClickListener(e ->
        goToUsers.getUI().ifPresent(ui ->
            ui.navigate("/users"))
    );
    this.goToPositions = new Button("Go To Positions");
    goToPositions.addClickListener(e ->
        goToPositions.getUI().ifPresent(ui ->
            ui.navigate("/positions"))
    );

    VerticalLayout header = getVerticalLayoutHeader(securityService);

    // build layout
    HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, goToUsers, goToPositions);

    Text space = new Text("       ");
    Text text = new Text("Issue management");

    VerticalLayout spacing = new VerticalLayout(space, text);
    spacing.setSpacing(true);
    spacing.setHeight("50px");
    spacing.setAlignItems(Alignment.CENTER);

    add(header, spacing, toggleButton, actions, grid, editor);

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
    grid.setHeight("500px");
    grid.setColumns("id", "posId", "issueTypeId", "problemId", "priority", "assignedId",
        "description", "creationDate");
    grid.getColumnByKey("id").setAutoWidth(true).setFlexGrow(0).setFrozen(true);
    grid.getColumnByKey("problemId").setAutoWidth(true).setFlexGrow(0);
    grid.getColumnByKey("priority").setAutoWidth(true).setFlexGrow(0);
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

  void listIssues(String filterText) {
    if (StringUtils.isEmpty(filterText)) {
      grid.setItems(repo.findAll());
    } else {
      grid.setItems(repo.
          findByDescriptionStartsWithIgnoreCase(filterText));
    }
  }
}
