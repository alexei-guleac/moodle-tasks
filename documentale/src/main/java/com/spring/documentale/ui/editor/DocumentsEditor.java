package com.spring.documentale.ui.editor;


import static com.spring.documentale.constants.ElementsSize.DEFAULT_FORM_MAX_WIDTH;
import static com.spring.documentale.constants.ElementsSize.DEFAULT_FORM_MIN_WIDTH;
import static com.spring.documentale.constants.Notifications.DEFAULT_SHOW_TIME;
import static org.apache.commons.lang3.StringUtils.isBlank;

import com.spring.documentale.model.entity.DocumentTypes;
import com.spring.documentale.model.entity.Documents;
import com.spring.documentale.model.entity.Institution;
import com.spring.documentale.model.entity.Project;
import com.spring.documentale.model.entity.User;
import com.spring.documentale.repository.DocumentRepository;
import com.spring.documentale.repository.DocumentTypeRepository;
import com.spring.documentale.repository.InstitutionRepository;
import com.spring.documentale.repository.ProjectRepository;
import com.spring.documentale.repository.UserRepository;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class DocumentsEditor extends VerticalLayout implements KeyNotifier {

  private final DocumentRepository repository;

  private final InstitutionRepository institutionRepository;

  private final UserRepository userRepository;

  private final DocumentTypeRepository documentTypeRepository;

  private final ProjectRepository projectRepository;

  /* Fields to edit properties in User entity */
  TextField name = new TextField("Name");
  TextField savedPath = new TextField("Saved Path");
  TextArea additionalInfo = new TextArea("Additional Info");

  ComboBox<Institution> institutionComboBox = new ComboBox<>("Institution");
  ComboBox<User> userCreatedComboBox = new ComboBox<>("User created");
  ComboBox<DocumentTypes> documentTypesComboBox = new ComboBox<>("Doc type");
  ComboBox<Project> projectComboBox = new ComboBox<>("Project");

  DateTimePicker groupingDate = new DateTimePicker();

  MultiFileMemoryBuffer multiFileMemoryBuffer = new MultiFileMemoryBuffer();
  Upload upload = new Upload(multiFileMemoryBuffer);

  /* Action buttons */
  Button save = new Button("Save", VaadinIcon.CHECK.create());
  Button cancel = new Button("Cancel", VaadinIcon.ESC.create());
  Button delete = new Button("Delete", VaadinIcon.TRASH.create());
  HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

  Binder<Documents> binder = new Binder<>(Documents.class);

  private Documents documents;

  private ChangeHandler changeHandler;

  @Autowired
  public DocumentsEditor(DocumentRepository repository,
      InstitutionRepository institutionRepository,
      UserRepository userRepository,
      DocumentTypeRepository documentTypeRepository,
      ProjectRepository projectRepository) {
    this.repository = repository;
    this.institutionRepository = institutionRepository;
    this.userRepository = userRepository;
    this.documentTypeRepository = documentTypeRepository;
    this.projectRepository = projectRepository;

    setupFields();

    VerticalLayout spacing = new VerticalLayout(name, upload, savedPath, institutionComboBox,
        userCreatedComboBox, documentTypesComboBox, projectComboBox, groupingDate, additionalInfo,
        actions);
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
    saveDialog.setHeader("Save document");
    saveDialog.setText("Do you want to save your changes?");
    saveDialog.setCancelable(true);
    saveDialog.setConfirmText("Save");
    saveDialog.setConfirmButtonTheme("primary success");
    saveDialog.addConfirmListener(e -> save());

    ConfirmDialog deleteDialog = new ConfirmDialog();
    deleteDialog.setHeader("Delete document");
    deleteDialog.setText("Do you want to delete document?");
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
    cancel.addClickListener(e -> editDocument(documents));

  }

  private void setupFields() {

    Button uploadButton = new Button("Upload Doc...");
    uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    upload.setUploadButton(uploadButton);
    upload.setDropAllowed(true);
    upload.setMaxFiles(1);
    int maxFileSizeInBytes = 5 * 1024 * 1024; // 5MB
    upload.setMaxFileSize(maxFileSizeInBytes);
    upload.setWidthFull();
    upload.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    upload.setMinWidth(DEFAULT_FORM_MIN_WIDTH);

    // Disable the upload button after the file is selected
    // Re-enable the upload button after the file is cleared
    upload.getElement().addEventListener("max-files-reached-changed", event -> {
      boolean maxFilesReached = event.getEventData().getBoolean("event.detail.value");
      uploadButton.setEnabled(!maxFilesReached);
    }).addEventData("event.detail.value");
    uploadButton.setEnabled(isBlank(savedPath.getValue()));

    upload.addSucceededListener(event -> {
      // Determine which file was uploaded
      final String fileName = event.getFileName();
      final String pathname = "src/main/resources/documents_data/" + fileName;
      File targetFile = new File(pathname);

      // Get input stream specifically for the finished file
      InputStream multiFileInputStream = multiFileMemoryBuffer.getInputStream(fileName);
      long contentLength = event.getContentLength();
      String mimeType = event.getMIMEType();

      // Do something with the file data
      try {
        java.nio.file.Files.copy(
            multiFileInputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        savedPath.setValue(pathname);
      } catch (IOException e) {
        e.printStackTrace();
      }

      IOUtils.closeQuietly(multiFileInputStream);
    });
    upload.addFileRejectedListener(event -> {
      String errorMessage = event.getErrorMessage();

      Notification notification = Notification.show(
          errorMessage,
          5000,
          Position.BOTTOM_END
      );
      notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    });

    name.setRequired(true);
    name.setWidthFull();
    name.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    name.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    name.setClearButtonVisible(true);

    savedPath.setRequired(true);
    savedPath.setWidthFull();
    savedPath.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    savedPath.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    savedPath.setClearButtonVisible(true);
    savedPath.setReadOnly(true);

    additionalInfo.setRequired(true);
    additionalInfo.setWidthFull();
    additionalInfo.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    additionalInfo.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    additionalInfo.setClearButtonVisible(true);

    institutionComboBox.setWidthFull();
    institutionComboBox.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    institutionComboBox.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    institutionComboBox.setRequired(true);
    institutionComboBox.setItems(institutionRepository.findAll());
    institutionComboBox.setItemLabelGenerator(Institution::getName);
    institutionComboBox.addValueChangeListener(this::setInstitution);

    userCreatedComboBox.setWidthFull();
    userCreatedComboBox.setRequired(true);
    userCreatedComboBox.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    userCreatedComboBox.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    List<User> userList = userRepository.findAll();
    userCreatedComboBox.setItems(userList);
    userCreatedComboBox.setItemLabelGenerator(user -> user.getId().toString());
    userCreatedComboBox.addValueChangeListener(this::setAssignedUser);

    documentTypesComboBox.setWidthFull();
    documentTypesComboBox.setRequired(true);
    documentTypesComboBox.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    documentTypesComboBox.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    List<DocumentTypes> documentTypesList = documentTypeRepository.findAll();
    documentTypesComboBox.setItems(documentTypesList);
    documentTypesComboBox.setItemLabelGenerator(issueTypes -> issueTypes.getName().name());
    documentTypesComboBox.addValueChangeListener(this::setDocumentType);

    projectComboBox.setWidthFull();
    projectComboBox.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    projectComboBox.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    projectComboBox.setRequired(true);
    projectComboBox.setItems(projectRepository.findAll());
    projectComboBox.setItemLabelGenerator(Project::getName);
    projectComboBox.addValueChangeListener(this::setProject);

    groupingDate.setWidthFull();
    groupingDate.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    groupingDate.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    groupingDate.setLabel("Grouped date and time");
    groupingDate.addValueChangeListener(this::setGroupedDate);
  }

  private void setGroupedDate(
      ComponentValueChangeEvent<DateTimePicker, LocalDateTime> changeEvent) {
    this.documents
        .setGroupingDate(changeEvent.getValue());
  }

  private void setInstitution(
      ComponentValueChangeEvent<ComboBox<Institution>, Institution> listener) {
    Institution value = listener.getValue();
    if (value != null) {
      this.documents
          .setInstitutionId(new Institution().withId(value.getId()));
    }
  }

  private void setAssignedUser(
      AbstractField.ComponentValueChangeEvent<ComboBox<User>, User> listener) {
    User value = listener.getValue();
    if (value != null) {
      this.documents.setUserCreatedId(new User().withId(value.getId()));
    }
  }

  private void setDocumentType(
      AbstractField.ComponentValueChangeEvent<ComboBox<DocumentTypes>, DocumentTypes> listener) {
    DocumentTypes value = listener.getValue();
    if (value != null) {
      this.documents.setDocumentTypesId(new DocumentTypes().withId(value.getId()));
    }
  }

  private void setProject(
      ComponentValueChangeEvent<ComboBox<Project>, Project> listener) {
    Project value = listener.getValue();
    if (value != null) {
      this.documents
          .setProjectId(new Project().withId(value.getId()));
    }
  }


  void delete() {
    final Long institutionId = documents.getId();
    repository.delete(documents);

    File fileToDelete = FileUtils.getFile(savedPath.getValue());
    boolean success = FileUtils.deleteQuietly(fileToDelete);
    System.out.println("File " + savedPath.getValue() + " deleted " + success);

    Notification notify = Notification
        .show("Document with id # " + institutionId + " deleted", DEFAULT_SHOW_TIME,
            Position.BOTTOM_END);
    notify.addThemeVariants(NotificationVariant.LUMO_CONTRAST);

    changeHandler.onChange();
  }

  void save() {

    if (documents.getUploadDate() == null) {
      documents.setUploadDate(LocalDateTime.now());
    }

    repository.save(documents);

    Notification notify = Notification
        .show("Document with name " + documents.getName() + " saved", DEFAULT_SHOW_TIME,
            Position.BOTTOM_END);
    notify.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    changeHandler.onChange();
  }


  public final void editDocument(Documents documents) {
    if (documents == null) {
      setVisible(false);
      return;
    }
    final boolean persisted = documents.getId() != null;
    if (persisted) {
      // Find fresh entity for editing
      this.documents = repository.findById(documents.getId()).get();

      institutionComboBox.setValue(new Institution()
          .withId(this.documents.getInstitutionId().getId())
          .withName(this.documents.getInstitutionId().getName()));

      userCreatedComboBox.setValue(new User().withId(this.documents.getUserCreatedId().getId()));

      documentTypesComboBox.setValue(new DocumentTypes()
          .withId(this.documents.getDocumentTypesId().getId())
          .withDocTypeName(this.documents.getDocumentTypesId().getName()));

      projectComboBox.setValue(new Project()
          .withId(this.documents.getProjectId().getId())
          .withName(this.documents.getProjectId().getName()));

    } else {
      this.documents = documents;

      institutionComboBox.setValue(null);
      userCreatedComboBox.setValue(null);
      documentTypesComboBox.setValue(null);
      projectComboBox.setValue(null);
    }
    cancel.setVisible(persisted);
        /* Bind documents properties to similarly named fields
         Could also use annotation or "manual binding" 
         or programmatically
         moving values from fields to entities before saving*/
    binder.setBean(this.documents);

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
