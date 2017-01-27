package tunnelers.app.views.components.keybinding;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import tunnelers.app.controls.FxKeyMap;

class KeyBindCell extends TableCell<KeyTableRow, KeyCode> {

	TextField tf_keyCode;
	KeyCode lastKeyPressed;

	public KeyBindCell() {
		super();
		lastKeyPressed = getItem();
		setText(getKeyBindString(lastKeyPressed));
		this.setAlignment(Pos.CENTER);
	}

	@Override
	public void startEdit() {
		super.startEdit();
		setText(null);
		// setting up editor
		setGraphic(createTextField());
		this.tf_keyCode.requestFocus();
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

	@Override
	public void commitEdit(KeyCode newValue) {
		super.commitEdit(newValue);
		KeyTableRow row = (KeyTableRow) getTableRow().getItem();
		row.setKeyCode(this, newValue);
		//row.controlSchemeManager.replaceKeyInput(newValue, row.getControlInput(0));
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
			KeyCode pressed = event.getCode();
			switch (event.getCode()) {
				case ESCAPE:
					cancelEdit();
					return;
				case ENTER:
					commitEdit(lastKeyPressed);
			}
			commitEdit(pressed); 
			if(true)
				return;
			lastKeyPressed = pressed;
			
			tf_keyCode.setText(getKeyBindString(pressed));
			event.consume();
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

	private String getKeyBindString() {
		KeyTableRow row = (KeyTableRow) getTableRow().getItem();
		if (row != null) {
			return getKeyBindString(getItem());
		}
		return null;
	}

	private String getKeyBindString(KeyCode keyCode) {
		return FxKeyMap.codeToStr(keyCode);
	}
}
