package com.example.spring.libra.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route(value = "/")
public class IndexView extends VerticalLayout {

  Button positions = new Button("Go to Positions");
  Button issues = new Button("Go to Issues");
  Button users = new Button("Go to Users");


  public IndexView() {
    VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSizeFull();

    VerticalLayout header = getVerticalLayout();

    setButtons();

    verticalLayout.add(header, positions, issues, users);
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

  private void setButtons() {
    positions.setWidthFull();
    positions.setMaxWidth("500px");
    positions.setMinWidth("100px");

    issues.setWidthFull();
    issues.setMaxWidth("500px");
    issues.setMinWidth("100px");

    users.setWidthFull();
    users.setMaxWidth("500px");
    users.setMinWidth("100px");

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
  }
}
