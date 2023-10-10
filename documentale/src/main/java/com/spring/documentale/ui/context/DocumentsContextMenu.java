package com.spring.documentale.ui.context;

import com.spring.documentale.model.entity.Documents;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.server.StreamResource;
import java.util.concurrent.atomic.AtomicReference;

public class DocumentsContextMenu extends GridContextMenu<Documents> {

  AtomicReference<String> savedPath = new AtomicReference<>("");

  AtomicReference<String> name1 = new AtomicReference<>("");

  public DocumentsContextMenu(Grid<Documents> target) {
    super(target);

    GridMenuItem<Documents> name = addItem("Name",
        e -> e.getItem().ifPresent(documents -> {
        }));
    GridMenuItem<Documents> model = addItem("Project",
        e -> e.getItem().ifPresent(documents -> {
        }));

    add(new Hr());

    GridMenuItem<Documents> download = addItem("Create link ->", e -> e.getItem().ifPresent(documents -> {
      savedPath.set(documents.getSavedPath());

      System.out.println("Saved path  =" + savedPath.toString());
      String[] split = savedPath.get().split("/");
      name1.set(split[split.length - 1]);
      System.out.println(name1);

      StreamResource streamResource = new StreamResource(name1.get(),
          () -> getClass().getResourceAsStream("/" + name1.get()));
      Anchor anchor = new Anchor(streamResource, "Download file");
      anchor.getElement().setAttribute("download", name1.get());
      addItem(anchor);
    }));

    setDynamicContentHandler(documents -> {
      // Do not show context menu when header is clicked
      if (documents == null) {
        return false;
      }
      savedPath.set(documents.getSavedPath());

      name.setText(String.format("Name: %s", documents.getName()));
      model.setText(String.format("Project: %s", documents.getProjectId().toString()));
      return true;
    });
  }
}
