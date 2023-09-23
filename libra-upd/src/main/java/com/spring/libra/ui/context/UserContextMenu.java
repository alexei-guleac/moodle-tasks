package com.spring.libra.ui.context;

import com.spring.libra.model.entity.User;
import com.spring.libra.repository.UserRepository;
import com.spring.libra.ui.editor.UserEditor;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Hr;

public class UserContextMenu extends GridContextMenu<User> {

  private final UserEditor editor;

  private final UserRepository userRepository;

  public UserContextMenu(Grid<User> target, UserEditor editor,
      UserRepository userRepository) {
    super(target);
    this.editor = editor;
    this.userRepository = userRepository;

    addItem("Edit", e -> e.getItem().ifPresent(user -> {
      System.out.printf("Edit: %s%n", user.getEmail());
      editor.editUser(user);
    }));
    addItem("Delete", e -> e.getItem().ifPresent(user -> {
      System.out.printf("Delete: %s%n", user.getId());

      ConfirmDialog deleteDialog = new ConfirmDialog();
      deleteDialog.setHeader("Delete");
      deleteDialog.setText("Do you want to delete entity?");
      deleteDialog.setCancelable(true);
      deleteDialog.setConfirmText("Delete");
      deleteDialog.setConfirmButtonTheme("primary error");
      deleteDialog.addConfirmListener(event -> userRepository.delete(user));
      deleteDialog.open();
    }));

    add(new Hr());

    GridMenuItem<User> email = addItem("Email",
        e -> e.getItem().ifPresent(user -> {
          // System.out.printf("Email: %s%n", user.getFullName());
        }));
    GridMenuItem<User> telephone = addItem("Telephone",
        e -> e.getItem().ifPresent(user -> {
          // System.out.printf("Phone: %s%n", user.getFullName());
        }));

    setDynamicContentHandler(user -> {
      // Do not show context menu when header is clicked
      if (user == null) {
        return false;
      }
      email.setText(String.format("Email: %s", user.getEmail()));
      telephone.setText(String.format("Telephone: %s", user.getTelephone()));
      return true;
    });
  }
}
