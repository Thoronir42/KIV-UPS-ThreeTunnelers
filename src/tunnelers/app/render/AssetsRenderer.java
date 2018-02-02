package tunnelers.app.render;

import javafx.geometry.Point2D;
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
	private static final Point2D CBO_NONE = new Point2D(0, 0),
			CBO_1 = new Point2D(1, 0),
			CBO_2 = new Point2D(1, 1),
			CBO_3 = new Point2D(0, 1);
	private final Assets assets;

	private final int[] directionRotations;

	private final HashMap<Player, Image[]> tankBody;

	private final Image[] assetCannon;

	private final Image[] assetProjectile;

	public AssetsRenderer(AColorScheme colorScheme, Assets assets) {
		super(colorScheme);
		tankBody = new HashMap<>();

		this.directionRotations = this.createRotationArray();

		this.assets = assets;

		this.assetCannon = this.initCannonAssets(colorScheme.getCannonColor());
		this.assetProjectile = this.initProjectileAssets(colorScheme.getProjectileColor());
	}

	private int[] createRotationArray() {
		int[] rotations = new int[Direction.values().length];

		for (Direction d : Direction.values()) {
			int intVal = d.byteValue();
			rotations[intVal] = (intVal - 1) / 2;
		}

		return rotations;
	}

	private Image[] initCannonAssets(Color c) {
		Image[] cannon = new Image[2];

		cannon[AssetDirection.Upward.getOrder()] = assets.getImage(Asset.TankCannon, c);
		cannon[AssetDirection.Diagonal.getOrder()] = assets.getImage(Asset.TankBodyDiag, c);

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

	public Image getTankBodyImage(Player playerId, boolean diagonal) {
		return imgDiagSwitch(tankBody.get(playerId), diagonal);
	}

	public Image getTankCannonImage(boolean diagonal) {
		return imgDiagSwitch(assetCannon, diagonal);
	}

	public Image getProjectileImage(boolean diagonal) {
		return imgDiagSwitch(assetProjectile, diagonal);
	}

	private Image imgDiagSwitch(Image[] img, boolean diagonal) {
		return diagonal ? img[AssetDirection.Diagonal.getOrder()] : img[AssetDirection.Upward.getOrder()];
	}

	public void drawTank(Tank t) {
		Image iv_body = this.getTankBodyImage(t.getPlayer(), this.isDiagonal(t.getDirection()));
		Image iv_cannon = this.getTankCannonImage(this.isDiagonal(t.getDirection()));

		IntDimension size = ShapeFactory.get(Direction.NorthEast, ShapeFactory.Type.TankBelt).getSize();
		double bw = blockSize.getWidth(), bh = blockSize.getHeight();

		int rotation = this.getRotation(t.getDirection());
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

	public void drawProjectile(Projectile p) {
		Image imageProjectile = this.getProjectileImage(isDiagonal(p.getDirection()));

		double bw = blockSize.getWidth(), bh = blockSize.getHeight();

		int rotation = this.getRotation(p.getDirection());
		Point2D cbo = this.getCenterBlockOffset(rotation);
		IntDimension size = p.getSize();
		int dx = size.getWidth() / 2,
				dy = size.getHeight() / 2;

		g.translate(cbo.getX() * bw, cbo.getY() * bh);

		g.rotate(rotation * 90);

		g.drawImage(imageProjectile, -dx * bw, -dy * bh,
				size.getWidth() * bw, size.getHeight() * bh);

	}

	private Point2D getCenterBlockOffset(int rotation) {
		switch (rotation) {
			case 0:
			default:
				return CBO_NONE;
			case 1:
				return CBO_1;
			case 2:
				return CBO_2;
			case 3:
				return CBO_3;
		}
	}

	private boolean isDiagonal(Direction direction) {
		return direction.isDiagonal();
	}

	private int getRotation(Direction direction) {
		if (direction == null) {
			direction = Direction.Undefined;
		}
		return this.directionRotations[direction.byteValue()];
	}
}
