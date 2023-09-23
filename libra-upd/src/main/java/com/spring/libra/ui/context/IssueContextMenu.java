package com.spring.libra.ui.context;

import com.spring.libra.model.entity.Issue;
import com.spring.libra.repository.IssueRepository;
import com.spring.libra.ui.editor.IssueEditor;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Hr;

public class IssueContextMenu extends GridContextMenu<Issue> {

  private final IssueEditor editor;

  private final IssueRepository issueRepository;

  public IssueContextMenu(Grid<Issue> target, IssueEditor editor,
      IssueRepository issueRepository) {
    super(target);
    this.editor = editor;
    this.issueRepository = issueRepository;

    addItem("Edit", e -> e.getItem().ifPresent(issue -> {
      System.out.printf("Edit: %s%n", issue.getPriority());
      editor.editIssue(issue);
    }));
    addItem("Delete", e -> e.getItem().ifPresent(issue -> {
      System.out.printf("Delete: %s%n", issue.getId());

      ConfirmDialog deleteDialog = new ConfirmDialog();
      deleteDialog.setHeader("Delete");
      deleteDialog.setText("Do you want to delete entity?");
      deleteDialog.setCancelable(true);
      deleteDialog.setConfirmText("Delete");
      deleteDialog.setConfirmButtonTheme("primary error");
      deleteDialog.addConfirmListener(event -> issueRepository.delete(issue));
      deleteDialog.open();
    }));

    add(new Hr());

    GridMenuItem<Issue> priority = addItem("Priority",
        e -> e.getItem().ifPresent(issue -> {
          // System.out.printf("Email: %s%n", issue.getFullName());
        }));
    GridMenuItem<Issue> position = addItem("Position",
        e -> e.getItem().ifPresent(issue -> {
          // System.out.printf("Phone: %s%n", issue.getFullName());
        }));

    setDynamicContentHandler(issue -> {
      // Do not show context menu when header is clicked
      if (issue == null) {
        return false;
      }
      priority.setText(String.format("Priority: %s", issue.getPriority()));
      position.setText(String.format("Position: %s", issue.getPosId().toString()));
      return true;
    });
  }
}
