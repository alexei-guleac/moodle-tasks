package com.spring.libra.ui.view;

import static com.spring.libra.constants.DateTime.CUSTOM_FORMATTER;
import static com.spring.libra.util.ui.GridUtils.createMenuToggle;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.spring.libra.config.security.SecurityService;
import com.spring.libra.model.entity.Issue;
import com.spring.libra.model.entity.IssueTypes;
import com.spring.libra.model.entity.Pos;
import com.spring.libra.model.entity.Statuses;
import com.spring.libra.model.entity.User;
import com.spring.libra.repository.IssueRepository;
import com.spring.libra.ui.editor.IssueEditor;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

  private final Map<Column<?>, String> toggleableColumns = new HashMap<>();

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
    grid.asSingleSelect().addValueChangeListener(showDetails());

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

  static ValueChangeListener<ComponentValueChangeEvent<Grid<Issue>, Issue>> showDetails() {
    return selection -> {
      Optional<Issue> issueOptional = Optional.ofNullable(selection.getValue());
      if (issueOptional.isPresent()) {
        Dialog dialog = new Dialog();
        final Issue issue = issueOptional.get();

        dialog.add(new H4("Issue id # "), new Text(issue.getId().toString()));
        dialog.add(new H4("Position id: "), new Text(issue.getPosId().toString()));
        dialog.add(new H4("Issue type: "), new Text(issue.getIssueTypeId().toString()));
        dialog.add(new H4("Problem id: "), new Text(issue.getProblemId().toString()));
        dialog.add(new H4("Priority "), new Text(issue.getPriority()));
        dialog.add(new H4("Status: "), new Text(issue.getStatusId().toString()));
        dialog.add(new H4("Memo: "), new Text(issue.getMemo()));
        dialog.add(new H4("User created: "), new Text(issue.getUserCreatedId().toString()));
        dialog.add(new H4("User assigned: "), new Text(issue.getAssignedId().toString()));
        dialog.add(new H4("Description: "), new Text(issue.getDescription()));
        dialog.add(new H4("Assign date: "),
            new Text(issue.getAssignedDate().format(CUSTOM_FORMATTER)));
        dialog.add(new H4("Creation date: "),
            new Text(issue.getCreationDate().format(CUSTOM_FORMATTER)));
        dialog.add(new H4("Solution: "), new Text(issue.getSolution()));

        dialog.setWidthFull();
        dialog.setMinWidth("200px");
        dialog.setMaxWidth("500px");
        dialog.open();
      }
    };
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

    final Column<Issue> column = grid.addComponentColumn(t -> {
      Button editButton = new Button("Edit");
      editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
      editButton.addClickListener(click -> editor.editIssue(t));
      return editButton;
    }).setWidth("auto").setFlexGrow(0);
    column.setHeader("Edit");

    grid.getColumns()
        .forEach(issueColumn -> {
          final String key = issueColumn.getKey();
          if (isNotBlank(key) && !key.equals("Edit")) {
            toggleableColumns.put(issueColumn, key);
          }
        });
    Column<Issue> settingColumn = grid.addColumn(box -> "").setWidth("auto").setFlexGrow(0);
    grid.getHeaderRows().get(0).getCell(settingColumn)
        .setComponent(createMenuToggle(toggleableColumns));

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
