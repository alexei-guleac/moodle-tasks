package com.spring.libra.ui.view;

import static com.spring.libra.constants.ElementsSize.DEFAULT_GRID_HEIGHT;
import static com.spring.libra.ui.view.IssuesView.showDetails;

import com.spring.libra.config.security.SecurityService;
import com.spring.libra.constants.Routes;
import com.spring.libra.model.entity.Issue;
import com.spring.libra.repository.IssueRepository;
import com.spring.libra.repository.PosRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.vaadin.klaudeta.PaginatedGrid;

@Route(value = Routes.POSITION_ISSUES + "/:posID")
public class RelatedIssuesView extends VerticalLayout implements BeforeEnterObserver {

  final PaginatedGrid<Issue> grid;

  final TextField filter;

  private final IssueRepository repo;

  private final PosRepository posRepository;

  private final SecurityService securityService;
  private final Button goToPositions;
  private final Button goToIndex;
  private String routeParameter;

  public RelatedIssuesView(IssueRepository repo,
      PosRepository posRepository,
      @Autowired SecurityService securityService) {
    this.posRepository = posRepository;
    this.securityService = securityService;
    this.repo = repo;
    this.grid = new PaginatedGrid<>(Issue.class);

    this.filter = new TextField();

    Button toggleButton = new Button("Toggle theme variant", click -> {
      ThemeList themeList = UI.getCurrent().getElement().getThemeList();
      if (themeList.contains(Lumo.DARK)) {
        themeList.remove(Lumo.DARK);
      } else {
        themeList.add(Lumo.DARK);
      }
    });

    this.goToPositions = new Button("Go To Positions");
    goToPositions.addClickListener(e ->
        goToPositions.getUI().ifPresent(ui ->
            ui.navigate(Routes.POSITIONS))
    );
    this.goToIndex = new Button("./");
    goToIndex.addClickListener(e ->
        goToIndex.getUI().ifPresent(ui ->
            ui.navigate(Routes.INDEX))
    );

    VerticalLayout header = getVerticalLayoutHeader(securityService);

    // build layout
    HorizontalLayout actions = new HorizontalLayout(filter, goToIndex);

    Text space = new Text("       ");
    Text text = new Text("My issues");

    VerticalLayout spacing = new VerticalLayout(space, text);
    spacing.setSpacing(true);
    spacing.setHeight("50px");
    spacing.setAlignItems(Alignment.CENTER);

    add(header, spacing, toggleButton, actions, grid);

    setupGrid();

    filter.setPlaceholder("Filter by description");

    // Hook logic to components
        /* Replace listing with filtered content when user
          changes filter*/
    filter.setValueChangeMode(ValueChangeMode.EAGER);
    filter.addValueChangeListener
        (e -> listUserIssues(e.getValue()));

    grid.asSingleSelect().addValueChangeListener(showDetails());
  }

//  @Override
//  public void setParameter(BeforeEvent event,
//      @WildcardParameter String parameter) {
//    System.out.println("obtainded " + parameter);
//    routeParameter = parameter;
//  }

  private void setupGrid() {
    grid.setHeight(DEFAULT_GRID_HEIGHT);
    grid.setColumns("id", "posId", "issueTypeId", "problemId", "priority", "assignedId",
        "description", "creationDate");
    grid.getColumnByKey("id").setAutoWidth(true).setFlexGrow(0).setFrozen(true);
    grid.getColumnByKey("problemId").setAutoWidth(true).setFlexGrow(0);
    grid.getColumnByKey("priority").setAutoWidth(true).setFlexGrow(0);
    grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

    grid.removeColumnByKey("creationDate");
    grid.addColumn(
        new LocalDateTimeRenderer<>(Issue::getCreationDate,
            DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)
        )
    ).setHeader("Creation Date").setAutoWidth(true).setFlexGrow(0);
    grid.addColumn(
        new LocalDateTimeRenderer<>(Issue::getAssignedDate,
            DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)
        )
    ).setHeader("Assigned Date").setAutoWidth(true).setFlexGrow(0);

    // Sets the max number of items to be rendered on the grid for each page
    grid.setPageSize(10);
    // Sets how many pages should be visible on the pagination before and/or after the current selected page
    grid.setPaginatorSize(5);
  }

  private VerticalLayout getVerticalLayoutHeader(@Autowired SecurityService securityService) {

    H1 logo = new H1("Libra");
    logo.addClassName("logo");
    VerticalLayout header;
    if (securityService.getAuthenticatedUser() != null) {

      ConfirmDialog dialog = new ConfirmDialog();
      final String logout = "Logout";
      dialog.setHeader(logout);
      dialog.setText("Do you want to log out from the system now?");
      dialog.setCancelable(true);

      dialog.setConfirmText(logout);
      dialog.addConfirmListener(event -> securityService.logout());

      Button button = new Button(logout, click ->
          dialog.open());
      VerticalLayout verticalLayout = new VerticalLayout(button);
      verticalLayout.setJustifyContentMode(JustifyContentMode.END);
      verticalLayout.setAlignItems((Alignment.END));
      verticalLayout.setAlignSelf(Alignment.END);
      verticalLayout.setSpacing(false);
      verticalLayout.setPadding(false);
      verticalLayout.setHeight("40px");

      header = new VerticalLayout(verticalLayout, logo);
      header.setSpacing(false);
      header.setPadding(false);
      header.setHeight("20px");
    } else {
      header = new VerticalLayout(logo);
    }
    header.setSizeFull();
    header.setAlignItems((Alignment.CENTER));

    return header;
  }

  void listUserIssues(String filterText) {
    if (routeParameter != null) {
      final Long posId = Long.parseLong(routeParameter);

      if (StringUtils.isEmpty(filterText)) {
        grid.setItems(repo.findByPosId(posRepository.findById(posId).get()));
      } else {
        grid.setItems(repo.
            findByPosIdAndDescriptionStartsWithIgnoreCase(posRepository.findById(posId).get(),
                filterText));
      }
    }
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    routeParameter = event.getRouteParameters().get("posID").get();

    // Initialize listing
    listUserIssues(null);
  }
}
