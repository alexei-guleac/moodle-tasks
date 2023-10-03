package com.spring.documentale.util.ui;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import java.util.Map;

public class GridUtils {

  public static MenuBar createMenuToggle(Map<Column<?>, String> toggleableColumns) {
    MenuBar menuBar = new MenuBar();
    menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
    MenuItem menuItem = menuBar.addItem(VaadinIcon.ELLIPSIS_DOTS_V.create());
    SubMenu subMenu = menuItem.getSubMenu();

    toggleableColumns.forEach(
        (column, header) -> {
          Checkbox checkbox = new Checkbox(header);
          checkbox.setValue(column.isVisible());
          checkbox.addValueChangeListener(e -> column.setVisible(e.getValue()));
          subMenu.addItem(checkbox);
        }
    );

    return menuBar;
  }

}
