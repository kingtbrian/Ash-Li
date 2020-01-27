package application.View.EmployeeView;

import java.util.ArrayList;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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


/**
 * This class is the view for the Viewing and Updating of Employees. 
 * 
 * @see EmployeeViewController
 * @see Employee
 *
 */
public class EmployeeViewer {
	private Scene scene;
	private AnchorPane formFrame;
	private GridPane gridPane;
	private Button back;
	private Button nameSort;
	private Button positionSort;
	private Button departmentSort;
	private Button trainingHourSort;
	private Button hireDateSort;
	private ArrayList<Button> buttonsGroup;
	private Label[][] labels;
	private final int rowHeight = 30;
	private final int colWidth = 110;
	private final int nodeHeight = rowHeight - 3;
	private final int nodeWidth = colWidth - 3;
	private final int fontSize = 12;
	int i;
	
	/**
	 * Constructor initially creates the buttons for the view and sets the scene variable. 
	 * 
	 * @param scene
	 */
	public EmployeeViewer(Scene scene) {
		this.createButtons();
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
				 this.gridPane.add(button, i++, 0);
				 this.gridPane.getColumnConstraints().add(
						 new ColumnConstraints() {{ setPercentWidth(20);
						 							setHalignment(HPos.CENTER); }});
			 });
		
		
		for (int row = 0; row < this.labels.length + 1; row++) {
			this.gridPane.getRowConstraints().add(
					new RowConstraints() {{ setPercentHeight(5);
											setFillHeight(true);
											//setVgrow(Priority.ALWAYS); 
						}});
					
		}
		
		this.populateData();
		return this.formFrame;
	}
	
	/**
	 * method populates labels containing Employee information within the View. 
	 */
	public void populateData() {
		for (int row = 0; row < labels.length; row++) {
			for (int col = 0; col < labels[row].length; col++) {
				this.gridPane.add(this.labels[row][col], col, row + 1);
				GridPane.setFillHeight(this.labels[row][col],true);
				GridPane.setFillWidth(this.labels[row][col],true);
			}
		}
	}
	
	/**
	 * method creates all the buttons for the View and adds them to a Button list. 
	 */
	public void createButtons() {
		this.buttonsGroup = new ArrayList<Button>();
		this.setNameSort("Name");
		this.setHireDateSort("Hire Date");
		this.setDepartmentSort("Department");
		this.setPositionSort("Position");
		this.setTrainingHourSort("Training Hours");
		this.setBackButton("Back");
		this.setButtonPreferences();
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
	
	public void setBackButton(String phrase) {
		this.back = new Button(phrase);
		this.setButtonAesthetic(this.back, this.nodeWidth, this.nodeHeight, fontSize, Color.YELLOW);
		this.addButtonToList(this.back);
	}
	
	public void setNameSort(String phrase) {
		this.nameSort = new Button(phrase);
		this.setButtonAesthetic(this.nameSort, this.nodeWidth, this.nodeHeight, fontSize, Color.BLUE);
		this.addButtonToList(this.nameSort);
	}
	
	public void setPositionSort(String phrase) {
		this.positionSort = new Button(phrase);
		this.setButtonAesthetic(this.positionSort, this.nodeWidth, this.nodeHeight, fontSize, Color.BLUE);
		this.addButtonToList(this.positionSort);
	}
	
	public void setDepartmentSort(String phrase) {
		this.departmentSort = new Button(phrase);
		this.setButtonAesthetic(this.departmentSort, this.nodeWidth, this.nodeHeight, fontSize, Color.BLUE);
		this.addButtonToList(this.departmentSort);
	}
	
	public void setTrainingHourSort(String phrase) {
		this.trainingHourSort = new Button(phrase);
		this.setButtonAesthetic(this.trainingHourSort, this.nodeWidth, this.nodeHeight, fontSize, Color.BLUE);
		this.addButtonToList(this.trainingHourSort);
	}
	
	public void setHireDateSort(String phrase) {
		this.hireDateSort = new Button(phrase);
		this.setButtonAesthetic(this.hireDateSort, this.nodeWidth, this.nodeHeight, fontSize, Color.BLUE);
		this.addButtonToList(this.hireDateSort);
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
								, new CornerRadii(20)
								, Insets.EMPTY)));
		b.setBorder((new Border(new BorderStroke(
								  color
								, BorderStrokeStyle.SOLID
								, new CornerRadii(20)
								, new BorderWidths(2)))));
		b.setAlignment(Pos.CENTER);
	}
	
	/**
	 * method sets the various default preferences and aesthics for all labels within the view.
	 * @param l - label
	 */
	public void setLabelDefaults(Label l) {
		l.setBackground(new Background(new BackgroundFill(
								 Color.WHITE
							   , new CornerRadii(5)
							   , Insets.EMPTY)));
		l.setPadding(new Insets(2,2,2,2));
		l.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		l.setAlignment(Pos.CENTER);
		l.setFont(new Font("Arial", 12));
		l.setWrapText(true);
		l.setBorder((new Border(new BorderStroke(
								 Color.BLACK
							   , BorderStrokeStyle.SOLID
							   , new CornerRadii(5)
							   , new BorderWidths(1)))));
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
			this.labels[i] = new Label[5];
			for (int j = 0; j < data[i].length; j++) {
				labels[i][j] = new Label(data[i][j]);
				this.setLabelDefaults(labels[i][j]);
			}
		}
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
	
	public void initLabels(int n) {
		this.labels = new Label[n][];
	}

	public Button getBackButton() {
		return this.back;
	}
	
	public Button getNameButton() {
		return this.nameSort;
	}
	
	public Button getHireDateButton() {
		return this.hireDateSort;
	}
	
	public Button getDepartmentButton() {
		return this.departmentSort;
	}
	
	public Button getPositionButton() {
		return this.positionSort;
	}
	
	public Button getTrainingHoursButton() {
		return this.trainingHourSort;
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
}

