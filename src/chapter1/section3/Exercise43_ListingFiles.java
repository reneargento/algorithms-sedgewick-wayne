package chapter1.section3;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;

/**
 * Created by Rene Argento on 8/23/16.
 */
public class Exercise43_ListingFiles {

    private Queue<String> fileQueue;

    public Exercise43_ListingFiles() {
        fileQueue = new Queue<>();
    }

    private void listFiles(File file, int depth) {
        if (!file.exists()) {
            return;
        }

        //Add directory
        addFileToQueue(file, depth);

        File[] fileList = file.listFiles();

        if (fileList != null) {
            for(File fileItem : fileList) {

                if (fileItem.isFile()) {
                    addFileToQueue(fileItem, depth);
                } else if (fileItem.isDirectory()) {
                    listFiles(fileItem, depth+1);
                }
            }
        }
    }

    private void addFileToQueue(File file, int depth) {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < depth; i++) {
            stringBuilder.append("  ");
        }

        stringBuilder.append(file.getName());

        fileQueue.enqueue(stringBuilder.toString());
    }

    public static void main(String[] args) {
        String folderPath = args[0];
        File folder = new File(folderPath);

        Exercise43_ListingFiles listFiles = new Exercise43_ListingFiles();
        listFiles.listFiles(folder, 0);

        for(String fileName : listFiles.fileQueue) {
            StdOut.println(fileName);
        }
    }

}
