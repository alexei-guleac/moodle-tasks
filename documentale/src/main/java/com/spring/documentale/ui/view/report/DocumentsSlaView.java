package com.spring.documentale.ui.view.report;


import static com.spring.documentale.constants.ElementsSize.SHORT_GRID_HEIGHT;
import static com.spring.documentale.util.ui.GridUtils.createMenuToggle;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.spring.documentale.config.security.SecurityService;
import com.spring.documentale.constants.Routes;
import com.spring.documentale.model.entity.DocumentTypeIerarchy;
import com.spring.documentale.model.entity.DocumentTypes;
import com.spring.documentale.model.entity.Documents;
import com.spring.documentale.model.enums.DocumentType;
import com.spring.documentale.repository.DocumentRepository;
import com.spring.documentale.repository.DocumentTypeIerarchyRepository;
import com.spring.documentale.repository.DocumentTypeRepository;
import com.spring.documentale.ui.view.parent.BankOperatorView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@Route(value = Routes.DOCUMENTS_SLA, layout = BankOperatorView.class)
public class DocumentsSlaView extends VerticalLayout {

  final Grid<Documents> grid;

  final TextField filterName;

  private final DocumentRepository repo;

  private final DocumentTypeIerarchyRepository documentTypeIerarchyRepository;

  private final DocumentTypeRepository documentTypeRepository;

  private final Button goToIndex;

  private final SecurityService securityService;

  private final Map<Column<?>, String> toggleableColumns = new HashMap<>();


  public DocumentsSlaView(DocumentRepository repo,
      DocumentTypeIerarchyRepository documentTypeIerarchyRepository,
      DocumentTypeRepository documentTypeRepository,
      @Autowired SecurityService securityService) {
    this.documentTypeIerarchyRepository = documentTypeIerarchyRepository;
    this.documentTypeRepository = documentTypeRepository;
    this.securityService = securityService;
    this.repo = repo;
    this.grid = new Grid<>(Documents.class);

    this.filterName = new TextField();

    Button toggleButton = new Button("Toggle theme variant", click -> {
      ThemeList themeList = UI.getCurrent().getElement().getThemeList();
      if (themeList.contains(Lumo.DARK)) {
        themeList.remove(Lumo.DARK);
      } else {
        themeList.add(Lumo.DARK);
      }
    });

    this.goToIndex = new Button("./");
    goToIndex.addClickListener(e ->
        goToIndex.getUI().ifPresent(ui ->
            ui.navigate(Routes.INDEX))
    );

    // build layout
    HorizontalLayout actions = new HorizontalLayout(filterName, goToIndex);

    Accordion accordion = new Accordion();
    final List<DocumentTypes> documentTypeList = getDocumentTypes();
    final List<Documents> documentsList = repo.findByDocumentTypesIdIn(documentTypeList);
    if (documentsList.isEmpty()) {
      accordion.add("No documents", new Text("No data"));
    } else {
      final Set<LocalDate> localDates = new HashSet<>();
      documentsList.forEach(documents ->
          localDates.add(documents.getGroupingDate().toLocalDate()));

      final HashMap<Integer, Set<String>> datesMap = new HashMap<>();
      localDates.forEach(localDate ->
          datesMap.put(localDate.getYear(), new HashSet<>()));

      localDates.forEach(localDate ->
          datesMap.get(localDate.getYear()).add(localDate.getMonth().getDisplayName(
              TextStyle.FULL, Locale.ENGLISH
          )));
      System.out.println(datesMap);

      datesMap.forEach((year, months) ->
      {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        months.forEach(s -> layout.add(new Span(s)));
        accordion.add(String.valueOf(year), layout);
      });
    }
    accordion.setWidth("300px");
    HorizontalLayout gridLayout = new HorizontalLayout(accordion, grid);
    gridLayout.setWidthFull();

    add(toggleButton, actions, gridLayout);

    setupGrid();

    filterName.setPlaceholder("Filter by name");

    // Hook logic to components
        /* Replace listing with filtered content when user
          changes filter*/
    filterName.setValueChangeMode(ValueChangeMode.EAGER);
    filterName.addValueChangeListener
        (e -> listDocuments(e.getValue()));

    // Initialize listing
    listDocuments(null);
  }


  private void setupGrid() {
    grid.setHeight(SHORT_GRID_HEIGHT);
    grid.setWidthFull();
    grid.setColumns("id", "institutionId", "userCreatedId", "documentTypesId", "projectId", "name",
        "savedPath", "uploadDate", "additionalInfo");
    grid.getColumnByKey("id").setAutoWidth(true).setFlexGrow(0).setFrozen(true);
    grid.getColumnByKey("institutionId").setAutoWidth(true).setFlexGrow(0);
    grid.getColumnByKey("userCreatedId").setAutoWidth(true).setFlexGrow(0);
    grid.getColumnByKey("documentTypesId").setAutoWidth(true).setFlexGrow(0);
    grid.getColumnByKey("projectId").setAutoWidth(true).setFlexGrow(0);

    grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

    grid.removeColumnByKey("uploadDate");
    grid.addColumn(
        new LocalDateTimeRenderer<>(Documents::getUploadDate,
            DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)
        )
    ).setHeader("Upload Date").setAutoWidth(true).setFlexGrow(0);
    grid.addColumn(
        new LocalDateTimeRenderer<>(Documents::getGroupingDate,
            DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)
        )
    ).setHeader("Grouping Date").setAutoWidth(true).setFlexGrow(0);

    grid.getColumns()
        .forEach(userColumn -> {
          final String key = userColumn.getKey();
          if (isNotBlank(key)) {
            toggleableColumns.put(userColumn, key);
          }
        });
    Column<Documents> settingColumn = grid.addColumn(box -> "").setWidth("auto").setFlexGrow(0);
    grid.getHeaderRows().get(0).getCell(settingColumn)
        .setComponent(createMenuToggle(toggleableColumns));
  }

  void listDocuments(String filterText) {
    final List<DocumentTypes> documentTypeList = getDocumentTypes();

    if (StringUtils.isEmpty(filterText)) {
      grid.setItems(repo.findByDocumentTypesIdIn(documentTypeList));
    } else {
      grid.setItems(repo.
          findByDocumentTypesIdInAndNameStartsWithIgnoreCase(documentTypeList, filterText));
    }
  }

  private List<DocumentTypes> getDocumentTypes() {

    final List<DocumentTypes> documentTypeList = new ArrayList<>();
    final DocumentTypes eType = documentTypeRepository.findByName(DocumentType.REPORT_SLA).get();
    final List<DocumentTypeIerarchy> documentTypeIerarchyList = documentTypeIerarchyRepository
        .findByIdMacro(eType.getId());
    final List<Long> documentTypeIdList =
        documentTypeIerarchyList.stream().map(DocumentTypeIerarchy::getIdMicro)
            .collect(Collectors.toList());
    documentTypeIdList
        .forEach(aLong -> documentTypeList.add(
            documentTypeRepository.findById(aLong).get()));
    documentTypeList.add(eType);

    System.out.println(documentTypeList);
    return documentTypeList;
  }
}
