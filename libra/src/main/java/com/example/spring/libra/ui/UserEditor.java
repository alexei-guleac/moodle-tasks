package com.example.spring.libra.ui;

import com.example.spring.libra.model.entity.Pos;
import com.example.spring.libra.model.entity.User;
import com.example.spring.libra.model.entity.UserTypes;
import com.example.spring.libra.repository.UserRepository;
import com.example.spring.libra.repository.UserTypesRepository;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringComponent
@UIScope
public class UserEditor extends VerticalLayout implements KeyNotifier {

  private final PasswordEncoder passwordEncoder;

  private final UserRepository repository;

  private final UserTypesRepository userTypesRepository;

  /* Fields to edit properties in User entity */
  TextField name = new TextField("Name");
  EmailField email = new EmailField("Email");
  TextField login = new TextField("Login");
  TextField password = new TextField("Password");
  TextField telephone = new TextField("Telephone");
  ComboBox<UserTypes> comboBox = new ComboBox<>("UserTypes");

  /* Action buttons */
  Button save = new Button
      ("Save", VaadinIcon.CHECK.create());
  Button cancel = new Button("Cancel", VaadinIcon.ESC.create());
  Button delete = new Button
      ("Delete", VaadinIcon.TRASH.create());
  HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

  Binder<User> binder = new Binder<>(User.class);

  private User user;

  private ChangeHandler changeHandler;

  @Autowired
  public UserEditor(PasswordEncoder passwordEncoder,
      UserRepository repository, UserTypesRepository userTypesRepository) {
    this.passwordEncoder = passwordEncoder;
    this.repository = repository;
    this.userTypesRepository = userTypesRepository;

    setupFields(userTypesRepository);

    VerticalLayout spacing = new VerticalLayout(name, email, login, password, telephone, comboBox,
        actions);
    spacing.setSpacing(true);
    spacing.setAlignItems(Alignment.CENTER);

    add(spacing);

    binder.forField(telephone)
        .withValidator(new RegexpValidator("Only 1-9 allowed","\\d*"))
        .bind(User::getTelephone, User::setTelephone);

    // bind using naming convention
    binder.bindInstanceFields(this);
    // Configure and style components
    setSpacing(true);

    password.addValueChangeListener(this::setPasswordValue);

    addActionButtons();

    setVisible(false);
  }

  private void addActionButtons() {

    addKeyPressListener(Key.ENTER, e -> save());
    // wire action buttons to save, delete and reset
    save.getElement().getThemeList().add("primary");
    save.addClickListener(e -> save());
    delete.getElement().getThemeList().add("error");
    delete.addClickListener(e -> delete());
    cancel.addClickListener(e -> editUser(user));

  }

  private void setupFields(UserTypesRepository userTypesRepository) {
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
    comboBox.setItems(userTypesRepository.findAll());
    comboBox.setItemLabelGenerator(userTypes -> userTypes.getUserType().name());
    comboBox.addValueChangeListener(this::setType);
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

  void delete() {
    repository.delete(user);
    changeHandler.onChange();
  }

  void save() {
    repository.save(user);
    changeHandler.onChange();
  }

  public final void editUser(User usr) {
    if (usr == null) {
      setVisible(false);
      return;
    }
    final boolean persisted = usr.getId() != null;
    if (persisted) {
      // Find fresh entity for editing
      user = repository.findById(usr.getId()).get();

      comboBox.setValue(new UserTypes()
          .withId(user.getUserTypeId().getId())
          .withUserType(user.getUserTypeId().getUserType()));

    } else {
      user = usr;
    }
    cancel.setVisible(persisted);
        /* Bind user properties to similarly named fields
         Could also use annotation or "manual binding" 
         or programmatically
         moving values from fields to entities before saving*/
    binder.setBean(user);

    setVisible(true);

    // Focus first name initially
    name.focus();
  }

  public void setChangeHandler(ChangeHandler h) {
        /* ChangeHandler is notified when either save or delete
         is clicked*/
    changeHandler = h;
  }

  public interface ChangeHandler {
    void onChange();
  }
}
