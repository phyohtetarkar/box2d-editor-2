package aurelienribon.bodyeditor.ui;

import aurelienribon.bodyeditor.Ctx;
import aurelienribon.bodyeditor.models.RigidBodyModel;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisScrollPane;

/**
 * @author phyohtetarkar
 */
public class RigidBodiesListActor extends ListView<RigidBodyModel> {

    public RigidBodiesListActor(RigidBodyListAdapter adapter) {
        super(adapter);

        setItemClickListener(Ctx.bodies::select);

        getMainTable().getListView().getMainTable().top();

        VisScrollPane scrollPane = getScrollPane();
//        scrollPane.getListeners().forEach(eventListener -> {
//            if (eventListener instanceof ActorGestureListener) {
//                return;
//            }
//            scrollPane.removeListener(eventListener);
//        });

        scrollPane.addListener(new InputListener() {
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                Stage stage = event.getStage();
                boolean hovered = scrollPane.hit(x ,y, scrollPane.isTouchable()) != null;
                if (scrollPane.hasScrollFocus() && stage != null && !hovered) {
                    stage.setScrollFocus(null);
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Stage stage = event.getStage();
                boolean hovered = scrollPane.hit(x ,y, scrollPane.isTouchable()) != null;
                if (!scrollPane.hasScrollFocus() && stage != null && hovered) {
                    stage.setScrollFocus(scrollPane);
                }

            }
        });

        Ctx.bodies.getModels().addListChangedListener((source, added, removed) -> {
            Array<RigidBodyModel> list = new Array<>();
            for (RigidBodyModel model : Ctx.bodies.getModels()) {
                list.add(model);
                model.addChangeListener((source1, propertyName) -> {
                    if (propertyName.equals(RigidBodyModel.PROP_NAME) || propertyName.equals(RigidBodyModel.PROP_IMAGEPATH)) {
                        adapter.itemsChanged();
                    }
                });
            }
            adapter.clear();
            adapter.addAll(list);
            adapter.itemsChanged();
        });

    }
}
