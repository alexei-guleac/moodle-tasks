package com.example.spring.libra.ui;

import com.example.spring.libra.model.entity.City;
import com.example.spring.libra.repository.CityRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class CityEditor extends VerticalLayout implements KeyNotifier {

  private final CityRepository repository;

  /* Fields to edit properties in User entity */
  TextField cityName = new TextField("Name");

  /* Action buttons */
  Button save = new Button
      ("Save", VaadinIcon.CHECK.create());
  Button cancel = new Button("Cancel", VaadinIcon.ESC.create());
  Button delete = new Button
      ("Delete", VaadinIcon.TRASH.create());
  HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

  Binder<City> binder = new Binder<>(City.class);

  private City city;

  private ChangeHandler changeHandler;

  @Autowired
  public CityEditor(CityRepository repository) {

    this.repository = repository;

    setupFields();

    VerticalLayout spacing = new VerticalLayout(cityName, actions);
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

    addKeyPressListener(Key.ENTER, e -> save());
    // wire action buttons to save, delete and reset
    save.getElement().getThemeList().add("primary");
    save.addClickListener(e -> save());
    delete.getElement().getThemeList().add("error");
    delete.addClickListener(e -> delete());
    cancel.addClickListener(e -> editCity(city));

  }

  private void setupFields() {

    cityName.setRequired(true);
    cityName.setWidthFull();
    cityName.setMaxWidth("350px");
    cityName.setMinWidth("100px");
    cityName.setClearButtonVisible(true);

  }

  void delete() {
    repository.delete(city);
    changeHandler.onChange();
  }

  void save() {
    repository.save(city);
    changeHandler.onChange();
  }

  public final void editCity(City ct) {
    if (ct == null) {
      setVisible(false);
      return;
    }
    final boolean persisted = ct.getId() != null;
    if (persisted) {
      // Find fresh entity for editing
      city = repository.findById(ct.getId()).get();
    } else {
      city = ct;
    }
    cancel.setVisible(persisted);
        /* Bind city properties to similarly named fields
         Could also use annotation or "manual binding" 
         or programmatically
         moving values from fields to entities before saving*/
    binder.setBean(city);

    setVisible(true);

    // Focus first name initially
    cityName.focus();
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
