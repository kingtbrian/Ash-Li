package application.Control.Employee;

import java.util.HashMap;

import application.Model.Employee;
import application.Model.Management;
import application.View.PrimaryView;
import application.View.EmployeeView.EmployeeViewer;
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
		System.out.println(this.manager.getEmpList().toString());
		this.empView = new EmployeeViewer(primaryView.getScene());
		this.initColorMap();
		this.initData();
	}
	
	/**
	 * Method calls multiple functions that set the handles for the buttons within the view, set up the labels
	 * for viewing within the view's gridpane, and sets the view as the child pane within the PrimaryView. 
	 */
	public void showForm() {
		this.setViewHandles();
		this.empView.initLabels(this.manager.getEmpList().size());
		this.txDataToLabels();
		this.empView.showEmployeeView();
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
			this.txDataToLabels();
			this.empView.showEmployeeView();
			this.setViewerForm();
		});
		
		this.empView.getHireDateButton().setOnAction(event -> {
			this.sortByDate();
			this.txDataToLabels();
			this.empView.showEmployeeView();
			this.setViewerForm();
		});
		
		this.empView.getDepartmentButton().setOnAction(event -> {
			this.sortByDepartment();;
			this.txDataToLabels();
			this.empView.showEmployeeView();
			this.setViewerForm();
		});
		
		this.empView.getPositionButton().setOnAction(event -> {
			this.sortByPosition();
			this.txDataToLabels();
			this.empView.showEmployeeView();
			this.setViewerForm();
		});
		
		this.empView.getTrainingHoursButton().setOnAction(event -> {
			this.sortByTrainingHours();
			this.txDataToLabels();
			this.empView.showEmployeeView();
			this.setViewerForm();
		});
		
		return;
	}
	
	/**
	 * sets the 2d Array to the size of employees within Management
	 */
	public void initData() {
		this.data = new String[this.manager.getEmpList().size()][];
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
	public void txDataToLabels() {
		System.out.println("in txDataToLabels");
		index = 0;
		this.manager.getEmpList()
					.stream()
					.forEach( employee -> {
						this.data[index] = new String[5];
						System.out.println(employee.toString());
						System.out.println(nameFormatter(employee));
						this.data[index][0] = this.nameFormatter(employee);
						this.data[index][1] = employee.getFormattedDate();
						this.data[index][2] = employee.getDepartment();
						this.data[index][3] = employee.getPosition();
						this.data[index][4] = employee.getTrainingHours()+ "";
						
						System.out.println(data[index][0] 
										 + data[index][1]
										 + data[index][2]
										 + data[index][3]
										 + data[index][4]);
						index++;
					});
		index = 0;
		this.empView.populateDataInLabels(this.data);
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
	public void sortByName() {
		this.empView.neutralizeButtonColors();
		
		if (this.colorMap.get(this.empView.getNameButton().getText()).equalsIgnoreCase("red")) {
			
			this.manager.getEmpList().sort((Employee e1, Employee e2) -> 
						this.nameFormatter(e1).compareTo(this.nameFormatter(e2)));
			
			this.redColorMap();
			this.empView.setButtonAesthetic(this.empView.getNameButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.GREEN);
			this.colorMap.put(this.empView.getNameButton().getText(), "green");
		
		} else {
			
			this.manager.getEmpList().sort((Employee e1, Employee e2) -> 
							this.nameFormatter(e2).compareTo(this.nameFormatter(e1)));

			this.empView.setButtonAesthetic(this.empView.getNameButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.RED);
			this.colorMap.put(this.empView.getNameButton().getText(), "red");
		}
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
		this.empView.neutralizeButtonColors();
		
		if (this.colorMap.get(this.empView.getHireDateButton().getText()).equalsIgnoreCase("red")) {
			
			this.manager.getEmpList().sort((Employee e1, Employee e2) -> 
						e1.getFormattedDate().compareTo(e2.getFormattedDate()));
			
			this.redColorMap();
			this.empView.setButtonAesthetic(this.empView.getHireDateButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.GREEN);
			this.colorMap.put(this.empView.getHireDateButton().getText(), "green");
			
		} else {
			
			this.manager.getEmpList().sort((Employee e1, Employee e2) -> 
						e2.getFormattedDate().compareTo(e1.getFormattedDate()));
		
			this.empView.setButtonAesthetic(this.empView.getHireDateButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.RED);
			this.colorMap.put(this.empView.getHireDateButton().getText(), "red");
			
		}
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
		this.empView.neutralizeButtonColors();
		if (this.colorMap.get(this.empView.getDepartmentButton().getText()).equalsIgnoreCase("red")) {
			
			this.manager.getEmpList().sort((Employee e1, Employee e2) -> 
						e1.getDepartment().compareTo(e2.getDepartment()));
			
			this.redColorMap();
			this.empView.setButtonAesthetic(this.empView.getDepartmentButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.GREEN);
			
			this.colorMap.put(this.empView.getDepartmentButton().getText(), "green");

		} else {
			
			this.manager.getEmpList().sort((Employee e1, Employee e2) -> 
						e2.getDepartment().compareTo(e1.getDepartment()));
			
			this.empView.setButtonAesthetic(this.empView.getDepartmentButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.RED);

			this.colorMap.put(this.empView.getDepartmentButton().getText(), "red");
		}
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
		this.empView.neutralizeButtonColors();
		if (this.colorMap.get(this.empView.getPositionButton().getText()).equalsIgnoreCase("red")) {
			
			this.manager.getEmpList().sort((Employee e1, Employee e2) -> 
						e1.getPosition().compareTo(e2.getPosition()));
			
			this.redColorMap();
			this.empView.setButtonAesthetic(this.empView.getPositionButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.GREEN);
			
			this.colorMap.put(this.empView.getPositionButton().getText(), "green");

		} else {
			
			this.manager.getEmpList().sort((Employee e1, Employee e2) -> 
						e2.getPosition().compareTo(e1.getPosition()));
			
			this.empView.setButtonAesthetic(this.empView.getPositionButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.RED);

			this.colorMap.put(this.empView.getPositionButton().getText(), "red");
		}
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
		this.empView.neutralizeButtonColors();
		if (this.colorMap.get(this.empView.getTrainingHoursButton().getText()).equalsIgnoreCase("red")) {
			
			this.manager.getEmpList().sort((Employee e1, Employee e2) -> 
						e1.getTrainingHours() - e2.getTrainingHours());
			
			this.redColorMap();
			this.empView.setButtonAesthetic(this.empView.getTrainingHoursButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.GREEN);
			
			this.colorMap.put(this.empView.getTrainingHoursButton().getText(), "green");

		} else {
			
			this.manager.getEmpList().sort((Employee e1, Employee e2) -> 
						e2.getTrainingHours() - e1.getTrainingHours());
			
			this.empView.setButtonAesthetic(this.empView.getTrainingHoursButton()
										  , this.empView.getNodeWidth()
										  , this.empView.getNodeHeight()
										  , this.empView.getFontSize()
										  , Color.RED);

			this.colorMap.put(this.empView.getTrainingHoursButton().getText(), "red");
		}
	}
	
	/**
	 * method sets up a new HashMap and attaches a default color of "red" to each entry key, a sorting button name. 
	 */
	public void initColorMap() {
		this.colorMap = new HashMap<String, String>();
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
