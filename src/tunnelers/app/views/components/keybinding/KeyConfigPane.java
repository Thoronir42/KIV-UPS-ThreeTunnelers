package tunnelers.app.views.components.keybinding;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import tunnelers.app.controls.FxControlsManager;
import tunnelers.core.player.controls.ControlInput;
import tunnelers.core.player.controls.InputAction;

import java.util.ArrayList;

public class KeyConfigPane extends TableView<KeyTableRow> {

	public static KeyConfigPane create(FxControlsManager controlSchemeManager) {
		byte[] kbLayoutIds = controlSchemeManager.getKeyboardLayoutIDs();
		KeyConfigPane kcp = new KeyConfigPane(kbLayoutIds);
		InputAction[] inputs = FxControlsManager.getEditableInputs();

		ObservableList<KeyTableRow> rows = kcp.getItems();
		KeyTableRow.setKLIDs(kbLayoutIds);
		for (InputAction input : inputs) {
			KeyTableRow row = new KeyTableRow(input, kbLayoutIds, controlSchemeManager);
			rows.add(row);
		}
		return kcp;
	}

	private final byte[] keyboardLayoutIds;

	private KeyConfigPane(byte[] keyboardLayoutIds) {
		super();
		this.keyboardLayoutIds = keyboardLayoutIds;
		this.setEditable(true);
		this.initColumns();
		this.initRows();
	}

	private void initColumns() {
		TableColumn<KeyTableRow, String> labelColumn = new TableColumn<>("Akce");
		labelColumn.setMinWidth(96);
		labelColumn.setMaxWidth(96);
		labelColumn.setEditable(false);
		labelColumn.setSortable(false);
		labelColumn.setResizable(false);
		labelColumn.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(column.getValue().inputAction.getLabel()));

		ArrayList<TableColumn<KeyTableRow, KeyCode>> layoutColumns = new ArrayList<>();
		KeyTableRow.inputColumnOffset = 1;

		for (int i = 0; i < this.keyboardLayoutIds.length; i++) {
			byte klid = this.keyboardLayoutIds[i];
			TableColumn<KeyTableRow, KeyCode> layoutColumn = new TableColumn<>("Klávesnice " + (klid + 1));
			layoutColumn.setMinWidth(104);
			layoutColumn.setEditable(true);
			layoutColumn.setSortable(false);
			layoutColumn.setCellValueFactory(new KeyBindCellValueFactory(i));
			layoutColumn.setCellFactory(cell -> new KeyBindCell());
			layoutColumns.add(layoutColumn);
		}

		this.getColumns().add(labelColumn);
		this.getColumns().addAll(layoutColumns);
	}

	private void initRows() {
		this.setFixedCellSize(40);
		this.prefHeightProperty().bind(this.fixedCellSizeProperty().multiply(Bindings.size(this.getItems()).add(1.25)));
		this.minHeightProperty().bind(this.prefHeightProperty());
		this.maxHeightProperty().bind(this.prefHeightProperty());
	}

	protected void refreshRowFor(ControlInput oldOccurrence) {
		InputAction action = oldOccurrence.getInput();

		byte col = KeyTableRow.schemeIdToCol(oldOccurrence.getControlScheme().getID());
		ObservableList<KeyTableRow> rows = this.getItems();
		for (int r = 0; r < rows.size(); r++) {
			KeyTableRow row = rows.get(r);
			rows.set(r, row);
		}
	}
}

class KeyBindCellValueFactory implements Callback<TableColumn.CellDataFeatures<KeyTableRow, KeyCode>, ObservableValue<KeyCode>> {

	private final int columnId;

	KeyBindCellValueFactory(int columnId) {
		this.columnId = columnId + KeyTableRow.inputColumnOffset;
	}

	@Override
	public ObservableValue<KeyCode> call(final TableColumn.CellDataFeatures<KeyTableRow, KeyCode> param) {
		KeyCode kc = param.getValue().getKeyCode(columnId);
		return new ReadOnlyObjectWrapper<>(kc);

	}

}
