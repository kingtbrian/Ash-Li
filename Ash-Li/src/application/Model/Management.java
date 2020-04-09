package application.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Management {
	private HashMap<Department, ArrayList<Employee>> empMap;
	private HashMap<String, Department> deptMap;
	private HashMap<Integer, Employee> empList;
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
		this.empList = new HashMap<Integer, Employee>();
		this.deptList = new ArrayList<Department>();
	}

	public void initFileOps() {
		this.fileOps = new FileOps();
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
		this.empList.put(employee.getId(), employee);
		this.addEmployeeToDept(employee);
	}
	
	public void addEmployeeToDept(Employee employee) {
		if (this.departmentExists(employee.getDepartment())) {
			this.deptMap.get(employee.getDepartment()).addEmployee(employee);
		}
	} 
	
	public void updateEmployee(int id, ArrayList<String> empInfo) {
		
		Employee emp = this.getEmployeeList().get(id);
		
		if (!this.deptMap.get(emp.getDepartment()).getName().equalsIgnoreCase(empInfo.get(4))) {
			this.deptMap.get(emp.getDepartment()).removeEmployee(emp);
		}

		emp.setFirst_name(empInfo.get(0));
		emp.setLast_name(empInfo.get(1));
		emp.setPosition(empInfo.get(2));
		emp.setDepartment(empInfo.get(4));
		emp.setTrainingHours(Integer.parseInt(empInfo.get(3)));
		emp.setFormattedDate(empInfo.get(5));
		
		this.deptMap.get(emp.getDepartment()).addEmployee(emp);
	}
	
	public void updateDepartment(ArrayList<String> deptInfo, String ogKey) {
		Department dept = this.deptMap.remove(ogKey);
		
		// change all employees departments to the change in the update
		if (!deptInfo.get(0).equalsIgnoreCase(dept.getName())) {
			dept.getEmployees().stream().forEach(emp -> {
					emp.setDepartment(deptInfo.get(0));
			});
		}
		
		dept.setName(deptInfo.get(0));
		dept.setAbbreviation(deptInfo.get(1));
		dept.setPopulation(deptInfo.get(3));
		
		this.deptMap.put(dept.getName(), dept);
		
	}
	
	public void loadManagement() {
		ThreadWorker tw = () -> {
			Runnable task = () -> {
				this.deptList = fileOps.loadDepartments();
	
				this.deptList.stream()
							.forEach( department -> {
							this.deptMap.put(department.getName(), department);
							});
				
				
				fileOps.loadPersonnel().forEach(employee -> {
					this.empList.put(employee.getId(), employee);
					this.deptMap.get(employee.getDepartment()).addEmployee(employee);
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
	}

	public boolean hasSameName(Employee e) {
		return this.getEmployeeArrayList().stream()
							.anyMatch(emp -> (emp.getFirst_name().equalsIgnoreCase(e.getFirst_name())
				&& emp.getLast_name().equalsIgnoreCase(e.getLast_name())));
	}

	public int countSameNames(Employee e) {
		return (int) this.getEmployeeArrayList().stream()
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
	
	public synchronized ArrayList<Department> getDeptList() {
		return this.deptList;
	}
	
	public HashMap<Integer, Employee> getEmployeeList() {
		return this.empList;
	}
	
	
	public ArrayList<Employee> getEmployeeArrayList() {
		ArrayList<Employee> employeeArr = new ArrayList<Employee>();
		this.empList.keySet().stream().forEach( key -> {
			employeeArr.add(this.empList.get(key));
		});
		return employeeArr;
	}
	
	
	public HashMap<Department, ArrayList<Employee>> getMap() {
		return this.empMap;
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
				fileOps.saveEmployees(this.getEmployeeArrayList());
			};
			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.execute(task);
			executor.shutdown();
		};
		tw.runJob();
	}
	
}
