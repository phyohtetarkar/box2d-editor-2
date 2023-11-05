package aurelienribon.bodyeditor.ui;

import aurelienribon.bodyeditor.Ctx;
import aurelienribon.bodyeditor.canvas.Canvas;
import aurelienribon.bodyeditor.models.RigidBodyModel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayAdapter;
import com.kotcrab.vis.ui.widget.Tooltip;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * @author phyohtetarkar
 */
public class RigidBodyListAdapter extends ArrayAdapter<RigidBodyModel, VisTable> {

    private final Drawable bg = VisUI.getSkin().getDrawable("window-bg");
    private final Drawable selection = VisUI.getSkin().getDrawable("list-selection");

    public RigidBodyListAdapter(Array<RigidBodyModel> array) {
        super(array);
        setSelectionMode(SelectionMode.MULTIPLE);
    }

    @Override
    protected VisTable createView(RigidBodyModel item) {
        VisLabel titleLabel = new VisLabel(item.getName());
        VisLabel subtitleLabel = new VisLabel();
        subtitleLabel.setColor(1, 1, 1, 0.7f);
        subtitleLabel.setEllipsis(true);


        VisImage image = new VisImage();
        image.setScaling(Scaling.fit);
        String imgPath = item.getImagePath();



        if (imgPath != null) {
            new Tooltip.Builder(imgPath).target(subtitleLabel).build();
            if (item.isImagePathValid()) {
                subtitleLabel.setText(imgPath);
                try {
                    Texture imgTexture = new Texture(Gdx.files.absolute(Ctx.io.getImageFile(imgPath).getPath()));
                    image.setDrawable(new TextureRegionDrawable(imgTexture));
                } catch (Exception ex) {
                    //ex.printStackTrace();
                }
            } else {
                subtitleLabel.setText("[not found] " + imgPath);
                try {
                    Texture imgTexture = new Texture(Gdx.files.internal("ui/unknown.png"));
                    image.setDrawable(new TextureRegionDrawable(imgTexture));
                } catch (Exception ex) {
                    //ex.printStackTrace();
                }
            }

        } else {
            subtitleLabel.setText("No associated image");
            image.setVisible(false);
        }

        VisTable textLayout = new VisTable(true);
        textLayout.add(titleLabel).width(Canvas.OFFSET_X - 100).left().top().expandX();
        textLayout.row();
        textLayout.add(subtitleLabel).width(Canvas.OFFSET_X - 100).left().top().expandX();

        VisTable table = new VisTable();
        table.pad(10);
        table.add(image).size(50).left().top();
        table.add(textLayout).left().top().padLeft(10).expandX().fillX();

        return table;
    }

    @Override
    protected void selectView(VisTable view) {
        view.setBackground(selection);
    }

    @Override
    protected void deselectView(VisTable view) {
        view.setBackground(bg);
    }
}
