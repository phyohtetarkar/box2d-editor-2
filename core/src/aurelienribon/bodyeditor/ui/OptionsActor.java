package aurelienribon.bodyeditor.ui;

import aurelienribon.bodyeditor.Settings;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.Set;

/**
 * @author phyohtetarkar
 */
public class OptionsActor extends VisTable {

    public OptionsActor() {
        super(true);
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

        left().top().pad(12);

        VisLabel label = new VisLabel("Options");
        add(label).left().colspan(3);
        row();
        add(cbDrawBackground).left().padTop(4);
        add(cbDrawConvexPolygon).left();
        add(cbDrawGridWithGap).left();
        add(selectBox).left();
        row();
        add(cbDrawShapes).left();
        add(cbDebugPhysics).left();
        add(cbEnableSnapGrid).colspan(2).left();
    }

}
