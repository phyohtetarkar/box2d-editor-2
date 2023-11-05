package aurelienribon.bodyeditor.ui;

import aurelienribon.bodyeditor.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.*;

import java.util.function.Consumer;

/**
 * @author phyohtetarkar
 */
public class AutoTracePropertiesDialog extends VisDialog {

    private final Consumer<Boolean> resultListener;

    private final VisSlider hullToleranceSlider;

    private final VisSlider alphaToleranceSlider;

    private final VisCheckBox multiPartDetectionCheck;

    private final VisCheckBox holeDetectionCheck;

    public AutoTracePropertiesDialog(String title, Consumer<Boolean> resultListener) {
        super(title);

        this.resultListener = resultListener;

        closeOnEscape();
        centerWindow();
        addCloseButton();
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    fadeOut();
                    return true;
                }
                return false;
            }
        });

        VisTable root = new VisTable(true);
        root.pad(14);

        Texture imageTexture = new Texture(Gdx.files.internal("ui/autoTrace.png"));
        Image image = new Image(new TextureRegionDrawable(imageTexture));

        VisLabel labelHullTolerance = new VisLabel("Hull tolerance:");
        VisLabel labelAlphaTolerance = new VisLabel("Alpha tolerance:");

        hullToleranceSlider = new VisSlider(100, 400, 1, false);
        hullToleranceSlider.setValue(Settings.autoTraceHullTolerance * 100);
        alphaToleranceSlider = new VisSlider(0, 255, 1, false);
        alphaToleranceSlider.setValue(128);

        multiPartDetectionCheck = new VisCheckBox("Multi-part detection");
        holeDetectionCheck = new VisCheckBox("Hole detection");

        VisLabel infoLabel = new VisLabel("Only check multi-part detection or hole detection if your image needs it.\nRemember that auto-trace is less precise than manually placed points.");
        infoLabel.setWrap(true);
        infoLabel.setColor(1, 1, 1, 0.7f);

        VisTextButton btnCancel = new VisTextButton("Cancel");
        VisTextButton btnOk = new VisTextButton("  OK  ");

        HorizontalGroup btnGroup = new HorizontalGroup();
        btnGroup.space(10);
        btnGroup.addActor(btnCancel);
        btnGroup.addActor(btnOk);

        VisTable options = new VisTable(true);
        options.add(labelHullTolerance).right();
        options.add(hullToleranceSlider).padLeft(10).width(240);
        options.row();
        options.add(labelAlphaTolerance).right();
        options.add(alphaToleranceSlider).padLeft(10).width(240);
        options.row().padTop(10);
        options.add(multiPartDetectionCheck).colspan(2).left();
        options.row();
        options.add(holeDetectionCheck).left().expandX();
        options.add(btnGroup).right();
        //options.debug();

        root.add(image).size(150);
        root.add(options).left().top().padLeft(10);
        root.row();
        root.add();
        root.add(infoLabel).expandX().fillX().padLeft(10);

        add(root);

        pack();

        btnCancel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                result(false);
                hide();
            }
        });
        btnOk.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                result(true);
                hide();
            }
        });
    }

    @Override
    protected void result(Object object) {
        if (resultListener == null) return;
        if (object == Boolean.TRUE) {
            Settings.autoTraceHullTolerance = hullToleranceSlider.getValue() / 100f;
            Settings.autoTraceAlphaTolerance = (int) alphaToleranceSlider.getValue();
            Settings.autoTraceMultiPartDetection = multiPartDetectionCheck.isChecked();
            Settings.autoTraceHoleDetection = holeDetectionCheck.isChecked();
        }
        resultListener.accept((Boolean) object);
    }
}
