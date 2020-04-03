package application.View.EmployeeView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;

//import application.Control.Employee.PrimaryEmployeeController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
import javafx.scene.text.TextAlignment;

public class EmployeeCreate {
	private Scene scene;
	private AnchorPane formFrame; // anchorPane containing custom form to be passed to PrimaryView and placed into
	private GridPane gp;
	private Button submit;
	private Button confirm;
	private Button confirmAndAdd;
	private Button back;
	private Button createDeptShortcut;
	private TextField fName;
	private TextField lName;
	private TextField position;
	private TextField trainingHours;
	private DatePicker dp;
	private ToggleGroup deptToggle;
	private ArrayList<RadioButton> deptButtons;
	private ArrayList<Button> buttonsGroup;
	int i, j;

	public EmployeeCreate(Scene scene) {
		this.scene = scene;
		this.createButtons();
		this.initRadioButtons();
		this.initToggle();
	}

	public AnchorPane createView() {
		this.formFrame = new AnchorPane();
		this.gp = new GridPane();
		this.fName = new TextField();
		this.lName = new TextField();
		this.position = new TextField();
		this.trainingHours = new TextField();
		this.dp = new DatePicker();

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

		Label lbl[] = { new Label("First Name"), new Label("Last Name"), new Label("Position"),
				new Label("Training Hours Complete"), new Label("Hire Date"), new Label("Departments") };

		TextField t[] = { fName, lName, position,trainingHours };

		i = 0;
		for (Label l : lbl) {
			this.setLabelDefaults(l);
			gp.add(l, 0, i++);
			gp.getRowConstraints().add(new RowConstraints(35));
		}

		j = 0;
		for (TextField tx : t) {
			gp.add(tx, 1, j++);
			GridPane.setFillWidth(tx, true);
			GridPane.setFillHeight(tx, true);
		}

		gp.add(dp, 1, j++);
		GridPane.setFillWidth(dp, true);
		GridPane.setFillHeight(dp, true);
		
		if (this.deptButtons.isEmpty()) {
			Label warning = this.makeWarningLabel("There are no current Departments on file. Please create a Department first to add "
					+ "Employees to. Your entered information will be saved and you will return to this page :)");
			gp.add(warning, 0, i++);
			gp.add(this.createDeptShortcut, 1, i++);
		} else {
			this.deptButtons.stream().forEach(rb -> {
				rb.setPrefSize(80, 80);
				rb.setAlignment(Pos.CENTER);
				gp.add(rb, 0, i++);
				gp.getRowConstraints().add(new RowConstraints(20));
			});
			gp.add(this.submit, 1, ++j);
		}	
		return this.formFrame;
	}
	
	public void setSavedInfo(String firstName, String lastName, String position, String trainingHours, LocalDate ld) {
		this.fName.setText(firstName);
		this.lName.setText(lastName);
		this.position.setText(position);
		this.trainingHours.setText(trainingHours);
		this.dp.setValue(ld);
	}
	
	
	public void finalValidation() {
		Label lbl[] = { new Label("First Name"), new Label("Last Name"), new Label("Position"), new Label("Department"),
				new Label("Training Hours Complete"), new Label("Hire Date") };

		Label userData[] = { new Label(this.getFirstName()), new Label(this.getLastName()),
				new Label(this.getPosition()), new Label(this.getDepartment()), new Label(this.getTrainingHours()),
				new Label((this.getDate() == null) ? null : this.getDate().toString()) };

		gp.getChildren().clear();
		int i = 0;
		for (Label l : userData) {
			this.setLabelDefaults(l);
			this.gp.add(l, 1, i++);
			gp.getRowConstraints().add(new RowConstraints(35));
		}

		i = 0;
		for (Label l : lbl) {
			this.setLabelDefaults(l);
			gp.add(l, 0, i++);
			gp.getRowConstraints().add(new RowConstraints(35));
		}

		this.gp.add(new Label("Is this data correct?"), 1, i++);
		this.gp.add(this.back, 0, i);
		this.gp.add(this.confirm, 1, i++);
		this.gp.add(this.confirmAndAdd, 1, i);

	}

	public Boolean dataIsValid() {
		if (this.getTrainingHours() == null || Pattern.matches("^[0-9]*",
				this.getTrainingHours().subSequence(0, this.getTrainingHours().length())) == true) { // may need .length
																										// - 1
			this.finalValidation();
			return true;
		} else {
			trainingHours.clear();
			trainingHours.setPromptText("Invalid Entry: Must input a number!");
		}
		return false;
	}

	public Boolean isDeptSelected() {
		return (this.deptToggle.getSelectedToggle() != null);
	}

	public void setSelectDeptInstruction() {
		Label deptInstruction = this.makeWarningLabel("Must select department to proceed. "
				+ "If department is not shown, please create the department first.");
		gp.add(deptInstruction, 0, i);
	}

	public String getFirstName() {
		return this.fName.getText().trim();
	}

	public String getLastName() {
		return this.lName.getText().trim();
	}

	public String getPosition() {
		return this.position.getText().trim();
	}

	public String getDepartment() {
		return this.deptToggle.getSelectedToggle().getUserData().toString();
	}

	public String getTrainingHours() {
		return this.trainingHours.getText().trim();
	}

	public LocalDate getDate() {
		return this.dp.getValue();
	}

	public void createButtons() {
		this.buttonsGroup = new ArrayList<Button>();
		this.setBackButton("Back");
		this.setSubmitButton("Submit");
		this.setConfirmationButton("Confirm");
		this.setDeptShortcutButton("Create New Department");
		this.setConfirmationAndAddButton("Add Another");
		this.setButtonPreferences();
	}
	
	public void setButtonAesthetic(Button b, int x, int y, int fontSize, Color color) {
		b.setPrefSize(x, y);
		b.setFont(new Font("Arial", fontSize));
		this.back.setBackground(new Background(new BackgroundFill(
								  Color.WHITE
								, new CornerRadii(20)
								, Insets.EMPTY)));
		this.back.setBorder((new Border(new BorderStroke(
								  color
								, BorderStrokeStyle.SOLID
								, new CornerRadii(20)
								, new BorderWidths(2)))));
	}

	public void setBackButton(String phrase) {
		this.back = new Button(phrase);
		this.setButtonAesthetic(this.back, 120, 30, 16, Color.RED);
		this.addButtonToList(this.back);
	}

	public void setSubmitButton(String phrase) {
		this.submit = new Button(phrase);
		this.setButtonAesthetic(this.submit, 120, 30, 16, Color.GREEN);
		this.addButtonToList(this.submit);
	}

	public void setConfirmationButton(String phrase) {
		this.confirm = new Button(phrase);
		this.setButtonAesthetic(this.confirm, 120, 30, 16, Color.GREEN);
		this.addButtonToList(this.confirm);
	}
	
	public void setConfirmationAndAddButton(String phrase) {
		this.confirmAndAdd = new Button(phrase);
		this.setButtonAesthetic(this.confirmAndAdd, 140, 30, 16, Color.GREEN);
		this.addButtonToList(this.confirmAndAdd);
	}
	
	public void setDeptShortcutButton(String phrase) {
		this.createDeptShortcut = new Button(phrase);
		this.setButtonAesthetic(this.createDeptShortcut, 200, 30, 12, Color.GREEN);
		this.addButtonToList(this.createDeptShortcut);
	}
	
	public void setLabelDefaults(Label l) {
		l.setBackground(new Background(new BackgroundFill(
								 Color.WHITE
							   , new CornerRadii(20)
							   , Insets.EMPTY)));
		l.setPrefSize(250, 30);
		l.setPadding(new Insets(3,3,3,3));
		l.setAlignment(Pos.CENTER);
		l.setFont(new Font("Arial", 16));
		l.setBorder((new Border(new BorderStroke(
								 Color.BLACK
							   , BorderStrokeStyle.SOLID
							   , new CornerRadii(20)
							   , new BorderWidths(2)))));
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
	
	public Button getDeptShortcutButton() {
		return this.createDeptShortcut;
	}
	public void initRadioButtons() {
		this.deptButtons = new ArrayList<RadioButton>();
	}

	public ArrayList<RadioButton> getRadioButtons() {
		return this.deptButtons;
	}

	public void initToggle() {
		this.deptToggle = new ToggleGroup();
	}

	public ToggleGroup getRadioToggle() {
		return this.deptToggle;
	}

	public void addButtonToList(Button b) {
		this.buttonsGroup.add(b);
	}

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
	
	
	public Label makeWarningLabel(String message) {
		Label warning = new Label(message);
		warning.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, new CornerRadii(10), Insets.EMPTY)));
		warning.setPrefSize(250, 100);
		warning.setPadding(new Insets(3,3,3,3));
		warning.setAlignment(Pos.CENTER);
		warning.setTextAlignment(TextAlignment.CENTER);
		warning.setFont(new Font("Arial", 14));
		warning.setWrapText(true);
		return warning;
	}
}
