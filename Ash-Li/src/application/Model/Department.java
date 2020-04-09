package application.Model;

import java.util.ArrayList;

/**
 * Class is designed to store information about a department which includes a list of Employees attached
 * to the department and the type of population the department serves.
 * 
 */
public class Department {
	private String name;
	private String abbreviation; 
	private String populationServed;
	private int sizeOfDepartment;
	private ArrayList<Employee> employees;

	public Department(String name, String abbreviation, String population) {
		this.setName(name);
		this.setAbbreviation(abbreviation);
		this.setPopulation(population);
		employees = new ArrayList<Employee>();
	}

	public Department() {
		this("","", "");
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("---Department---\n" + "\tName: " + this.getName() 
					+ "\n\tAbbreviation: " + this.getAbbreviation()
					+ "\n\tPopulation Served: " + this.getPopulation() + "\n");
		for (Employee e : this.employees) {
			sb.append("\t" + e.toString());
		}
		return sb.toString();
	}
	
	// equals overridden to compare based on name of department
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Department)) {
			return false;
		}

		Department dept = (Department) o;
		return (dept.getName().equalsIgnoreCase(this.getName()));
	}
	
	public ArrayList<Employee> getEmployees() {
		return this.employees;
	}

	/**
	 * method adds employee to department and updates size of department.
	 * @param emp
	 */
	public void addEmployee(Employee emp) {
		employees.add(emp);
		this.setDepartmentSize();
	}

	/**
	 * method removes employee from the department and updates the size. 
	 * @param emp
	 */
	public void removeEmployee(Employee emp) {
		if (employeeExists(emp)) {
			employees.remove(emp);
			this.setDepartmentSize();
		}
	}

	public Boolean employeeExists(Employee emp) {
		return this.employees.contains(emp);
	}

	public void setDepartmentSize() {
		this.sizeOfDepartment = this.employees.size();
	}

	public int getDepartmentSize() {
		return this.sizeOfDepartment;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getAbbreviation() {
		return this.abbreviation;
	}
	
	public void setPopulation(String pop) {
		this.populationServed = pop;
	}
	
	public String getPopulation() {
		return this.populationServed;
	}
}
