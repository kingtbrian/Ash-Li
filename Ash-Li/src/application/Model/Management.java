package application.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Management {
	private HashMap<Department, ArrayList<Employee>> empMap;
	private HashMap<String, Department> deptMap;
	private ArrayList<Employee> empList;
	private ArrayList<Department> deptList;
	private FileOps fileOps;

	public Management() {
		this.initMap();
		this.initList();
		this.initFileOps();
		this.loadManagement();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("__DEPARTMENTS & THEIR EMPLOYEES__\n");
		deptList.stream()
				.forEach(dept-> sb.append(dept.toString()));
		sb.append("\n__Department Map__\n");
		deptMap.keySet()
				.forEach( key -> sb.append("Key: " + key + "|| Value: " + this.deptMap.get(key) + "\n"));
		sb.append("\n__Employee Map__\n");
		empMap.keySet()
				.forEach( key -> sb.append("Key: " + key + "|| Value: " + this.empMap.get(key) + "\n"));
		return sb.toString();
	}
	
	public void initMap() {
		this.empMap = new HashMap<Department, ArrayList<Employee>>();
		this.deptMap = new HashMap<String, Department>();
	}

	public void initList() {
		this.empList = new ArrayList<Employee>();
		this.deptList = new ArrayList<Department>();
	}

	public void initFileOps() {
		this.fileOps = new FileOps();
	}
	
	public ArrayList<Employee> getEmployeeList() {
		return this.empList;
	}

	public HashMap<Department, ArrayList<Employee>> getMap() {
		return this.empMap;
	}

	public boolean departmentExists(String name) {
		return (this.deptMap.get(name) != null);
	}

	// maybe this can use a simpler form of verification
	public void addDepartment(Department dept) {
		if (this.departmentExists(dept.getName())) {
			return;
		} else {
			this.deptList.add(dept);
			this.empMap.put(dept, dept.getEmployees());
		}
	}
	
	public void addEmployee(Employee employee) {
		if (hasSameName(employee)) {
			employee.fixDuplicateName(this.countSameNames(employee));
		}
		this.empList.add(employee);
		this.addEmployeeToDept(employee);
	}
	
	public void addEmployeeToDept(Employee employee) {
		if (this.departmentExists(employee.getDepartment())) {
			this.deptMap.get(employee.getDepartment()).addEmployee(employee);
		}
	} 
	
	public void loadManagement() {
		ThreadWorker tw = () -> {
			Runnable task = () -> {
				this.deptList = fileOps.loadDepartments();
				this.deptList.stream().forEach(department -> System.out.println(department.toString()));
				this.empList = fileOps.loadPersonnel();
				this.empList.stream().forEach(employee -> System.out.println(employee.toString()));
				
				// add department to hashmap: Key = dept name, Value = department
				this.deptList.stream()
							.forEach( department -> {
							this.deptMap.put(department.getName(), department);
							});
				
				// 
				this.empList.stream()
							.forEach( employee -> {
							this.deptMap.get(employee.getDepartment())
										.addEmployee(employee);
							});
				
				this.deptList.stream()
							.forEach( department -> {
							this.empMap.put(department, department.getEmployees());
							});
				
			};
			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.execute(task);
			executor.shutdown();
		};
		tw.runJob();
		System.out.println(this.toString());
	}

	public boolean hasSameName(Employee e) {
		return this.empList.stream()
							.anyMatch(emp -> (emp.getFirst_name().equalsIgnoreCase(e.getFirst_name())
				&& emp.getLast_name().equalsIgnoreCase(e.getLast_name())));
	}

	public int countSameNames(Employee e) {
		return (int) this.empList.stream()
								.filter(emp -> (emp.getFirst_name().equalsIgnoreCase(e.getFirst_name())
				&& emp.getLast_name().contains(e.getLast_name() + " "))).count();
	}
	
	public boolean noDepartmentsExist() {
		return this.empMap.keySet().isEmpty();
	}
	
	public boolean noEmployeesExist() {
		return this.empList.isEmpty();
	}
	
	public synchronized HashMap<Department, ArrayList<Employee>> getEmpMap() {
		return this.empMap;
	};
	
	public synchronized HashMap<String, Department> getDeptMap() {
		return this.deptMap;
	}
	
	public synchronized ArrayList<Employee> getEmpList() {
		return this.empList;
	}
	
	public synchronized ArrayList<Department> deptList() {
		return this.deptList;
	}
	
	public void saveDepartments() {
		ThreadWorker tw = () -> {
			Runnable task = () -> {
				fileOps.saveDepartments(this.deptList);
			};
			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.execute(task);
			executor.shutdown();
		};
		tw.runJob();
	}
	
	public void saveEmployees() {
		ThreadWorker tw = () -> {
			Runnable task = () -> {
				fileOps.saveEmployees(this.empList);
			};
			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.execute(task);
			executor.shutdown();
		};
		tw.runJob();
	}

}
