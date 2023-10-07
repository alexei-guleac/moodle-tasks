package com.spring.documentale.ui.editor;


import static com.spring.documentale.constants.ElementsSize.DEFAULT_FORM_MAX_WIDTH;
import static com.spring.documentale.constants.ElementsSize.DEFAULT_FORM_MIN_WIDTH;
import static com.spring.documentale.constants.Notifications.DEFAULT_SHOW_TIME;

import com.spring.documentale.model.entity.Institution;
import com.spring.documentale.model.entity.Project;
import com.spring.documentale.model.entity.User;
import com.spring.documentale.repository.InstitutionRepository;
import com.spring.documentale.repository.ProjectRepository;
import com.spring.documentale.repository.UserRepository;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class ProjectsEditor extends VerticalLayout implements KeyNotifier {

  private final ProjectRepository repository;

  private final InstitutionRepository institutionRepository;

  private final UserRepository userRepository;

  /* Fields to edit properties in User entity */
  TextField name = new TextField("Name");
  TextArea additionalInfo = new TextArea("Additional Info");

  ComboBox<Institution> institutionComboBox = new ComboBox<>("Institution");
  ComboBox<User> userCreatedComboBox = new ComboBox<>("User created");

  DateTimePicker dateFrom = new DateTimePicker();
  DateTimePicker dateTill = new DateTimePicker();

  Checkbox isActive = new Checkbox("Is active");

  /* Action buttons */
  Button save = new Button("Save", VaadinIcon.CHECK.create());
  Button cancel = new Button("Cancel", VaadinIcon.ESC.create());
  Button delete = new Button("Delete", VaadinIcon.TRASH.create());
  HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

  Binder<Project> binder = new Binder<>(Project.class);

  private Project project;

  private ChangeHandler changeHandler;

  @Autowired
  public ProjectsEditor(ProjectRepository repository,
      InstitutionRepository institutionRepository,
      UserRepository userRepository) {
    this.repository = repository;
    this.institutionRepository = institutionRepository;
    this.userRepository = userRepository;

    setupFields();

    VerticalLayout spacing = new VerticalLayout(name, institutionComboBox, userCreatedComboBox,
        dateFrom, dateTill, isActive, additionalInfo, actions);
    spacing.setSpacing(true);
    spacing.setAlignItems(Alignment.CENTER);

    add(spacing);
    // bind using naming convention
    binder.bindInstanceFields(this);
    // Configure and style components
    setSpacing(true);

    addActionButtons();

    setVisible(false);
  }

  private void addActionButtons() {

    ConfirmDialog saveDialog = new ConfirmDialog();
    saveDialog.setHeader("Save project");
    saveDialog.setText("Do you want to save your changes?");
    saveDialog.setCancelable(true);
    saveDialog.setConfirmText("Save");
    saveDialog.setConfirmButtonTheme("primary success");
    saveDialog.addConfirmListener(e -> save());

    ConfirmDialog deleteDialog = new ConfirmDialog();
    deleteDialog.setHeader("Delete project");
    deleteDialog.setText("Do you want to delete project?");
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
    cancel.addClickListener(e -> editProject(project));

  }

  private void setupFields() {

    name.setRequired(true);
    name.setWidthFull();
    name.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    name.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    name.setClearButtonVisible(true);

    institutionComboBox.setWidthFull();
    institutionComboBox.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    institutionComboBox.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    institutionComboBox.setRequired(true);
    institutionComboBox.setItems(institutionRepository.findAll());
    institutionComboBox.setItemLabelGenerator(Institution::getName);
    institutionComboBox.addValueChangeListener(this::setProject);

    userCreatedComboBox.setWidthFull();
    userCreatedComboBox.setRequired(true);
    userCreatedComboBox.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    userCreatedComboBox.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    List<User> userList = userRepository.findAll();
    userCreatedComboBox.setItems(userList);
    userCreatedComboBox.setItemLabelGenerator(user -> user.getId().toString());
    userCreatedComboBox.addValueChangeListener(this::setAssignedUser);

    additionalInfo.setRequired(true);
    additionalInfo.setWidthFull();
    additionalInfo.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    additionalInfo.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    additionalInfo.setClearButtonVisible(true);

    dateFrom.setWidthFull();
    dateFrom.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    dateFrom.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    dateFrom.setLabel("From date and time");
    dateFrom.addValueChangeListener(this::setFromDate);

    dateTill.setWidthFull();
    dateTill.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    dateTill.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    dateTill.setLabel("Till date and time");
    dateTill.addValueChangeListener(this::setTillDate);
  }

  private void setFromDate(
      ComponentValueChangeEvent<DateTimePicker, LocalDateTime> changeEvent) {
    this.project
        .setDateFrom(changeEvent.getValue());
  }

  private void setTillDate(
      ComponentValueChangeEvent<DateTimePicker, LocalDateTime> changeEvent) {
    this.project
        .setDateTill(changeEvent.getValue());
  }


  private void setProject(
      ComponentValueChangeEvent<ComboBox<Institution>, Institution> listener) {
    Institution value = listener.getValue();
    if (value != null) {
      this.project
          .setInstitutionId(new Institution().withId(value.getId()));
    }
  }

  private void setAssignedUser(
      AbstractField.ComponentValueChangeEvent<ComboBox<User>, User> listener) {
    User value = listener.getValue();
    if (value != null) {
      this.project.setUserCreatedId(new User().withId(value.getId()));
    }
  }

  void delete() {
    final Long institutionId = project.getId();
    repository.delete(project);

    Notification notify = Notification
        .show("Project with id # " + institutionId + " deleted", DEFAULT_SHOW_TIME,
            Position.BOTTOM_END);
    notify.addThemeVariants(NotificationVariant.LUMO_CONTRAST);

    changeHandler.onChange();
  }

  void save() {
    repository.save(project);

    Notification notify = Notification
        .show("Project with name " + project.getName() + " saved", DEFAULT_SHOW_TIME,
            Position.BOTTOM_END);
    notify.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    changeHandler.onChange();
  }

  public final void editProject(Project project) {
    if (project == null) {
      setVisible(false);
      return;
    }
    final boolean persisted = project.getId() != null;
    if (persisted) {
      // Find fresh entity for editing
      this.project = repository.findById(project.getId()).get();

      institutionComboBox.setValue(new Institution()
          .withId(this.project.getInstitutionId().getId())
          .withName(this.project.getInstitutionId().getName()));

      userCreatedComboBox.setValue(new User().withId(this.project.getUserCreatedId().getId()));


    } else {
      this.project = project;

      institutionComboBox.setValue(null);
      userCreatedComboBox.setValue(null);
    }
    cancel.setVisible(persisted);
        /* Bind project properties to similarly named fields
         Could also use annotation or "manual binding" 
         or programmatically
         moving values from fields to entities before saving*/
    binder.setBean(this.project);

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
