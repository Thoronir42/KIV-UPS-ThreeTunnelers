package tunnelers.app.render;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import tunnelers.app.assets.Asset;
import tunnelers.app.assets.AssetDirection;
import tunnelers.app.assets.Assets;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.core.model.entities.*;
import tunnelers.core.player.Player;

import java.util.HashMap;

public class AssetsRenderer extends ARenderer {

	// Center block offset
	private static final Point2D[] centralBlockOffsets = createBlockOffsetArray();
	private static final int[] directionRotations = createRotationArray();

	private final Assets assets;

	private final HashMap<Player, Image[]> tankBody;

	private final Image[] assetCannon;

	private final Image[] assetProjectile;

	public AssetsRenderer(Assets assets, AColorScheme colorScheme) {
		super(colorScheme);

		this.assets = assets;

		this.tankBody = new HashMap<>();
		this.assetCannon = this.initCannonAssets(colorScheme.getCannonColor());
		this.assetProjectile = this.initProjectileAssets(colorScheme.getProjectileColor());
	}

	private Image[] initCannonAssets(Color c) {
		Image[] cannon = new Image[2];

		cannon[AssetDirection.Upward.getOrder()] = assets.getImage(Asset.TankCannon, c);
		cannon[AssetDirection.Diagonal.getOrder()] = assets.getImage(Asset.TankCannonDiag, c);

		return cannon;
	}

	private Image[] initProjectileAssets(Color c) {
		Image[] projectile = new Image[2];

		projectile[AssetDirection.Upward.getOrder()] = assets.getImage(Asset.Projectile, c);
		projectile[AssetDirection.Diagonal.getOrder()] = assets.getImage(Asset.ProjectileDiag, c);

		return projectile;
	}

	public void initGameAssets(Player[] players) {
		for (Player player : players) {
			if (player == null) {
				continue;
			}

			Color c = colorScheme.playerColors().get(player).color();

			Image[] tankImages = new Image[2];
			tankImages[AssetDirection.Upward.getOrder()] = assets.getImage(Asset.TankBody, c);
			tankImages[AssetDirection.Diagonal.getOrder()] = assets.getImage(Asset.TankBodyDiag, c);
			tankBody.put(player, tankImages);
		}
	}

	private Image getTankBodyImage(Player player, AssetDirection direction) {
		return imgDiagSwitch(tankBody.get(player), direction);
	}

	private Image getTankCannonImage(AssetDirection direction) {
		return imgDiagSwitch(assetCannon, direction);
	}

	private Image getProjectileImage(AssetDirection direction) {
		return imgDiagSwitch(assetProjectile, direction);
	}

	private Image imgDiagSwitch(Image[] img, AssetDirection direction) {
		return img[direction.getOrder()];
	}

	public void drawTank(GraphicsContext g, Tank t) {
		Image iv_body = this.getTankBodyImage(t.getPlayer(), assetDirection(t.getDirection()));
		Image iv_cannon = this.getTankCannonImage(assetDirection(t.getDirection()));

		IntDimension size = ShapeFactory.get(Direction.NorthEast, ShapeFactory.Type.TankBelt).getSize();
		double bw = blockSize.getWidth(), bh = blockSize.getHeight();

		int rotation = getRotation(t.getDirection());
		Point2D cbo = this.getCenterBlockOffset(rotation);

		int dx = size.getWidth() / 2,
				dy = size.getHeight() / 2;

		g.translate(cbo.getX() * bw, cbo.getY() * bh);

		g.rotate(rotation * 90);
		g.drawImage(iv_body, -dx * bw, -dy * bh,
				size.getWidth() * bw, size.getHeight() * bh);
		g.drawImage(iv_cannon, -dx * bw, -dy * bh,
				size.getWidth() * bw, size.getHeight() * bh);

	}

	public void drawProjectile(GraphicsContext g, Projectile p) {
		Image imageProjectile = this.getProjectileImage(assetDirection(p.getDirection()));

		double bw = blockSize.getWidth(), bh = blockSize.getHeight();

		int rotation = getRotation(p.getDirection());
		Point2D cbo = this.getCenterBlockOffset(rotation);
		IntDimension size = p.getSize();
		int dx = size.getWidth() / 2,
				dy = size.getHeight() / 2;

		g.translate(cbo.getX() * bw, cbo.getY() * bh);

		g.rotate(rotation * 90);

		g.drawImage(imageProjectile, -dx * bw, -dy * bh,
				size.getWidth() * bw, size.getHeight() * bh);

	}

	private static Point2D[] createBlockOffsetArray() {
		return new Point2D[]{
				new Point2D(0, 0),
				new Point2D(1, 0),
				new Point2D(1, 1),
				new Point2D(0, 1),
		};
	}

	private Point2D getCenterBlockOffset(int rotation) {
		if(rotation < 0 || rotation > 3) {
			rotation = 0;
		}
		return centralBlockOffsets[rotation];
	}


	private static AssetDirection assetDirection(Direction direction) {
		return direction.isDiagonal() ? AssetDirection.Diagonal : AssetDirection.Upward;
	}

	private static int[] createRotationArray() {
		int[] rotations = new int[Direction.values().length];

		for (Direction d : Direction.values()) {
			int intVal = d.byteValue();
			rotations[intVal] = (intVal - 1) / 2;
		}

		return rotations;
	}

	private static int getRotation(Direction direction) {
		if (direction == null) {
			direction = Direction.Undefined;
		}
		return directionRotations[direction.byteValue()];
	}
}
