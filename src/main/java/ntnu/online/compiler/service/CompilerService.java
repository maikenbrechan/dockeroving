package ntnu.online.compiler.service;


import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;

@Service
public class CompilerService {

    public String compiler(String inputCode) throws Exception{
        String[] words = inputCode.split(" ");

        String className = words[2];

        //Creates .java file
        try (PrintWriter out = new PrintWriter("src/" + className + ".java")) {
            out.println(inputCode);
        }

        createDockerFileJava(className);

        String imageName = "compile" + new Date().getTime();

        //Creates docker image
        String[] dockerCommand = new String[] {"docker", "image", "build", "-t", imageName, "."};
        ProcessBuilder probuilder = new ProcessBuilder(dockerCommand);
        Process process = probuilder.start();
        process.waitFor();

        //Creats docker container and runs
        dockerCommand = new String[] {"docker", "container", "run", imageName};
        probuilder = new ProcessBuilder(dockerCommand);

        probuilder.redirectErrorStream(true);
        probuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        process = probuilder.start();

        process.waitFor();

        //Creates file with program output
        StringBuilder stringBuilder = new StringBuilder();

        //Prints output to terminal
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = reader.readLine();
            }
            //Removes extra \n
            stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }

        //Deletes the .java and docker file on exit
        File javaFile = new File("src/" + className + ".java");
        javaFile.deleteOnExit();
        File dockerFile = new File ("Dockerfile");
        dockerFile.deleteOnExit();

        if(stringBuilder.toString().length()>255){
            return "The Error is too long to display";
        }

        return stringBuilder.toString();
    }

    public void createDockerFileJava(String className) throws FileNotFoundException {
        String dokcerfileContent = "FROM openjdk:15\nCOPY ./src/ /tmp\nWORKDIR /tmp\nRUN javac "
                + className + ".java\nENTRYPOINT [\"java\",\"" + className
                + "\"]";

        try (PrintWriter out = new PrintWriter( "Dockerfile")) {
            out.println(dokcerfileContent);
        }
    }



        /*
        public class HelloWorld { public static void main(String[] args) {
        System.out.println("Hello world!Goodbye world!Hello again!");
                   }};
                    */
}


