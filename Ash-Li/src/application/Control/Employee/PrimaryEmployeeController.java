package application.Control.Employee;


import application.Model.Management;
import application.View.PrimaryView;

/**
 * This controller is the primary controller for handling employee related selections
 * within the PrimaryView's MenuBar's options. 
 * 
 * @see EmployeeCreationController
 * @see EmployeeViewControdller
 * @see Employee
 */
public class PrimaryEmployeeController {
	private PrimaryView primaryView;
	private EmployeeCreationController empCreateControl;
	private EmployeeViewController empViewControl;
	Management manager;

	/**
	 * Constructor also creates the Management object as well as transfers the PrimaryView. 
	 * 
	 * @param primaryView
	 * @param manager
	 */
	public PrimaryEmployeeController(PrimaryView primaryView, Management manager) {
		this.manager = manager;
		this.primaryView = primaryView;
	}
	
	/**
	 * Method handles the "Create Employee" selection from PrimaryView's MenuBar.
	 * An EmployeeCreationController object is created, passing the PrimaryView and Management objects
	 * and Calls the object to create the child pane to be shown in the PrimaryView. 
	 * 
	 * @see EmployeeCreationController
	 */
	public void showCreateEmployeeForm() {
		this.empCreateControl = new EmployeeCreationController(this.primaryView, this.manager);
		this.empCreateControl.createForm();
	}
	
	/**
	 * Method handles the "View/Update Employee" selection from PrimaryView's MenuBar.
	 * An EmployeeViewController object is created, passing the PrimaryView and Management objects
	 * and Calls the object to create the child pane to be shown in the PrimaryView.
	 * 
	 * @see EmployeeViewController
	 */
	public void showViewEmployeeForm() {
		this.empViewControl = new EmployeeViewController(this.primaryView, this.manager);
		this.empViewControl.showForm();
	}
	

}