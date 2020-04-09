package application.Control.Department;

import java.util.ArrayList;

import application.Control.Employee.EmployeeCreationController;
import application.Model.Department;
import application.Model.Management;
import application.View.PrimaryView;
import application.View.DepartmentView.DepartmentCreate;


public class DepartmentCreationController {
	private PrimaryView primaryView;
	private DepartmentCreate deptCreate;
	private EmployeeCreationController empCreateController;
	private Management manager;
	private ArrayList<Department> deptList;
	
	public DepartmentCreationController(PrimaryView primaryView, Management manager) {
		this.primaryView = primaryView;
		this.manager = manager;
		this.deptList = new ArrayList<Department>();
		this.deptCreate = new DepartmentCreate(this.primaryView.getScene());
	}
	
	public DepartmentCreationController(PrimaryView primaryView, Management manager, EmployeeCreationController empCreateController) {
		this.empCreateController = empCreateController;
		this.primaryView = primaryView;
		this.manager = manager;
		this.deptCreate = new DepartmentCreate(this.primaryView.getScene());
		this.deptCreate.setEmployeeShortcutUsed(true);
		this.deptList = new ArrayList<Department>();
	}
	
	public void createForm() {
		this.setDepartmentForm();
		this.setCreateHandles();
	}
	
	public void departmentSaveData() {
		this.deptList.stream()
					.forEach(department -> this.manager.addDepartment(department));
		this.manager.saveDepartments();
	}
	
	public void departmentRecordCreateData() {
		this.deptList.add(new Department(this.deptCreate.getName(), this.deptCreate.getAbbreviation(), this.deptCreate.getPopulation()));

	}

	public void setDepartmentForm() {
		this.primaryView.setSecondaryView(this.deptCreate.createView());
	}

	public void setCreateHandles() {
		this.deptCreate.getSubmitButton().setOnAction(event -> {
				this.deptCreate.finalValidation();
		});

		// set controls for back button in View
		this.deptCreate.getBackButton().setOnAction(event -> {
			this.setDepartmentForm();
		});

		// set controls for recording data in View
		this.deptCreate.getConfirmationButton().setOnAction(event -> {
			this.departmentRecordCreateData();
			this.departmentSaveData();
			if (this.deptCreate.getEmployeeShortcutUsed()) {
				this.empCreateController.shortcutReturn();
			} else {
				this.primaryView.setDefaultSecondaryView();
			}
		});
		
		this.deptCreate.getConfirmAndAddButton().setOnAction(event -> {
			this.departmentRecordCreateData();
			this.setDepartmentForm();
		});
	}
	
}


