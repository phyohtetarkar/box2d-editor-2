package aurelienribon.bodyeditor.ui;

import aurelienribon.bodyeditor.Ctx;
import aurelienribon.bodyeditor.IoManager;
import aurelienribon.utils.notifications.ChangeListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author phyohtetarkar
 */
@SuppressWarnings("ExtractMethodRecommender")
public class ProjectActor extends VisTable {

    public ProjectActor() {
        super(true);

        VisLabel projectLabel = new VisLabel("Project file:");
        VisTextField projectTextField = new VisTextField();
        projectTextField.setReadOnly(true);
        Path dir = Paths.get("");

        Texture newTexture = new Texture(Gdx.files.internal("ui/ic_new.png"));
        Texture loadTexture = new Texture(Gdx.files.internal("ui/ic_open.png"));
        Texture saveTexture = new Texture(Gdx.files.internal("ui/ic_save.png"));

        VisLabel title = new VisLabel("Project");

        VisImageTextButton btnNew = new VisImageTextButton("New", new TextureRegionDrawable(newTexture));
        new Tooltip.Builder("New project").target(btnNew).build();
        VisImageTextButton btnLoad = new VisImageTextButton("Load", new TextureRegionDrawable(loadTexture));
        new Tooltip.Builder("Load project").target(btnLoad).build();
        VisImageTextButton btnSave = new VisImageTextButton("Save", new TextureRegionDrawable(saveTexture));
        new Tooltip.Builder("Save project").target(btnSave).build();

        btnNew.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FileTypeFilter typeFilter = new FileTypeFilter(false);
                typeFilter.addRule("JSON files (*.json)", "json");
                FileChooser saveChooser = FileChoosers.buildSaveFileChooser(file -> {
                    if (file.isEmpty()) return;
                    FileHandle fileHandle = file.first();
                    String path = fileHandle.file().getAbsolutePath();
                    projectTextField.setText(path);
                    projectTextField.setCursorPosition(path.length());
                    Ctx.bodies.getModels().clear();
                    Ctx.io.setProjectFile(fileHandle.file());
                });
                saveChooser.setFileTypeFilter(typeFilter);
                saveChooser.setDirectory(dir.toFile());
                getStage().addActor(saveChooser.fadeIn());
            }
        });
        btnLoad.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FileChooser openChooser = FileChoosers.buildOpenFileChooser(file -> {
                    projectTextField.setInputValid(true);
                    if (file.isEmpty()) return;
                    FileHandle fileHandle = file.first();
                    try {
                        File loadedFile = fileHandle.file();
                        Ctx.io.setProjectFile(loadedFile);
                        Ctx.io.importFromFile();
                    } catch (Exception e) {
                        Ctx.io.setProjectFile(null);
                        projectTextField.setInputValid(false);
                        Dialogs.showErrorDialog(getStage(), e.getMessage(), e);
                    }
                });
                openChooser.setDirectory(dir.toFile());
                getStage().addActor(openChooser.fadeIn());
            }
        });
        btnSave.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Ctx.bodies.getModels().isEmpty()) return;
                try {
                    Ctx.io.exportToFile();
                    Dialogs.showOKDialog(getStage(), "Success", "Save successfully done.");
                } catch (Exception e) {
                    Dialogs.showErrorDialog(getStage(), "Something went wrong while saving.", e);
                }
            }
        });

        HorizontalGroup hGroupBtn = new HorizontalGroup();
        hGroupBtn.space(10);
        hGroupBtn.addActor(btnNew);
        hGroupBtn.addActor(btnLoad);
        hGroupBtn.addActor(btnSave);

        top().pad(10, 12, 16, 12);

        add(title).left().top().colspan(2);
        row();
        add(hGroupBtn).left().colspan(2);
        row().padTop(8);
        add(projectLabel).left();
        add(projectTextField).expandX().fillX();

        Ctx.io.addChangeListener((source, propertyName) -> {
            if (propertyName.equals(IoManager.PROP_PROJECTFILE)) {
                Ctx.bodies.getModels().clear();
                File file = Ctx.io.getProjectFile();
                if (file == null) return;
                projectTextField.setText(file.getAbsolutePath());
                projectTextField.setCursorPosition(file.getAbsolutePath().length());
            }
        });
    }

}
