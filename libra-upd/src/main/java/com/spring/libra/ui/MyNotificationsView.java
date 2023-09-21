package com.spring.libra.ui;

import com.spring.libra.config.security.SecurityService;
import com.spring.libra.model.entity.Notification;
import com.spring.libra.model.entity.User;
import com.spring.libra.repository.NotificationRepository;
import com.spring.libra.repository.UserRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.vaadin.klaudeta.PaginatedGrid;

@Route(value = "/mynotifications")
public class MyNotificationsView extends VerticalLayout {

  final PaginatedGrid<Notification> grid;

  final TextField filter;

  private final NotificationRepository repo;

  private final UserRepository userRepository;

  private final SecurityService securityService;

  public MyNotificationsView(NotificationRepository repo,
      UserRepository userRepository,
      @Autowired SecurityService securityService) {
    this.userRepository = userRepository;
    this.securityService = securityService;
    this.repo = repo;
    this.grid = new PaginatedGrid<>(Notification.class);

    this.filter = new TextField();

    VerticalLayout header = getVerticalLayoutHeader(securityService);

    // build layout
    HorizontalLayout actions = new HorizontalLayout(filter);

    Text space = new Text("       ");
    Text text = new Text("My notifications");

    VerticalLayout spacing = new VerticalLayout(space, text);
    spacing.setSpacing(true);
    spacing.setHeight("50px");
    spacing.setAlignItems(Alignment.CENTER);

    add(header, spacing, actions, grid);

    setupGrid();

    filter.setPlaceholder("Filter by description");

    // Hook logic to components
        /* Replace listing with filtered content when user
          changes filter*/
    filter.setValueChangeMode(ValueChangeMode.EAGER);
    filter.addValueChangeListener
        (e -> listUserIssues(e.getValue()));

    // Initialize listing
    listUserIssues(null);
  }


  private void setupGrid() {
    grid.setHeight("500px");
    grid.setColumns("id", "issueId", "priority", "userCreatedId",
        "description", "creationDate");
    grid.getColumnByKey("id").setWidth("60px").
        setFlexGrow(0);
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
      header = new VerticalLayout(logo, logout);
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
