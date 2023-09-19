package com.example.spring.libra.ui;

import com.example.spring.libra.model.entity.Issue;
import com.example.spring.libra.model.entity.IssueTypes;
import com.example.spring.libra.model.entity.Pos;
import com.example.spring.libra.model.entity.Statuses;
import com.example.spring.libra.model.entity.User;
import com.example.spring.libra.repository.IssueRepository;
import com.example.spring.libra.repository.IssueTypesRepository;
import com.example.spring.libra.repository.PosRepository;
import com.example.spring.libra.repository.StatusesRepository;
import com.example.spring.libra.repository.UserRepository;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
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


@SpringComponent
@UIScope
public class IssueEditor extends VerticalLayout implements KeyNotifier {

  private final IssueRepository repository;

  private final IssueTypesRepository issueTypesRepository;

  private final PosRepository posRepository;

  private final StatusesRepository statusesRepository;

  private final UserRepository userRepository;

  /* Fields to edit properties in User entity */
  TextField problemId = new TextField("ProblemId");
  TextField priority = new TextField("Priority");
  TextField memo = new TextField("Memo");
  TextField description = new TextField("Description");

  DateTimePicker assignedDateTimePicker = new DateTimePicker();

  TextField solution = new TextField("Solution");

  ComboBox<Pos> posIdComboBox = new ComboBox<>("PosId");
  ComboBox<IssueTypes> issueTypesComboBox = new ComboBox<>("IssueTypeName");
  ComboBox<Statuses> statusesComboBox = new ComboBox<>("Statuses");
  ComboBox<User> userAssignedComboBox = new ComboBox<>("UserAssigned");

  /* Action buttons */
  Button save = new Button
      ("Save", VaadinIcon.CHECK.create());
  Button cancel = new Button("Cancel", VaadinIcon.ESC.create());
  Button delete = new Button
      ("Delete", VaadinIcon.TRASH.create());
  HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

  Binder<Issue> binder = new Binder<>(Issue.class);

  private Issue issues;

  private ChangeHandler changeHandler;

  @Autowired
  public IssueEditor(IssueRepository repository, IssueTypesRepository issueTypesRepository,
      PosRepository posRepository,
      StatusesRepository statusesRepository,
      UserRepository userRepository) {
    this.repository = repository;
    this.issueTypesRepository = issueTypesRepository;
    this.posRepository = posRepository;
    this.statusesRepository = statusesRepository;
    this.userRepository = userRepository;

    setupFields(issueTypesRepository, posRepository, statusesRepository, userRepository);

    VerticalLayout spacing = new VerticalLayout(problemId, priority, memo, description,
        assignedDateTimePicker, solution, posIdComboBox, issueTypesComboBox, statusesComboBox,
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

    addKeyPressListener(Key.ENTER, e -> save());
    // wire action buttons to save, delete and reset
    save.getElement().getThemeList().add("primary");
    save.addClickListener(e -> save());
    delete.getElement().getThemeList().add("error");
    delete.addClickListener(e -> delete());
    cancel.addClickListener(e -> editIssue(issues));

  }

  private void setupFields(IssueTypesRepository issueTypesRepository, PosRepository posRepository,
      StatusesRepository statusesRepository, UserRepository userRepository) {
    problemId.setRequired(true);
    problemId.setWidthFull();
    problemId.setMaxWidth("350px");
    problemId.setMinWidth("100px");

    priority.setRequired(true);
    priority.setWidthFull();
    priority.setMaxWidth("350px");
    priority.setMinWidth("100px");
    priority.setClearButtonVisible(true);

    memo.setWidthFull();
    memo.setMaxWidth("350px");
    memo.setMinWidth("100px");
    memo.setClearButtonVisible(true);

    description.setRequired(true);
    description.setWidthFull();
    description.setMaxWidth("350px");
    description.setMinWidth("100px");
    description.setClearButtonVisible(true);

    assignedDateTimePicker.setWidthFull();
    assignedDateTimePicker.setMaxWidth("350px");
    assignedDateTimePicker.setMinWidth("100px");
    assignedDateTimePicker.setLabel("Assigned date and time");
    assignedDateTimePicker.addValueChangeListener(this::setAssignedDate);

    solution.setWidthFull();
    solution.setMaxWidth("350px");
    solution.setMinWidth("100px");
    solution.setClearButtonVisible(true);

    posIdComboBox.setWidthFull();
    posIdComboBox.setRequired(true);
    posIdComboBox.setMaxWidth("350px");
    posIdComboBox.setMinWidth("100px");
    List<Pos> all = posRepository.findAll();
    System.out.println("POS " + all);
    posIdComboBox.setItems(all);
    posIdComboBox.setItemLabelGenerator(pos -> pos.getId().toString());
    posIdComboBox.addValueChangeListener(this::setPos);

    issueTypesComboBox.setWidthFull();
    issueTypesComboBox.setRequired(true);
    issueTypesComboBox.setMaxWidth("350px");
    issueTypesComboBox.setMinWidth("100px");
    List<IssueTypes> all1 = issueTypesRepository.findAll();
    System.out.println("issueTypesComboBox " + all1);
    issueTypesComboBox.setItems(all1);
    issueTypesComboBox.setItemLabelGenerator(issueTypes -> issueTypes.getIssueTypeName().name());
    issueTypesComboBox.addValueChangeListener(this::setIssueType);

    statusesComboBox.setWidthFull();
    statusesComboBox.setRequired(true);
    statusesComboBox.setMaxWidth("350px");
    statusesComboBox.setMinWidth("100px");
    List<Statuses> all2 = statusesRepository.findAll();
    System.out.println("all2 " + all2);
    statusesComboBox.setItems(all2);
    statusesComboBox.setItemLabelGenerator(statuses -> statuses.getStatus().name());
    statusesComboBox.addValueChangeListener(this::setStatusType);

    userAssignedComboBox.setWidthFull();
    userAssignedComboBox.setRequired(true);
    userAssignedComboBox.setMaxWidth("350px");
    userAssignedComboBox.setMinWidth("100px");
    List<User> all3 = userRepository.findAll();
    System.out.println("all3 " + all3);
    userAssignedComboBox.setItems(all3);
    userAssignedComboBox.setItemLabelGenerator(user -> user.getId().toString());
    userAssignedComboBox.addValueChangeListener(this::setAssignedUser);
  }

  private void setAssignedDate(
      ComponentValueChangeEvent<DateTimePicker, LocalDateTime> dateTimePickerLocalDateTimeComponentValueChangeEvent) {
    System.out.println(
        "DATE " + dateTimePickerLocalDateTimeComponentValueChangeEvent.getValue());
    this.issues
        .setAssignedDate(dateTimePickerLocalDateTimeComponentValueChangeEvent.getValue());
  }

  private void setIssueType(
      AbstractField.ComponentValueChangeEvent<ComboBox<IssueTypes>, IssueTypes> listener) {
    this.issues.setIssueTypeId(new IssueTypes().withId(listener.getValue().getId()));
  }

  private void setPos(
      AbstractField.ComponentValueChangeEvent<ComboBox<Pos>, Pos> listener) {
    this.issues.setPosId(new Pos().withId(listener.getValue().getId()));
  }

  private void setStatusType(
      AbstractField.ComponentValueChangeEvent<ComboBox<Statuses>, Statuses> listener) {
    this.issues.setStatusId(new Statuses().withId(listener.getValue().getId()));
  }

  private void setAssignedUser(
      AbstractField.ComponentValueChangeEvent<ComboBox<User>, User> listener) {
    this.issues.setAssignedId(new User().withId(listener.getValue().getId()));
  }

  void delete() {
    repository.delete(issues);
    changeHandler.onChange();
  }

  void save() {

    if (issues.getCreationDate() == null) {
      issues.setCreationDate(LocalDateTime.now());
    }
    issues.setModifDate(LocalDateTime.now());

    System.out.println("FOR SAVE " + issues.toString());

    issues.setUserCreatedId(new User().withId(1L));
    repository.saveAndFlush(issues);

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
