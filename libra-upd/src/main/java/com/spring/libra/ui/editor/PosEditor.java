package com.spring.libra.ui.editor;

import static com.spring.libra.constants.ElementsSize.DEFAULT_FORM_MAX_WIDTH;
import static com.spring.libra.constants.ElementsSize.DEFAULT_FORM_MIN_WIDTH;
import static com.spring.libra.constants.Notifications.DEFAULT_SHOW_TIME;

import com.spring.libra.model.entity.City;
import com.spring.libra.model.entity.ConnectionTypes;
import com.spring.libra.model.entity.Pos;
import com.spring.libra.repository.CityRepository;
import com.spring.libra.repository.ConnectionTypesRepository;
import com.spring.libra.repository.PosRepository;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class PosEditor extends VerticalLayout implements KeyNotifier {

  private final PosRepository repository;

  private final ConnectionTypesRepository connectionTypesRepository;

  private final CityRepository cityRepository;


  /* Fields to edit properties in User entity */
  TextField posName = new TextField("Pos Name");
  TextField telephone = new TextField("Telephone");
  TextField cellPhone = new TextField("CellPhone");
  TextField address = new TextField("Address");

  TextField model = new TextField("Model");
  TextField brand = new TextField("Brand");
  TextField daysClosed = new TextField("Days Closed");

  ComboBox<City> cityComboBox = new ComboBox<>("City Id");
  ComboBox<ConnectionTypes> connectionTypesComboBox = new ComboBox<>("Connection Type");

  Checkbox morningOpening = new Checkbox("Morning Opening");
  Checkbox morningClosing = new Checkbox("Morning Closing");
  Checkbox afternoonOpening = new Checkbox("Afternoon Open");
  Checkbox afternoonClosing = new Checkbox("Afternoon Close");

  /* Action buttons */
  Button save = new Button("Save", VaadinIcon.CHECK.create());
  Button cancel = new Button("Cancel", VaadinIcon.ESC.create());
  Button delete = new Button("Delete", VaadinIcon.TRASH.create());
  HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

  Binder<Pos> binder = new Binder<>(Pos.class);

  private Pos pos;

  private ChangeHandler changeHandler;

  @Autowired
  public PosEditor(
      PosRepository repository,
      ConnectionTypesRepository connectionTypesRepository,
      CityRepository cityRepository) {
    this.repository = repository;

    this.connectionTypesRepository = connectionTypesRepository;
    this.cityRepository = cityRepository;

    setupFields(connectionTypesRepository, cityRepository);

    actions.setSpacing(true);
    actions.setHeight("80px");
    actions.setJustifyContentMode(JustifyContentMode.CENTER);
    actions.setAlignItems((Alignment.CENTER));
    actions.setAlignSelf(Alignment.CENTER);

    FormLayout formLayout = new FormLayout(
        new HorizontalLayout(posName, telephone, cellPhone),
        new HorizontalLayout(address, model, brand),
        new HorizontalLayout(cityComboBox, connectionTypesComboBox, daysClosed),
        new HorizontalLayout(morningOpening, morningClosing, afternoonOpening, afternoonClosing),
        actions);

    formLayout.setResponsiveSteps(
        // Use one column by default
        new ResponsiveStep("0", 1),
        // Use two columns, if layout's width exceeds 1200px
        new ResponsiveStep("1200px", 2));
    formLayout.setColspan(actions, 2);

    final VerticalLayout verticalLayout = new VerticalLayout(formLayout);
    verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    verticalLayout.setAlignItems((Alignment.CENTER));
    verticalLayout.setAlignSelf(Alignment.CENTER);
    verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER);
    add(verticalLayout);

    // bind using naming convention
    binder.forField(daysClosed)
        .withConverter(new StringToIntegerConverter("must be integer"))
        .bind(Pos::getDaysClosed, Pos::setDaysClosed);

    binder.forField(telephone)
        .withValidator(new RegexpValidator("Only 1-9 allowed", "\\d*"))
        .bind(Pos::getTelephone, Pos::setTelephone);
    binder.forField(cellPhone)
        .withValidator(new RegexpValidator("Only 1-9 allowed", "\\d*"))
        .bind(Pos::getCellPhone, Pos::setCellPhone);

    binder.bindInstanceFields(this);
    // Configure and style components
    setSpacing(true);

    addActionsForButtons();

    setVisible(false);
  }

  private void addActionsForButtons() {

    ConfirmDialog saveDialog = new ConfirmDialog();

    saveDialog.setHeader("Save position");

    saveDialog.setText("Do you want to save your changes?");
    saveDialog.setCancelable(true);
    saveDialog.setConfirmText("Save");
    saveDialog.setConfirmButtonTheme("primary success");
    saveDialog.addConfirmListener(e -> save());

    ConfirmDialog deleteDialog = new ConfirmDialog();

    deleteDialog.setHeader("Delete pos");

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
    cancel.addClickListener(e -> editPosition(pos));

  }

  private void setupFields(ConnectionTypesRepository connectionTypesRepository,
      CityRepository cityRepository) {
    posName.setRequired(true);
    posName.setWidthFull();
    posName.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    posName.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    posName.setClearButtonVisible(true);

    telephone.setRequired(true);
    telephone.setWidthFull();
    telephone.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    telephone.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    telephone.setClearButtonVisible(true);

    cellPhone.setWidthFull();
    cellPhone.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    cellPhone.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    cellPhone.setClearButtonVisible(true);

    address.setRequired(true);
    address.setWidthFull();
    address.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    address.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    address.setClearButtonVisible(true);

    model.setRequired(true);
    model.setWidthFull();
    model.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    model.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    model.setClearButtonVisible(true);

    brand.setRequired(true);
    brand.setWidthFull();
    brand.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    brand.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    brand.setClearButtonVisible(true);

    daysClosed.setRequired(true);
    daysClosed.setWidthFull();
    daysClosed.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    daysClosed.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    daysClosed.setClearButtonVisible(true);

    cityComboBox.setWidthFull();
    cityComboBox.setRequired(true);
    cityComboBox.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    cityComboBox.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    cityComboBox.setItems(cityRepository.findAll());
    cityComboBox.setItemLabelGenerator(City::getCityName);
    cityComboBox.addValueChangeListener(this::setCity);

    connectionTypesComboBox.setWidthFull();
    connectionTypesComboBox.setRequired(true);
    connectionTypesComboBox.setMaxWidth(DEFAULT_FORM_MAX_WIDTH);
    connectionTypesComboBox.setMinWidth(DEFAULT_FORM_MIN_WIDTH);
    List<ConnectionTypes> connectionTypesList = connectionTypesRepository.findAll();
    connectionTypesComboBox.setItems(connectionTypesList);
    connectionTypesComboBox
        .setItemLabelGenerator(connectionTypes -> connectionTypes.getConnectionType().name());
    connectionTypesComboBox.addValueChangeListener(this::setConnectionType);

  }

  private void setConnectionType(
      ComponentValueChangeEvent<ComboBox<ConnectionTypes>, ConnectionTypes> listener) {
    ConnectionTypes value = listener.getValue();
    if (value != null) {
      this.pos.setConnectionTypeId(new ConnectionTypes().withId(value.getId()));
    }
  }

  private void setCity(
      ComponentValueChangeEvent<ComboBox<City>, City> listener) {
    City value = listener.getValue();
    if (value != null) {
      this.pos.setCityId(new City().withId(value.getId()));
    }
  }

  void delete() {
    final Long posId = pos.getId();
    repository.delete(pos);

    Notification notify = Notification
        .show("Position with id # " + posId + " deleted", DEFAULT_SHOW_TIME,
            Position.BOTTOM_END);
    notify.addThemeVariants(NotificationVariant.LUMO_CONTRAST);

    changeHandler.onChange();
  }

  void save() {

    if (pos.getInsertDate() == null) {
      pos.setInsertDate(LocalDateTime.now());
    }
    System.out.println("FOR SAVE " + pos.toString());

    repository.saveAndFlush(pos);

    Notification notify = Notification
        .show("Position with name " + pos.getPosName() + " saved", DEFAULT_SHOW_TIME,
            Position.BOTTOM_END);
    notify.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    changeHandler.onChange();
  }

  public final void editPosition(Pos position) {
    if (position == null) {
      setVisible(false);
      return;
    }
    final boolean persisted = position.getId() != null;
    if (persisted) {
      // Find fresh entity for editing
      pos = repository.findById(position.getId()).get();

      cityComboBox.setValue(new City()
          .withId(pos.getCityId().getId())
          .withName(pos.getCityId().getCityName()));

      connectionTypesComboBox.setValue(new ConnectionTypes()
          .withId(pos.getConnectionTypeId().getId())
          .withConnectionType(pos.getConnectionTypeId().getConnectionType()));

    } else {
      pos = position;

      cityComboBox.setValue(null);
      connectionTypesComboBox.setValue(null);
    }
    cancel.setVisible(persisted);
        /* Bind user properties to similarly named fields
         Could also use annotation or "manual binding" 
         or programmatically
         moving values from fields to entities before saving*/
    binder.setBean(pos);

    setVisible(true);

    // Focus first name initially
    model.focus();
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
