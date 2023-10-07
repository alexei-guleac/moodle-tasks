package com.spring.documentale.ui.view.parent;


import com.spring.documentale.config.security.SecurityService;
import com.spring.documentale.constants.Routes;
import com.spring.documentale.ui.view.editable.InstitutionsView;
import com.spring.documentale.ui.view.editable.UsersView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLayout;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = Routes.ADMINISTRATION)
public class AdministratorView extends VerticalLayout implements RouterLayout {

  private final Map<Tab, String> tabToUrlMap = new LinkedHashMap<>();

  private final SecurityService securityService;

  public AdministratorView(@Autowired SecurityService securityService) {
    this.securityService = securityService;

    Tabs tabs = getTabs();

    UI.getCurrent().navigate(InstitutionsView.class);
    tabs.addSelectedChangeListener(
        e -> UI.getCurrent().navigate(tabToUrlMap.get(e.getSelectedTab())));

    // Logo text
    VerticalLayout header = getVerticalLayoutForHeader(securityService);

    Text space = new Text("       ");
    Text text = new Text("Administration management");

    VerticalLayout spacing = new VerticalLayout(space, text);
    spacing.setSpacing(true);
    spacing.setHeight("50px");
    spacing.setAlignItems(Alignment.CENTER);

    add(header, spacing, tabs);

  }

  private VerticalLayout getVerticalLayoutForHeader(@Autowired SecurityService securityService) {

    H1 logo = new H1("Documentale");
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


  private Tabs getTabs() {

    RouteConfiguration routeConfiguration = RouteConfiguration.forApplicationScope();

    tabToUrlMap.put(new Tab("Institutions"), routeConfiguration.getUrl(InstitutionsView.class));
    tabToUrlMap.put(new Tab("Users"), routeConfiguration.getUrl(UsersView.class));

    //code for rest tabs
    Tabs tabs = new Tabs(tabToUrlMap.keySet().toArray(new Tab[]{}));
    tabs.getStyle().set("margin", "auto");

    return tabs;

  }


}
