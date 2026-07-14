package org.targol.resoplan.ui.panels;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.problems.Problem;
import org.targol.resoplan.model.problems.ProblemsRegistry;
import org.targol.resoplan.model.problems.Severity;
import org.targol.resoplan.ui.components.CustomProblemLevelCheckBox;
import org.targol.resoplan.ui.utils.ThemesManager;
import org.targol.resoplan.ui.utils.events.ProblemsUpdatedEvent;
import org.targol.resoplan.ui.utils.events.ThemeChangeEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ProblemsTitledPane extends TitledPane {

	private final CustomProblemLevelCheckBox cboxError = new CustomProblemLevelCheckBox(Severity.ERROR);
	private final CustomProblemLevelCheckBox cboxWarning = new CustomProblemLevelCheckBox(Severity.WARNING);
	private final CustomProblemLevelCheckBox cboxInfo = new CustomProblemLevelCheckBox(Severity.INFO);

	private final CheckBox cboxGeneral = new CheckBox(Messages.getString("ProblemsTitledPane.filter.general.label")); //$NON-NLS-1$
	private final CheckBox cboxProject = new CheckBox(Messages.getString("ProblemsTitledPane.filter.project.label")); //$NON-NLS-1$
	private final CheckBox cboxFloor = new CheckBox(Messages.getString("ProblemsTitledPane.filter.floor.label")); //$NON-NLS-1$
	private final CheckBox cboxLayer = new CheckBox(Messages.getString("ProblemsTitledPane.filter.layer.label")); //$NON-NLS-1$
	private final CheckBox cboxNode = new CheckBox(Messages.getString("ProblemsTitledPane.filter.node.label")); //$NON-NLS-1$

	private final TableView<Problem> tableView = new TableView<>();
	private final ObservableList<Problem> allProblems = FXCollections.observableArrayList();
	private final FilteredList<Problem> filteredProblems = new FilteredList<>(this.allProblems);
	private final SortedList<Problem> sortedProblems = new SortedList<>(this.filteredProblems);

	private final ProblemsRegistry registry = SpringContextHelper.getBean(ProblemsRegistry.class);

	public ProblemsTitledPane() {
		setAnimated(true);
		setExpanded(true);
		this.tableView.setMinHeight(100); // Taille minimale pour forcer le layout
		this.tableView.setPrefHeight(200);
		buildHeader();
		initTableColumns();
		updateProblems();
		this.tableView.setItems(this.sortedProblems);
		this.sortedProblems.comparatorProperty().bind(this.tableView.comparatorProperty());
		setContent(this.tableView);
		setupFilterLogic();
		UiEventBus.register(this, ThemeChangeEvent.THEME_CHANGE, (event) -> {
			Platform.runLater(() -> this.tableView.refresh());
		});
		UiEventBus.register(this, ProblemsUpdatedEvent.DISPLAY_PROBLEMS, (event) -> updateProblems());
	}

	public void updateProblems() {
		if (Platform.isFxApplicationThread()) {
			loadData();
		} else {
			Platform.runLater(this::loadData);
		}
	}

	private void loadData() {
		this.allProblems.clear();
		this.allProblems.addAll(this.registry.getProblems());
	}

	private void buildHeader() {
		final Label titleLabel = new Label(Messages.getString("ProblemsTitledPane.title")); //$NON-NLS-1$
		titleLabel.setStyle("-fx-font-weight: bold;"); //$NON-NLS-1$
		final HBox headerLayout = new HBox(20);

		this.cboxError.setSelected(true);
		this.cboxWarning.setSelected(true);
		this.cboxInfo.setSelected(true);
		final HBox severityGroup = new HBox(10, this.cboxError, this.cboxWarning, this.cboxInfo);
		severityGroup.setOnMouseClicked(Event::consume);

		this.cboxGeneral.setSelected(true);
		this.cboxGeneral.setTooltip(new Tooltip(Messages.getString("ProblemsTitledPane.filter.general.tooltip"))); //$NON-NLS-1$
		this.cboxProject.setSelected(true);
		this.cboxProject.setTooltip(new Tooltip(Messages.getString("ProblemsTitledPane.filter.project.tooltip"))); //$NON-NLS-1$
		this.cboxFloor.setSelected(true);
		this.cboxFloor.setTooltip(new Tooltip(Messages.getString("ProblemsTitledPane.filter.floor.tooltip"))); //$NON-NLS-1$
		this.cboxLayer.setSelected(true);
		this.cboxLayer.setTooltip(new Tooltip(Messages.getString("ProblemsTitledPane.filter.layer.tooltip"))); //$NON-NLS-1$
		this.cboxNode.setSelected(true);
		this.cboxNode.setTooltip(new Tooltip(Messages.getString("ProblemsTitledPane.filter.node.tooltip"))); //$NON-NLS-1$
		final HBox typeGroup = new HBox(10, this.cboxGeneral, this.cboxProject, this.cboxFloor, this.cboxLayer,
				this.cboxNode);
		typeGroup.setOnMouseClicked(Event::consume);

		headerLayout.getChildren().add(titleLabel);
		headerLayout.getChildren().add(severityGroup);
		headerLayout.getChildren().add(typeGroup);

		// Liaison de la largeur pour que l'en-tête s'adapte à la taille du volet
		headerLayout.maxWidthProperty().bind(widthProperty().subtract(50));

		// Application à l'en-tête du TitledPane
		setText(""); //$NON-NLS-1$
		setGraphic(headerLayout);
	}

	private void initTableColumns() {
		// --- 1. Colonne Gravité (Icône) ---
		final TableColumn<Problem, Severity> colSeverity = new TableColumn<>("!"); //$NON-NLS-1$
		colSeverity.setPrefWidth(30);
		colSeverity.setMaxWidth(30);
		colSeverity.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSeverity()));
		colSeverity.setCellFactory(column -> new TableCell<>() {
			@Override
			protected void updateItem(final Severity item, final boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setGraphic(null);
				} else {
					// Ici, tu instancies ton icône perso selon le niveau
					setGraphic(buidSeverityIcon(item));
				}
			}
		});
		// --- 2. Colonne Projet ---
		final TableColumn<Problem, Boolean> colProject = new TableColumn<>(
				Messages.getString("ProblemsTitledPane.filter.project.label")); //$NON-NLS-1$
		colProject.setPrefWidth(100);
		colProject.setMaxWidth(100);
		colProject.setCellValueFactory(cellData -> {
			final Problem p = cellData.getValue();
			final boolean isProjectOnly = p.getProjectId() != null && p.getFloorId() == null;
			return new SimpleBooleanProperty(isProjectOnly);
		});
		setCheckMarkCellFactory(colProject);

		// --- 3. Colonne Étage ---
		final TableColumn<Problem, Boolean> colFloor = new TableColumn<>(
				Messages.getString("ProblemsTitledPane.filter.floor.label")); //$NON-NLS-1$
		colFloor.setPrefWidth(100);
		colFloor.setMaxWidth(100);
		colFloor.setCellValueFactory(cellData -> {
			final Problem p = cellData.getValue();
			final boolean isFloorOnly = p.getFloorId() != null && p.getLayerType() == null;
			return new SimpleBooleanProperty(isFloorOnly);
		});
		setCheckMarkCellFactory(colFloor);

		// --- 4. Colonne Layer ---
		final TableColumn<Problem, Boolean> colLayer = new TableColumn<>(
				Messages.getString("ProblemsTitledPane.filter.layer.label")); //$NON-NLS-1$
		colLayer.setPrefWidth(100);
		colLayer.setMaxWidth(100);
		colLayer.setCellValueFactory(cellData -> {
			final Problem p = cellData.getValue();
			final boolean isLayerOnly = p.getLayerType() != null && p.getNodeId() == null;
			return new SimpleBooleanProperty(isLayerOnly);
		});
		setCheckMarkCellFactory(colLayer);

		// --- 5. Colonne Node ---
		final TableColumn<Problem, Boolean> colNode = new TableColumn<>(
				Messages.getString("ProblemsTitledPane.filter.node.label")); //$NON-NLS-1$
		colNode.setPrefWidth(100);
		colNode.setMaxWidth(100);
		colNode.setCellValueFactory(cellData -> {
			final Problem p = cellData.getValue();
			final boolean isNode = p.getNodeId() != null;
			return new SimpleBooleanProperty(isNode);
		});
		setCheckMarkCellFactory(colNode);

		// --- 6. Colonne Message ---
		final TableColumn<Problem, String> colMessage = new TableColumn<>(Messages.getString("ProblemsTitledPane.message")); //$NON-NLS-1$
		colMessage.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMessage()));
		// Permet à la colonne message de prendre tout l'espace restant
		this.tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
		// Ajout de toutes les colonnes configurées au TableView
		this.tableView.getColumns().addAll(colSeverity, colProject, colFloor, colLayer, colNode, colMessage);
	}

	private ImageView buidSeverityIcon(final Severity severity) {
		if (severity == null) {
			return null;
		}
		final ImageView view = new ImageView(ThemesManager.getInstance().getIcon(severity.getKey()));
		view.setPreserveRatio(true);
		view.fitWidthProperty().set(20);
		return view;
	}

	private void setCheckMarkCellFactory(final TableColumn<Problem, Boolean> column) {
		column.setCellFactory(col -> new TableCell<>() {
			@Override
			protected void updateItem(final Boolean item, final boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null || !item) {
					setGraphic(null);
					setText("");
				} else {
					// Création du caractère de coche ✓ ou d'un Label avec ton icône SVG
					final Label checkMark = new Label("✓");
					checkMark.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
					setGraphic(checkMark);
				}
			}
		});
		// On centre la coche dans la cellule
		column.setStyle("-fx-alignment: CENTER;");
	}

	private void setupFilterLogic() {
		final InvalidationListener filterTrigger = observable -> updateFilter();

		this.cboxError.selectedProperty().addListener(filterTrigger);
		this.cboxWarning.selectedProperty().addListener(filterTrigger);
		this.cboxInfo.selectedProperty().addListener(filterTrigger);

		this.cboxGeneral.selectedProperty().addListener(filterTrigger);
		this.cboxProject.selectedProperty().addListener(filterTrigger);
		this.cboxFloor.selectedProperty().addListener(filterTrigger);
		this.cboxLayer.selectedProperty().addListener(filterTrigger);
		this.cboxNode.selectedProperty().addListener(filterTrigger);
	}

	private void updateFilter() {
		this.filteredProblems.setPredicate(problem -> {
			// --- 1. Filtre par Gravité -> filtre exculsif ---
			if (problem.getSeverity() == Severity.ERROR && !this.cboxError.isSelected()) {
				return false;
			}
			if (problem.getSeverity() == Severity.WARNING && !this.cboxWarning.isSelected()) {
				return false;
			}
			if (problem.getSeverity() == Severity.INFO && !this.cboxInfo.isSelected()) {
				return false;
			}

			// --- 2. Filtre par Cible -> filtre cumulatif ---
			if (problem.getNodeId() != null && !this.cboxNode.isSelected()) {
				return false;
			}
			if (problem.getLayerType() != null && problem.getNodeId() == null && !this.cboxLayer.isSelected()) {
				return false;
			}
			if (problem.getFloorId() != null && problem.getLayerType() == null && !this.cboxFloor.isSelected()) {
				return false;
			}
			if (problem.getProjectId() != null && problem.getFloorId() == null && !this.cboxProject.isSelected()) {
				return false;
			}
			if (problem.isGeneral() && !this.cboxGeneral.isSelected()) {
				return false;
			}
			return true;
		});
	}
}