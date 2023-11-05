package aurelienribon.bodyeditor.ui;

import aurelienribon.bodyeditor.Ctx;
import aurelienribon.bodyeditor.RigidBodiesManager;
import aurelienribon.bodyeditor.models.RigidBodyModel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.Tooltip;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * @author phyohtetarkar
 */
public class RigidBodiesListHeaderActor extends VisTable {

    public RigidBodiesListHeaderActor(RigidBodyListAdapter adapter) {
        super(true);

        VisLabel headerLabel = new VisLabel("Rigid bodies");

        Texture addTexture = new Texture(Gdx.files.internal("ui/ic_add.png"));
        Texture renameTexture = new Texture(Gdx.files.internal("ui/ic_edit.png"));
        Texture delTexture = new Texture(Gdx.files.internal("ui/ic_delete.png"));
        Texture upTexture = new Texture(Gdx.files.internal("ui/ic_up.png"));
        Texture downTexture = new Texture(Gdx.files.internal("ui/ic_down.png"));

        VisImageButton btnAdd = new VisImageButton(new TextureRegionDrawable(addTexture));
        new Tooltip.Builder("Add model").target(btnAdd).build();
        VisImageButton btnRename = new VisImageButton(new TextureRegionDrawable(renameTexture));
        new Tooltip.Builder("Rename model").target(btnRename).build();
        VisImageButton btnDel = new VisImageButton(new TextureRegionDrawable(delTexture));
        new Tooltip.Builder("Delete model").target(btnDel).build();
        VisImageButton btnUp = new VisImageButton(new TextureRegionDrawable(upTexture));
        btnUp.setVisible(false);
        VisImageButton btnDown = new VisImageButton(new TextureRegionDrawable(downTexture));
        btnDown.setVisible(false);

        btnAdd.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Ctx.io.getProjectFile() == null) return;
                CreateRigidBodyModelDialog dialog = new CreateRigidBodyModelDialog("New Rigid Body");
                getStage().addActor(dialog.fadeIn());
            }
        });
        btnRename.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                RigidBodyModel model = Ctx.bodies.getSelectedModel();
                if (Ctx.bodies.getSelectedModel() != null) {
                    InputValidator inputValidator = new SimpleFormValidator.EmptyInputValidator("Sorry, you cannot use empty name.");
                    Dialogs.showInputDialog(getStage(), "Rename rigid body", null, inputValidator, new InputDialogAdapter() {
                        @Override
                        public void finished(String input) {
                            if (Ctx.bodies.getModel(input) != null && Ctx.bodies.getModel(input) != model) {
                                Dialogs.showOKDialog(getStage(), "Error", "Sorry, there is already a body with this name.");
                            } else {
                                model.setName(input);
                            }

                        }
                    }).setText(model.getName());
                }
            }
        });
        btnDel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Array<RigidBodyModel> selection = new Array<>(adapter.getSelection());
                for (RigidBodyModel model : selection) {
                    Ctx.bodies.getModels().remove(model);
                }
                //adapter.removeAll(selection, false);
            }
        });
        btnUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });
        btnDown.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });

        HorizontalGroup btnGroup = new HorizontalGroup();
        btnGroup.space(8);
        btnGroup.addActor(btnAdd);
        btnGroup.addActor(btnRename);
        btnGroup.addActor(btnDel);
        btnGroup.addActor(btnUp);
        btnGroup.addActor(btnDown);

        pad(10);
        add(headerLabel).left();
        row().padTop(8);
        add(btnGroup).expandX().fillX();

        Ctx.bodies.addChangeListener((source, propertyName) -> {
            if (propertyName.equals(RigidBodiesManager.PROP_SELECTION)) {
                RigidBodyModel model = Ctx.bodies.getSelectedModel();
                boolean selected = model != null;
                btnRename.setDisabled(!selected);
                btnDel.setDisabled(!selected);
                if (selected) {
                    adapter.selectView(adapter.getView(model));
                    adapter.getSelection().add(model);
                }

            }
        });

    }

}
