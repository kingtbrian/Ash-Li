package application.Control.Employee;

import java.time.LocalDate;
import java.util.ArrayList;

import application.Control.Department.DepartmentCreationController;
import application.Model.Department;
import application.Model.Employee;
import application.Model.FileOps;
import application.Model.Management;
import application.View.PrimaryView;
import application.View.EmployeeView.EmployeeCreate;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * This class is the controller for the view that allows the user to create a new Employee.
 * 
 * @see Employee
 */
public class EmployeeCreationController {
	private PrimaryView primaryView;
	private EmployeeCreate empCreate;
	private Management manager;
	private ArrayList<Employee> empList;
	
	/**
	 * Constructor takes the PrimaryView and Management. Additionally, a new 
	 * list of Employees is initialized. 
	 * 
	 * @param primaryView
	 * @param manager
	 */
	public EmployeeCreationController(PrimaryView primaryView, Management manager) {
		this.empCreate = new EmployeeCreate(primaryView.getScene());
		this.primaryView = primaryView;
		this.manager = manager;
		this.empList = new ArrayList<Employee>();
	}
	
	/**
	 * method calls 3 other functions that initialize radio buttons, loads child pane 
	 * into PrimaryView and creates the handles for the buttons within the Employee Create View
	 * 
	 * @see EmployeeCreate
	 */
	public void createForm() {
		this.setComboBox();
		this.setEmployeeForm();
		this.setCreateHandles();
	}
	
	/**
	 * method saves the data in the current list of employees located in this object to 
	 * the Employee data file
	 * 
	 * @see Management
	 * @see FileOps
	 */
	public void employeeSaveData() {
		this.empList.stream()
					.forEach( employee -> this.manager.addEmployee(employee));
		manager.saveEmployees();
	}
	
	/**
	 *  Method creates a new Employee by gathering data from the TextFields in the EmployeeCreate view 
	 *  and loads the Employee into the Employee List
	 *  
	 *  @see EmployeeCreate
	 *  @see Employee
	 */
	public void employeeRecordCreateData() {
		this.empList.add(new Employee(this.empCreate.getFirstName()
									, this.empCreate.getLastName()
									, this.empCreate.getPosition()
									, this.empCreate.getDepartment()
									, this.empCreate.getTrainingHours()
									, this.empCreate.getDate()));
	}

	/**
	 * Method sets the view created by Employee Create as the child pane within the 
	 * PrimaryView.
	 * 
	 * @see PrimaryView
	 */
	public void setEmployeeForm() {
		this.primaryView.setSecondaryView(this.empCreate.createView());
	}

	/**
	 * Method creates the handles for all the buttons located within the Employee Creation View. 
	 */
	public void setCreateHandles() {
		// submit button first checks if there are departments on to add employee to and handles appropriately. 
		this.empCreate.getSubmitButton().setOnAction(event -> {
			if (this.empCreate.isDeptSelected()) {
				this.empCreate.finalValidation();
			} else {
				this.empCreate.setSelectDeptInstruction();
			}
		});

		// set controls for back button in View
		this.empCreate.getBackButton().setOnAction(event -> {
			this.setEmployeeForm();
		});

		// set controls for recording data in View
		this.empCreate.getConfirmationButton().setOnAction(event -> {
			this.employeeRecordCreateData();
			this.employeeSaveData();
			this.primaryView.setDefaultSecondaryView();
		});
		
		// set controls for confirming data entry by user
		this.empCreate.getConfirmAndAddButton().setOnAction(event -> {
			this.employeeRecordCreateData();
			this.setEmployeeForm();
		});
		
		// set controls for navigating to Department creation if there are no departments in Management
		this.empCreate.getDeptShortcutButton().setOnAction(event -> {
			this.deptShortcut();
		}); 
	}
	
	/**
	 * Method is called when a user attempts to create a new Employee and no Departments exist in Management. 
	 * User will be given a prompt from another method within the EmployeeCreate and only shown the "back" button
	 * and "Add Department" Button. The handle for the "Add Department" Button will call this method. 
	 * Method used to allow user to navigate to the Department Creation View from within the child pane currently in the PrimaryView.
	 * Although, the user can still navigate to Department Creation through the MenuBar in the PrimaryView, this object and the
	 * EmployeeCreate View will store information entered by user until the user completes the creation of the Department. Once User 
	 * has completed creating a Department after using this shortcut, they will return to EmployeeCreate View and shown their stored
	 * information. 
	 * 
	 *  @see Department
	 *  @see DepartmentCreationController
	 *  @see DepartmmentCreate
	 *  @see Employee Create
	 */
	public void deptShortcut() {
		DepartmentCreationController deptController = new DepartmentCreationController(this.primaryView, this.manager, this);
		deptController.createForm();
	}
	
	/**
	 * This method is called upon returning from the Department Creation View.
	 * The saved Data is loaded into a the appropriate nodes within the view allowing the creation
	 * of the Employee.
	 */
	public void shortcutReturn() {
		String firstName = this.empCreate.getFirstName();
		String lastName = this.empCreate.getLastName();
		String position = this.empCreate.getPosition();
		String training = this.empCreate.getTrainingHours();
		LocalDate ld = this.empCreate.getDate();
		this.setComboBox();
		this.setEmployeeForm();
		this.empCreate.setSavedInfo(firstName, lastName, position, training, ld);
	}
	
	public void setComboBox() {
		this.empCreate.setComboBox();
		this.empCreate.getComboBox().setBackground(new Background(new BackgroundFill(
				 Color.WHITE
			   , new CornerRadii(20)
			   , Insets.EMPTY)));
		
		this.empCreate.getComboBox().setPrefSize(250, 30);
		this.empCreate.getComboBox().setPadding(new Insets(3,3,3,3));
		this.empCreate.getComboBox().setBorder((new Border(new BorderStroke(
				 Color.BLACK
			   , BorderStrokeStyle.SOLID
			   , new CornerRadii(20)
			   , new BorderWidths(2)))));
		
		this.empCreate.getComboBox().addEventHandler(MouseEvent.MOUSE_ENTERED, (event) -> {
			this.empCreate.getComboBox().setEffect(new DropShadow());
			this.primaryView.getScene().setCursor(Cursor.OPEN_HAND);
		});
		
		this.empCreate.getComboBox().addEventHandler(MouseEvent.MOUSE_EXITED, (event) -> {
			this.empCreate.getComboBox().setEffect(null);
			this.primaryView.getScene().setCursor(Cursor.DEFAULT);
		});
		
		this.empCreate.getComboBox().setPromptText("Select Department...");;
		if (this.manager.getDeptList() == null) {
			return;
		}
		this.manager.getDeptList().stream().forEach(dept -> {
			this.empCreate.getComboBox().getItems().add(dept.getName());
		});
		this.empCreate.getComboBox().setTooltip(new Tooltip("Select Department from Dropdown Box for employee to be added to."));
	}
	
}
