package application.View.DepartmentView;

import java.util.ArrayList;

import application.Control.Department.DepartmentCreationController;
import application.Model.Department;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
 * Class is used to create the GUI for creating a department. View will be managed by
 * the DepartmentCreationController. 
 * 
 * @see DepartmentCreationController
 * @see Department
 */
public class DepartmentCreate {
	private AnchorPane formFrame; // anchorPane containing custom form to be passed to PrimaryView and placed into
	private GridPane gp;
	private Scene scene;
	private Button submit;
	private Button confirm;
	private Button confirmAndAdd;
	private Button back;
	private ArrayList<Button> buttonsGroup;
	private TextField deptName;
	private TextField populationServed;
	private boolean employeeShortcutUsed = false; 
	int i, j;
	
	/**
	 * The constructor immediately creates all buttons associated in view. Immediately following,
	 * the DepartmentCreationController sets the handles.
	 */
	public DepartmentCreate(Scene scene) {
		this.scene = scene;
		this.createButtons();
	}
	
	/**
	 * Method creates and returns the GUI wrapped in an Anchor Pane, to be used as the child
	 * pane within the PrimaryView. 
	 * 
	 * @return AnchorPane
	 */
	public AnchorPane createView() {
		this.formFrame = new AnchorPane();
		this.gp = new GridPane();
		this.deptName = new TextField();
		this.populationServed = new TextField();

		gp.setGridLinesVisible(false);
		gp.setPadding(new Insets(5, 5, 5, 5));
		gp.setStyle("-fx-background-color: rosybrown;");
		gp.setMinSize(600, 600);
		gp.getColumnConstraints().add(new ColumnConstraints(300));
		gp.getColumnConstraints().add(new ColumnConstraints(300));
		gp.setVgap(8);

		this.formFrame.getChildren().add(gp);

		AnchorPane.setBottomAnchor(gp, 0.0);
		AnchorPane.setLeftAnchor(gp, 0.0);
		AnchorPane.setTopAnchor(gp, 0.0);
		AnchorPane.setRightAnchor(gp, 0.0);

		Label lbl[] = { new Label("Department Name")
					  , new Label("Population Served") };

		TextField t[] = { this.deptName
						, this.populationServed };

		i = 0;
		for (Label l : lbl) {
			l.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(20), Insets.EMPTY)));
			l.setPrefSize(250, 30);
			l.setAlignment(Pos.CENTER);
			l.setFont(new Font("Arial", 16));
			l.setBorder((new Border(
					new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2)))));
			gp.add(l, 0, i++);
			gp.getRowConstraints().add(new RowConstraints(35));
		}

		j = 0;
		for (TextField tx : t) {
			gp.add(tx, 1, j++);
			GridPane.setFillWidth(tx, true);
			GridPane.setFillHeight(tx, true);
		}

		gp.add(this.submit, 1, ++j);
		return this.formFrame;
	}
	
	/**
	 * method allows user to confirm their input. After an initial input in the gui has been entered
	 * and the "submit button" selected. The view will change with the TextFields becoming Labels no longer being able to be edited.
	 * User can select to confirm or go back to correct their entry. 
	 */
	public void finalValidation() {
		Label lbl[] = { new Label("Department Name")
					  , new Label("Population Served") };

		Label userData[] = { new Label(this.getName())
						   , new Label(this.getPopulation()) };

		gp.getChildren().clear();
		int i = 0;
		for (Label l : userData) {
			l.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(20), Insets.EMPTY)));
			l.setPrefSize(250, 30);
			l.setAlignment(Pos.CENTER);
			l.setFont(new Font("Arial", 16));
			l.setBorder((new Border(
					new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2)))));
			this.gp.add(l, 1, i++);
			gp.getRowConstraints().add(new RowConstraints(35));
		}

		i = 0;
		for (Label l : lbl) {
			l.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(20), Insets.EMPTY)));
			l.setPrefSize(250, 30);
			l.setAlignment(Pos.CENTER);
			l.setFont(new Font("Arial", 16));
			gp.add(l, 0, i++);
			gp.getRowConstraints().add(new RowConstraints(35));
		}

		this.gp.add(new Label("Is this data correct?"), 1, i++);
		this.gp.add(this.back, 0, i);
		this.gp.add(this.confirm, 1, i);
		this.gp.add(this.confirmAndAdd, 1, ++i);

	}
	
	/**
	 * Method creates all the buttons within the current View and adds them to a list of buttons. 
	 * Additionally, after the creation of the Buttons, their preferences and aesthetics are set. 
	 */
	public void createButtons() {
		this.buttonsGroup = new ArrayList<Button>();
		this.setBackButton("Back");
		this.setSubmitButton("Submit");
		this.setConfirmationButton("Confirm");
		this.setConfirmAndAddButton("Add Another");
		this.setButtonPreferences();
	}
	
	/**
	 * method sets the button preferences, 
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
		this.back.setPrefSize(120, 30);
		this.back.setFont(new Font("Arial", 16));
		this.back.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(20), Insets.EMPTY)));
		this.back.setBorder((new Border(
				new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2)))));
		this.addButtonToList(this.back);
	}

	public void setSubmitButton(String phrase) {
		this.submit = new Button(phrase);
		this.submit.setPrefSize(120, 30);
		this.submit.setFont(new Font("Arial", 16));
		this.submit.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(20), Insets.EMPTY)));
		this.submit.setBorder((new Border(
				new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2)))));
		this.addButtonToList(this.submit);
	}

	public void setConfirmationButton(String phrase) {
		this.confirm = new Button(phrase);
		this.confirm.setPrefSize(120, 30);
		this.confirm.setFont(new Font("Arial", 16));
		this.confirm.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(20), Insets.EMPTY)));
		this.confirm.setBorder((new Border(
				new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2)))));
		this.addButtonToList(this.confirm);
	}
	
	public void setConfirmAndAddButton(String phrase) {
		this.confirmAndAdd = new Button(phrase);
		this.confirmAndAdd.setPrefSize(120, 30);
		this.confirmAndAdd.setFont(new Font("Arial", 16));
		this.confirmAndAdd.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(20), Insets.EMPTY)));
		this.confirmAndAdd.setBorder((new Border(
				new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2)))));
		this.addButtonToList(this.confirmAndAdd);
	}
	
	public String getName() {
		return this.deptName.getText().trim();
	}
	
	public String getPopulation() {
		return this.populationServed.getText().trim();
	}
	
	public Button getBackButton() {
		return this.back;
	}

	public Button getSubmitButton() {
		return this.submit;
	}

	public Button getConfirmationButton() {
		return this.confirm;
	}
	
	public Button getConfirmAndAddButton() {
		return this.confirmAndAdd;
	}
	
	public void addButtonToList(Button b) {
		this.buttonsGroup.add(b);
	}
	
	public void setEmployeeShortcutUsed(Boolean b) {
		this.employeeShortcutUsed = b;
	}
	
	public boolean getEmployeeShortcutUsed() {
		return this.employeeShortcutUsed;
	}
}
