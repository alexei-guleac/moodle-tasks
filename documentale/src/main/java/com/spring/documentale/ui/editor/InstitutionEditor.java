package com.spring.documentale.ui.editor;


import static com.spring.documentale.constants.ElementsSize.DEFAULT_FORM_MAX_WIDTH;
import static com.spring.documentale.constants.ElementsSize.DEFAULT_FORM_MIN_WIDTH;
import static com.spring.documentale.constants.Notifications.DEFAULT_SHOW_TIME;

import com.spring.documentale.model.entity.Institution;
import com.spring.documentale.repository.InstitutionRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class InstitutionEditor extends VerticalLayout implements KeyNotifier {

  private final InstitutionRepository repository;

  /* Fields to edit properties in User entity */
  TextField institutionName = new TextField("Name");
  TextField code = new TextField("Code");
  TextField additionalInfo = new TextField("Additional Info");

  /* Action buttons */
  Button save = new Button("Save", VaadinIcon.CHECK.create());
  Button cancel = new Button("Cancel", VaadinIcon.ESC.create());
  Button delete = new Button("Delete", VaadinIcon.TRASH.create());
  HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

  Binder<Institution> binder = new Binder<>(Institution.class);

  private Institution institution;

  private ChangeHandler changeHandler;

  @Autowired
  public InstitutionEditor(InstitutionRepository repository) {
    this.repository = repository;

    setupFields();

    VerticalLayout spacing = new VerticalLayout(institutionName, code, additionalInfo, actions);
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
    saveDialog.setHeader("Save institution");
    saveDialog.setText("Do you want to save your changes?");
    saveDialog.setCancelable(true);
    saveDialog.setConfirmText("Save");
    saveDialog.setConfirmButtonTheme("primary success");
    saveDialog.addConfirmListener(e -> save());

    ConfirmDialog deleteDialog = new ConfirmDialog();
    deleteDialog.setHeader("Delete institution");
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
    cancel.addClickListener(e -> editInstitution(institution));

  }

  private void setupFields() {

    institutionName.setRequired(true);
    institutionName.setWidthFull();
    institutionName.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    institutionName.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    institutionName.setClearButtonVisible(true);

    code.setRequired(true);
    code.setWidthFull();
    code.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    code.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    code.setClearButtonVisible(true);

    additionalInfo.setRequired(true);
    additionalInfo.setWidthFull();
    additionalInfo.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    additionalInfo.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    additionalInfo.setClearButtonVisible(true);
  }

  void delete() {
    final Long institutionId = institution.getId();
    repository.delete(institution);

    Notification notify = Notification
        .show("City with id # " + institutionId + " deleted", DEFAULT_SHOW_TIME,
            Position.BOTTOM_END);
    notify.addThemeVariants(NotificationVariant.LUMO_CONTRAST);

    changeHandler.onChange();
  }

  void save() {
    repository.save(institution);

    Notification notify = Notification
        .show("City with name " + institution.getName() + " saved", DEFAULT_SHOW_TIME,
            Position.BOTTOM_END);
    notify.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    changeHandler.onChange();
  }

  public final void editInstitution(Institution institution) {
    if (institution == null) {
      setVisible(false);
      return;
    }
    final boolean persisted = institution.getId() != null;
    if (persisted) {
      // Find fresh entity for editing
      this.institution = repository.findById(institution.getId()).get();
    } else {
      this.institution = institution;
    }
    cancel.setVisible(persisted);
        /* Bind institution properties to similarly named fields
         Could also use annotation or "manual binding" 
         or programmatically
         moving values from fields to entities before saving*/
    binder.setBean(this.institution);

    setVisible(true);

    // Focus first name initially
    institutionName.focus();
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
