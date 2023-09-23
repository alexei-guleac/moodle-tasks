package com.spring.libra.ui.view;

import static com.spring.libra.constants.DateTime.CUSTOM_FORMATTER;
import static com.spring.libra.constants.ElementsSize.DEFAULT_GRID_HEIGHT;
import static com.spring.libra.constants.ElementsSize.DEFAULT_INDEX_MAX_WIDTH;

import com.spring.libra.config.security.SecurityService;
import com.spring.libra.constants.Routes;
import com.spring.libra.model.entity.Notification;
import com.spring.libra.model.entity.User;
import com.spring.libra.repository.NotificationRepository;
import com.spring.libra.repository.UserRepository;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.vaadin.klaudeta.PaginatedGrid;

@Route(value = Routes.MY_NOTIFICATIONS)
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
    Text text = new Text("My notifications");

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

  private ValueChangeListener<ComponentValueChangeEvent<Grid<Notification>, Notification>> showDetails() {
    return selection -> {
      Optional<Notification> notificationOptional = Optional.ofNullable(selection.getValue());
      if (notificationOptional.isPresent()) {
        Dialog dialog = new Dialog();
        final Notification notification = notificationOptional.get();

        dialog.add(new H4("Notification id # "), new Text(notification.getId().toString()));
        dialog.add(new H4("Issue: "), new Text(notification.getIssueId().toString()));
        dialog.add(new H4("Priority: "), new Text(notification.getPriority()));
        dialog.add(new H4("User created: "), new Text(notification.getUserCreatedId().toString()));
        dialog.add(new H4("User assigned: "), new Text(notification.getAssignedId().toString()));
        dialog.add(new H4("Description: "), new Text(notification.getDescription()));
        dialog.add(new H4("Assign date: "), new Text(notification.getAssignedDate().format(CUSTOM_FORMATTER)));
        dialog.add(new H4("Creation date: "), new Text(notification.getCreationDate().format(CUSTOM_FORMATTER)));

        dialog.setWidthFull();
        dialog.setMinWidth("200px");
        dialog.setMaxWidth(DEFAULT_INDEX_MAX_WIDTH);
        dialog.open();
      }
    };
  }

  private void setupGrid() {
    grid.setHeight(DEFAULT_GRID_HEIGHT);
    grid.setColumns("id", "issueId", "priority", "userCreatedId",
        "description", "creationDate");
    grid.getColumnByKey("id").setAutoWidth(true).setFlexGrow(0).setFrozen(true);
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
      final String logout = "Logout";
      dialog.setHeader(logout);
      dialog.setText("Do you want to log out from the system now?");
      dialog.setCancelable(true);

      dialog.setConfirmText(logout);
      dialog.addConfirmListener(event -> securityService.logout());

      Button button = new Button(logout, click ->
          dialog.open());
      VerticalLayout verticalLayout = new VerticalLayout(button);
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
