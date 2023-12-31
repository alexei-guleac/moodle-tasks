package com.example.spring.vaadin.ui;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.Collections;

@Tag("sa-login-view")
@Route(value = LoginView.ROUTE)
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

  public static final String ROUTE = "login";

  private final LoginForm login = new LoginForm();

  public LoginView() {
    VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSizeFull();

    verticalLayout.add(login);
    verticalLayout.setAlignItems((Alignment.CENTER));

    login.setAction("login");
    getElement().appendChild(verticalLayout.getElement());
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
