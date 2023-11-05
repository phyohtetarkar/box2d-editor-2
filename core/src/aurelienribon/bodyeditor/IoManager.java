package aurelienribon.bodyeditor;

import aurelienribon.bodyeditor.io.JsonIo;
import aurelienribon.utils.io.FilenameHelper;
import aurelienribon.utils.notifications.ChangeableObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class IoManager extends ChangeableObject {
    public static final String PROP_PROJECTFILE = "projectFile";
    private File projectFile;

    public File getProjectFile() {
        return projectFile;
    }

    public File getProjectDir() {
        return projectFile.getAbsoluteFile().getParentFile();
    }

    public void setProjectFile(File projectFile) {
        this.projectFile = projectFile;
        firePropertyChanged(PROP_PROJECTFILE);
    }

    public void exportToFile() throws IOException {
        assert projectFile != null;

        String str = JsonIo.serialize();
        FileUtils.writeStringToFile(projectFile, str);
    }

    public void importFromFile() throws IOException {
        assert projectFile != null;
        assert projectFile.isFile();

        Ctx.bodies.getModels().clear();
        String str = FileUtils.readFileToString(projectFile);

        JsonIo.deserialize(str);
    }

    public String buildImagePath(File imgFile) {
        return FilenameHelper.getRelativePath(imgFile.getPath(), getProjectDir().getAbsolutePath());
    }

    public File getImageFile(String imgPath) {
        if (imgPath == null) return null;
        return new File(projectFile.getParent(), imgPath);
    }
}
