package com.spring.libra.ui.view;

import static com.spring.libra.ui.view.IssuesView.showDetails;

import com.spring.libra.config.security.SecurityService;
import com.spring.libra.model.entity.Issue;
import com.spring.libra.model.entity.User;
import com.spring.libra.repository.IssueRepository;
import com.spring.libra.repository.UserRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.vaadin.klaudeta.PaginatedGrid;

@Route(value = "/myissues")
public class MyIssuesView extends VerticalLayout {

  final PaginatedGrid<Issue> grid;

  final TextField filter;

  private final IssueRepository repo;

  private final UserRepository userRepository;

  private final SecurityService securityService;

  public MyIssuesView(IssueRepository repo,
      UserRepository userRepository,
      @Autowired SecurityService securityService) {
    this.userRepository = userRepository;
    this.securityService = securityService;
    this.repo = repo;
    this.grid = new PaginatedGrid<>(Issue.class);

    this.filter = new TextField();

    Button toggleButton = new Button("Toggle theme variant", click -> {
      ThemeList themeList = UI.getCurrent().getElement().getThemeList();
      if (themeList.contains(Lumo.DARK)) {
        themeList.remove(Lumo.DARK);
      } else {
        themeList.add(Lumo.DARK);
      }
    });

    VerticalLayout header = getVerticalLayoutHeader(securityService);

    // build layout
    HorizontalLayout actions = new HorizontalLayout(filter);

    Text space = new Text("       ");
    Text text = new Text("My issues");

    VerticalLayout spacing = new VerticalLayout(space, text);
    spacing.setSpacing(true);
    spacing.setHeight("50px");
    spacing.setAlignItems(Alignment.CENTER);

    add(header, spacing, toggleButton, actions, grid);

    setupGrid();

    filter.setPlaceholder("Filter by description");

    // Hook logic to components
        /* Replace listing with filtered content when user
          changes filter*/
    filter.setValueChangeMode(ValueChangeMode.EAGER);
    filter.addValueChangeListener
        (e -> listUserIssues(e.getValue()));

    grid.asSingleSelect().addValueChangeListener(showDetails());

    // Initialize listing
    listUserIssues(null);
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

  void listUserIssues(String filterText) {

    if (securityService.getAuthenticatedUser() != null) {
      User user = userRepository.findByLogin(securityService.getAuthenticatedUser().getUsername())
          .orElseThrow(() -> new UsernameNotFoundException(
              "User Not Found with username: " + securityService.getAuthenticatedUser()
                  .getUsername()));

      if (StringUtils.isEmpty(filterText)) {
        grid.setItems(repo.findByAssignedId(user));
      } else {
        grid.setItems(repo.
            findByAssignedIdAndDescriptionStartsWithIgnoreCase(user, filterText));
      }
    }
  }
}
