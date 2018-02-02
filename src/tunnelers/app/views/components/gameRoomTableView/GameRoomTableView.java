package tunnelers.app.views.components.gameRoomTableView;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import tunnelers.app.views.components.gameRoomTreeView.GameRoomViewWrapper;
import tunnelers.app.views.components.roomListing.GameRoomListEvent;

public class GameRoomTableView extends TableView<GameRoomViewWrapper> {

	private EventHandler<GameRoomListEvent> onRoomSelected;

	public GameRoomTableView() {
		this.initColumns();
		this.setOnMouseClicked(this::onMouseClick);
	}

	private void initColumns() {
		TableColumn<GameRoomViewWrapper, String> columnName = new TableColumn<>("Název");
		columnName.setCellValueFactory(c -> {
			GameRoomViewWrapper item = c.getValue();
			return new ReadOnlyStringWrapper(item.getTitle());
		});
		columnName.setPrefWidth(128);

		TableColumn<GameRoomViewWrapper, String> columnPlayers = new TableColumn<>("Hráčů");
		columnPlayers.setCellValueFactory(c -> {
			GameRoomViewWrapper item = c.getValue();
			return new ReadOnlyStringWrapper(item.getOccupancy());
		});

		columnPlayers.setPrefWidth(72);
		columnPlayers.setResizable(false);


		TableColumn<GameRoomViewWrapper, String> columnGamemode = new TableColumn<>("Mód");
		columnGamemode.setCellValueFactory(c -> {
			GameRoomViewWrapper item = c.getValue();
			return new ReadOnlyStringWrapper(item.getGameModeView().toString());
		});

		columnGamemode.setPrefWidth(72);
		columnGamemode.setResizable(false);

		TableColumn<GameRoomViewWrapper, String> columnFlags = new TableColumn<>("Příznaky");
		columnFlags.setCellValueFactory(c -> {
			GameRoomViewWrapper item = c.getValue();
			return new ReadOnlyStringWrapper(item.describeFlags());
		});

		Insets insets = this.getInsets();
		double horizontalInsets = insets.getLeft() + insets.getRight() + 2;

		columnFlags.prefWidthProperty().bind(
				this.widthProperty()
						.subtract(columnName.widthProperty())
						.subtract(columnPlayers.widthProperty())
						.subtract(columnGamemode.widthProperty())
						.subtract(horizontalInsets)
		);

		ObservableList<TableColumn<GameRoomViewWrapper, ?>> columns = this.getColumns();
		columns.add(columnName);
		columns.add(columnPlayers);
		columns.add(columnGamemode);
		columns.add(columnFlags);
	}

	public void clearItems() {
		this.getItems().clear();
	}

	private void onMouseClick(MouseEvent e) {
		GameRoomViewWrapper selected = this.getSelectionModel().getSelectedItem();

		if (this.onRoomSelected == null) {
			return;
		}

		if (selected == null || !(selected.isGame())) {
			return;
		}
		if (e.getClickCount() == 2) {
			this.onRoomSelected.handle(new GameRoomListEvent(selected));
		}
	}

	public void setOnRoomSelected(EventHandler<GameRoomListEvent> onRoomSelected) {
		this.onRoomSelected = onRoomSelected;
	}
}
