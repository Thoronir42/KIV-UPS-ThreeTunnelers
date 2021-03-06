package tunnelers.app.views.lobby;

import javafx.scene.layout.VBox;
import tunnelers.app.render.colors.FxPlayerColorManager;
import tunnelers.core.player.Player;

public class PlayerListView extends VBox {

	private final FxPlayerColorManager colors;

	private final PlayerView[] playerViews;

	PlayerListView(FxPlayerColorManager colors, int capacity) {
		this.colors = colors;
		this.playerViews = this.createViews(capacity);

		this.setSpacing(4);

		for (int i = 0; i < capacity; i++) {
			this.getChildren().add(playerViews[i]);
		}
	}

	private PlayerView[] createViews(int n) {
		PlayerView[] views = new PlayerView[n];
		for (int i = 0; i < n; i++) {
			views[i] = new PlayerView();
		}

		return views;
	}

	public void renderPlayer(int i, Player p) {
		if (p != null) {
			playerViews[i].set(p.getName(), this.colors.get(p), p.isReady());
		} else {
			playerViews[i].clear();
		}
	}
}
