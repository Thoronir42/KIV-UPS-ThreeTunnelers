package tunnelers.app.views.serverList.GameRoomView;

import java.util.Collection;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import tunnelers.app.views.serverList.RoomDifficulty;

/**
 *
 * @author Stepan
 */
public class GameRoomTreeTableView extends TreeTableView<GRTVItem> {

	public static GameRoomTreeTableView createInstance() {
		return new GameRoomTreeTableView(new TreeItem<>(new GRTVRoot()));
	}

	final TreeItem<GRTVItem> root;

	private GameRoomTreeTableView(TreeItem<GRTVItem> root) {
		super(root);
		this.root = root;
		this.showRootProperty().set(false);
		clearItems();
		initColumns();
	}

	public void clearItems() {
		root.getChildren().clear();
		for (RoomDifficulty d : RoomDifficulty.values()) {
			root.getChildren().add(new TreeItem<>(d));
		}
		root.setExpanded(true);
	}

	private void initColumns() {
		TreeTableColumn<GRTVItem, String> columnName = new TreeTableColumn<>("Název");
		columnName.setCellValueFactory(c -> {
			GRTVItem item = c.getValue().getValue();
			return new ReadOnlyStringWrapper(item.getTitle());
		});
		TreeTableColumn<GRTVItem, String> columnPlayers = new TreeTableColumn<>("Hráčů");
		columnPlayers.setCellValueFactory(c -> {
			GRTVItem item = c.getValue().getValue();
			return new ReadOnlyStringWrapper(item.getOccupancy());
		});

		TreeTableColumn<GRTVItem, String> columnFlags = new TreeTableColumn<>("Příznaky");
		columnFlags.setCellValueFactory(c -> {
			GRTVItem item = c.getValue().getValue();
			return new ReadOnlyStringWrapper(item.getFlags());
		});
		columnFlags.setPrefWidth(180);

		this.getColumns().addAll(columnName, columnPlayers, columnFlags);

	}

	public void addAll(Collection<GRTVItem> items) {
		for (GRTVItem item : items) {
			add(item);
		}
	}

	public void add(GRTVItem gr) {
		TreeItem<GRTVItem> result = root.getChildren().stream().filter(child -> child.getValue().getDIfficulty() == gr.getDIfficulty()).findFirst().get();
		if (result != null) {
			result.getChildren().add(new TreeItem<>(gr));
		}
	}

	public GRTVItem getSelectedItem() {
		TreeItem<GRTVItem> selected = getSelectionModel().getSelectedItem();
		if (selected == null) {
			return null;
		}
		return selected.getValue();
	}
}
