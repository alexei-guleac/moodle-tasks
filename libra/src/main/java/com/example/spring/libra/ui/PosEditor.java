package com.example.spring.libra.ui;

import com.example.spring.libra.model.entity.City;
import com.example.spring.libra.model.entity.ConnectionTypes;
import com.example.spring.libra.model.entity.Pos;
import com.example.spring.libra.repository.CityRepository;
import com.example.spring.libra.repository.ConnectionTypesRepository;
import com.example.spring.libra.repository.PosRepository;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
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
  TextField posName = new TextField("PosName");
  TextField telephone = new TextField("Telephone");
  TextField cellPhone = new TextField("CellPhone");
  TextField address = new TextField("Address");

  TextField model = new TextField("Model");
  TextField brand = new TextField("Brand");
  TextField daysClosed = new TextField("DaysClosed");

  ComboBox<City> cityComboBox = new ComboBox<>("CityId");
  ComboBox<ConnectionTypes> connectionTypesComboBox = new ComboBox<>("ConnectionTypeId");

  Checkbox morningOpening = new Checkbox("morningOpening");
  Checkbox morningClosing = new Checkbox("morningClosing");
  Checkbox afternoonOpening = new Checkbox("afternoonOpening");
  Checkbox afternoonClosing = new Checkbox("afternoonClosing");

  /* Action buttons */
  Button save = new Button
      ("Save", VaadinIcon.CHECK.create());
  Button cancel = new Button("Cancel", VaadinIcon.ESC.create());
  Button delete = new Button
      ("Delete", VaadinIcon.TRASH.create());
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

    VerticalLayout spacing = new VerticalLayout(posName, telephone, cellPhone, address,
        model, brand, cityComboBox, connectionTypesComboBox,
        morningOpening, morningClosing, afternoonOpening, afternoonClosing, daysClosed, actions);
    spacing.setSpacing(true);
    spacing.setAlignItems(Alignment.CENTER);

    add(spacing);
    // bind using naming convention
    binder.forField(daysClosed)
        .withConverter(new StringToIntegerConverter("must be integer"))
        .bind(Pos::getDaysClosed, Pos::setDaysClosed);

    binder.forField(telephone)
        .withValidator(new RegexpValidator("Only 1-9 allowed","\\d*"))
        .bind(Pos::getTelephone, Pos::setTelephone);
    binder.forField(cellPhone)
        .withValidator(new RegexpValidator("Only 1-9 allowed","\\d*"))
        .bind(Pos::getCellPhone, Pos::setCellPhone);

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
    cancel.addClickListener(e -> editPosition(pos));

  }

  private void setupFields(ConnectionTypesRepository connectionTypesRepository,
      CityRepository cityRepository) {
    posName.setRequired(true);
    posName.setWidthFull();
    posName.setMaxWidth("350px");
    posName.setMinWidth("100px");
    posName.setClearButtonVisible(true);

    telephone.setRequired(true);
    telephone.setWidthFull();
    telephone.setMaxWidth("350px");
    telephone.setMinWidth("100px");
    telephone.setClearButtonVisible(true);

    cellPhone.setWidthFull();
    cellPhone.setMaxWidth("350px");
    cellPhone.setMinWidth("100px");
    cellPhone.setClearButtonVisible(true);

    address.setRequired(true);
    address.setWidthFull();
    address.setMaxWidth("350px");
    address.setMinWidth("100px");
    address.setClearButtonVisible(true);

    model.setRequired(true);
    model.setWidthFull();
    model.setMaxWidth("350px");
    model.setMinWidth("100px");
    model.setClearButtonVisible(true);

    brand.setRequired(true);
    brand.setWidthFull();
    brand.setMaxWidth("350px");
    brand.setMinWidth("100px");
    brand.setClearButtonVisible(true);

    daysClosed.setRequired(true);
    daysClosed.setWidthFull();
    daysClosed.setMaxWidth("350px");
    daysClosed.setMinWidth("100px");
    daysClosed.setClearButtonVisible(true);

    cityComboBox.setWidthFull();
    cityComboBox.setRequired(true);
    cityComboBox.setMaxWidth("350px");
    cityComboBox.setMinWidth("100px");
    cityComboBox.setItems(cityRepository.findAll());
    cityComboBox.setItemLabelGenerator(City::getCityName);
    cityComboBox.addValueChangeListener(this::setCity);

    connectionTypesComboBox.setWidthFull();
    connectionTypesComboBox.setRequired(true);
    connectionTypesComboBox.setMaxWidth("350px");
    connectionTypesComboBox.setMinWidth("100px");
    List<ConnectionTypes> all1 = connectionTypesRepository.findAll();
    connectionTypesComboBox.setItems(all1);
    connectionTypesComboBox
        .setItemLabelGenerator(connectionTypes -> connectionTypes.getConnectionType().name());
    connectionTypesComboBox.addValueChangeListener(this::setConnectionType);

  }

  private void setConnectionType(
      ComponentValueChangeEvent<ComboBox<ConnectionTypes>, ConnectionTypes> listener) {
    this.pos.setConnectionTypeId(new ConnectionTypes().withId(listener.getValue().getId()));
  }

  private void setCity(
      ComponentValueChangeEvent<ComboBox<City>, City> listener) {
    this.pos.setCityId(new City().withId(listener.getValue().getId()));
  }

  void delete() {
    repository.delete(pos);
    changeHandler.onChange();
  }

  void save() {

    if (pos.getInsertDate() == null) {
      pos.setInsertDate(LocalDateTime.now());
    }
    System.out.println("FOR SAVE " + pos.toString());

    repository.saveAndFlush(pos);

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
