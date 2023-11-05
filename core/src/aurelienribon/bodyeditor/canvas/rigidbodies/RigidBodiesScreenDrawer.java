package aurelienribon.bodyeditor.canvas.rigidbodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class RigidBodiesScreenDrawer {
    private static final Color BALLTHROWPATH_COLOR = new Color(0.2f, 0.2f, 0.2f, 1);

    private final ShapeRenderer drawer = new ShapeRenderer();
    private final OrthographicCamera camera;

    public RigidBodiesScreenDrawer(OrthographicCamera camera) {
        this.camera = camera;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public void drawBallThrowPath(Vector2 p1, Vector2 p2) {
        if (p1 == null || p2 == null) return;

        Gdx.gl.glLineWidth(3);
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        float w = 0.03f * camera.zoom;

        drawer.setProjectionMatrix(camera.combined);
        drawer.begin(ShapeRenderer.ShapeType.Line);
        drawer.setColor(BALLTHROWPATH_COLOR);
        drawer.line(p1.x, p1.y, p2.x, p2.y);
        drawer.end();

        drawer.setProjectionMatrix(camera.combined);
        drawer.begin(ShapeRenderer.ShapeType.Filled);
        drawer.setColor(BALLTHROWPATH_COLOR);
        drawer.rect(p2.cpy().sub(w / 2, w / 2).x, p2.cpy().sub(w / 2, w / 2).y, w, w);
        drawer.end();
    }
}
