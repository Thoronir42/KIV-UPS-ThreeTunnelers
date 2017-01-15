package tunnelers.app.views.serverList.GameRoomView;

import java.util.Collection;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import tunnelers.app.views.serverList.GameRoomDifficulty;

/**
 *
 * @author Stepan
 */
public class GameRoomTreeTableView extends TreeTableView<IGameRoomTreeViewItem> {

	final TreeItem<IGameRoomTreeViewItem> root;

	public GameRoomTreeTableView() {
		this(new TreeItem<>(new GameRoomTreeViewRoot()));
	}

	private GameRoomTreeTableView(TreeItem<IGameRoomTreeViewItem> root) {
		super(root);
		this.root = root;
		this.showRootProperty().set(false);
		clearItems();
		initColumns();
	}

	public void clearItems() {
		root.getChildren().clear();
		for (GameRoomDifficulty d : GameRoomDifficulty.values()) {
			root.getChildren().add(new TreeItem<>(new GameRoomViewWrapper(d)));
		}
		root.setExpanded(true);
	}

	private void initColumns() {
		TreeTableColumn<IGameRoomTreeViewItem, String> columnName = new TreeTableColumn<>("Název");
		columnName.setCellValueFactory(c -> {
			IGameRoomTreeViewItem item = c.getValue().getValue();
			return new ReadOnlyStringWrapper(item.getTitle());
		});
		TreeTableColumn<IGameRoomTreeViewItem, String> columnPlayers = new TreeTableColumn<>("Hráčů");
		columnPlayers.setCellValueFactory(c -> {
			IGameRoomTreeViewItem item = c.getValue().getValue();
			return new ReadOnlyStringWrapper(item.getOccupancy());
		});
		
		columnPlayers.setPrefWidth(72);
		columnPlayers.setResizable(false);

		TreeTableColumn<IGameRoomTreeViewItem, String> columnFlags = new TreeTableColumn<>("Příznaky");
		columnFlags.setCellValueFactory(c -> {
			IGameRoomTreeViewItem item = c.getValue().getValue();
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

	public void addAll(Collection<IGameRoomTreeViewItem> items) {
		for (IGameRoomTreeViewItem item : items) {
			add(item);
		}
	}

	public void add(IGameRoomTreeViewItem gr) {
		TreeItem<IGameRoomTreeViewItem> result = root.getChildren().stream().filter(child -> child.getValue().getDifficultyView() == gr.getDifficultyView()).findFirst().orElse(null);
		if (result == null) {
			System.err.println("failed adding game room tree view item");
			return;
		}

		result.getChildren().add(new TreeItem<>(gr));
	}

	public IGameRoomTreeViewItem getSelectedItem() {
		TreeItem<IGameRoomTreeViewItem> selected = getSelectionModel().getSelectedItem();
		if (selected == null) {
			return null;
		}
		return selected.getValue();
	}
}
