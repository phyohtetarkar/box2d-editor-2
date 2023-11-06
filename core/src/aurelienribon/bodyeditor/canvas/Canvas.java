package aurelienribon.bodyeditor.canvas;

import aurelienribon.accessors.SpriteAccessor;
import aurelienribon.bodyeditor.Ctx;
import aurelienribon.bodyeditor.RigidBodiesManager;
import aurelienribon.bodyeditor.Settings;
import aurelienribon.bodyeditor.canvas.dynamicobjects.DynamicObjectsScreen;
import aurelienribon.bodyeditor.canvas.rigidbodies.RigidBodiesScreen;
import aurelienribon.tweenengine.Tween;
import aurelienribon.utils.notifications.ChangeListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class Canvas extends ScreenAdapter {

    public static final int OFFSET_X = 360;
    public static final int OFFSET_Y = 140;


    public OrthographicCamera worldCamera;
    public OrthographicCamera screenCamera;
    public SpriteBatch batch;
    public BitmapFont font;
    public CanvasDrawer drawer;
    public InputMultiplexer input;

    public Stage stage;

    public enum Mode {BODIES, OBJECTS}

    private Mode mode = Mode.BODIES;

    private RigidBodiesScreen rigidBodiesScreen;
    private DynamicObjectsScreen dynamicObjectsScreen;

    private Sprite infoLabel;
    private Texture backgroundTexture;

    public Canvas(Batch batch, InputMultiplexer input, Stage stage) {
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        this.stage = stage;

        //setFillParent(true);

        worldCamera = new OrthographicCamera();
        screenCamera = new OrthographicCamera();
        resetCameras();

        this.batch = (SpriteBatch) batch;
        font = new BitmapFont();
        drawer = new CanvasDrawer(this.batch, worldCamera);

        backgroundTexture = Assets.inst().get("data/transparent-light.png", Texture.class);
        backgroundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        infoLabel = new Sprite(Assets.inst().get("data/white.png", Texture.class));
        infoLabel.setPosition(0, 0);
        infoLabel.setSize(120, 60);
        infoLabel.setColor(new Color(0x2A / 255f, 0x3B / 255f, 0x56 / 255f, 180 / 255f));

        this.input = input;
        input.addProcessor(new PanZoomInputProcessor(this));

        rigidBodiesScreen = new RigidBodiesScreen(this);
        dynamicObjectsScreen = new DynamicObjectsScreen(this);


        initializeSelectionListeners();
    }

    // -------------------------------------------------------------------------
    // Init
    // -------------------------------------------------------------------------

    private void initializeSelectionListeners() {

        Ctx.bodies.addChangeListener(new ChangeListener() {
            @Override
            public void propertyChanged(Object source, String propertyName) {
                if (propertyName.equals(RigidBodiesManager.PROP_SELECTION)) {
                    if (Ctx.bodies.getSelectedModel() != null) {
                        Mode oldMode = mode;
                        mode = Mode.BODIES;
                        if (mode != oldMode) fireModeChanged(mode);
                    }
                }
            }
        });

        Ctx.objects.addChangeListener(new ChangeListener() {
            @Override
            public void propertyChanged(Object source, String propertyName) {
                if (propertyName.equals(RigidBodiesManager.PROP_SELECTION)) {
                    if (Ctx.objects.getSelectedModel() != null) {
                        Mode oldMode = mode;
                        mode = Mode.OBJECTS;
                        if (mode != oldMode) fireModeChanged(mode);
                    }
                }
            }
        });
    }

    // -------------------------------------------------------------------------
    // Render
    // -------------------------------------------------------------------------

    @Override
    public void render(float delta) {

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        resetCameras();

        batch.setProjectionMatrix(screenCamera.combined);

        batch.begin();
        batch.disableBlending();
        float tw = backgroundTexture.getWidth();
        float th = backgroundTexture.getHeight();
        batch.draw(backgroundTexture, 0, 0, w, h, 0f, 0f, w / tw, h / th);
        batch.enableBlending();
        batch.end();

        rigidBodiesScreen.render();
        //dynamicObjectsScreen.render();

        batch.setProjectionMatrix(screenCamera.combined);
        batch.begin();
        infoLabel.draw(batch);
        font.setColor(Color.WHITE);
        font.draw(batch, String.format(Locale.US, "Zoom: %.0f %%", 100f / worldCamera.zoom), 10, 45);
        font.draw(batch, "Fps: " + Gdx.graphics.getFramesPerSecond(), 10, 25);
        batch.end();
    }

    //@Override
    public void resize(int width, int height) {
        resetCameras();
        worldCamera.position.set(0.5f, 0.5f, 0);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public static Vector2 getCanvasBound(float x, float y) {
        Vector2 vec2 = new Vector2(1, 1);
        int w = Gdx.graphics.getWidth() - OFFSET_X;
        if (x >= w) {
           vec2.x = 0;
        }
        if (y <= OFFSET_Y) {
            vec2.y = 0;
        }

        return vec2;
    };

    public Vector2 screenToWorld(int x, int y) {
        int w = Gdx.graphics.getWidth() - OFFSET_X;
        int h = Gdx.graphics.getHeight() - OFFSET_Y;
        Vector2 bound = getCanvasBound(x, y);
        if (bound.x == 0) {
            x = w - 5;
        }
        if (bound.y == 0) {
            y = OFFSET_Y + 4;
        }
        Vector3 v3 = worldCamera.unproject(new Vector3(x, y, 0), 0, 0,w, h);
        return new Vector2(v3.x, v3.y);
    }

    public Vector2 alignedScreenToWorld(int x, int y) {
        Vector2 p = screenToWorld(x, y);
        if (Settings.isSnapToGridEnabled) {
            float gap = Settings.gridGap;
            p.x = Math.round(p.x / gap) * gap;
            p.y = Math.round(p.y / gap) * gap;
        }
        return p;
    }

    // -------------------------------------------------------------------------
    // Events
    // -------------------------------------------------------------------------

    private final List<Listener> listeners = new CopyOnWriteArrayList<Listener>();

    public static interface Listener {
        public void modeChanged(Mode mode);
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    private void fireModeChanged(Mode mode) {
        for (Listener listener : listeners) listener.modeChanged(mode);
    }

    // -------------------------------------------------------------------------
    // Internals
    // -------------------------------------------------------------------------

    private void resetCameras() {

        int w = Gdx.graphics.getWidth() - OFFSET_X;
        int h = Gdx.graphics.getHeight() - OFFSET_Y;

        HdpiUtils.glViewport(0, 0,  w, h);

        worldCamera.viewportWidth = 2f;
        worldCamera.viewportHeight = 2f * ((float) h / w);
        //worldCamera.position.set(0.5f, 0.5f, 0);
        worldCamera.update();

        screenCamera.setToOrtho(false, w, h);
        screenCamera.position.set(w / 2f, h / 2f, 0);
        screenCamera.update();
    }
}
