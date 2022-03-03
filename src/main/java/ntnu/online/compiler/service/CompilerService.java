package ntnu.online.compiler.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.List;

@Service
public class CompilerService {
    BufferedReader br;
    PrintWriter writer;

    private static void createDockerFileJava(String className) throws FileNotFoundException {
        String dokcerfileContent = "FROM openjdk:15\nCOPY ./src/ /tmp\nWORKDIR /tmp\nRUN javac "
                + className + ".java\nENTRYPOINT [\"java\",\"" + className
                + "\"]";

        //Creates Dockerfile
        try (PrintWriter out = new PrintWriter( "Dockerfile")) {
            out.println(dokcerfileContent);
        }
    }
    public static StringBuilder compileAndRun(String code, String className) throws IOException, InterruptedException {
        try (PrintWriter out = new PrintWriter("src/" + className + ".java")) {
            out.println(code);
        }

        createDockerFileJava(className);
        System.out.println("dockerfile created");
        String imageName = "compile" + new Date().getTime();

        //Creates docker image
        String[] dockerCommand = new String[] {"docker", "image", "build", "-t", imageName, " ."};
        ProcessBuilder probuilder = new ProcessBuilder(dockerCommand);
        Process commandline = probuilder.start();
        commandline.waitFor();
        System.out.println("image built");

        Thread.sleep(5000);

        //Creates image and runs
        ProcessBuilder rundocker = new ProcessBuilder("docker","container","run", imageName);
        rundocker.redirectErrorStream(true);
        rundocker.redirectError(ProcessBuilder.Redirect.INHERIT);
        commandline = rundocker.start();
        commandline.waitFor();
        System.out.println("code running in docker");
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(commandline.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                System.out.println(line);
                line = reader.readLine();
            }
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }
        System.out.println("deleting files");
        //File javaFile = new File("src/" + className + ".java");
        //javaFile.deleteOnExit();
        //File dockerFile = new File ("Dockerfile");
        //dockerFile.deleteOnExit();

        return sb;
    }

}
