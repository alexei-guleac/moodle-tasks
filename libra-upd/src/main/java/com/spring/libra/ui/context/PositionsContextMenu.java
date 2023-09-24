package com.spring.libra.ui.context;

import com.spring.libra.constants.Routes;
import com.spring.libra.model.entity.Pos;
import com.spring.libra.repository.PosRepository;
import com.spring.libra.ui.editor.PosEditor;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Hr;

public class PositionsContextMenu extends GridContextMenu<Pos> {

  private final PosEditor editor;

  private final PosRepository posRepository;

  public PositionsContextMenu(Grid<Pos> target, PosEditor editor,
      PosRepository posRepository) {
    super(target);
    this.editor = editor;
    this.posRepository = posRepository;

    addItem("Edit", e -> e.getItem().ifPresent(pos -> {
      System.out.printf("Edit: %s%n", pos.getId());
      editor.editPosition(pos);
    }));
    addItem("Delete", e -> e.getItem().ifPresent(pos -> {
      System.out.printf("Delete: %s%n", pos.getId());

      ConfirmDialog deleteDialog = new ConfirmDialog();
      deleteDialog.setHeader("Delete");
      deleteDialog.setText("Do you want to delete entity?");
      deleteDialog.setCancelable(true);
      deleteDialog.setConfirmText("Delete");
      deleteDialog.setConfirmButtonTheme("primary error");
      deleteDialog.addConfirmListener(event -> posRepository.delete(pos));
      deleteDialog.open();
    }));
    addItem("Go to issues", e -> e.getItem().ifPresent(pos -> {
      UI.getCurrent().navigate(Routes.POSITION_ISSUES + "/" + pos.getId());
    }));

    add(new Hr());

    GridMenuItem<Pos> name = addItem("Name",
        e -> e.getItem().ifPresent(pos -> {
        }));
    GridMenuItem<Pos> model = addItem("Model",
        e -> e.getItem().ifPresent(pos -> {
        }));

    setDynamicContentHandler(pos -> {
      // Do not show context menu when header is clicked
      if (pos == null) {
        return false;
      }
      name.setText(String.format("Name: %s", pos.getPosName()));
      model.setText(String.format("Model: %s", pos.getModel()));
      return true;
    });
  }
}
