package com.omfgdevelop.privatebookshelf.vaadinui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {

    public static String getVersion() throws IOException {
        System.out.println("Hello World");
        String command = "git describe --abbrev=0 --tags";
        Process p = Runtime.getRuntime().exec(command);
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        String text = "";
        System.out.println(text);
        while ((line = input.readLine()) != null) {
            text += line;
            System.out.println("Line: " + line);
        }
        return text;
    }
}
