package com.spring.documentale.ui;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.Collections;

@Tag("sa-login-view")
@Route(value = LoginView.ROUTE)
@PageTitle("login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

  public static final String ROUTE = "login";

  private final LoginOverlay login = new LoginOverlay();

  Button reg = new Button("Register");


  public LoginView() {
    VerticalLayout verticalLayout = new VerticalLayout();
    setupVerticalLayout(verticalLayout);

    login.setOpened(true);
    login.setAction("login");
    login.setTitle("Libra");
    login.setDescription("Issue tracker");

    addActionForRegistrationButton();

    getElement().appendChild(verticalLayout.getElement());
  }

  private void addActionForRegistrationButton() {
    reg.addClickListener(e ->
        reg.getUI().ifPresent(ui ->
            ui.navigate("/register"))
    );
  }

  private void setupVerticalLayout(VerticalLayout verticalLayout) {

    verticalLayout.setSizeFull();
    verticalLayout.add(login, reg);
    verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    verticalLayout.setAlignItems((Alignment.CENTER));
    verticalLayout.setAlignSelf(Alignment.CENTER);
    verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER);
    
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    // inform the user about an authentication error
    // (yes, the API for resolving query parameters is annoying...)
    if (!event.getLocation().getQueryParameters().getParameters()
        .getOrDefault("error", Collections.emptyList()).isEmpty()) {
      login.setError(true);
    }
  }
}
