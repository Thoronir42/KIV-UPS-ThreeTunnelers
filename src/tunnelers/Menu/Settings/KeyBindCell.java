package tunnelers.Menu.Settings;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import tunnelers.Game.IO.KeyMap;

class KeyBindCell extends TableCell<KeyTableRow, KeyCode> {
	
	TextField tf_keyCode;
	KeyCode lastKeyPressed;

	public KeyBindCell(){
		super();
		lastKeyPressed = getItem();
		setText(getKeyBindString(lastKeyPressed));
	}
	
	@Override
	public void startEdit() {
		super.startEdit();
		setText(null);		
		// setting up editor
		setGraphic(createTextField());
		lastKeyPressed = null;
	}
	
	// switching from editation to presentation mode
	@Override
	public void cancelEdit() {
		super.cancelEdit();
		
		// setting label 
		setText(getKeyBindString());
		// removing date picker  
		setGraphic(null);
	}
	
	// creating date picker
	private TextField createTextField() {
		tf_keyCode = new TextField(getKeyBindString());
		tf_keyCode.setEditable(false);
		tf_keyCode.setOnAction(event -> {
			if (lastKeyPressed != null) {
				commitEdit(lastKeyPressed);
			} else {
				cancelEdit();
			}
		});
		tf_keyCode.setOnKeyPressed(event -> {
			lastKeyPressed = event.getCode();
			tf_keyCode.setText(getKeyBindString());
		});
		return tf_keyCode;
	}
	
	// setting value to the cell when model is changed or manipulated
	// this method have to be always overloaded, when new cell type is created
	@Override
	public void updateItem(KeyCode item, boolean empty) {
		// propagating change to the parent class
		super.updateItem(item, empty);
		
		// when no item is provided, cell is showing no information
		if (empty) {
			setText(getKeyBindString());
			setGraphic(null);
			return;
		}
		
		// in editation mode
		if (isEditing()) {
			// disabling label
			setText(null);
			// setting up editor
			setGraphic(tf_keyCode);
		// in presentation mode	
		} else {
			setText(getKeyBindString());
			// disabling editor
			setGraphic(null);
		}
	}
	
	private String getKeyBindString(){
		KeyTableRow row = (KeyTableRow)getTableRow().getItem();
		if(row != null){
			return getKeyBindString(getItem());
		}
		return null;
	}
	
	private String getKeyBindString(KeyCode keyCode){
		return KeyMap.codeToStr(keyCode);
	}
}