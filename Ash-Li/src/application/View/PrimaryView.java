package application.View;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * PrimaryView is a the primary view for the program. It is a parent frame composed of 
 * a ScrollFrame wrapped in an AnchorFrame that has a MenuBar. Initially, a default child frame will be shown 
 * consisting of the program logo. 
 * 
 */
public class PrimaryView {
	private Stage primaryStage;
	private Scene primaryScene;
	private ScrollPane scrollFrame;
	private AnchorPane rootPane;
	private AnchorPane secondaryView;
	private MenuBar menuBar;
	private Menu fileMenu;
	private MenuItem fileAbout;
	private Menu employeeMenu;
	private Menu empCreate;
	private MenuItem empCreateEmployee;
	private MenuItem empCreateDepartment;
	private Menu empUpdate;
	private MenuItem empUpdateEmployee;
	private MenuItem empUpdateDepartment;
	private Menu empDelete;
	private MenuItem empDeleteEmployee;
	private MenuItem empDeleteDepartment;
	private Menu empView;
	private MenuItem empViewEmployee;
	private MenuItem empViewDepartment;
	private Menu dataMenu;
	private Menu services;
	private MenuItem byMonth;
	private MenuItem byYear;
	private MenuItem byIndividual;
	private Menu housingMenu;
	private MenuItem unfinished;
	private final String logoImgLocation = "/UIimg/Ash-Li(logo).png";
	
	
	public PrimaryView() {
		this.getMenus();
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Ash-Li Housing Management Tool");
	}
	
	/**
	 * method constructs the parent frame for the primary view
	 */
	public void showShell() {
		try {

			this.rootPane = new AnchorPane();
			this.getDefaultSecondaryView();
			this.scrollFrame = new ScrollPane(this.secondaryView);

			rootPane.setPrefSize(800, 800);
			rootPane.setStyle("-fx-background-color: black;");

			secondaryView.prefWidthProperty().bind(Bindings.add(-5, this.scrollFrame.widthProperty()));
			secondaryView.prefHeightProperty().bind(Bindings.add(-5, this.scrollFrame.heightProperty()));

			AnchorPane.setLeftAnchor(this.menuBar, 0.0);
			AnchorPane.setRightAnchor(this.menuBar, 0.00);
			AnchorPane.setTopAnchor(this.scrollFrame, this.menuBar.getPrefHeight());
			AnchorPane.setLeftAnchor(this.scrollFrame, 0.0);
			AnchorPane.setRightAnchor(this.scrollFrame, 0.0);
			AnchorPane.setBottomAnchor(this.scrollFrame, 0.0);
			

			rootPane.getChildren().addAll(this.menuBar, this.scrollFrame);
			this.primaryScene = new Scene(rootPane, 800, 800);
			this.primaryStage.setScene(this.primaryScene);
			this.primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * method accepts an AnchorPane parameter and will be the child pane within the primary view.
	 * child pane will be wrapped within the ScrollFrame of the PrimaryView
	 * 
	 * @param pane
	 */
	public void setSecondaryView(AnchorPane pane) {
		pane.prefWidthProperty().bind(Bindings.add(-5, this.scrollFrame.widthProperty()));
		pane.prefHeightProperty().bind(Bindings.add(-5, this.scrollFrame.heightProperty()));
		this.scrollFrame.setContent(pane);
	}

	/**
	 * method creates and sets a default child pane within the parent frames of the PrimaryView.
	 * method can be and is called by various controllers at various times when the user is finished
	 * with a view.
	 */
	public void getDefaultSecondaryView() {
		VBox vb = new VBox();
		ImageView logo = new ImageView();
		Image img = new Image(logoImgLocation); 
		this.secondaryView = new AnchorPane();
	
		this.secondaryView.setPrefSize(800, 800);

		vb.setPrefSize(800, 800);
		vb.setStyle("-fx-background-color: rosybrown;");
		vb.setAlignment(Pos.TOP_CENTER);
		
		logo.setPreserveRatio(true);
		logo.setFitHeight(200);
		logo.setImage(img);
		
		vb.getChildren().add(logo);
		AnchorPane.setBottomAnchor(vb, 0.0);
		AnchorPane.setTopAnchor(vb, 0.0);
		AnchorPane.setRightAnchor(vb, 0.0);
		AnchorPane.setLeftAnchor(vb, 0.0);
		secondaryView.getChildren().add(vb);
	}

	public void setDefaultSecondaryView() {
		this.setSecondaryView(this.secondaryView);
	}

	/**
	 * method creates MenuBar for PrimaryView and calls various menu creating methods.
	 * As the program grows, most of the features and utilities will be able to called
	 * from this main MenuBar.
	 */
	public void getMenus() {
		this.menuBar = new MenuBar();
		this.makeFileMenu();
		this.makeEmployeeMenu();
		this.makeDataMenu();
		this.makeHousingInventoryMenu();
		this.menuBar.getMenus().addAll(this.fileMenu, this.employeeMenu, this.dataMenu, this.housingMenu);
		this.menuBar.setPrefSize(750, 25);
	}
	
	/**
	 * Creates File Menu for MenuBar in PrimaryView.
	 * File Menu is designed to perform various file commands and operations.
	 * 
	 * @see FileOps
	 */
	public void makeFileMenu() {
		this.fileMenu = new Menu("File");
		this.fileAbout = new MenuItem("About");
		this.fileMenu.getItems().add(fileAbout);
	}

	/**
	 * Create Employee Menu for MenuBar in PrimaryView. 
	 * Employee Menu will contain all the commands and functions necessary for the
	 * Creation, Reading, Updating and Deleting of Employees and Departments.
	 * 
	 * @see Employee
	 * @see Department
	 */
	public void makeEmployeeMenu() {
		this.employeeMenu = new Menu("Employee");

		this.empCreate = new Menu("Create");
		this.empCreateEmployee = new MenuItem("New Employee");

		this.empCreateDepartment = new MenuItem("New Department");
		this.empCreate.getItems().addAll(empCreateEmployee, empCreateDepartment);

		/*
		this.empUpdate = new Menu("Update");
		this.empUpdateEmployee = new MenuItem("Update Employee");
		this.empUpdateDepartment = new MenuItem("Update Department");
		this.empUpdate.getItems().addAll(empUpdateEmployee, empUpdateDepartment);
		*/
		
		this.empDelete = new Menu("Delete");
		this.empDeleteEmployee = new MenuItem("Delete Employee");
		this.empDeleteDepartment = new MenuItem("Delete Department");
		this.empDelete.getItems().addAll(empDeleteEmployee, empDeleteDepartment);

		this.empView = new Menu("View/Update");
		this.empViewEmployee = new MenuItem("Employee");
		this.empViewDepartment = new MenuItem("Department");
		this.empView.getItems().addAll(empViewEmployee, empViewDepartment);

		this.employeeMenu.getItems().addAll(empCreate, empDelete, empView);
	}

	/**
	 * Create Data Menu for MenuBar in PrimaryView.
	 * Data Menu will primarily be used to link the data from services to different employees
	 * and departments. 
	 */
	public void makeDataMenu() {
		this.dataMenu = new Menu("Data");

		this.services = new Menu("Services");
		this.byMonth = new MenuItem("By Month");
		this.byYear = new MenuItem("By Year");
		this.byIndividual = new MenuItem("By Individual");

		this.services.getItems().addAll(byMonth, byYear, byIndividual);
		this.dataMenu.getItems().addAll(services);
	}
	
	/**
	 * Create Housing Inventory Menu in MenuBar in PrimaryView. 
	 */
	public void makeHousingInventoryMenu() {
		this.housingMenu = new Menu("Housing");
		this.unfinished = new MenuItem("Unfinished");
		this.housingMenu.getItems().addAll(unfinished);
	}
	
	public Scene getScene() {
		return this.primaryScene;
	}

	public MenuItem getEmployeeCreateMenuItem() {
		return this.empCreateEmployee;
	}
	
	public MenuItem getDepartmentCreateMenuItem() {
		return this.empCreateDepartment;
	}
	
	public MenuItem getEmployeeViewMenuItem() {
		return this.empViewEmployee;
	}
	
	public MenuItem getDepartmentViewMenuitem() {
		return this.empViewDepartment;
	}
	

}
