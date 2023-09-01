package com.example.spring.vaadin.ui;

import com.example.spring.vaadin.entity.Customer;
import com.example.spring.vaadin.repository.CustomerRepository;
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
public class UserEditor extends VerticalLayout implements KeyNotifier {

  private final CustomerRepository repository;

  /* Fields to edit properties in Customer entity */
  TextField firstName = new TextField("First name");
  TextField lastName = new TextField("Last name");
  TextField email = new TextField("Email");

  /* Action buttons */
  Button save = new Button
      ("Save", VaadinIcon.CHECK.create());
  Button cancel = new Button("Cancel");
  Button delete = new Button
      ("Delete", VaadinIcon.TRASH.create());
  HorizontalLayout actions = new HorizontalLayout
      (save, cancel, delete);

  Binder<Customer> binder = new Binder<>(Customer.class);

  private Customer user;

  private ChangeHandler changeHandler;

  @Autowired
  public UserEditor(CustomerRepository repository) {
    this.repository = repository;

    VerticalLayout spacing = new VerticalLayout(firstName, lastName, email, actions);
    spacing.setSpacing(true);
    spacing.setAlignItems(Alignment.CENTER);

    add(spacing);
    // bind using naming convention
    binder.bindInstanceFields(this);
    // Configure and style components
    setSpacing(true);
    save.getElement().getThemeList().add("primary");
    delete.getElement().getThemeList().add("error");
    addKeyPressListener(Key.ENTER, e -> save());
    // wire action buttons to save, delete and reset
    save.addClickListener(e -> save());
    delete.addClickListener(e -> delete());
    cancel.addClickListener(e -> editUser(user));
    setVisible(false);
  }

  void delete() {
    repository.delete(user);
    changeHandler.onChange();
  }

  void save() {
    repository.save(user);
    changeHandler.onChange();
  }

  public final void editUser(Customer usr) {
    if (usr == null) {
      setVisible(false);
      return;
    }
    final boolean persisted = usr.getId() != null;
    if (persisted) {
      // Find fresh entity for editing
      user = repository.findById(usr.getId()).get();
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
    firstName.focus();
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
