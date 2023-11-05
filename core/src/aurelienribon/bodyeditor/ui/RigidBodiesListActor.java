package aurelienribon.bodyeditor.ui;

import aurelienribon.bodyeditor.Ctx;
import aurelienribon.bodyeditor.RigidBodiesManager;
import aurelienribon.bodyeditor.models.RigidBodyModel;
import aurelienribon.utils.notifications.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
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
        scrollPane.getListeners().forEach(eventListener -> {
            if (eventListener instanceof ActorGestureListener) {
                return;
            }
            scrollPane.removeListener(eventListener);
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
