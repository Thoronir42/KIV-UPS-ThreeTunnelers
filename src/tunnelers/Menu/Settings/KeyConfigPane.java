package tunnelers.Menu.Settings;

import java.util.ArrayList;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.Game.IO.AControlScheme;
import tunnelers.Game.IO.InputAction;
import tunnelers.Game.IO.ControlInput;

/**
 *
 * @author Stepan
 */
public class KeyConfigPane extends TableView<KeyTableRow>{

	public static KeyConfigPane create(ControlSchemeManager controlSchemeManager){
		byte[] kbLayoutIds = ControlSchemeManager.getKeyboardLayoutIDs();
		KeyConfigPane kcp = new KeyConfigPane(kbLayoutIds);
		InputAction[] inputs = controlSchemeManager.getEditableInputs();
		
		for (InputAction input : inputs) {
			KeyTableRow row = new KeyTableRow(input, kbLayoutIds, controlSchemeManager);
			kcp.getItems().add(row);
		}
		for (int p = 0; p < kbLayoutIds.length; p++) {
			byte klid = kbLayoutIds[p];
			AControlScheme controlScheme = controlSchemeManager.getKeyboardScheme(klid);
			
		}
		return kcp;
	}
	
	byte[] keyboardLayoutIds;
	
	private KeyConfigPane(byte[] keyboardLayoutIds) {
		super();
		this.keyboardLayoutIds = keyboardLayoutIds;
		this.initRows();
		
		
	}
	
	private void initRows(){
		TableColumn<KeyTableRow, String> labelColumn = new TableColumn<>("Action");
		labelColumn.setMinWidth(96); labelColumn.setMaxWidth(96);
		labelColumn.setEditable(false); labelColumn.setSortable(false);
		labelColumn.setResizable(false);
		labelColumn.setCellValueFactory(column -> {
			return new ReadOnlyObjectWrapper<>(column.getValue().inputAction.getLabel());
		});
		
		ArrayList<TableColumn<KeyTableRow, KeyCode>> layoutColumns = new ArrayList<>();
		KeyTableRow.inputColumnOffset = 1;
		for(int i = 0; i < this.keyboardLayoutIds.length; i++){
			byte klid = this.keyboardLayoutIds[i];
			TableColumn<KeyTableRow, KeyCode> layoutColumn = new TableColumn<>("Keyboard " + klid);
			layoutColumn.setMinWidth(96);
			layoutColumn.setCellValueFactory( new KeyBindCellValueFactory(i));
			//layoutColumn.setCellFactory(TextFieldTableCell.forTableColumn());
			layoutColumns.add(layoutColumn);
		}
		
		this.getColumns().add(labelColumn);
		this.getColumns().addAll(layoutColumns);
	}
}

class KeyBindCellValueFactory implements Callback<TableColumn.CellDataFeatures<KeyTableRow, KeyCode>, ObservableValue<KeyCode>>{

	final int columnId;
	
	public KeyBindCellValueFactory(int columnId){
		this.columnId = columnId + KeyTableRow.inputColumnOffset;
	}
	
	@Override
	public ObservableValue<KeyCode> call(final TableColumn.CellDataFeatures<KeyTableRow, KeyCode> param) {
		//ControlInput ci = param.getValue().getControlInput(columnId);
		KeyCode kc = param.getValue().getKeyCode(columnId);
		return new ReadOnlyObjectWrapper<>(kc);

	}
	
}