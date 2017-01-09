package tunnelers.app.views.serverList.GameRoomView;

import java.util.Collection;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import tunnelers.app.views.serverList.GameRoomDifficulty;

/**
 *
 * @author Stepan
 */
public class GameRoomTreeView extends TreeView<IGameRoomTreeViewItem> {

	public static GameRoomTreeView createInstance() {
		return new GameRoomTreeView(new TreeItem<>(new GameRoomTreeViewRoot()));
	}

	TreeItem<IGameRoomTreeViewItem> root;

	private GameRoomTreeView(TreeItem<IGameRoomTreeViewItem> root) {
		super(root);
		this.root = root;
		clearItems();
	}

	public void clearItems() {
		root.getChildren().clear();
		for (GameRoomDifficulty d : GameRoomDifficulty.values()) {
			root.getChildren().add(new TreeItem<>(new GameRoomViewWrapper(d)));
		}
		root.setExpanded(true);
	}

	public void addAll(Collection<IGameRoomTreeViewItem> items) {
		for (IGameRoomTreeViewItem item : items) {
			add(item);
		}
	}

	public void add(IGameRoomTreeViewItem gr) {
		TreeItem<IGameRoomTreeViewItem> result = root.getChildren().stream().filter(child -> child.getValue().getDifficultyView() == gr.getDifficultyView()).findFirst().get();
		if (result != null) {
			result.getChildren().add(new TreeItem<>(gr));
		}
	}

	public IGameRoomTreeViewItem getSelectedItem() {
		TreeItem<IGameRoomTreeViewItem> selected = getSelectionModel().getSelectedItem();
		if (selected == null) {
			return null;
		}
		return selected.getValue();
	}
}
