package com.spring.libra.ui;

import com.spring.libra.model.entity.User;
import com.spring.libra.model.entity.UserTypes;
import com.spring.libra.repository.UserRepository;
import com.spring.libra.repository.UserTypesRepository;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@PermitAll
@AnonymousAllowed
@Route(value = RegistrationView.ROUTE)
public class RegistrationView extends VerticalLayout {

  public static final String ROUTE = "/register";

  private final PasswordEncoder passwordEncoder;

  private final UserRepository repository;

  private final UserTypesRepository userTypesRepository;

  private final User user = new User(null, "", "", "", "", "", "", new UserTypes());

  Binder<User> binder = new Binder<>(User.class);
  TextField name = new TextField("Name");
  EmailField email = new EmailField("Email");
  TextField login = new TextField("Login");
  TextField password = new TextField("Password");
  TextField telephone = new TextField("Telephone");
  ComboBox<UserTypes> comboBox = new ComboBox<>("UserTypes");
  Button reg = new Button
      ("Register", VaadinIcon.CHECK_CIRCLE.create());

  @Autowired
  public RegistrationView(
      PasswordEncoder passwordEncoder,
      UserRepository repository,
      UserTypesRepository userTypesRepository) {
    this.passwordEncoder = passwordEncoder;
    this.repository = repository;
    this.userTypesRepository = userTypesRepository;

    H2 logo = new H2("Add new user");
    logo.addClassName("welcome");

    setupFields();

    VerticalLayout verticalLayout = getVerticalLayoutForFileds(logo);

    setupParentLayout(verticalLayout);

    binder.bindInstanceFields(this);
    binder.setBean(user);
    // Configure and style components
    setSpacing(true);

    addActionsForButtons();

    getElement().appendChild(verticalLayout.getElement());
  }

  private void addActionsForButtons() {

    name.addKeyPressListener(Key.ENTER, e -> save());
    telephone.addKeyPressListener(Key.ENTER, e -> save());
    email.addKeyPressListener(Key.ENTER, e -> save());
    password.addValueChangeListener(this::setPasswordValue);

    reg.addClickListener(e -> save());
  }

  private VerticalLayout getVerticalLayoutForFileds(H2 logo) {

    VerticalLayout verticalLayout = new VerticalLayout(logo, name, email, login, password,
        telephone, comboBox, reg);
    verticalLayout.setSizeFull();
    verticalLayout.setSpacing(true);
    verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    verticalLayout.setAlignItems((Alignment.CENTER));
    verticalLayout.setAlignSelf(Alignment.CENTER);
    verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER);

    return verticalLayout;
  }

  private void setupParentLayout(VerticalLayout verticalLayout) {

    setSizeFull();
    setSpacing(true);
    setJustifyContentMode(JustifyContentMode.CENTER);
    setAlignItems((Alignment.CENTER));
    setAlignSelf(Alignment.CENTER);
    setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    setHorizontalComponentAlignment(Alignment.CENTER);

    add(verticalLayout);
  }

  private void setupFields() {
    name.setRequired(true);
    name.setWidthFull();
    name.setMaxWidth("350px");
    name.setMinWidth("100px");
    name.setClearButtonVisible(true);

    email.setWidthFull();
    email.setMaxWidth("350px");
    email.setMinWidth("100px");
    email.setClearButtonVisible(true);
    email.setPrefixComponent(VaadinIcon.ENVELOPE.create());
    email.setPlaceholder("example@gmail.com");
    email.setInvalid(true);
    email.setErrorMessage("Enter a valid email address");
    email.setRequiredIndicatorVisible(true);

    login.setRequired(true);
    login.setWidthFull();
    login.setMaxWidth("350px");
    login.setMinWidth("100px");
    login.setClearButtonVisible(true);

    password.setRequired(true);
    password.setWidthFull();
    password.setMaxWidth("350px");
    password.setMinWidth("100px");
    password.setClearButtonVisible(true);

    telephone.setRequired(true);
    telephone.setWidthFull();
    telephone.setMaxWidth("350px");
    telephone.setMinWidth("100px");
    telephone.setClearButtonVisible(true);

    comboBox.setWidthFull();
    comboBox.setMaxWidth("350px");
    comboBox.setMinWidth("100px");
    comboBox.setRequired(true);
    comboBox.setItems(this.userTypesRepository.findAll());
    comboBox.setItemLabelGenerator(userTypes -> userTypes.getUserType().name());
    comboBox.addValueChangeListener(this::setType);

    reg.setWidthFull();
    reg.setMaxWidth("250px");
    reg.setMinWidth("100px");
  }

  void save() {
    repository.save(user);
  }

  private void setPasswordValue(ComponentValueChangeEvent<TextField, String> listener) {
    System.out.println("ENCODED " + passwordEncoder.encode(listener.getValue()));
    this.user
        .setEncodedPassword(passwordEncoder.encode(listener.getValue()));
  }

  private void setType(
      AbstractField.ComponentValueChangeEvent<ComboBox<UserTypes>, UserTypes> listener) {
    System.out.println(
        "TYPE " + userTypesRepository.findByUserType(listener.getValue().getUserType()).get());
    this.user
        .setUserTypeId(userTypesRepository.findByUserType(listener.getValue().getUserType()).get());
  }
}
