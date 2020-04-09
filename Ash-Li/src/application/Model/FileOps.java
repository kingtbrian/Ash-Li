package application.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileOps {
	private final String employeeFileName = "Data/Personnel/Emp/emp.csv";
	private final String departmentFileName = "Data/Personnel/Depts/dept.csv";
	
	
	public FileOps() {
		this.defaultFileCheck();
	}
	
	public String commaFilter(String phrase) {
		return phrase.replaceAll(",", "|");
	}
	
	public String pipeFilter(String phrase) {
		return phrase.replaceAll("\\|", ",");
	}

	public void defaultFileCheck() {
		File f = new File(this.employeeFileName);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		f = new File(this.departmentFileName);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<Employee> loadPersonnel() {
		ArrayList<Employee> employeeList = new ArrayList<Employee>();
		String delim = ",";
		String line = "";
		BufferedReader reader = null;

		try {
			// String first_name, String last_name, String position, String department,
			// String trainingHours, LocalDate hireDate
			reader = new BufferedReader(new FileReader(employeeFileName));
			while ((line = reader.readLine()) != null) {
				String args[] = line.split(delim);
				Employee emp = new Employee(pipeFilter(args[0])
										  , pipeFilter(args[1])
										  , pipeFilter(args[2])
										  , pipeFilter(args[3])
										  , pipeFilter(args[4])
										  , pipeFilter(args[5]));
				employeeList.add(emp);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return employeeList;
	}
	
	public ArrayList<Department> loadDepartments() {
		ArrayList<Department> deptList = new ArrayList<Department>();
		String delim = ",";
		String line = "";
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(departmentFileName));
			while ((line = reader.readLine()) != null) {
				String args[] = line.split(delim);
				Department dept = new Department(pipeFilter(args[0])
											   , pipeFilter(args[1])
											   , pipeFilter(args[2]));
				deptList.add(dept);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return deptList;
	}
	
	public void saveDepartments(ArrayList<Department> depts) {
		try {
			FileWriter deptWriter = new FileWriter(new File(departmentFileName));
			StringBuilder sb = new StringBuilder();
			String cv = ",";
			String nl = "\n";
			
			depts.stream()
					.forEach( dept -> {
						sb.append(commaFilter(dept.getName()) + cv
								+ commaFilter(dept.getAbbreviation()) + cv
								+ commaFilter(dept.getPopulation()) + nl);
					});
			
			deptWriter.write(sb.toString());
			deptWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveEmployees(ArrayList<Employee> emp) {
		try {
			FileWriter empWriter = new FileWriter(new File(employeeFileName));
			StringBuilder sb = new StringBuilder();
			String cv = ",";
			String nl = "\n";
			
			emp.stream().forEach( employee -> {
				sb.append(commaFilter(employee.getFirst_name()) + cv 
						+ commaFilter(employee.getLast_name()) + cv 
						+ commaFilter(employee.getPosition()) + cv 
						+ employee.getDepartment() + cv
						+ employee.getTrainingHours() + cv
						+ employee.getFormattedDate() + nl);
			});
			
			empWriter.write(sb.toString());
			empWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
