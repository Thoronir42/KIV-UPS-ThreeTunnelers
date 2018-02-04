package tunnelers.app.views.debug;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Affine;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.assets.Assets;
import tunnelers.app.render.AssetsRenderer;
import tunnelers.app.render.colors.FxDefaultColorScheme;
import tunnelers.app.render.colors.FxPlayerColorManager;
import tunnelers.core.model.entities.Direction;
import tunnelers.core.model.entities.IntPoint;
import tunnelers.core.model.entities.Projectile;
import tunnelers.core.model.entities.Tank;
import tunnelers.core.player.Player;
import tunnelers.core.settings.Settings;

public class AssetDebugScene extends ATunnelersScene {

	private final AssetsRenderer assetsRenderer;
	protected AnchorPane anchorPane;

	private Player[] players;

	private Dimension2D blockSize = new Dimension2D(8, 8);

	public AssetDebugScene(Settings settings, Assets assets) {
		super(new AnchorPane(), settings.getWindowWidth(), settings.getWindowHeight(), "Assets debug");

		this.assetsRenderer = new AssetsRenderer(assets, new FxDefaultColorScheme(new FxPlayerColorManager()));

		this.players = new Player[]{
				new Player(null, 1),
				new Player(null, 2),
				new Player(null, 3),
				new Player(null, 4),
		};

		assetsRenderer.initGameAssets(this.players);

		assetsRenderer.setBlockSize(blockSize);
	}

	@Override
	public void update(long tick) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

		Affine transform = g.getTransform();

		for (int i = 0; i < players.length; i++) {
			Tank t = new Tank(players[i], new IntPoint());
			int y = 0;
			for (Direction direction : Direction.values()) {
				if(direction ==Direction.Undefined) {
					continue;
				}
				g.setTransform(transform);
				g.translate(i * 10 * blockSize.getWidth(), y * blockSize.getHeight());
				t.setDirection(direction);
				assetsRenderer.drawTank(g, t);
				y += 8;


			}
		}

		int y = 0;
		Projectile p = new Projectile(new IntPoint(), Direction.North, null);
		for (Direction direction : Direction.values()) {
			if(direction ==Direction.Undefined) {
				continue;
			}
			g.setTransform(transform);
			g.translate(players.length * 10 * blockSize.getWidth(), y * blockSize.getHeight());
			p.setDirection(direction);

			assetsRenderer.drawProjectile(g, p);
			y += 8;
		}

		g.setTransform(transform);
	}
}
