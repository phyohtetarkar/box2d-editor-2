package aurelienribon.bodyeditor.ui;

import aurelienribon.bodyeditor.Settings;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;

/**
 * @author phyohtetarkar
 */
public class OptionsActor extends VisScrollPane {

    public OptionsActor() {
        super(new VisTable(true));

        VisTable table = (VisTable) getActor();

        VisCheckBox cbDrawBackground = new VisCheckBox("Draw background image", Settings.isImageDrawn);
        VisCheckBox cbDrawConvexPolygon = new VisCheckBox("Draw convex polygons", Settings.isPolygonDrawn);
        VisCheckBox cbDrawGridWithGap = new VisCheckBox("Draw grid with gap", Settings.isGridShown);
        VisCheckBox cbDrawShapes = new VisCheckBox("Draw shapes", Settings.isShapeDrawn);
        VisCheckBox cbDebugPhysics = new VisCheckBox("Debug physics", Settings.isPhysicsDebugEnabled);
        VisCheckBox cbEnableSnapGrid = new VisCheckBox("Enable snap-to-grid", Settings.isSnapToGridEnabled);
        VisSelectBox<Float> selectBox = new VisSelectBox<>();
        selectBox.setItems(0.025f, 0.05f, 0.075f, 0.1f);

        cbDrawBackground.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.isImageDrawn = cbDrawBackground.isChecked();
            }
        });
        cbDrawConvexPolygon.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.isPolygonDrawn = cbDrawConvexPolygon.isChecked();
            }
        });
        cbDrawGridWithGap.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.isGridShown = cbDrawGridWithGap.isChecked();
            }
        });
        cbDrawShapes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.isShapeDrawn = cbDrawShapes.isChecked();
            }
        });
        cbDebugPhysics.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.isPhysicsDebugEnabled = cbDebugPhysics.isChecked();
            }
        });
        cbEnableSnapGrid.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.isSnapToGridEnabled = cbEnableSnapGrid.isChecked();
            }
        });
        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.gridGap = selectBox.getSelected();
            }
        });

        table.left().top().pad(12);

        VisLabel label = new VisLabel("Options");
        table.add(label).left().colspan(3);
        table.row();
        table.add(cbDrawBackground).left().padTop(4);
        table.add(cbDrawConvexPolygon).left();
        table.add(cbDrawGridWithGap).left();
        table.add(selectBox).left();
        table.row();
        table.add(cbDrawShapes).left();
        table.add(cbDebugPhysics).left();
        table.add(cbEnableSnapGrid).colspan(2).left();

        Drawable bg = VisUI.getSkin().getDrawable("window-bg");
        table.setBackground(bg);

        pack();

        addListener(new InputListener() {
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                Stage stage = event.getStage();
                boolean hovered = hit(x ,y, isTouchable()) != null;
                if (hasScrollFocus() && stage != null && !hovered) {
                    stage.setScrollFocus(null);
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Stage stage = event.getStage();
                boolean hovered = hit(x ,y, isTouchable()) != null;
                if (!hasScrollFocus() && stage != null && hovered) {
                    stage.setScrollFocus(OptionsActor.this);
                }

            }
        });
    }

}
