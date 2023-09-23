package com.spring.libra.ui.editor;

import static com.spring.libra.constants.ElementsSize.DEFAULT_FORM_MAX_WIDTH;
import static com.spring.libra.constants.ElementsSize.DEFAULT_FORM_MIN_WIDTH;
import static com.spring.libra.constants.Notifications.DEFAULT_SHOW_TIME;

import com.spring.libra.config.security.SecurityService;
import com.spring.libra.model.entity.Issue;
import com.spring.libra.model.entity.IssueTypes;
import com.spring.libra.model.entity.Notification;
import com.spring.libra.model.entity.Pos;
import com.spring.libra.model.entity.Statuses;
import com.spring.libra.model.entity.User;
import com.spring.libra.repository.IssueRepository;
import com.spring.libra.repository.IssueTypesRepository;
import com.spring.libra.repository.NotificationRepository;
import com.spring.libra.repository.PosRepository;
import com.spring.libra.repository.StatusesRepository;
import com.spring.libra.repository.UserRepository;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@SpringComponent
@UIScope
public class IssueEditor extends VerticalLayout implements KeyNotifier {

  private final IssueRepository repository;

  private final IssueTypesRepository issueTypesRepository;

  private final PosRepository posRepository;

  private final StatusesRepository statusesRepository;

  private final NotificationRepository notificationRepository;

  private final UserRepository userRepository;

  private final SecurityService securityService;

  /* Fields to edit properties in User entity */
  TextField problemId = new TextField("ProblemId");
  TextField priority = new TextField("Priority");
  TextField memo = new TextField("Memo");
  TextField description = new TextField("Description");

  DateTimePicker assignedDate = new DateTimePicker();

  TextField solution = new TextField("Solution");

  ComboBox<Pos> posIdComboBox = new ComboBox<>("PosId");
  ComboBox<IssueTypes> issueTypesComboBox = new ComboBox<>("IssueTypeName");
  ComboBox<Statuses> statusesComboBox = new ComboBox<>("Statuses");
  ComboBox<User> userAssignedComboBox = new ComboBox<>("UserAssigned");

  /* Action buttons */
  Button save = new Button("Save", VaadinIcon.CHECK.create());
  Button cancel = new Button("Cancel", VaadinIcon.ESC.create());
  Button delete = new Button("Delete", VaadinIcon.TRASH.create());
  HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

  Binder<Issue> binder = new Binder<>(Issue.class);

  private Issue issues;

  private ChangeHandler changeHandler;

  @Autowired
  public IssueEditor(IssueRepository repository, IssueTypesRepository issueTypesRepository,
      PosRepository posRepository,
      StatusesRepository statusesRepository,
      NotificationRepository notificationRepository,
      UserRepository userRepository,
      SecurityService securityService) {
    this.repository = repository;
    this.issueTypesRepository = issueTypesRepository;
    this.posRepository = posRepository;
    this.statusesRepository = statusesRepository;
    this.notificationRepository = notificationRepository;
    this.userRepository = userRepository;
    this.securityService = securityService;

    setupFields(issueTypesRepository, posRepository, statusesRepository, userRepository);

    VerticalLayout spacing = new VerticalLayout(problemId, priority, memo, description,
        assignedDate, solution, posIdComboBox, issueTypesComboBox, statusesComboBox,
        userAssignedComboBox, actions);
    spacing.setSpacing(true);
    spacing.setAlignItems(Alignment.CENTER);

    add(spacing);
    // bind using naming convention
    binder.forField(problemId)
        .withConverter(new StringToIntegerConverter("must be integer"))
        .bind(Issue::getProblemId, Issue::setProblemId);

    binder.bindInstanceFields(this);
    // Configure and style components
    setSpacing(true);

    addActionsForButtons();

    setVisible(false);
  }

  private void addActionsForButtons() {

    ConfirmDialog saveDialog = new ConfirmDialog();

    saveDialog.setHeader("Save issue");

    saveDialog.setText("Do you want to save your changes?");
    saveDialog.setCancelable(true);
    saveDialog.setConfirmText("Save");
    saveDialog.setConfirmButtonTheme("primary success");
    saveDialog.addConfirmListener(e -> save());

    ConfirmDialog deleteDialog = new ConfirmDialog();
    deleteDialog.setHeader("Delete issue");
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
    cancel.addClickListener(e -> editIssue(issues));

  }

  private void setupFields(IssueTypesRepository issueTypesRepository, PosRepository posRepository,
      StatusesRepository statusesRepository, UserRepository userRepository) {
    problemId.setRequired(true);
    problemId.setWidthFull();
    problemId.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    problemId.setMinWidth(DEFAULT_FORM_MIN_WIDTH);

    priority.setRequired(true);
    priority.setWidthFull();
    priority.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    priority.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    priority.setClearButtonVisible(true);

    memo.setWidthFull();
    memo.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    memo.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    memo.setClearButtonVisible(true);

    description.setRequired(true);
    description.setWidthFull();
    description.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    description.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    description.setClearButtonVisible(true);

    assignedDate.setWidthFull();
    assignedDate.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    assignedDate.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    assignedDate.setLabel("Assigned date and time");
    assignedDate.addValueChangeListener(this::setAssignedDate);

    solution.setWidthFull();
    solution.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    solution.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    solution.setClearButtonVisible(true);

    posIdComboBox.setWidthFull();
    posIdComboBox.setRequired(true);
    posIdComboBox.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    posIdComboBox.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    List<Pos> posList = posRepository.findAll();
    posIdComboBox.setItems(posList);
    posIdComboBox.setItemLabelGenerator(pos -> pos.getId().toString());
    posIdComboBox.addValueChangeListener(this::setPos);

    issueTypesComboBox.setWidthFull();
    issueTypesComboBox.setRequired(true);
    issueTypesComboBox.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    issueTypesComboBox.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    List<IssueTypes> issueTypesList = issueTypesRepository.findAll();
    issueTypesComboBox.setItems(issueTypesList);
    issueTypesComboBox.setItemLabelGenerator(issueTypes -> issueTypes.getIssueTypeName().name());
    issueTypesComboBox.addValueChangeListener(this::setIssueType);

    statusesComboBox.setWidthFull();
    statusesComboBox.setRequired(true);
    statusesComboBox.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    statusesComboBox.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    List<Statuses> statusesList = statusesRepository.findAll();
    statusesComboBox.setItems(statusesList);
    statusesComboBox.setItemLabelGenerator(statuses -> statuses.getStatus().name());
    statusesComboBox.addValueChangeListener(this::setStatusType);

    userAssignedComboBox.setWidthFull();
    userAssignedComboBox.setRequired(true);
    userAssignedComboBox.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    userAssignedComboBox.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    List<User> userList = userRepository.findAll();
    userAssignedComboBox.setItems(userList);
    userAssignedComboBox.setItemLabelGenerator(user -> user.getId().toString());
    userAssignedComboBox.addValueChangeListener(this::setAssignedUser);
  }

  private void setAssignedDate(
      ComponentValueChangeEvent<DateTimePicker, LocalDateTime> changeEvent) {
    this.issues
        .setAssignedDate(changeEvent.getValue());
  }

  private void setIssueType(
      AbstractField.ComponentValueChangeEvent<ComboBox<IssueTypes>, IssueTypes> listener) {
    IssueTypes value = listener.getValue();
    if (value != null) {
      this.issues.setIssueTypeId(new IssueTypes().withId(value.getId()));
    }
  }

  private void setPos(
      AbstractField.ComponentValueChangeEvent<ComboBox<Pos>, Pos> listener) {
    Pos value = listener.getValue();
    if (value != null) {
      this.issues.setPosId(new Pos().withId(value.getId()));
    }
  }

  private void setStatusType(
      AbstractField.ComponentValueChangeEvent<ComboBox<Statuses>, Statuses> listener) {
    Statuses value = listener.getValue();
    if (value != null) {
      this.issues.setStatusId(new Statuses().withId(value.getId()));
    }
  }

  private void setAssignedUser(
      AbstractField.ComponentValueChangeEvent<ComboBox<User>, User> listener) {
    User value = listener.getValue();
    if (value != null) {
      this.issues.setAssignedId(new User().withId(value.getId()));
    }
  }

  void delete() {
    repository.delete(issues);

    com.vaadin.flow.component.notification.Notification notify = com.vaadin.flow.component.notification.Notification
        .show("Issue with id # " + issues.getId() + " deleted", DEFAULT_SHOW_TIME,
            Position.BOTTOM_END);
    notify.addThemeVariants(NotificationVariant.LUMO_CONTRAST);

    changeHandler.onChange();
  }

  void save() {

    if (issues.getCreationDate() == null) {
      issues.setCreationDate(LocalDateTime.now());
    }
    issues.setModifDate(LocalDateTime.now());

    System.out.println("FOR SAVE " + issues.toString());

    Notification notification = Notification.builder()
        .priority("normal")
        .assignedId(issues.getAssignedId())
        .description("You have new issue related to position " + issues.getPosId().getId())
        .assignedDate(issues.getAssignedDate())
        .creationDate(issues.getCreationDate())
        .build();

    if (securityService.getAuthenticatedUser() != null) {
      User user = userRepository.findByLogin(securityService.getAuthenticatedUser().getUsername())
          .orElseThrow(() -> new UsernameNotFoundException(
              "User Not Found with username: " + securityService.getAuthenticatedUser()
                  .getUsername()));
      issues.setUserCreatedId(new User().withId(user.getId()));
      notification.setUserCreatedId(new User().withId(user.getId()));
    }

    Issue issue = repository.saveAndFlush(issues);
    notification.setIssueId(issue);
    notificationRepository.saveAndFlush(notification);

    com.vaadin.flow.component.notification.Notification notify = com.vaadin.flow.component.notification.Notification
        .show("Issue with priority " + issues.getPriority() + " saved", DEFAULT_SHOW_TIME,
            Position.BOTTOM_END);
    notify.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    changeHandler.onChange();
  }

  public final void editIssue(Issue issue) {
    if (issue == null) {
      setVisible(false);
      return;
    }
    final boolean persisted = issue.getId() != null;
    if (persisted) {
      // Find fresh entity for editing
      issues = repository.findById(issue.getId()).get();

      posIdComboBox.setValue(new Pos().withId(issues.getPosId().getId()));

      issueTypesComboBox.setValue(new IssueTypes()
          .withId(issues.getIssueTypeId().getId())
          .withIssueTypeName(issues.getIssueTypeId().getIssueTypeName()));
      statusesComboBox.setValue(new Statuses()
          .withId(issues.getStatusId().getId())
          .withStatus(issues.getStatusId().getStatus()));

      userAssignedComboBox.setValue(new User().withId(issues.getAssignedId().getId()));

    } else {
      issues = issue;

      posIdComboBox.setValue(null);
      issueTypesComboBox.setValue(null);
      statusesComboBox.setValue(null);
      userAssignedComboBox.setValue(null);
    }
    cancel.setVisible(persisted);
        /* Bind user properties to similarly named fields
         Could also use annotation or "manual binding" 
         or programmatically
         moving values from fields to entities before saving*/
    binder.setBean(issues);

    setVisible(true);

    // Focus first name initially
    description.focus();
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
