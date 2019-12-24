package co.mil.imi.image_loader.core;

import java.io.*;

public class ModelServer {

    BufferedReader reader;
    BufferedWriter writer;

    public void sendTask(String task) {
        try {
            writer.write(task);
            writer.newLine();
            writer.flush();

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.equals("finish :)"))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadModel() {
        ProcessBuilder pb = new ProcessBuilder("venv/bin/python", "detect_from_file.py");
        pb.directory(new File("../detector/"));
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
            e.printStackTrace();
        }
    }
}
