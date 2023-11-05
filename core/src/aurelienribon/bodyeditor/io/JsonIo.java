package aurelienribon.bodyeditor.io;

import aurelienribon.bodyeditor.Ctx;
import aurelienribon.bodyeditor.models.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import org.apache.commons.io.FilenameUtils;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com
 */
public class JsonIo {
    public static String serialize() {
        JsonValue toJson = new JsonValue(JsonValue.ValueType.object);
        JsonValue rigidBodies = new JsonValue(JsonValue.ValueType.array);

        for (RigidBodyModel model : Ctx.bodies.getModels()) {

            JsonValue body = new JsonValue(JsonValue.ValueType.object);
            body.addChild("name", new JsonValue(model.getName()));
            body.addChild("imagePath", new JsonValue(FilenameUtils.separatorsToUnix(model.getImagePath())));

            JsonValue origin = new JsonValue(JsonValue.ValueType.object);
            origin.addChild("x", new JsonValue(model.getOrigin().x));
            origin.addChild("y", new JsonValue(model.getOrigin().y));
            body.addChild("origin", origin);

            JsonValue polygons = new JsonValue(JsonValue.ValueType.array);

            for (PolygonModel pm : model.getPolygons()) {
                JsonValue array = new JsonValue(JsonValue.ValueType.array);
                for (Vector2 vertex : pm.vertices) {
                    JsonValue object = new JsonValue(JsonValue.ValueType.object);
                    object.addChild("x", new JsonValue(vertex.x));
                    object.addChild("y", new JsonValue(vertex.y));
                    array.addChild(object);
                }

                polygons.addChild(array);
            }

            body.addChild("polygons", polygons);

            JsonValue circles = new JsonValue(JsonValue.ValueType.array);

            for (CircleModel cm : model.getCircles()) {
                JsonValue object = new JsonValue(JsonValue.ValueType.object);
                object.addChild("cx", new JsonValue(cm.center.x));
                object.addChild("cy", new JsonValue(cm.center.y));
                object.addChild("r", new JsonValue(cm.radius));
                circles.addChild(object);
            }

            body.addChild("circles", circles);

            JsonValue shapes = new JsonValue(JsonValue.ValueType.array);

            for (ShapeModel sm : model.getShapes()) {
                JsonValue object = new JsonValue(JsonValue.ValueType.object);
                object.addChild("type", new JsonValue(sm.getType().name()));
                JsonValue vertices = new JsonValue(JsonValue.ValueType.array);
                for (Vector2 vertex : sm.getVertices()) {
                    JsonValue vertexObj = new JsonValue(JsonValue.ValueType.object);
                    vertexObj.addChild("x", new JsonValue(vertex.x));
                    vertexObj.addChild("y", new JsonValue(vertex.y));
                    vertices.addChild(vertexObj);
                }
                object.addChild("vertices", vertices);
                shapes.addChild(object);
            }

            body.addChild("shapes", shapes);

            rigidBodies.addChild(body);
        }

        toJson.addChild("rigidBodies", rigidBodies);

        JsonValue dynamicObjects = new JsonValue(JsonValue.ValueType.array);
        for (DynamicObjectModel dm : Ctx.objects.getModels()) {
            JsonValue object = new JsonValue(JsonValue.ValueType.object);
            object.addChild("name", new JsonValue(dm.getName()));

            dynamicObjects.addChild(object);
        }

        toJson.addChild("dynamicObjects", dynamicObjects);

        return toJson.toJson(JsonWriter.OutputType.json);
    }

    public static void deserialize(String str) {
        JsonReader reader = new JsonReader();
        JsonValue json = reader.parse(str);

        JsonValue bodies = json.get("rigidBodies");

        for (JsonValue body : bodies) {
            RigidBodyModel model = new RigidBodyModel();
            model.setName(body.getString("name"));
            model.setImagePath(body.getString("imagePath", null));

            JsonValue origin = body.get("origin");
            model.getOrigin().x = origin.getFloat("x");
            model.getOrigin().y = origin.getFloat("y");

            JsonValue polygons = body.get("polygons");

            for (JsonValue vertices : polygons) {
                PolygonModel pm = new PolygonModel();

                for (JsonValue vertex : vertices) {
                    Vector2 vec = new Vector2(vertex.getFloat("x"), vertex.getFloat("y"));
                    pm.vertices.add(vec);
                }

                model.getPolygons().add(pm);
            }

            JsonValue circles = body.get("circles");

            for (JsonValue circle : circles) {
                CircleModel cm = new CircleModel();

                cm.center.x = circle.getFloat("cx");
                cm.center.y = circle.getFloat("cy");
                cm.radius = circle.getFloat("r");

                model.getCircles().add(cm);
            }

            JsonValue shapes = body.get("shapes");

            for (JsonValue shape : shapes) {
                ShapeModel.Type type = ShapeModel.Type.valueOf(shape.getString("type"));
                ShapeModel sm = new ShapeModel(type);

                JsonValue vertices = shape.get("vertices");

                for (JsonValue vertex : vertices) {
                    Vector2 vec = new Vector2(vertex.getFloat("x"), vertex.getFloat("y"));
                    sm.getVertices().add(vec);
                }

                model.getShapes().add(sm);

                sm.close();
            }

            Ctx.bodies.getModels().add(model);
        }

        // dynamic objects
        JsonValue dynamicObjects = json.get("dynamicObjects");

        if (dynamicObjects == null) return;

        for (JsonValue object : dynamicObjects) {
            DynamicObjectModel dm = new DynamicObjectModel();
            dm.setName(object.getString("name"));

            Ctx.objects.getModels().add(dm);
        }
    }
}
