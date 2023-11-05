package com.phyohtet.bodyeditor;

import aurelienribon.bodyeditor.canvas.Assets;
import aurelienribon.bodyeditor.canvas.Canvas;
import aurelienribon.bodyeditor.ui.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.file.FileChooser;

/**
 * @author phyohtetarkar
 */
public class PhysicsBodyEditor extends Game {

    public static final  String version = "v1.0.0";

    SpriteBatch batch;

    Stage stage;

    InputMultiplexer input;

    @Override
    public void create() {
        Assets.inst().initialize();
        VisUI.load();
        FileChooser.setDefaultPrefsName("com.phyohtet.bodyeditor.filechooser");

        batch = new SpriteBatch();
        input = new InputMultiplexer();

        Camera camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, batch);

        VisTable table = new VisTable(false);
        table.setFillParent(true);

        OptionsActor optionsActor = new OptionsActor();
        VisScrollPane optionsPanel = new VisScrollPane(optionsActor);
        optionsPanel.setFadeScrollBars(false);
        optionsPanel.getListeners().forEach(eventListener -> {
            if (eventListener instanceof ActorGestureListener) {
                return;
            }
            optionsPanel.removeListener(eventListener);
        });

        RigidBodyListAdapter adapter = new RigidBodyListAdapter(new Array<>());
        RigidBodiesListActor rigidBodiesListActor = new RigidBodiesListActor(adapter);

        VisLabel versionLabel = new VisLabel(version);
        versionLabel.setColor(1, 1, 1, 0.8f);


        VisTable rightPanel = new VisTable(false);
        rightPanel.top().left();
        rightPanel.add(new HeadingActor()).fill();
        rightPanel.row();
        rightPanel.addSeparator();
        rightPanel.add(new ProjectActor()).fill();
        rightPanel.row();
        rightPanel.addSeparator();
        rightPanel.add(new RigidBodiesListHeaderActor(adapter)).fill();
        rightPanel.row();
        rightPanel.addSeparator();
        rightPanel.add(rigidBodiesListActor.getMainTable()).expandY().fill();
        rightPanel.row();
        rightPanel.addSeparator();
        rightPanel.add(versionLabel).right().padRight(10).padTop(8).padBottom(8).expandX();

        table.add(optionsPanel).height(Canvas.OFFSET_Y).expandX().fillX().left().top();
        table.addSeparator(true);
        table.add(rightPanel).width(Canvas.OFFSET_X).top().expandY().fill();

        Drawable bg = VisUI.getSkin().getDrawable("window-bg");
        optionsActor.setBackground(bg);
        rightPanel.setBackground(bg);


        stage.addActor(table);

        input.addProcessor(stage);
        Gdx.input.setInputProcessor(input);

        //stage.setDebugAll(true);

        setScreen(new Canvas(batch, input, stage));
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
        super.render();

        HdpiUtils.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        batch.dispose();
        VisUI.dispose();
    }
}
