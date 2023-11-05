package aurelienribon.bodyeditor.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.LinkLabel;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * @author phyohtetarkar
 */
public class HeadingActor extends VisTable {

    public HeadingActor() {
        Texture texture = new Texture(Gdx.files.internal("ui/title.png"));
        Image logo = new Image(texture);
        //logo.setSize(60f,30f);

        float textScale = 0.7f;

        VisLabel nameLabel = new VisLabel("2012 - Aurelien Ribon");
        nameLabel.setAlignment(Align.right);
        nameLabel.setFontScale(textScale);

        VisLabel urlLabel = new VisLabel("www.aurelienribon.com");
        urlLabel.setAlignment(Align.right);
        urlLabel.setFontScale(textScale);

        VisLabel manualLabel = new LinkLabel("Manual", "http://www.aurelienribon.com/blog/projects/physics-body-editor/");
        manualLabel.setAlignment(Align.right);
        manualLabel.setFontScale(0.9f);

        Table labelGroup = new Table();
        labelGroup.add(nameLabel).right();
        labelGroup.row();
        labelGroup.add(urlLabel).right();
        labelGroup.row();
        labelGroup.add(manualLabel).right().padTop(8);

        top().left().pad(20, 12, 20, 12);

        add(logo).top().expandX();
        add(labelGroup).padLeft(20).top().right();
    }


}
