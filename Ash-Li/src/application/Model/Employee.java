package application.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Employee {

	private String first_name;
	private String last_name;
	private String position;
	private String department;
	private int trainingHours;
	private LocalDate hireDate;
	private String formattedDate;
	private int id;
	private static int idCounter;

	public Employee(String first_name, String last_name, String position, String department, String trainingHours,
			String hireDate) {
		this.setFirst_name(first_name);
		this.setLast_name(last_name);
		this.setPosition(position);
		this.setDepartment(department);
		this.setTrainingHours((trainingHours.length() == 0) ? 0 : Integer.parseInt(trainingHours));
		this.setFormattedDate(hireDate);
		this.setId(Employee.idCounter++);
	}

	public Employee(String first_name, String last_name, String position, String department, String trainingHours,
			LocalDate hireDate) {
		this.setFirst_name(first_name);
		this.setLast_name(last_name);
		this.setPosition(position);
		this.setDepartment(department);
		this.setTrainingHours((trainingHours.length() == 0) ? 0 : Integer.parseInt(trainingHours));
		this.setFormattedDate(hireDate);
		this.setId(Employee.idCounter++);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("---Employee---\n" + "\tID: " + this.getId() +"\n\tName: " + this.getFirst_name() + " " + this.getLast_name() + "\n"
				+ "\tPosition: " + this.getPosition() + "\n" + "\tDepartment: " + this.getDepartment() + "\n"
				+ "\tTraining Hours: " + this.getTrainingHours() + "\n" + "\tHire Date: " + this.formattedDate + "\n");
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Employee)) {
			return false;
		}

		Employee emp = (Employee) o;
		return (emp.first_name.equalsIgnoreCase(this.first_name) && emp.last_name.equalsIgnoreCase(this.last_name)
				&& emp.position.equalsIgnoreCase(this.position)
				&& emp.formattedDate.equalsIgnoreCase(this.formattedDate));
	}

	public void fixDuplicateName(int n) {
		this.last_name = this.last_name + " " + ++n;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name.substring(0, 1).toUpperCase() + first_name.substring(1).toLowerCase();
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name.substring(0, 1).toUpperCase() + last_name.substring(1).toLowerCase();
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getTrainingHours() {
		return trainingHours;
	}

	public void setTrainingHours(int trainingHours) {
		this.trainingHours = trainingHours;
	}

	public LocalDate getHireDate() {
		return hireDate;
	}

	public void setHireDate(LocalDate hireDate) {
		this.hireDate = hireDate;
	}

	public void setFormattedDate(LocalDate ld) {
		if (ld != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");
			this.formattedDate = formatter.format(ld);
		} else {
			this.formattedDate = "99/99/9999";
		}
	}

	public void setFormattedDate(String date) {
		this.formattedDate = date;
	}

	public String getFormattedDate() {
		return this.formattedDate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public static int getTotalID() {
		return Employee.idCounter;
	}

}
