package project.cloudmanager.other;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * File System Tree Item:
 * This class is a TreeItem for the given file Path. It does
 * this by overriding the TreeItem's getChildren() and the isLeaf() methods.
 */
public class FSTreeItem extends TreeItem<File> {


    // Cache whether the file is a leaf or not. A file is a leaf if
    // it is not a directory. The isLeaf() is called often, and doing
    // the actual check on Path is expensive.
    private boolean isLeaf;

    // Do the children and leaf testing only once, and then set these
    // booleans to false so that we do not check again during this run.
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isRoot = false;



    /**
     * Constructor
     *
     * @param file The root input used to build the file tree
     */
    public FSTreeItem(File file) {

        super(file);
    }

    /**
     * Constructor - special, only use with getChildren to get list of root directories
     *
     * @param root Any string, it doesn't matter
     */
    public FSTreeItem(String root) {
        this.isRoot = true;
    }

    /**
     * Checks if this TreeItem is a Leaf
     *
     * @return True if it's a leaf, False if it's a directory
     */
    @Override
    public boolean isLeaf() {
        if (isRoot) {
            return false;
        }

        if (isFirstTimeLeaf) {

            isFirstTimeLeaf = false;
            File file = getValue();
            isLeaf = Files.isRegularFile(file.toPath());
        }

        return isLeaf;
    }

    public boolean isRoot() {
        return isRoot;
    }

    /**
     * Gets an ObservableList of TreeItems that contains the children of this TreeItem
     *
     * @return An ObservableList of children TreeItems
     */
    @Override
    public ObservableList<TreeItem<File>> getChildren() {
        if (isRoot) {
            return Arrays.stream(File.listRoots())
                    .map(p -> new FSTreeItem(p))
                    .collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));
        }
        if (isFirstTimeChildren) {

            isFirstTimeChildren = false;

            // First getChildren() call, so we actually go off and
            // determine the children of the file contained in this TreeItem.
            super.getChildren().setAll(buildChildren(this));
        }

        return super.getChildren();
    }

    /**
     * Creates an ObservableList of TreeItems that contains the children of the given TreeItem
     *
     * @param treeItem The parent node
     * @return An ObservableList of children TreeItems
     */
    private ObservableList<TreeItem<File>> buildChildren(
            TreeItem<File> treeItem) {

        File file = treeItem.getValue();

        if ((file != null) && (file.isDirectory())) {

            try(Stream<Path> pathStream = Files.list(file.toPath())) {

                return pathStream
                        .map(p -> new FSTreeItem(p.toFile()))
                        .collect(Collectors.toCollection(() ->
                                FXCollections.observableArrayList()));
            }
            catch(IOException e) {

                throw new UncheckedIOException(e);
            }
        }

        return FXCollections.emptyObservableList();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FSTreeItem other = (FSTreeItem) obj;
        // Assuming FSTreeItem is wrapping a file, compare by file path
        return this.getValue().getPath().equals(other.getValue().getPath());
    }

    @Override
    public int hashCode() {
        return this.getValue().getPath().hashCode();
    }

    public static void main(String[] args) {
        FSTreeItem dir;
        File[] rootDrives = File.listRoots();

        for (File rootDrive: rootDrives) {
            System.out.println("Drive: " + rootDrive.getPath());
            dir = new FSTreeItem(rootDrive);
            for (TreeItem<File> file: dir.getChildren()) {
                System.out.println("\t" + file.getValue().getPath() + file.isLeaf());
            }
        }
    }
}
