package application.Control;

import application.Control.Department.PrimaryDepartmentController;
import application.Control.Employee.PrimaryEmployeeController;
import application.Model.Management;
import application.View.PrimaryView;
import javafx.stage.Stage;


/**
 * Primary controller of program. This controller governs the interactions 
 * between the PrimaryView and Model of the program. 
 * 
 * @see PrimaryView
 * @see PrimaryEmployeeController
 * @see PrimaryDepartmentController
 * @see Management
 *
 */
public class PrimaryController {
	private PrimaryView primaryView;
	private PrimaryEmployeeController employeeControl;
	private PrimaryDepartmentController departmentControl;
	private Management manager;
	private Stage primaryStage;

	/**
	 * Constructor of PrimaryController. 
	 * instantiates the PrimaryView, Loads Management with Data (file reads), 
	 * sets handles for the buttons used in the PrimaryView
	 * 
	 * @param primaryStage
	 * @see Management
	 * @see PrimaryView
	 */
	public PrimaryController(Stage primaryStage) {
		this.setPrimaryStage(primaryStage);
		this.primaryView = new PrimaryView();
		this.manager = new Management();
		this.setEmployeeHandles();
		this.setDepartmentHandles();
	}

	/**
	 * method sets the Stage within the PrimaryView and calls the object to construct
	 * the primary view for the program. This will be the default view, that will hold
	 * multiple views; an outside shell view (window) that can hold different frames within it. 
	 * 
	 * @see PrimaryView
	 */
	public void showPrimaryView() {
		primaryView.setPrimaryStage(this.getPrimaryStage());
		primaryView.showShell();
	}

	// sets the primary stage
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	// returns the primary stage
	public Stage getPrimaryStage() {
		return this.primaryStage;
	}

	/**
	 * method handles the event in which the user selects to create a 
	 * new employee for Management. There is the creation of the PrimaryEmployeeController
	 * which will handles multiple interactions and creations of other controllers for various
	 * views which allow the user to CRUD an Employee and in some instances a department
	 * 
	 * @see PrimaryEmployeeController
	 * @see EmployeeCreationController
	 * @see Management
	 * @see Employee
	 */
	public void handleEmployeeCreate() {
		this.employeeControl = new PrimaryEmployeeController(this.primaryView, this.manager);
		this.employeeControl.showCreateEmployeeForm();
	}
	
	/**
	 * method handles the event in which the user selects to view the employees within Management. 
	 * There is the creation of the PrimaryEmployeeController which will handles multiple 
	 * interactions and creations of other controllers for various views which allow the user 
	 * to CRUD an Employee. 
	 * 
	 * @see PrimaryEmployeeController
	 * @see EmployeeCreationController
	 * @see Management
	 * @see Employee
	 */
	public void handleEmployeeView() {
		this.employeeControl = new PrimaryEmployeeController(this.primaryView, this.manager);
		this.employeeControl.showViewEmployeeForm();
	}
	
	/**
	 * method handles the event in which the user selects to create a 
	 * new department for Management. There is the creation of the PrimaryEmployeeController
	 * which will handles multiple interactions and creations of other controllers for various
	 * views which allow the user to CRUD a Department.
	 * 
	 * @see PrimaryDepartmentController
	 * @see DepartmentCreationController
	 * @see Management
	 * @see Department
	 */
	public void handleDepartmentCreate() {
		this.departmentControl = new PrimaryDepartmentController(this.primaryView, this.manager);
		this.departmentControl.showCreateDepartmentForm();
	}
	
	public void handleDepartmentView() {
		this.departmentControl = new PrimaryDepartmentController(this.primaryView, this.manager);
		this.departmentControl.showViewDepartmentForm();
	}
	
	/**
	 * method sets the handles for the buttons within the PrimaryView that involve
	 * Employee interactions
	 * 
	 * @see PrimaryView
	 */
	public void setEmployeeHandles() {
		this.primaryView.getEmployeeCreateMenuItem().setOnAction(event -> {
			this.handleEmployeeCreate();
		});
		
		this.primaryView.getEmployeeViewMenuItem().setOnAction(event -> {
			this.handleEmployeeView();
		});
	}
	
	/**
	 * method sets the handles for the buttons within the PrimaryView that involve
	 * Department interactions
	 * 
	 * @see PrimaryView
	 */
	public void setDepartmentHandles() {
		this.primaryView.getDepartmentCreateMenuItem().setOnAction(event -> {
			this.handleDepartmentCreate();
		});
		
		this.primaryView.getDepartmentViewMenuitem().setOnAction(event -> {
			this.handleDepartmentView();
		});
	}

}
