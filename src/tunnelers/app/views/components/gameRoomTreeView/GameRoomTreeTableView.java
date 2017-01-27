package tunnelers.app.views.components.gameRoomTreeView;

import java.util.Collection;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import tunnelers.app.views.serverList.GameMode;
import tunnelers.app.views.components.roomListing.IGameRoomListItem;

/**
 *
 * @author Stepan
 */
public class GameRoomTreeTableView extends TreeTableView<IGameRoomListItem> {

	final TreeItem<IGameRoomListItem> root;

	public GameRoomTreeTableView() {
		this(new TreeItem<>(new GameRoomTreeViewRoot()));
	}

	private GameRoomTreeTableView(TreeItem<IGameRoomListItem> root) {
		super(root);
		this.root = root;
		this.showRootProperty().set(false);
		clearItems();
		initColumns();
	}

	public void clearItems() {
		root.getChildren().clear();
		for (GameMode d : GameMode.values()) {
			root.getChildren().add(new TreeItem<>(new GameRoomViewWrapper(d)));
		}
		root.setExpanded(true);
	}

	private void initColumns() {
		TreeTableColumn<IGameRoomListItem, String> columnName = new TreeTableColumn<>("Název");
		columnName.setCellValueFactory(c -> {
			IGameRoomListItem item = c.getValue().getValue();
			return new ReadOnlyStringWrapper(item.getTitle());
		});
		TreeTableColumn<IGameRoomListItem, String> columnPlayers = new TreeTableColumn<>("Hráčů");
		columnPlayers.setCellValueFactory(c -> {
			IGameRoomListItem item = c.getValue().getValue();
			return new ReadOnlyStringWrapper(item.getOccupancy());
		});
		
		columnPlayers.setPrefWidth(72);
		columnPlayers.setResizable(false);

		TreeTableColumn<IGameRoomListItem, String> columnFlags = new TreeTableColumn<>("Příznaky");
		columnFlags.setCellValueFactory(c -> {
			IGameRoomListItem item = c.getValue().getValue();
			return new ReadOnlyStringWrapper(item.describeFlags());
		});
		
		Insets insets = this.getInsets();
		double horizontalInsets = insets.getLeft() + insets.getRight() + 2;
		
		columnFlags.prefWidthProperty().bind(
				this.widthProperty()
						.subtract(columnName.widthProperty())
						.subtract(columnPlayers.widthProperty())
						.subtract(horizontalInsets)
		);

		this.getColumns().addAll(columnName, columnPlayers, columnFlags);

	}

	public void addAll(Collection<IGameRoomListItem> items) {
		for (IGameRoomListItem item : items) {
			add(item);
		}
	}

	public void add(IGameRoomListItem gr) {
		TreeItem<IGameRoomListItem> result = root.getChildren().stream().filter(child -> child.getValue().getGameModeView() == gr.getGameModeView()).findFirst().orElse(null);
		if (result == null) {
			System.err.println("failed adding game room tree view item");
			return;
		}

		result.getChildren().add(new TreeItem<>(gr));
	}

	public IGameRoomListItem getSelectedItem() {
		TreeItem<IGameRoomListItem> selected = getSelectionModel().getSelectedItem();
		if (selected == null) {
			return null;
		}
		return selected.getValue();
	}
}
