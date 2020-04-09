package application.Control.Department;

import application.Model.Management;
import application.View.PrimaryView;


public class PrimaryDepartmentController {
	private PrimaryView primaryView;
	private DepartmentCreationController deptCreateControl;
	private DepartmentViewController deptViewControl; 
	Management manager;

	public PrimaryDepartmentController(PrimaryView primaryView, Management manager) {
		this.manager = manager;
		this.primaryView = primaryView;
	}

	public void showCreateDepartmentForm() {
		this.deptCreateControl = new DepartmentCreationController(this.primaryView, this.manager);
		this.deptCreateControl.createForm();
	}
	
	public void showViewDepartmentForm() {
		this.deptViewControl = new DepartmentViewController(this.primaryView, this.manager);
		this.deptViewControl.showForm();
	}
}
