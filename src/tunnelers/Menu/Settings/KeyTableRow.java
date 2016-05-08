package tunnelers.Menu.Settings;

import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.Game.IO.AControlScheme;
import tunnelers.Game.IO.InputAction;
import tunnelers.Game.IO.ControlInput;

/**
 *
 * @author Stepan
 */
class KeyTableRow {

	static int inputColumnOffset = 0;
	static byte[] keyboardLayoutIds;

	static void setKLIDs(byte[] kbLayoutIds) {
		System.err.println("Keyboard layout IDs were already set");
		keyboardLayoutIds = kbLayoutIds;
	}

	static byte schemeIdToCol(byte id) {
		for (int i = 0; i < keyboardLayoutIds.length; i++) {
			if (keyboardLayoutIds[i] == id) {
				return id;
			}
		}
		return -1;
	}

	InputAction inputAction;
	final ControlInput[] inputs;
	ControlSchemeManager controlSchemeManager;

	public KeyTableRow(InputAction input, byte[] keyboardLayoutIds, ControlSchemeManager csm) {
		this.inputAction = input;
		this.controlSchemeManager = csm;

		this.inputs = new ControlInput[keyboardLayoutIds.length];
		for (byte i = 0; i < keyboardLayoutIds.length; i++) {
			inputs[i] = new ControlInput(controlSchemeManager.getKeyboardScheme(i), input);
		}

	}

	public ControlInput getControlInput(int i) {
		return this.inputs[i - inputColumnOffset];
	}

	KeyCode getKeyCode(int i) {
		return this.controlSchemeManager.getKeyCode(this.inputs[i - inputColumnOffset]);
	}

	@Override
	public String toString() {
		return "KeyTableRow for " + inputAction;
	}

	void setKeyCode(KeyBindCell sender, KeyCode newValue) {
		TableColumn<KeyTableRow, KeyCode> column = sender.getTableColumn();
		int colId = column.getTableView().getColumns().indexOf(column) - inputColumnOffset;
		AControlScheme scheme = controlSchemeManager.getKeyboardScheme(keyboardLayoutIds[colId]);
		ControlInput input = new ControlInput(scheme, inputAction);

		ControlInput oldOccurence = controlSchemeManager.replaceKeyInput(newValue, input);

		if (oldOccurence != null) {
			KeyConfigPane pane = (KeyConfigPane) sender.getTableView();
			pane.refreshRowFor(oldOccurence);
		}
	}

}
