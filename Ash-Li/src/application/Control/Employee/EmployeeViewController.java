package application.Control.Employee;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import application.Model.Employee;
import application.Model.Management;
import application.View.PrimaryView;
import application.View.EmployeeView.EmployeeViewer;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * This class is the controller for the view in which a user is able to view the entire list of employees
 * as well as update the employees information. 
 * 
 * @see EmployeeViewer
 * @see Management
 * @see Employee
 *
 */
public class EmployeeViewController {
	private PrimaryView primaryView;
	private EmployeeViewer empView;
	private Management manager;
	private String[][] data;
	private HashMap<String, String> colorMap; // used for determining color of buttons; sorting up = green, sorting down = red
	private int index;
	
	
	/**
	 * Constructor carries the PrimaryView object to enable to setting of a child pane within the PrimaryView.
	 * Additionally, a management object is loaded to load the grid in the view with Employee Data. 
	 * 
	 * @param primaryView
	 * @param manager
	 */
	public EmployeeViewController(PrimaryView primaryView, Management manager) {
		this.primaryView = primaryView;
		this.manager = manager;
		this.empView = new EmployeeViewer(primaryView.getScene(), this.manager.getEmployeeArrayList().size());
		this.initColorMap();
		this.initData();
	}
	
	/**
	 * Method calls multiple functions that set the handles for the buttons within the view, set up the labels
	 * for viewing within the view's gridpane, and sets the view as the child pane within the PrimaryView. 
	 */
	public void showForm() {
		this.setViewHandles();
		this.empView.initLabels(this.manager.getEmployeeArrayList().size());
		this.txDataToLabels(this.manager.getEmployeeArrayList());
		this.setViewerForm();
	}
	
	/**
	 * sets the pane created within the EmployeeView as the child pane within the PrimaryView
	 */
	public void setViewerForm() {
		this.primaryView.setSecondaryView(this.empView.showEmployeeView());
	}
	
	/**
	 * sets the handles for the buttons within the view. Depending on the button, its conditional calling will be 
	 * handled within the functions it calls, not within the handle itself. Conditional calling being, how the data will be
	 * sorted based on the current color of the button. 
	 */
	public void setViewHandles() {
		this.empView.getBackButton().setOnAction(event -> {
			this.primaryView.setDefaultSecondaryView();
		});
		
		this.empView.getNameButton().setOnAction(event -> {
			this.sortByName();
			this.setViewerForm();
		});
		
		this.empView.getHireDateButton().setOnAction(event -> {
			this.sortByDate();
			this.setViewerForm();
		});
		
		this.empView.getDepartmentButton().setOnAction(event -> {
			this.sortByDepartment();;
			this.setViewerForm();
		});
		
		this.empView.getPositionButton().setOnAction(event -> {
			this.sortByPosition();
			this.setViewerForm();
		});
		
		this.empView.getTrainingHoursButton().setOnAction(event -> {
			this.sortByTrainingHours();
			this.setViewerForm();
		});
		
		this.empView.getUpdateButton().setOnAction(event -> {
			this.empView.setUpdateTextFields(this.manager.getDeptList());
			this.empView.getButtonGroup().stream()
						.forEach( button -> {
							button.setDisable(true);
			});
		});
		
		this.empView.getUpdateAllButton().setOnAction(event -> { 
			this.getUpdatedEmployeeInfo();
			this.empView.getButtonGroup().stream()
						.forEach(button-> {
							button.setDisable(false);
						});
			this.empView.uncheckBoxes();
			this.showForm();
		});
		
		return;
	}
	
	/**
	 * sets the 2d Array to the size of employees within Management
	 */
	public void initData() {
		this.data = new String[this.manager.getEmployeeList().keySet().size()][];
	}
	
	/**
	 * Method formats an Employee first and last name to "Last Name, First"
	 * 
	 * @param employee
	 * @return String "Last Name, First"
	 */
	public String nameFormatter(Employee employee) {
		if ( employee != null ) {
			return employee.getLast_name() + ", " + employee.getFirst_name();
		} else {
			return "NullValue";
		}
	}
	
	/**
	 * Method populates a 2d Array by iterating through each employee within the Management employee list and 
	 * adding employee information to each array index. Additionally, after completion, method will pass 2d Array to 
	 * the view for the information to be populated within labels. 
	 * 
	 * @see EmployeeView
	 */
	public void txDataToLabels(ArrayList<Employee> empArr) {
		index = 0;
		empArr
					.stream()
					.forEach( employee -> {
						this.data[index] = new String[6];
						this.data[index][0] = employee.getId() + "";
						this.data[index][1] = this.nameFormatter(employee);
						this.data[index][2] = employee.getPosition();
						this.data[index][3] = employee.getTrainingHours()+ "";
						this.data[index][4] = employee.getDepartment();
						this.data[index][5] = employee.getFormattedDate();
						index++;
					});
		index = 0;
		this.empView.populateDataInLabels(this.data);
	}
	
	public void sortByName() {
		ArrayList<Employee> empArr = this.manager.getEmployeeArrayList();
		this.empView.neutralizeButtonColors();
		
		if (this.colorMap.get(this.empView.getNameButton().getText()).equalsIgnoreCase("red")) {
			empArr.sort((Employee e1, Employee e2) -> 
							this.nameFormatter(e1).compareTo(this.nameFormatter(e2)));
			this.redColorMap();
			this.empView.setButtonAesthetic(this.empView.getNameButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.GREEN);
			this.colorMap.put(this.empView.getNameButton().getText(), "green");
			
		} else {
			empArr.sort((Employee e1, Employee e2) -> 
							this.nameFormatter(e2).compareTo(this.nameFormatter(e1)));
			this.empView.setButtonAesthetic(this.empView.getNameButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.RED);
			this.colorMap.put(this.empView.getNameButton().getText(), "red");
		}
		this.txDataToLabels(empArr);
	}
	
	/**
	 * this method first checks to see what color is attached to the button name, and depending on the color
	 * a Min/Max sort of the Management employee list will take place based on the parameter of the button's name. 
	 * Min sort = button will turn green. Max sort = button will turn red. Method will neutralize other button colors as well as
	 * calling functions within the controller to re-populate the data within the view respective to the sorting that just
	 * occurred
	 * 
	 * @see Management
	 */
	public void sortByID() {
		ArrayList<Employee> empArr = this.manager.getEmployeeArrayList();
		this.empView.neutralizeButtonColors();
		
		if (this.colorMap.get(this.empView.getNameButton().getText()).equalsIgnoreCase("red")) {
			empArr.sort((Employee e1, Employee e2) -> 
							e1.getId() - e2.getId());
			this.redColorMap();
			this.empView.setButtonAesthetic(this.empView.getNameButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.GREEN);
			this.colorMap.put(this.empView.getNameButton().getText(), "green");
			
		} else {
			empArr.sort((Employee e1, Employee e2) -> 
							e2.getId() - e1.getId());
			this.empView.setButtonAesthetic(this.empView.getNameButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.RED);
			this.colorMap.put(this.empView.getNameButton().getText(), "red");
		}
		this.txDataToLabels(empArr);
	}
	
	/**
	 * this method first checks to see what color is attached to the button name, and depending on the color
	 * a Min/Max sort of the Management employee list will take place based on the parameter of the button's name. 
	 * Min sort = button will turn green. Max sort = button will turn red. Method will neutralize other button colors as well as
	 * calling functions within the controller to re-populate the data within the view respective to the sorting that just
	 * occured
	 * 
	 * @see Management
	 */
	public void sortByDate() {
		ArrayList<Employee> empArr = this.manager.getEmployeeArrayList();
		this.empView.neutralizeButtonColors();
		
		if (this.colorMap.get(this.empView.getHireDateButton().getText()).equalsIgnoreCase("red")) {
			empArr.sort((Employee e1, Employee e2) -> 
						e1.getFormattedDate().compareTo(e2.getFormattedDate()));
			this.redColorMap();
			this.empView.setButtonAesthetic(this.empView.getHireDateButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.GREEN);
			this.colorMap.put(this.empView.getHireDateButton().getText(), "green");
			
		} else {
			empArr.sort((Employee e1, Employee e2) -> 
						e2.getFormattedDate().compareTo(e1.getFormattedDate()));
			this.empView.setButtonAesthetic(this.empView.getHireDateButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.RED);
			this.colorMap.put(this.empView.getHireDateButton().getText(), "red");
		}
		this.txDataToLabels(empArr);
	}
	
	/**
	 * this method first checks to see what color is attached to the button name, and depending on the color
	 * a Min/Max sort of the Management employee list will take place based on the parameter of the button's name. 
	 * Min sort = button will turn green. Max sort = button will turn red. Method will neutralize other button colors as well as
	 * calling functions within the controller to re-populate the data within the view respective to the sorting that just
	 * occured
	 * 
	 * @see Management
	 */
	public void sortByDepartment() {
		ArrayList<Employee> empArr = this.manager.getEmployeeArrayList();
		this.empView.neutralizeButtonColors();
		
		if (this.colorMap.get(this.empView.getDepartmentButton().getText()).equalsIgnoreCase("red")) {
			empArr.sort((Employee e1, Employee e2) -> 
						e1.getDepartment().compareTo(e2.getDepartment()));
			this.redColorMap();
			this.empView.setButtonAesthetic(this.empView.getDepartmentButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.GREEN);
			this.colorMap.put(this.empView.getDepartmentButton().getText(), "green");
		} else {
			empArr.sort((Employee e1, Employee e2) -> 
						e2.getDepartment().compareTo(e1.getDepartment()));
			this.empView.setButtonAesthetic(this.empView.getDepartmentButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.RED);
			this.colorMap.put(this.empView.getDepartmentButton().getText(), "red");
		}
		this.txDataToLabels(empArr);
	}
	
	/**
	 * this method first checks to see what color is attached to the button name, and depending on the color
	 * a Min/Max sort of the Management employee list will take place based on the parameter of the button's name. 
	 * Min sort = button will turn green. Max sort = button will turn red. Method will neutralize other button colors as well as
	 * calling functions within the controller to re-populate the data within the view respective to the sorting that just
	 * occured
	 * 
	 * @see Management
	 */
	public void sortByPosition() {
		ArrayList<Employee> empArr = this.manager.getEmployeeArrayList();
		this.empView.neutralizeButtonColors();
		
		if (this.colorMap.get(this.empView.getPositionButton().getText()).equalsIgnoreCase("red")) {
			empArr.sort((Employee e1, Employee e2) -> 
						e1.getPosition().compareTo(e2.getPosition()));
			this.redColorMap();
			this.empView.setButtonAesthetic(this.empView.getPositionButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.GREEN);
			this.colorMap.put(this.empView.getPositionButton().getText(), "green");

		} else {
			empArr.sort((Employee e1, Employee e2) -> 
						e2.getPosition().compareTo(e1.getPosition()));
			this.empView.setButtonAesthetic(this.empView.getPositionButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.RED);
			this.colorMap.put(this.empView.getPositionButton().getText(), "red");
		}
		this.txDataToLabels(empArr);
	}
	
	/**
	 * this method first checks to see what color is attached to the button name, and depending on the color
	 * a Min/Max sort of the Management employee list will take place based on the parameter of the button's name. 
	 * Min sort = button will turn green. Max sort = button will turn red. Method will neutralize other button colors as well as
	 * calling functions within the controller to re-populate the data within the view respective to the sorting that just
	 * occured
	 * 
	 * @see Management
	 */
	public void sortByTrainingHours() {
		ArrayList<Employee> empArr = this.manager.getEmployeeArrayList();
		this.empView.neutralizeButtonColors();
		
		if (this.colorMap.get(this.empView.getTrainingHoursButton().getText()).equalsIgnoreCase("red")) {
			empArr.sort((Employee e1, Employee e2) -> 
						e1.getTrainingHours() - e2.getTrainingHours());
			this.redColorMap();
			this.empView.setButtonAesthetic(this.empView.getTrainingHoursButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.GREEN);
			this.colorMap.put(this.empView.getTrainingHoursButton().getText(), "green");

		} else {
			empArr.sort((Employee e1, Employee e2) -> 
						e2.getTrainingHours() - e1.getTrainingHours());
			this.empView.setButtonAesthetic(this.empView.getTrainingHoursButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.RED);
			this.colorMap.put(this.empView.getTrainingHoursButton().getText(), "red");
		}
		this.txDataToLabels(empArr);
	}
	
	public void getUpdatedEmployeeInfo() {
		String names[] = new String[2];
		ArrayList<String> empInfo = new ArrayList<String>();
		int id = -1; 
		int row, col;
		
		for (row = 1; row < this.empView.getGridPaneHeight(); row++) {
			
			if (this.empView.getNodes()[row][0] instanceof CheckBox && ((CheckBox)this.empView.getNodes()[row][0]).isSelected()) {
				for (col = 1; col < this.empView.getGridPaneWidth(); col++) {
					
					if (this.empView.getNodes()[row][col] instanceof Label) {
						id = Integer.parseInt(((Label)this.empView.getNodes()[row][col]).getText().trim());
						
					} else if (this.empView.getNodes()[row][col] instanceof TextField && col == 2) {
						names = (((TextField)this.empView.getNodes()[row][col]).getText().isEmpty())? 
							 nameDeformatter(((TextField)this.empView.getNodes()[row][col]).getPromptText().trim()) :
							 nameDeformatter(((TextField)this.empView.getNodes()[row][col]).getText().trim());
							 
						if (names.length == 2) {
							empInfo.add(names[1]);
							empInfo.add(names[0]);
						}
						
					} else if (this.empView.getNodes()[row][col] instanceof TextField) {
						empInfo.add(
							(((TextField)this.empView.getNodes()[row][col]).getText().isEmpty())?
									((TextField)this.empView.getNodes()[row][col]).getPromptText().trim() :
									((TextField)this.empView.getNodes()[row][col]).getText().trim());
								
					} else if (this.empView.getNodes()[row][col] instanceof DatePicker) {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");
						empInfo.add(
								(((DatePicker)this.empView.getNodes()[row][col]).getValue() != null)?
										((DatePicker)this.empView.getNodes()[row][col]).getValue().format(formatter) :
										((DatePicker)this.empView.getNodes()[row][col]).getPromptText().trim());
						
					} else if (this.empView.getNodes()[row][col] instanceof ChoiceBox) {
						empInfo.add(
								(ChoiceBox.class.cast(this.empView.getNodes()[row][col])).getValue().toString()
								);
					}
				}
				
				this.manager.updateEmployee(id, empInfo);
			}	
		}
		this.manager.saveEmployees();
	}
	
	
	public String[] nameDeformatter(String name) throws IllegalArgumentException {
		if (!name.contains(",")) {
			throw new IllegalArgumentException("Name must have a comma separating last and first name.");
		}
		String str[] = new String[2];
		str = name.split(", ");
		return str;
	}
	
	/**
	 * method sets up a new HashMap and attaches a default color of "red" to each entry key, a sorting button name. 
	 */
	public void initColorMap() {
		this.colorMap = new HashMap<String, String>();
		this.colorMap.put(this.empView.getIDSort().getText(), "red");
		this.colorMap.put(this.empView.getNameButton().getText(), "red");
		this.colorMap.put(this.empView.getHireDateButton().getText(), "red");
		this.colorMap.put(this.empView.getDepartmentButton().getText(), "red");
		this.colorMap.put(this.empView.getPositionButton().getText(), "red");
		this.colorMap.put(this.empView.getTrainingHoursButton().getText(), "red");
	}
	
	/**
	 *  makes all color mappings red. Used when a new sort is selected, forcing a Min sort every time a new button is selected. 
	 */
	public void redColorMap() {
		this.colorMap.keySet().stream()
							  .forEach( key -> {
								  this.colorMap.put(key, "red");
							  });
	
	}	
}
