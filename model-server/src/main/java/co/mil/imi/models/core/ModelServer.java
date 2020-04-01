package co.mil.imi.models.core;

import java.io.*;
import java.util.ArrayList;

public class ModelServer {

    BufferedReader reader;
    BufferedWriter writer;
    private boolean hasFailed = false;

    public ModelServer(String directory, ArrayList<String> command) {
        loadModel(directory, command);
    }

    public String sendTask(String task) {
        StringBuilder builder = new StringBuilder();
        try {
            writer.write(task);
            writer.newLine();
            writer.flush();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("Finish :)"))
                    break;
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            hasFailed = true;
        }
        return builder.toString();
    }

    private void loadModel(String directory, ArrayList<String> command) {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(directory));
        System.out.println(pb.directory());
        try {
            Process process = pb.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.equals("finish loading model"))
                    break;
            }
        } catch (IOException e) {
            hasFailed = true;
            e.printStackTrace();
        }
    }

    public boolean validate() {
        return !hasFailed;
    }
}
