package application.Control.Department;

import java.util.ArrayList;
import java.util.HashMap;

import application.Model.Department;
import application.Model.Management;
import application.View.PrimaryView;
import application.View.DepartmentView.DepartmentViewer;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class DepartmentViewController {
	private PrimaryView primaryView;
	private Management manager;
	private DepartmentViewer deptView;
	private HashMap<String, String> colorMap; // used for determining color of buttons; sorting up = green, sorting down = red
	private ArrayList<String> deptKeys;
	private String[][] data;
	private int index, row, col;
	
	
	public DepartmentViewController(PrimaryView primaryView, Management manager) {
		this.primaryView = primaryView;
		this.manager = manager;
		this.deptView = new DepartmentViewer(primaryView.getScene(), this.manager.getDeptList().size());
		this.initColorMap();
		this.initData();
	}
	
	public void showForm() {
		this.setViewHandles();
		this.deptView.initLabels(this.manager.getDeptList().size());
		this.deptView.initTextAreaList(this.manager.getDeptList().size());
		this.deptView.initListViewList(this.manager.getDeptList().size());
		this.deptView.populateListViews(this.manager.getDeptList());
		this.txDataToLabels(this.manager.getDeptList());
		this.setViewerForm();
	}
	
	public void setViewerForm() {
		this.primaryView.setSecondaryView(this.deptView.showEmployeeView());
	}
	
	public void setViewHandles() {
		this.deptView.getBackButton().setOnAction(event -> {
			this.primaryView.setDefaultSecondaryView();
		});
		
		this.deptView.getNameButton().setOnAction(event -> {
			this.deptView.clearTextLists();
			this.sortByName();
			this.setViewerForm();
		});
		
		this.deptView.getAbbrButton().setOnAction(event -> {
			this.deptView.clearTextLists();
			this.sortByAbbr();
			this.setViewerForm();
		});
		
		this.deptView.getSizeSortButton().setOnAction(event -> {
			this.deptView.clearTextLists();
			this.sortBySize();
			this.setViewerForm();
		});
		
		this.deptView.getUpdateButton().setOnAction(event-> {
			this.deptKeys = this.deptView.setUpdateTextFields(this.manager.getDeptList());
			this.deptView.getButtonGroup().stream()
						.forEach( button -> {
							button.setDisable(true);
			});
			this.deptView.disableLabelHeaders(true);
		});
		
		this.deptView.getUpdateAllButton().setOnAction(event -> {
			this.getUpdatedDepartmentInfo();
			this.deptView.getButtonGroup().stream()
						.forEach( button -> {
							button.setDisable(false);
			});
			this.deptView.disableLabelHeaders(false);
			this.deptKeys.clear();
			this.showForm();
		});

	}
	
	public void getUpdatedDepartmentInfo() {
		ArrayList<String> deptInfo = new ArrayList<String>();
		int keyIndex = 0;  
		System.out.println(this.deptKeys);
		for (row = 1; row < this.deptView.getGridPaneHeight(); row++) {
			if (this.deptView.getNodes()[row][0] instanceof CheckBox && ((CheckBox)this.deptView.getNodes()[row][0]).isSelected()) {
				
				for (col = 1; col < this.deptView.getGridPaneWidth(); col++) {
					if (this.deptView.getNodes()[row][col] instanceof Label) {
						deptInfo.add(((Label)this.deptView.getNodes()[row][col]).getText().trim());
						
					} else if (this.deptView.getNodes()[row][col] instanceof TextField) {
						deptInfo.add(
								(((TextField)this.deptView.getNodes()[row][col]).getText().isEmpty())?
								((TextField)this.deptView.getNodes()[row][col]).getPromptText().trim() :
								((TextField)this.deptView.getNodes()[row][col]).getText().trim());
						
					} else if (this.deptView.getNodes()[row][col] instanceof TextArea) {
						deptInfo.add(
								(((TextArea)this.deptView.getNodes()[row][col]).getText().isEmpty())?
								"" :
								((TextArea)this.deptView.getNodes()[row][col]).getText().trim());
					} 
				}
				
				this.manager.updateDepartment(deptInfo, this.deptKeys.get(keyIndex++));
				
				deptInfo.clear();
			} 
		}
		this.manager.saveDepartments();
		this.manager.saveEmployees();
	}
	
	public void initData() {
		this.data = new String[this.manager.getDeptList().size()][];
	}
	
	public void txDataToLabels(ArrayList<Department> deptList) {
		index = 0;
		deptList
					.stream()
					.forEach(department -> {
						this.data[index] = new String[4];
						this.data[index][0] = department.getName();
						this.data[index][1] = department.getAbbreviation();
						this.data[index][2] = department.getDepartmentSize() + "";
						this.data[index][3] = department.getPopulation();
						index++;
					});
		index = 0;
		this.deptView.populateDataInLabels(this.data);
	}
	
	public void sortByName() {
		ArrayList<Department> deptArr = this.manager.getDeptList();
		this.deptView.neutralizeButtonColors();
		
		if (this.colorMap.get(this.deptView.getNameButton().getText()).equalsIgnoreCase("red")) {
			deptArr.sort((Department d1, Department d2) -> 
							d1.getName().compareTo(d2.getName()));
			
			this.redColorMap();
			this.deptView.setButtonAesthetic(this.deptView.getNameButton()
										  , this.deptView.getNodeWidth()
										  , this.deptView.getNodeHeight()
										  , this.deptView.getFontSize()
										  , Color.GREEN);
			this.colorMap.put(this.deptView.getNameButton().getText(), "green");
			
		} else {
			deptArr.sort((Department d1, Department d2) -> 
								d2.getName().compareTo(d1.getName()));
			this.deptView.setButtonAesthetic(this.deptView.getNameButton()
										  , this.deptView.getNodeWidth()
										  , this.deptView.getNodeHeight()
										  , this.deptView.getFontSize()
										  , Color.RED);
			this.colorMap.put(this.deptView.getNameButton().getText(), "red");
		}
		this.deptView.populateListViews(deptArr);
		this.txDataToLabels(deptArr);
	}
	
	public void sortBySize() {
		ArrayList<Department> deptArr = this.manager.getDeptList();
		this.deptView.neutralizeButtonColors();
		
		if (this.colorMap.get(this.deptView.getSizeSortButton().getText()).equalsIgnoreCase("red")) {
			deptArr.sort((Department d1, Department d2) -> 
							d1.getDepartmentSize() - d2.getDepartmentSize());
			
			this.redColorMap();
			this.deptView.setButtonAesthetic(this.deptView.getSizeSortButton()
										  , this.deptView.getNodeWidth()
										  , this.deptView.getNodeHeight()
										  , this.deptView.getFontSize()
										  , Color.GREEN);
			this.colorMap.put(this.deptView.getSizeSortButton().getText(), "green");
			
		} else {
			deptArr.sort((Department d1, Department d2) -> 
							d2.getDepartmentSize() - d1.getDepartmentSize());
			this.deptView.setButtonAesthetic(this.deptView.getSizeSortButton()
										  , this.deptView.getNodeWidth()
										  , this.deptView.getNodeHeight()
										  , this.deptView.getFontSize()
										  , Color.RED);
			this.colorMap.put(this.deptView.getSizeSortButton().getText(), "red");
		}
		this.deptView.populateListViews(deptArr);
		this.txDataToLabels(deptArr);
	}
	
	public void sortByAbbr() {
		ArrayList<Department> deptArr = this.manager.getDeptList();
		this.deptView.neutralizeButtonColors();
		
		if (this.colorMap.get(this.deptView.getAbbrButton().getText()).equalsIgnoreCase("red")) {
			deptArr.sort((Department d1, Department d2) -> 
							d1.getAbbreviation().compareTo(d2.getAbbreviation()));
			
			this.redColorMap();
			this.deptView.setButtonAesthetic(this.deptView.getAbbrButton()
										  , this.deptView.getNodeWidth()
										  , this.deptView.getNodeHeight()
										  , this.deptView.getFontSize()
										  , Color.GREEN);
			this.colorMap.put(this.deptView.getAbbrButton().getText(), "green");
			
		} else {
			deptArr.sort((Department d1, Department d2) -> 
								d2.getAbbreviation().compareTo(d1.getAbbreviation()));
			this.deptView.setButtonAesthetic(this.deptView.getAbbrButton()
										  , this.deptView.getNodeWidth()
										  , this.deptView.getNodeHeight()
										  , this.deptView.getFontSize()
										  , Color.RED);
			this.colorMap.put(this.deptView.getAbbrButton().getText(), "red");
		}
		this.deptView.populateListViews(deptArr);
		this.txDataToLabels(deptArr);
	}
	
	/**
	 * method sets up a new HashMap and attaches a default color of "red" to each entry key, a sorting button name. 
	 */
	public void initColorMap() {
		this.colorMap = new HashMap<String, String>();
		this.colorMap.put(this.deptView.getNameButton().getText(), "red");
		this.colorMap.put(this.deptView.getAbbrButton().getText(), "red");
		this.colorMap.put(this.deptView.getSizeSortButton().getText(), "red");
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
