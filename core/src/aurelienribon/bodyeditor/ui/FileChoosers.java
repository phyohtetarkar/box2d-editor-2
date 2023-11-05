package aurelienribon.bodyeditor.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;

import java.util.function.Consumer;

/**
 * @author phyohtetarkar
 */
public class FileChoosers {

    public static FileChooser buildOpenFileChooser(Consumer<Array<FileHandle>> onSelected) {
        FileTypeFilter typeFilter = new FileTypeFilter(false);
        typeFilter.addRule("JSON files (*.json)", "json");
        return buildOpenFileChooser(onSelected, typeFilter, FileChooser.SelectionMode.FILES);
    }

    public static FileChooser buildOpenFileChooser(Consumer<Array<FileHandle>> onSelected, FileTypeFilter fileTypeFilter, FileChooser.SelectionMode selectionMode) {
        FileChooser openChooser = new FileChooser(FileChooser.Mode.OPEN);
        openChooser.setSelectionMode(selectionMode);
        openChooser.setFileTypeFilter(fileTypeFilter);
        openChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                if (onSelected == null) return;
                onSelected.accept(file);
            }
        });

        return openChooser;
    }

    public static FileChooser buildSaveFileChooser(Consumer<Array<FileHandle>> onSelected) {
        FileChooser openChooser = new FileChooser(FileChooser.Mode.SAVE);
        openChooser.setSelectionMode(FileChooser.SelectionMode.DIRECTORIES);
        openChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                if (onSelected == null) return;
                onSelected.accept(file);
            }
        });

        return openChooser;
    }

}
