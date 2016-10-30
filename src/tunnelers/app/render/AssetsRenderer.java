package tunnelers.app.render;

import tunnelers.app.render.colors.AColorScheme;
import java.util.Collection;
import java.util.HashMap;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import tunnelers.app.assets.Assets;
import tunnelers.app.assets.IAssetImagesProvider;
import tunnelers.core.model.entities.Projectile;
import tunnelers.core.model.entities.Tank;
import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class AssetsRenderer extends ARenderer {

	private static final Point2D
			CBO_NONE = new Point2D(0, 0),
			CBO_1 = new Point2D(1, 0),
			CBO_2 = new Point2D(1, 1),
			CBO_3 = new Point2D(0, 1);
	
	final HashMap<Integer, Image[]> tankBody;

	final Image[] tankCannon;

	final Image[] projectile;

	public AssetsRenderer(AColorScheme colorScheme, Assets assets, Collection<Player> players) {
		super(colorScheme);
		tankBody = new HashMap<>();
		tankCannon = new Image[2];
		projectile = new Image[2];

		players.stream().forEach((player) -> {
			Color c = colorScheme.playerColors().get(player);
			Image[] tankImages = new Image[2];
			tankImages[IAssetImagesProvider.IMG_REG] = assets.getImage(IAssetImagesProvider.TANK_BODY, c);
			tankImages[IAssetImagesProvider.IMG_DIAG] = assets.getImage(IAssetImagesProvider.TANK_BODY_DIAG, c);
			tankBody.put(player.getID(), tankImages);
		});

		Color cannon = this.colorScheme.playerColors().getCannonColor();
		Color cProj = this.colorScheme.playerColors().getProjectileColor();

		tankCannon[IAssetImagesProvider.IMG_REG] = assets.getImage(IAssetImagesProvider.TANK_CANNON, cannon);
		tankCannon[IAssetImagesProvider.IMG_DIAG] = assets.getImage(IAssetImagesProvider.TANK_CANNON_DIAG, cannon);
		projectile[IAssetImagesProvider.IMG_REG] = assets.getImage(IAssetImagesProvider.PROJECTILE, cProj);
		projectile[IAssetImagesProvider.IMG_DIAG] = assets.getImage(IAssetImagesProvider.PROJECTILE_DIAG, cProj);
	}

	public Image getTankBodyImage(int playerId, boolean diagonal) {
		return imgDiagSwitch(tankBody.get(playerId), diagonal);
	}

	public Image getTankCannonImage(boolean diagonal) {
		return imgDiagSwitch(tankCannon, diagonal);
	}

	public Image getProjectileImage(boolean diagonal) {
		return imgDiagSwitch(projectile, diagonal);
	}

	private Image imgDiagSwitch(Image[] img, boolean diagonal) {
		return diagonal ? img[IAssetImagesProvider.IMG_DIAG] : img[IAssetImagesProvider.IMG_REG];
	}

	public void drawTank(Tank t) {
		Image iv_body = this.getTankBodyImage(t.getPlayerId(), t.getDirection().isDiagonal());
		Image iv_cannon = this.getTankCannonImage(t.getDirection().isDiagonal());

		Dimension2D size = Tank.SIZE;
		double bw = blockSize.getWidth(), bh = blockSize.getHeight();

		int rotation = t.getDirection().getRotation();
		Point2D cbo = this.getCenterBlockOffset(rotation);
		
		int dx = (int) (size.getWidth() / 2),
				dy = (int) (size.getHeight() / 2);
		
		g.translate(cbo.getX() * bw, cbo.getY() * bh);
		
		g.rotate(rotation * 90);
		g.drawImage(iv_body, -dx * bw, -dy * bh,
				size.getWidth() * bw, size.getHeight() * bh);
		g.drawImage(iv_cannon, -dx * bw, -dy * bh,
				size.getWidth() * bw, size.getHeight() * bh);

	}

	public void drawProjectile(Projectile p) {
		Image imageProjectile = this.getProjectileImage(p.getDirection().isDiagonal());
		
		double bw = blockSize.getWidth(), bh = blockSize.getHeight();

		int rotation = p.getDirection().getRotation();
		Point2D cbo = this.getCenterBlockOffset(rotation);
		Dimension2D size = p.getSize();
		int dx = (int) (size.getWidth() / 2),
				dy = (int) (size.getHeight() / 2);
		
		g.translate(cbo.getX() * bw, cbo.getY() * bh);
		
		g.rotate(rotation * 90);
		
		g.drawImage(imageProjectile, -dx * bw, -dy * bh,
				size.getWidth() * bw, size.getHeight() * bh);

	}

	private Point2D getCenterBlockOffset(int rotation) {
		switch (rotation) {
			case 0:
			default: return CBO_NONE;
			case 1: return CBO_1;
			case 2: return CBO_2;
			case 3: return CBO_3;
		}
	}
}
