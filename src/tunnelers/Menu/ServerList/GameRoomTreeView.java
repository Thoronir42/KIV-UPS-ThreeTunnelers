package tunnelers.Menu.ServerList;

import java.util.Collection;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 *
 * @author Stepan
 */
public class GameRoomTreeView extends TreeView<GRTVItem>{

	public static GameRoomTreeView createInstance(){
		return new GameRoomTreeView(new TreeItem<>(new GRTVRoot()));
	}
	
	TreeItem<GRTVItem> root;
	
	private GameRoomTreeView(TreeItem<GRTVItem> root){
		super(root);
		this.root = root;
		clearItems();
	}
	
	public void clearItems() {
		root.getChildren().clear();
		for(Difficulty d : Difficulty.values()){
			root.getChildren().add(new TreeItem<>(d));
		}
		root.setExpanded(true);
	}
	
	public void addAll(Collection<GRTVItem> items){
		for(GRTVItem item : items){
			add(item);
		}
	}

	public void add(GRTVItem gr) {
		TreeItem<GRTVItem> result = root.getChildren().stream().filter(child -> child.getValue().getDIfficulty() == gr.getDIfficulty()).findFirst().get();
		if(result != null){
			result.getChildren().add(new TreeItem<>(gr));
		}
	}

	public GRTVItem getSelectedItem() {
		TreeItem<GRTVItem> selected = getSelectionModel().getSelectedItem();
		if(selected == null){
			return null;
		}
		return selected.getValue();
	}
	
}
