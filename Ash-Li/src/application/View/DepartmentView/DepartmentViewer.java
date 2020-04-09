package application.View.DepartmentView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import application.Control.Employee.EmployeeViewController;
import application.Model.Department;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DepartmentViewer {
	private Scene scene;
	private AnchorPane formFrame;
	private GridPane gridPane;
	private Button back;
	private Button nameSort;
	private Button abbrSort;
	private Button sizeSort;
	private Button updateButton;
	private Button updateAllButton;
	private ArrayList<CheckBox> checkBoxes;
	private ArrayList<Button> buttonsGroup;
	private ArrayList<TextArea> textAreaList;
	private ArrayList<ListView<String>> listViewList;
	private Label txtDesc;
	private Label empLabel;
	private Label[][] labels;
	private TextField[][] textFields;
	private Node[][] nodes;
	private int i, row, col;
	private int idx;
	private int numUpdates;
	private int gridPaneWidth;
	private int gridPaneHeight;
	private int numDepartments;
	private final int rowHeight = 30;
	private final int colWidth = 110;
	private final int nodeHeight = rowHeight - 3;
	private final int nodeWidth = colWidth;
	private final int fontSize = 12;
	
	/**
	 * Constructor initially creates the buttons for the view and sets the scene variable. 
	 * 
	 * @param scene
	 */
	public DepartmentViewer(Scene scene, int numDepartments) {
		this.initBoxes();
		this.createButtons();
		this.setGridPaneWidth(this.buttonsGroup.size() + 2);
		this.setNumDepartments(numDepartments);
		this.setGridPaneHeight(this.numDepartments + 2);
		this.initNodeKeeper();
		this.scene = scene;
	}
	
	/**
	 * Method creates the panes that will be used as the child pane within the PrimaryView. Additionally, 
	 * method calls another method to populate labels within the grid containing the Employee information. 
	 * 
	 * @return AnchorPane
	 */
	public AnchorPane showEmployeeView() {
		this.formFrame = new AnchorPane();
		this.gridPane = new GridPane();
		
		this.gridPane.setPadding(new Insets(5, 5, 5, 5));
		this.gridPane.setStyle("-fx-background-color: rosybrown;");
		this.gridPane.setMinWidth(700);
		this.gridPane.setMinHeight(700);
		this.gridPane.setVgap(1);
		this.gridPane.setHgap(1);
		
		this.formFrame.getChildren().add(gridPane);
		
		AnchorPane.setBottomAnchor(gridPane, 0.0);
		AnchorPane.setLeftAnchor(gridPane, 0.0);
		AnchorPane.setTopAnchor(gridPane, 0.0);
		AnchorPane.setRightAnchor(gridPane, 0.0);
		
		i = 0;
		this.buttonsGroup.stream()
			 .forEach(button -> {
				 button.setVisible(true);
				 this.nodes[0][i] = button;
				 this.gridPane.add(button, i++, 0);
				 
				 if (button.getText().equalsIgnoreCase("Size")) {
					 this.gridPane.getColumnConstraints().add(
							 new ColumnConstraints() {{ setPercentWidth(10);
							 							setMinWidth(10);
							 							setHalignment(HPos.CENTER); }});
				 } else {
					 this.gridPane.getColumnConstraints().add(
						 new ColumnConstraints() {{ setPercentWidth(20);
						 							setMinWidth(40);
						 							setHalignment(HPos.CENTER); }});
				 }
			 });
		
		this.nodes[0][i] = this.txtDesc;
		this.gridPane.add(this.txtDesc, i++, 0);
		this.gridPane.getColumnConstraints().add(
				 new ColumnConstraints() {{ setPercentWidth(20);
				 							setMinWidth(40);
				 							setHalignment(HPos.CENTER); }});
		
		this.nodes[0][i] = this.empLabel;
		this.gridPane.add(this.empLabel, i++, 0);
		this.gridPane.getColumnConstraints().add(
				 new ColumnConstraints() {{ setPercentWidth(20);
				 							setMinWidth(40);
				 							setHalignment(HPos.CENTER); }});
		
		for (int row = 0; row < this.labels.length + 1; row++) {
			if (row == 0) {
				this.gridPane.getRowConstraints().add(
						new RowConstraints() {{ setPercentHeight(5);
												setFillHeight(true);
							}});
			} else {
				this.gridPane.getRowConstraints().add(
						new RowConstraints() {{ setPercentHeight(10);
												setMinHeight(15);
												setFillHeight(true);
							}});
			}	
		}
		
		
		this.populateData();
		
		this.nodes[this.labels.length+1][0] = this.back;
		this.gridPane.add(this.back, 0, this.labels.length + 1);
		
		return this.formFrame;
	}
	
	/**
	 * method takes data from the Employee View Controller and iterated through the Array Passed. 
	 * Each element with be constructed into a Label, have the Label default set and stored into a label Array. 
	 * 
	 * @param data
	 * @see EmployeeViewController
	 */
	public void populateDataInLabels(String[][] data) {
		for (int i = 0; i < data.length; i++) {
			this.labels[i] = new Label[this.buttonsGroup.size() - 1];
			for (int j = 0; j < data[i].length - 1; j++) {
				labels[i][j] = new Label(data[i][j]);
				this.setLabelDefaults(labels[i][j]);
			}
			TextArea textArea = new TextArea();
			textArea.setBackground(new Background(new BackgroundFill(
					  Color.WHITE
					, new CornerRadii(5)
					, Insets.EMPTY)));
			textArea.setBorder((new Border(new BorderStroke(
					  Color.BLACK
					, BorderStrokeStyle.SOLID
					, new CornerRadii(5)
					, new BorderWidths(2)))));
			textArea.setWrapText(true);
			textArea.setEditable(false);
			textArea.setText(data[i][data[i].length - 1]);
			this.textAreaList.add(textArea);
		}
	}
	
	/**
	 * 
	 */
	public void populateData() {
		for (row = 0; row < labels.length; row++) {
			CheckBox cb = new CheckBox("Update");
			cb.setTooltip(new Tooltip("Selects department to update"));
			this.gridPane.add(cb, 0, row + 1);
			this.nodes[row+1][0] = cb;
			this.checkBoxes.add(cb);
			for (col = 0; col < labels[row].length; col++) {
				this.gridPane.add(this.labels[row][col], col + 1, row + 1);
				this.nodes[row+1][col+1] = this.labels[row][col];
				GridPane.setFillHeight(this.labels[row][col],true);
				GridPane.setFillWidth(this.labels[row][col],true);
			}
			
			this.gridPane.add(this.textAreaList.get(row), col + 1, row + 1);
			this.nodes[row+1][col+1] = this.textAreaList.get(row);
			col++;
			
			this.gridPane.add(this.listViewList.get(row), col + 1, row + 1);
			this.nodes[row+1][col+1] = this.listViewList.get(row);
			
		}
	}
	
	public void removeNodesFromPane() {
		this.gridPane.getChildren().clear();
	}
	
	public void populateNodesIntoPane() {
		for (int i = 0; i < this.nodes.length; i++) {
			for (int j = 0; j < this.nodes[i].length; j++) {
				if (this.nodes[i][j] != null) {
					this.gridPane.add(this.nodes[i][j], j, i);
				}
			}
		}
	}
	
	public ArrayList<String> setUpdateTextFields(ArrayList<Department> deptList) {
		
		ArrayList<String> deptKeys = new ArrayList<String>();
		this.nodes[0][0] = this.updateAllButton;
		idx = 0;
		this.initTextFields(this.checkBoxes.size());
		
		this.checkBoxes.stream().forEach( checkBox -> {
			if (checkBox.isSelected()) {
				numUpdates++;
				// creates new TextFields for the updating of department fields, except the Date Picking field And Department
				this.textFields[idx] = new TextField[this.labels[GridPane.getRowIndex(checkBox) - 1].length - 1];
				
				for (i = 0; i < this.labels[GridPane.getRowIndex(checkBox) - 1].length - 1; i++) {
					
					if (i == 0) {
						deptKeys.add(this.labels[GridPane.getRowIndex(checkBox) - 1][i].getText());
					}
					TextField tf = new TextField();
					tf.setPromptText(this.labels[GridPane.getRowIndex(checkBox) - 1][i].getText());
					this.textFields[idx][i] = tf;
					this.nodes[GridPane.getRowIndex(checkBox)][i + 1] = tf;
				}
				this.labels[GridPane.getRowIndex(checkBox) - 1][i].setDisable(true);
				this.textAreaList.get(GridPane.getRowIndex(checkBox) - 1).setEditable(true);
				idx++;
			} else {
				// disable labels/check boxes for fields not to be updated
				for (i = 0; i < this.labels[GridPane.getRowIndex(checkBox) - 1].length; i++) {
					this.labels[GridPane.getRowIndex(checkBox) - 1][i].setDisable(true);
				}
				this.textAreaList.get(GridPane.getRowIndex(checkBox) - 1).setDisable(true);
				checkBox.setDisable(true);
			}
			
		});
		
		this.removeNodesFromPane();
		this.populateNodesIntoPane();
		return deptKeys;
	}
	
	
	/**
	 * method creates all the buttons for the View and adds them to a Button list. 
	 */
	public void createButtons() {
		this.buttonsGroup = new ArrayList<Button>();
		this.setUpdate("Update Selected");
		this.setNameSort("Name");
		this.setAbbrSort("Abbr.");
		this.setSizeSort("Size");
		this.setBackButton("Back");
		this.setUpdateAll("Update Now");
		this.setTxtDesc("Description");
		this.setEmpLabel("Employees");
		this.setButtonPreferences();
		this.buttonsGroup.remove(this.back);
		this.buttonsGroup.remove(this.updateAllButton);
	}
	
	public void addButtonToList(Button button) {
		this.buttonsGroup.add(button);
	}
	
	/**
	 * Method sets preferences for each button within this objects button list. 
	 * Preferences Currently:
	 * 		Mouse Entering/Exit Button - 
	 * 			Drop Shadow
	 * 			Cursor changes to Open Hand and Default
	 */
	public void setButtonPreferences() {
		this.buttonsGroup.stream().forEach(button -> {
			button.addEventHandler(MouseEvent.MOUSE_ENTERED, (event) -> {
				button.setEffect(new DropShadow());
				this.scene.setCursor(Cursor.OPEN_HAND);
			});
			button.addEventHandler(MouseEvent.MOUSE_EXITED, (event) -> {
				button.setEffect(null);
				this.scene.setCursor(Cursor.DEFAULT);
			});
		});
	}
	
	public void setUpdate(String phrase) {
		this.updateButton = new Button(phrase);
		this.updateButton.setTooltip(new Tooltip("Selecting this after checking off which employees are to be updated,"
				+ "will enable the editing of fields for the employee."));
		this.setButtonAesthetic(this.updateButton, this.nodeWidth, this.nodeHeight, fontSize, Color.BLUE);
		this.addButtonToList(this.updateButton);
	}
	
	public void setUpdateAll(String phrase) {
		this.updateAllButton = new Button(phrase);
		this.updateAllButton.setTooltip(new Tooltip("Selecting this after editing employee fields will record the data"
				+ "and save it to the employees profile."));
		this.setButtonAesthetic(this.updateAllButton, this.nodeWidth, this.nodeHeight, fontSize, Color.GREEN);
		this.addButtonToList(this.updateAllButton); // will be removed from list
	}
	
	public void setBackButton(String phrase) {
		this.back = new Button(phrase);
		this.back.setTooltip(new Tooltip("Go back to main menu."));
		this.setButtonAesthetic(this.back, this.nodeWidth, this.nodeHeight, fontSize, Color.RED);
		this.addButtonToList(this.back); //will be removed from list
	}
	
	public void setNameSort(String phrase) {
		this.nameSort = new Button(phrase);
		this.nameSort.setTooltip(new Tooltip("Sort by Name"));
		this.setButtonAesthetic(this.nameSort, this.nodeWidth, this.nodeHeight, fontSize, Color.BLUE);
		this.addButtonToList(this.nameSort);
	}
	
	public void setAbbrSort(String phrase) {
		this.abbrSort = new Button(phrase);
		this.abbrSort.setTooltip(new Tooltip("Sort by Abbreviation"));
		this.setButtonAesthetic(this.abbrSort, this.nodeWidth, this.nodeHeight, fontSize, Color.BLUE);
		this.addButtonToList(this.abbrSort);
	}
	
	public void setSizeSort(String phrase) {
		this.sizeSort = new Button(phrase);
		this.sizeSort.setTooltip(new Tooltip("Sort by Department Size"));
		this.setButtonAesthetic(this.sizeSort, this.nodeWidth, this.nodeHeight, fontSize, Color.BLUE);
		this.addButtonToList(this.sizeSort);
	}
	
	public void setTxtDesc(String phrase) {
		this.txtDesc = new Label(phrase);
		this.setLabelHeaders(this.txtDesc);
	}
	
	public void setEmpLabel(String phrase) {
		this.empLabel = new Label(phrase);
		this.setLabelHeaders(this.empLabel);
	}
	
	public void populateListViews(ArrayList<Department> depts) {
		depts.stream().forEach(dept -> {
			ListView<String> lv = new ListView<String>();
			lv.setBackground(new Background(new BackgroundFill(
					  Color.WHITE
					, new CornerRadii(5)
					, Insets.EMPTY)));
			lv.setBorder((new Border(new BorderStroke(
					  Color.BLACK
					, BorderStrokeStyle.SOLID
					, new CornerRadii(5)
					, new BorderWidths(2)))));
			dept.getEmployees().stream().forEach(emp -> {
				lv.getItems().add(emp.getFirst_name() + " " + emp.getLast_name());
			});
			this.listViewList.add(lv);
		});
	}
	
	public void setLabelHeaders(Label l) {
		l.setPrefSize(nodeWidth, nodeHeight);
		l.setFont(new Font("Arial", fontSize));
		l.setBackground(new Background(new BackgroundFill(
								  Color.WHITE
								, new CornerRadii(10)
								, Insets.EMPTY)));
		l.setBorder((new Border(new BorderStroke(
								  Color.BLUE
								, BorderStrokeStyle.SOLID
								, new CornerRadii(10)
								, new BorderWidths(2)))));
		l.setAlignment(Pos.CENTER);
		l.setWrapText(true);
	}
	
	public void disableLabelHeaders(boolean b) {
		this.empLabel.setDisable(b);
		this.txtDesc.setDisable(b);
	}
	
	/**
	 * Method sets default button aesthetics. 
	 * 
	 * @param b
	 * @param x - width
	 * @param y - height
	 * @param fontSize
	 * @param color
	 */
	public void setButtonAesthetic(Button b, int x, int y, int fontSize, Color color) {
		b.setPrefSize(x, y);
		b.setFont(new Font("Arial", fontSize));
		b.setBackground(new Background(new BackgroundFill(
								  Color.WHITE
								, new CornerRadii(10)
								, Insets.EMPTY)));
		b.setBorder((new Border(new BorderStroke(
								  color
								, BorderStrokeStyle.SOLID
								, new CornerRadii(10)
								, new BorderWidths(2)))));
		b.setAlignment(Pos.CENTER);
		b.setWrapText(true);
	}
	
	/**
	 * method sets the various default preferences and aesthetics for all labels within the view.
	 * @param l - label
	 */
	public void setLabelDefaults(Label l) {
		l.setBackground(new Background(new BackgroundFill(
								 Color.WHITE
							   , new CornerRadii(10)
							   , Insets.EMPTY)));
		l.setPadding(new Insets(4,4,4,4));
		l.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		l.setAlignment(Pos.CENTER);
		l.setFont(new Font("Arial", 12));
		l.setWrapText(true);
		l.setBorder((new Border(new BorderStroke(
								 Color.BLACK
							   , BorderStrokeStyle.SOLID
							   , new CornerRadii(10)
							   , new BorderWidths(1)))));
	}	
	
	/**
	 * method is to reset all button colors to Blue, after multiple sorting buttons are pressed. 
	 */
	public void neutralizeButtonColors() {
		this.buttonsGroup.forEach( button -> {
			if (!button.getText().equalsIgnoreCase("back")) {
				this.setButtonAesthetic(button, this.nodeWidth, this.nodeHeight, this.fontSize, Color.BLUE);
			}
		});	
	}
	
	public void uncheckBoxes() {
		this.checkBoxes.stream().forEach(checkbox -> {
			checkbox.setSelected(false);
		});
	}
	
	public void clearTextLists() {
		this.textAreaList.clear();
		this.listViewList.clear();
	}
	
	public LocalDate convertToLocalDate(String date) {
		LocalDate ld = null;
		ld = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		return ld;
	}
	
	public String setFormattedDate(LocalDate ld) {
		if (ld != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");
			return formatter.format(ld);
		} else {
			return "99/99/9999";
		}
	}
	
	public void setGridPaneWidth(int n) {
		this.gridPaneWidth = n;
	}
	
	public void setGridPaneHeight(int n) {
		this.gridPaneHeight = n;
	}
	
	// called from controller
	public void initLabels(int n) {
		this.labels = new Label[n][];
	}
	
	public void initNodeKeeper() {
		this.nodes = new Node[this.gridPaneHeight][this.gridPaneWidth];
	}
	public void initTextFields(int n) {
		this.textFields = new TextField[n][];
	}
	
	public void initTextAreaList(int n) {
		this.textAreaList = new ArrayList<TextArea>();
	}
	
	public void initListViewList(int n) {
		this.listViewList = new ArrayList<ListView<String>>();
	}
	
	public void initBoxes() {
		this.checkBoxes = new ArrayList<CheckBox>();
	}
	
	public void setNumDepartments(int numDepartments) {
		this.numDepartments = numDepartments;
	}

	public int getNumDepartments() {
		return this.numDepartments;
	}
	
	public ArrayList<Button> getButtonGroup() {
		return this.buttonsGroup;
	}

	public Button getBackButton() {
		return this.back;
	}
	
	public Button getNameButton() {
		return this.nameSort;
	}
	
	public Button getAbbrButton() {
		return this.abbrSort;
	}
	
	public Button getUpdateButton() {
		return this.updateButton;
	}
	
	public Button getSizeSortButton() {
		return this.sizeSort;
	}
	
	public Button getUpdateAllButton() {
		return this.updateAllButton;
	}
	
	public Label getTxtDesc() {
		return this.txtDesc;
	}
	
	public Label getEmpLabel() {
		return this.empLabel;
	}
	
	public ArrayList<TextArea> getTextAreaList() {
		return this.textAreaList;
	}
	
	public ArrayList<ListView<String>> getListViewList() {
		return this.listViewList;
	}
	
	public Node[][] getNodes() {
		return this.nodes;
	}
	
	public int getNodeHeight() {
		return this.nodeHeight;
	}
	
	public int getNodeWidth() {
		return this.nodeWidth;
	}
	
	public int getFontSize() {
		return this.fontSize;
	}
	
	public int getNumUpdates() {
		return this.numUpdates;
	}
	
	public int getGridPaneHeight() {
		return this.gridPaneHeight;
	}
	
	public int getGridPaneWidth() {
		return this.gridPaneWidth;
	}
	
	public ArrayList<CheckBox> getUpdateBoxSelections() {
		return this.checkBoxes;
	}
	
	public GridPane getGridPane() {
		return this.gridPane;
	}

}


