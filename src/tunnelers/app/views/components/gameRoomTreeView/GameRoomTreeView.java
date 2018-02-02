package tunnelers.app.views.components.gameRoomTreeView;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import tunnelers.app.views.components.roomListing.IGameRoomListItem;
import tunnelers.app.views.serverList.GameMode;

import java.util.Collection;

public class GameRoomTreeView extends TreeView<IGameRoomListItem> {

	public static GameRoomTreeView createInstance() {
		return new GameRoomTreeView(new TreeItem<>(new GameRoomTreeViewRoot()));
	}

	private final TreeItem<IGameRoomListItem> root;

	private GameRoomTreeView(TreeItem<IGameRoomListItem> root) {
		super(root);
		this.root = root;
		clearItems();
	}

	public void clearItems() {
		root.getChildren().clear();
		for (GameMode d : GameMode.values()) {
			root.getChildren().add(new TreeItem<>(new GameRoomViewWrapper(d)));
		}
		root.setExpanded(true);
	}

	public void addAll(Collection<IGameRoomListItem> items) {
		for (IGameRoomListItem item : items) {
			add(item);
		}
	}

	public void add(IGameRoomListItem gr) {
		root.getChildren().stream()
				.filter(child -> child.getValue().getGameModeView() == gr.getGameModeView())
				.findFirst()
				.ifPresent(item -> item.getChildren().add(new TreeItem<>(gr)));
	}

	public IGameRoomListItem getSelectedItem() {
		TreeItem<IGameRoomListItem> selected = getSelectionModel().getSelectedItem();
		if (selected == null) {
			return null;
		}
		return selected.getValue();
	}
}
