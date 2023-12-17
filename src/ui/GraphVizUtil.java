package ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.awt.Desktop;

public class GraphVizUtil {

    /**
     * Create a new temp file and write dot inside.
     * 
     * @param dot A graph in the dot language.
     * @return The temp dot file.
     * @throws IOException
     */
    private static File saveDotFile(String dot) throws IOException {
        File file = File.createTempFile("paa_dot", ".dot");
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(dot);
        }
        file.deleteOnExit();
        return file;
    }

    /**
     * Create a temp png file.
     * 
     * @return The image file.
     * @throws IOException
     */
    private static File getImageOutput() throws IOException {
        File file = File.createTempFile("paa_img", ".png");
        file.deleteOnExit();
        return file;
    }

    /**
     * Check if the system has graphviz in the path.
     * 
     * @return true if graphviz is found, false otherwise.
     */
    private static boolean hasGraphViz() {
        boolean res = false;
        try {
            Process p = Runtime.getRuntime().exec("dot -V");
            p.waitFor();
            if (p.exitValue() == 0) {
                res = true;
            }
        } catch (IOException | InterruptedException ignored) {
        }
        return res;
    }

    /**
     * Call dot and create the image. This function is like `dot -Tpng dotFile >
     * outFile` in the terminal.
     * 
     * @param dotFile The file who contains the graph in the dot language.
     * @param outFile The output image file.
     * @throws IOException
     * @throws InterruptedException
     */
    private static void generateImage(File dotFile, File outFile) throws IOException, InterruptedException {
        String command = String.format("dot -Tpng %s", dotFile.getAbsolutePath());
        Process p = Runtime.getRuntime().exec(command);
        int exitCode = p.waitFor();
        if (exitCode != 0)
            throw new IOException("Error when execute dot.");
        Files.copy(p.getInputStream(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Display the graph in the system image viewer if possible.
     * 
     * @param dot The graph in the dot language.
     */
    public static void displayGraph(String dot) {
        if (!hasGraphViz()) {
            System.out.println("Warning: GraphViz is not found!");
            return;
        }
        try {
            File dotFile = saveDotFile(dot);
            File outFile = getImageOutput();
            generateImage(dotFile, outFile);
            Desktop.getDesktop().open(outFile);
        } catch (IOException | InterruptedException e) {
            System.out.println("Warning: can't show the graph!");
            System.out.println(e);
        }
    }

}
