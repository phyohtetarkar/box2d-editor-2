package aurelienribon.bodyeditor.ui;

import aurelienribon.bodyeditor.Ctx;
import aurelienribon.bodyeditor.models.RigidBodyModel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;

import java.io.File;

/**
 * @author phyohtetarkar
 */
public class CreateRigidBodyModelDialog extends VisDialog {
    public CreateRigidBodyModelDialog(String title) {
        super(title);

        closeOnEscape();
        centerWindow();
        //setResizable(true);
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

        Texture imageTexture = new Texture(Gdx.files.internal("ui/newBody.png"));
        Image image = new Image(new TextureRegionDrawable(imageTexture));
        image.setScaling(Scaling.fit);

        VisTextField nameTextFieldImage = new VisTextField("Name");
        VisTextButton btnCreateFromImage = new VisTextButton("Create body from image");
        VisLabel labelCreateFromImage = new VisLabel("Creates a new body associated to an image.\n" +
                "You are still able to change the image later.");
        labelCreateFromImage.setWrap(true);


        VisTextField nameTextFieldEmpty = new VisTextField("Name");
        VisTextButton btnCreateEmpty = new VisTextButton("Create new empty body");
        VisLabel labelCreateEmpty = new VisLabel("Creates a new empty body with no associated image.\n" +
                "You are still able to associate an image with it later.");
        labelCreateEmpty.setWrap(true);


        VisTextButton btnCreateFromImages = new VisTextButton("Create bodies from images");
        VisLabel labelCreateFromImages = new VisLabel("Creates multiple bodies associated to selected images.\n" +
                "You are still able to change the images later.");

        VisTable tableCreateFromImage = new VisTable();
        tableCreateFromImage.pad(10);
        tableCreateFromImage.add(nameTextFieldImage).width(200);
        tableCreateFromImage.add(btnCreateFromImage).padLeft(10).width(200);
        tableCreateFromImage.row();
        tableCreateFromImage.add(labelCreateFromImage).colspan(2).expandX().fill().padTop(4);

        VisTable tableCreateEmpty = new VisTable();
        tableCreateEmpty.pad(10);
        tableCreateEmpty.add(nameTextFieldEmpty).width(200);
        tableCreateEmpty.add(btnCreateEmpty).padLeft(10).width(200);
        tableCreateEmpty.row();
        tableCreateEmpty.add(labelCreateEmpty).colspan(2).expandX().fill().padTop(4);

        VisTable tableCreateFromImages = new VisTable();
        tableCreateFromImages.pad(10);
        tableCreateFromImages.add(btnCreateFromImages).expandX().fill();
        tableCreateFromImages.row();
        tableCreateFromImages.add(labelCreateFromImages).colspan(2).expandX().fill().padTop(4);

        VisTable options = new VisTable();
        options.add(tableCreateFromImage).top();
        options.row();
        options.add(new VisLabel("--OR--")).expandX();
        options.row();
        options.add(tableCreateEmpty);
        options.row();
        options.add(new VisLabel("--OR--")).expandX();
        options.row();
        options.add(tableCreateFromImages).expandX().fill();


        root.add(image).width(150);
        root.add(options).padLeft(10).top();

        add(root);


        pack();

        btnCreateFromImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String value = nameTextFieldImage.getText();
                if (value.isEmpty()) {
                    return;
                }
                if (Ctx.bodies.getModel(value) != null) {
                    Dialogs.showOKDialog(getStage(), "Error", "Sorry, there is already a body with this name.");
                    return;
                }
                createFromImage(value);
            }
        });
        btnCreateEmpty.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String value = nameTextFieldEmpty.getText();
                if (value.isEmpty()) {
                    return;
                }
                if (Ctx.bodies.getModel(value) != null) {
                    Dialogs.showOKDialog(getStage(), "Error", "Sorry, there is already a body with this name.");
                    return;
                }
                createEmpty(value);
            }
        });
        btnCreateFromImages.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createFromImages();
            }
        });
    }

    private void createEmpty(String name) {
        hide();
        RigidBodyModel model = new RigidBodyModel();
        model.setName(name);
        Ctx.bodies.getModels().add(model);
        Ctx.bodies.select(model);
    }

    private void createFromImage(String name) {
        FileTypeFilter typeFilter = new FileTypeFilter(false);
        typeFilter.addRule("Image files (*.png, *.jpg, *.jpeg)", "png", "jpg", "jpeg");
        FileChooser fileChooser = FileChoosers.buildOpenFileChooser(file -> {
            hide();
            if (file.isEmpty()) return;
            RigidBodyModel model = new RigidBodyModel();
            model.setName(name);
            model.setImagePath(Ctx.io.buildImagePath(file.first().file()));
            Ctx.bodies.getModels().add(model);
            Ctx.bodies.select(model);
        }, typeFilter, FileChooser.SelectionMode.FILES);

        fileChooser.setDirectory(Ctx.io.getProjectDir());
        fileChooser.getTitleLabel().setText("Select the image associated to the new model");

        getStage().addActor(fileChooser.fadeIn());
    }

    private void createFromImages() {
        FileTypeFilter typeFilter = new FileTypeFilter(false);
        typeFilter.addRule("Image files (*.png, *.jpg, *.jpeg)", "png", "jpg", "jpeg");
        FileChooser fileChooser = FileChoosers.buildOpenFileChooser(file -> {
            hide();
            if (file.isEmpty()) return;
            for (FileHandle fh : file) {
                File f = fh.file();
                String name = f.getName();
                String origName = name;
                int i = 0;
                while (Ctx.bodies.getModel(name) != null) {
                    name = origName + "-" + (++i);
                }

                RigidBodyModel model = new RigidBodyModel();
                model.setName(name);
                model.setImagePath(Ctx.io.buildImagePath(f));
                Ctx.bodies.getModels().add(model);
                Ctx.bodies.select(model);
            }
        }, typeFilter, FileChooser.SelectionMode.FILES);
        fileChooser.setDirectory(Ctx.io.getProjectDir());
        fileChooser.getTitleLabel().setText("Select the images for the new models");
        fileChooser.setMultiSelectionEnabled(true);

        getStage().addActor(fileChooser.fadeIn());
    }
}
