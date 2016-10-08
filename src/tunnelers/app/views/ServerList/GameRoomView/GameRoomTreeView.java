package tunnelers.app.views.ServerList.GameRoomView;

import java.util.Collection;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import tunnelers.app.views.ServerList.RoomDifficulty;

/**
 *
 * @author Stepan
 */
public class GameRoomTreeView extends TreeView<GRTVItem> implements IGameRoomView{

	public static GameRoomTreeView createInstance(){
		return new GameRoomTreeView(new TreeItem<>(new GRTVRoot()));
	}
	
	TreeItem<GRTVItem> root;
	
	private GameRoomTreeView(TreeItem<GRTVItem> root){
		super(root);
		this.root = root;
		clearItems();
	}
	
	@Override
	public void clearItems() {
		root.getChildren().clear();
		for(RoomDifficulty d : RoomDifficulty.values()){
			root.getChildren().add(new TreeItem<>(d));
		}
		root.setExpanded(true);
	}
	
	@Override
	public void addAll(Collection<GRTVItem> items){
		for(GRTVItem item : items){
			add(item);
		}
	}

	@Override
	public void add(GRTVItem gr) {
		TreeItem<GRTVItem> result = root.getChildren().stream().filter(child -> child.getValue().getDIfficulty() == gr.getDIfficulty()).findFirst().get();
		if(result != null){
			result.getChildren().add(new TreeItem<>(gr));
		}
	}

	@Override
	public GRTVItem getSelectedItem() {
		TreeItem<GRTVItem> selected = getSelectionModel().getSelectedItem();
		if(selected == null){
			return null;
		}
		return selected.getValue();
	}
	
}
