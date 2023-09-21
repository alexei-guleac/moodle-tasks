package com.spring.libra.ui;

import com.spring.libra.config.security.SecurityService;
import com.spring.libra.model.entity.User;
import com.spring.libra.model.enums.UserType;
import com.spring.libra.repository.UserRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Route(value = "/")
public class IndexView extends VerticalLayout {

  private final SecurityService securityService;

  private final UserRepository userRepository;

  Button positions = new Button("Go to Positions");
  Button issues = new Button("Go to Issues");
  Button users = new Button("Go to Users");
  Button myNotifications = new Button("My Notifications");
  Button myIssues = new Button("My Issues");


  public IndexView(@Autowired SecurityService securityService,
      UserRepository userRepository) {
    this.securityService = securityService;
    this.userRepository = userRepository;

    VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSizeFull();

    VerticalLayout header = getVerticalLayout();

    if (securityService.getAuthenticatedUser() != null) {
      User user = userRepository.findByLogin(securityService.getAuthenticatedUser().getUsername())
          .orElseThrow(() -> new UsernameNotFoundException(
              "User Not Found with username: " + securityService.getAuthenticatedUser()
                  .getUsername()));

      if (user.getUserTypeId().getUserType().equals(UserType.ADMIN)) {
        setAdminButtons();
        verticalLayout.add(header, positions, issues, users, myIssues, myNotifications);
      } else {
        setUserButtons();
        verticalLayout.add(header, myIssues, myNotifications, positions, issues, users);
      }

    }
    verticalLayout.setAlignItems((Alignment.CENTER));

    getElement().appendChild(verticalLayout.getElement());
  }

  private VerticalLayout getVerticalLayout() {

    H2 logo = new H2("Welcome to Libra");
    logo.addClassName("welcome");
    VerticalLayout header;
    header = new VerticalLayout(logo);
    header.setSizeFull();
    header.setAlignItems((Alignment.CENTER));

    return header;
  }

  private void setAdminButtons() {
    positions.setWidthFull();
    positions.setMaxWidth("500px");
    positions.setMinWidth("100px");

    issues.setWidthFull();
    issues.setMaxWidth("500px");
    issues.setMinWidth("100px");

    users.setWidthFull();
    users.setMaxWidth("500px");
    users.setMinWidth("100px");

    myIssues.setWidthFull();
    myIssues.setMaxWidth("500px");
    myIssues.setMinWidth("100px");

    myNotifications.setWidthFull();
    myNotifications.setMaxWidth("500px");
    myNotifications.setMinWidth("100px");

    positions.addClickListener(e ->
        positions.getUI().ifPresent(ui ->
            ui.navigate("positions"))
    );

    issues.addClickListener(e ->
        issues.getUI().ifPresent(ui ->
            ui.navigate("issues"))
    );

    users.addClickListener(e ->
        users.getUI().ifPresent(ui ->
            ui.navigate("users"))
    );

    myIssues.addClickListener(e ->
        users.getUI().ifPresent(ui ->
            ui.navigate("myissues"))
    );

    myNotifications.addClickListener(e ->
        users.getUI().ifPresent(ui ->
            ui.navigate("mynotifications"))
    );
  }

  private void setUserButtons() {

    positions.setWidthFull();
    positions.setMaxWidth("500px");
    positions.setMinWidth("100px");
    positions.setEnabled(false);

    issues.setWidthFull();
    issues.setMaxWidth("500px");
    issues.setMinWidth("100px");
    issues.setEnabled(false);

    users.setWidthFull();
    users.setMaxWidth("500px");
    users.setMinWidth("100px");
    users.setEnabled(false);

    myIssues.setWidthFull();
    myIssues.setMaxWidth("500px");
    myIssues.setMinWidth("100px");

    myNotifications.setWidthFull();
    myNotifications.setMaxWidth("500px");
    myNotifications.setMinWidth("100px");

    myIssues.addClickListener(e ->
        users.getUI().ifPresent(ui ->
            ui.navigate("myissues"))
    );

    myNotifications.addClickListener(e ->
        users.getUI().ifPresent(ui ->
            ui.navigate("mynotifications"))
    );
  }
}
