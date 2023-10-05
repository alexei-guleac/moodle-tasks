package com.spring.documentale.ui.editor;


import static com.spring.documentale.constants.ElementsSize.DEFAULT_FORM_MAX_WIDTH;
import static com.spring.documentale.constants.ElementsSize.DEFAULT_FORM_MIN_WIDTH;
import static com.spring.documentale.constants.Notifications.DEFAULT_SHOW_TIME;

import com.spring.documentale.model.entity.Institution;
import com.spring.documentale.model.entity.User;
import com.spring.documentale.model.entity.UserRole;
import com.spring.documentale.repository.InstitutionRepository;
import com.spring.documentale.repository.UserRepository;
import com.spring.documentale.repository.UserRolesRepository;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringComponent
@UIScope
public class UserEditor extends VerticalLayout implements KeyNotifier {

  private final PasswordEncoder passwordEncoder;

  private final UserRepository repository;

  private final UserRolesRepository userTypesRepository;

  private final InstitutionRepository institutionRepository;

  /* Fields to edit properties in User entity */
  TextField name = new TextField("Name");
  EmailField email = new EmailField("Email");
  TextField login = new TextField("Login");
  PasswordField password = new PasswordField("Password");

  TextField surname = new TextField("Surname");
  TextField patronymic = new TextField("Patronymic");

  Checkbox isEnabled = new Checkbox("Is enabled");

  ComboBox<UserRole> userRolesComboBox = new ComboBox<>("User Role");
  ComboBox<Institution> institutionComboBox = new ComboBox<>("Institution");


  /* Action buttons */
  Button save = new Button("Save", VaadinIcon.CHECK.create());
  Button cancel = new Button("Cancel", VaadinIcon.ESC.create());
  Button delete = new Button("Delete", VaadinIcon.TRASH.create());
  HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

  Binder<User> binder = new Binder<>(User.class);

  private User user;

  private ChangeHandler changeHandler;

  @Autowired
  public UserEditor(PasswordEncoder passwordEncoder,
      UserRepository repository, UserRolesRepository userTypesRepository,
      InstitutionRepository institutionRepository) {
    this.passwordEncoder = passwordEncoder;
    this.repository = repository;
    this.userTypesRepository = userTypesRepository;
    this.institutionRepository = institutionRepository;

    setupFields(userTypesRepository, institutionRepository);

    VerticalLayout spacing = new VerticalLayout(name, institutionComboBox, email, login, password,
        surname, patronymic, isEnabled,
        userRolesComboBox,
        actions);
    spacing.setSpacing(true);
    spacing.setAlignItems(Alignment.CENTER);

    add(spacing);

//    binder.forField(telephone)
//        .withValidator(new RegexpValidator("Only 1-9 allowed", "\\d*"))
//        .bind(User::getTelephone, User::setTelephone);

    // bind using naming convention
    binder.bindInstanceFields(this);
    // Configure and style components
    setSpacing(true);

    password.addValueChangeListener(this::setPasswordValue);

    addActionButtons();

    setVisible(false);
  }

  private void addActionButtons() {

    ConfirmDialog saveDialog = new ConfirmDialog();

    saveDialog.setHeader("Save user");

    saveDialog.setText("Do you want to save your changes?");
    saveDialog.setCancelable(true);
    saveDialog.setConfirmText("Save");
    saveDialog.setConfirmButtonTheme("primary success");
    saveDialog.addConfirmListener(e -> save());

    ConfirmDialog deleteDialog = new ConfirmDialog();

    deleteDialog.setHeader("Delete user");

    deleteDialog.setText("Do you want to delete entity?");
    deleteDialog.setCancelable(true);
    deleteDialog.setConfirmText("Delete");
    deleteDialog.setConfirmButtonTheme("primary error");
    deleteDialog.addConfirmListener(e -> delete());

    addKeyPressListener(Key.ENTER, e -> saveDialog.open());
    // wire action buttons to save, delete and reset
    save.getElement().getThemeList().add("primary");
    save.addClickListener(e -> saveDialog.open());
    delete.getElement().getThemeList().add("error");
    delete.addClickListener(e -> deleteDialog.open());
    cancel.addClickListener(e -> editUser(user));

  }

  private void setupFields(UserRolesRepository userTypesRepository,
      InstitutionRepository institutionRepository) {
    name.setRequired(true);
    name.setWidthFull();
    name.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    name.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    name.setClearButtonVisible(true);

    email.setWidthFull();
    email.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    email.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    email.setClearButtonVisible(true);
    email.setPrefixComponent(VaadinIcon.ENVELOPE.create());
    email.setInvalid(true);
    email.setErrorMessage("Enter a valid email address");
    email.setRequiredIndicatorVisible(true);

    login.setRequired(true);
    login.setWidthFull();
    login.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    login.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    login.setClearButtonVisible(true);

    password.setRequired(true);
    password.setWidthFull();
    password.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    password.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    password.setAllowedCharPattern("[A-Za-z0-9]");
    password.setMinLength(6);
    password.setMaxLength(12);
    password.setClearButtonVisible(true);

    surname.setRequired(true);
    surname.setWidthFull();
    surname.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    surname.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    surname.setClearButtonVisible(true);

    patronymic.setRequired(true);
    patronymic.setWidthFull();
    patronymic.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    patronymic.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    patronymic.setClearButtonVisible(true);

    userRolesComboBox.setWidthFull();
    userRolesComboBox.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    userRolesComboBox.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    userRolesComboBox.setRequired(true);
    userRolesComboBox.setItems(userTypesRepository.findAll());
    userRolesComboBox.setItemLabelGenerator(userTypes -> userTypes.getRole().name());
    userRolesComboBox.addValueChangeListener(this::setType);

    institutionComboBox.setWidthFull();
    institutionComboBox.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    institutionComboBox.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    institutionComboBox.setRequired(true);
    institutionComboBox.setItems(institutionRepository.findAll());
    institutionComboBox.setItemLabelGenerator(Institution::getName);
    institutionComboBox.addValueChangeListener(this::setInstitution);
  }

  private void setPasswordValue(ComponentValueChangeEvent<PasswordField, String> listener) {
    System.out.println("ENCODED " + passwordEncoder.encode(listener.getValue()));
    this.user
        .setEncodedPassword(passwordEncoder.encode(listener.getValue()));
  }

  private void setType(
      ComponentValueChangeEvent<ComboBox<UserRole>, UserRole> listener) {
    UserRole value = listener.getValue();
    if (value != null) {
      this.user
          .setUserRoleId(userTypesRepository.findByRole(value.getRole()).get());
    }
  }

  private void setInstitution(
      ComponentValueChangeEvent<ComboBox<Institution>, Institution> listener) {
    Institution value = listener.getValue();
    if (value != null) {
      this.user
          .setInstitutionId(new Institution().withId(value.getId()));
    }
  }

  void delete() {
    final Long userId = user.getId();
    repository.delete(user);

    Notification notify = Notification
        .show("User with id # " + userId + " deleted", DEFAULT_SHOW_TIME,
            Position.BOTTOM_END);
    notify.addThemeVariants(NotificationVariant.LUMO_CONTRAST);

    changeHandler.onChange();
  }

  void save() {
    repository.save(user);

    Notification notify = Notification
        .show("User with name " + user.getName() + " saved", DEFAULT_SHOW_TIME,
            Position.BOTTOM_END);
    notify.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

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

      institutionComboBox.setValue(new Institution()
          .withId(user.getInstitutionId().getId())
          .withName(user.getInstitutionId().getName()));

      userRolesComboBox.setValue(new UserRole()
          .withId(user.getUserRoleId().getId())
          .withRole(user.getUserRoleId().getRole()));

    } else {
      user = usr;

      institutionComboBox.setValue(null);
      userRolesComboBox.setValue(null);
    }
    cancel.setVisible(persisted);
        /* Bind user properties to similarly named fields
         Could also use annotation or "manual binding" 
         or programmatically
         moving values from fields to entities before saving*/
    binder.setBean(user);

    setVisible(true);

    // Focus first name initially
    login.focus();
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
