package com.spring.documentale.ui.view;


import static com.spring.documentale.constants.ElementsSize.DEFAULT_INDEX_MAX_WIDTH;
import static com.spring.documentale.constants.ElementsSize.DEFAULT_INDEX_MIN_WIDTH;

import com.spring.documentale.config.security.SecurityService;
import com.spring.documentale.constants.Routes;
import com.spring.documentale.model.entity.User;
import com.spring.documentale.model.enums.Role;
import com.spring.documentale.repository.UserRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Route(value = Routes.INDEX)
public class IndexView extends VerticalLayout {

  private final SecurityService securityService;

  private final UserRepository userRepository;

  Button administration = new Button("Administration");
  Button operatorCedacri = new Button("Operator Cedacri");
  Button operatorBank = new Button("Operator bank");


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

      if (user.getUserRoleId().getRole().equals(Role.ADMIN)) {
        setAdminButtons();
        verticalLayout.add(header, administration, operatorCedacri, operatorBank);
      } else if (user.getUserRoleId().getRole().equals(Role.CED_OPERATOR)) {
        setOperatorCedButtons();
        verticalLayout.add(header, administration, operatorCedacri, operatorBank);
      } else {
        setOperatorBankButtons();
        verticalLayout.add(header, administration, operatorCedacri, operatorBank);
      }

    }
    verticalLayout.setAlignItems((Alignment.CENTER));

    getElement().appendChild(verticalLayout.getElement());
  }

  private VerticalLayout getVerticalLayout() {

    H2 logo = new H2("Welcome to Documentale");
    logo.addClassName("welcome");
    VerticalLayout header;
    header = new VerticalLayout(logo);
    header.setSizeFull();
    header.setAlignItems((Alignment.CENTER));

    return header;
  }

  private void setAdminButtons() {
    administration.setWidthFull();
    administration.setMaxWidth(DEFAULT_INDEX_MAX_WIDTH);
    administration.setMinWidth(DEFAULT_INDEX_MIN_WIDTH);

    operatorCedacri.setWidthFull();
    operatorCedacri.setMaxWidth(DEFAULT_INDEX_MAX_WIDTH);
    operatorCedacri.setMinWidth(DEFAULT_INDEX_MIN_WIDTH);

    operatorBank.setWidthFull();
    operatorBank.setMaxWidth(DEFAULT_INDEX_MAX_WIDTH);
    operatorBank.setMinWidth(DEFAULT_INDEX_MIN_WIDTH);

    administration.addClickListener(e ->
        administration.getUI().ifPresent(ui ->
            ui.navigate(Routes.ADMINISTRATION))
    );

    operatorCedacri.addClickListener(e ->
        operatorCedacri.getUI().ifPresent(ui ->
            ui.navigate(Routes.OPERATOR_CEDACRI))
    );

    operatorBank.addClickListener(e ->
        operatorBank.getUI().ifPresent(ui ->
            ui.navigate(Routes.OPERATOR_BANK))
    );

  }

  private void setOperatorCedButtons() {
    administration.setWidthFull();
    administration.setMaxWidth(DEFAULT_INDEX_MAX_WIDTH);
    administration.setMinWidth(DEFAULT_INDEX_MIN_WIDTH);
    administration.setEnabled(false);

    operatorCedacri.setWidthFull();
    operatorCedacri.setMaxWidth(DEFAULT_INDEX_MAX_WIDTH);
    operatorCedacri.setMinWidth(DEFAULT_INDEX_MIN_WIDTH);

    operatorBank.setWidthFull();
    operatorBank.setMaxWidth(DEFAULT_INDEX_MAX_WIDTH);
    operatorBank.setMinWidth(DEFAULT_INDEX_MIN_WIDTH);
    operatorBank.setEnabled(false);

    operatorCedacri.addClickListener(e ->
        operatorCedacri.getUI().ifPresent(ui ->
            ui.navigate(Routes.OPERATOR_CEDACRI))
    );
  }


  private void setOperatorBankButtons() {
    administration.setWidthFull();
    administration.setMaxWidth(DEFAULT_INDEX_MAX_WIDTH);
    administration.setMinWidth(DEFAULT_INDEX_MIN_WIDTH);
    administration.setEnabled(false);

    operatorCedacri.setWidthFull();
    operatorCedacri.setMaxWidth(DEFAULT_INDEX_MAX_WIDTH);
    operatorCedacri.setMinWidth(DEFAULT_INDEX_MIN_WIDTH);
    operatorBank.setEnabled(false);

    operatorBank.setWidthFull();
    operatorBank.setMaxWidth(DEFAULT_INDEX_MAX_WIDTH);
    operatorBank.setMinWidth(DEFAULT_INDEX_MIN_WIDTH);

    operatorBank.addClickListener(e ->
        operatorBank.getUI().ifPresent(ui ->
            ui.navigate(Routes.OPERATOR_BANK))
    );
  }

}
