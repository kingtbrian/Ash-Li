package application.Control.Employee;

import java.time.LocalDate;
import java.util.ArrayList;

import application.Control.Department.DepartmentCreationController;
import application.Model.Employee;
import application.Model.Management;
import application.View.PrimaryView;
import application.View.EmployeeView.EmployeeCreate;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Font;

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
		this.setRadioButtons();
		//System.out.println("Control works!");
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
		System.out.println("Employee Record Data Works!!");
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
				this.empCreate.dataIsValid();
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
		this.setRadioButtons();
		this.setEmployeeForm();
		this.empCreate.setSavedInfo(firstName, lastName, position, training, ld);
	}
	
	/**
	 * This method creates radio buttons and a ToggleGroup for the RadioButtons. Each Department
	 * will be attached to a RadioButton
	 */
	public void setRadioButtons() {
		this.manager.getMap().keySet().stream().forEach(department -> {
			RadioButton rb = new RadioButton(department.getName());
			rb.setFont(new Font("Arial", 16));
			rb.setUserData(department.getName());
			rb.setToggleGroup(this.empCreate.getRadioToggle());
			this.empCreate.getRadioButtons().add(rb);
		});
	}
	
}
