package me.Maxim.Compiler;

import me.Maxim.Compiler.GUI.Menu;

import java.util.Scanner;

public class Main {

    //Edit this for the pseudocode you want. This code currently shows off all the supported features. Now includes for loop.
    /*
    This string used to be what this program compiled, but now look at it as a way to see what this compiler can do.
    static String inCode =
            "BOOLEAN e\n" +
            "BOOLEAN hello\n" +
            "BOOLEAN b\n" +
            "STRING str\n" +
            "STRING value\n" +
            "SET value TO INPUT\n" +
            "INTEGER i\n" +
            "DOUBLE d\n" +
            "CHAR c\n" +
            "OBJECT o\n" +
            "SET b TO INPUT(BOOLEAN)\n" +
            "SET d TO INPUT(DOUBLE)\n" +
            "SET i TO INPUT(INT)\n" +
            "SET c TO INPUT(CHAR)\n" +
            "SET o TO (d*i)-i\n" +
            "SET hello TO TRUE\n" +
            "SET hello TO NOT hello\n" +
            "SET str TO \"Hello people!\"\n" +
            "SET i TO 1\n" +
            "#WHILE i < 10:\n" +
            "    SET i TO i+1\n" +
            "    OUTPUT i + \"\"\n" +
            "#ENDWHILE\n" +
            "#FOR SET INTEGER j TO 0 COMP j < 10 MODIFY j++ :\n" +
            "   OUTPUT j + \"\"\n" +
            "#ENDFOR\n" +
            "#FOR SET CHAR c IN str :\n" +
            "   OUTPUT c + \"\"\n" +
            "#ENDFOR\n" +
            "#IF value NOT = \"\":\n" +
            "    SET e TO TRUE\n" +
            "#ENDIF\n" +
            "#IF e = true:\n" +
            "    #IF NOT hello:\n" +
            "        SET hello TO true\n" +
            "    #ENDIF\n" +
            "    OUTPUT \"Hello\"\n" +
            "#ENDIF\n" +
            "#ELIF hello:\n" +
            "    OUTPUT \"Hello 2\"\n" +
            "#ENDIF\n" +
            "#ELSE\n" +
            "    OUTPUT \"Hello 3\"\n" +
            "#ENDIF\n";
*/
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.setVisible(true);
        Scanner input = new Scanner(System.in);
        String userInput = "";
        String compiled = "";
        System.out.println("Enter your code below. Indentation does not matter, but do NOT use more than 4 spaces for indentation.");
        System.out.println("You can use tab if you want indentation. To compile your code write \"--endWriteProcess\" on a new line:");
        //Worst text editor ever (I'll make a new one soon)
        while (!userInput.equals("--endWriteProcess")) {
            userInput = input.nextLine();
            compiled = compiled + userInput + "\n";
        }
        System.out.println(compile(compiled));
    }

    public static String compile(String code) {
        code = code.replaceAll("    ", "");
        code = code.replaceAll("   ", "");
        code = code.replaceAll("  ", "");
        code = code.replaceAll("\t", "");
        code = code.replaceAll(" ", "@@@");
        String lines[] = code.split("\n");

        String result = "";
        int tabs = 0;
        boolean nextName = false;
        boolean nextLast = false;
        boolean nextValue = false;
        boolean nextCond = false;
        boolean nextBrac = false;
        boolean nextStr = false;
        boolean lastOut = false;
        boolean nextFor = false;
        boolean nextSet = false;
        boolean nextComp = false;
        boolean nextMod = false;

        result = result + "package com.maxim.compiler;\n\n";

        if (code.contains("INPUT")) {
            result = result + "import java.util.Scanner;\n";
        }

        result = result + "\n";
        result = result + "public class Main {\n\t";
        tabs++;
        result = result + "public static void main(String[] args]) {\n\t\t";
        tabs++;

        if (code.contains("INPUT")) {
            result = result + "Scanner scanner = new Scanner(System.in);\n\t\t";
        }
        //Good luck looking through this to try and understand it! Sorry for the weird variable names and messy code. I'll improve over time as I code more.
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].split("@@@").length; j++) {
                String a = lines[i].split("@@@")[j];
                if (!nextName && !nextValue && !nextFor) {
                    switch (a) {
                        case "BOOLEAN":
                            result = result + "boolean ";
                            nextName = true;
                            nextLast = true;
                            break;
                        case "INTEGER":
                            result = result + "int ";
                            nextName = true;
                            nextLast = true;
                            break;
                        case "DOUBLE":
                            result = result + "double ";
                            nextName = true;
                            nextLast = true;
                            break;
                        case "CHAR":
                            result = result + "char ";
                            nextName = true;
                            nextLast = true;
                            break;
                        case "OBJECT":
                            result = result + "Object ";
                            nextName = true;
                            nextLast = true;
                            break;
                        case "STRING":
                            result = result + "String ";
                            nextName = true;
                            nextLast = true;
                            break;
                        case "SET":
                            nextName = true;
                            nextLast = false;
                            break;
                        case "TO":
                            result = result + " = ";
                            nextValue = true;
                            nextLast = true;
                            break;
                        case "#IF":
                            result = result + "if (";
                            nextValue = true;
                            nextCond = true;
                            tabs++;
                            break;
                        case "#ELIF":
                        case "#ELSEIF":
                            result = result + "else if (";
                            nextValue = true;
                            nextCond = true;
                            tabs++;
                            break;
                        case "#WHILE":
                            result = result + "while (";
                            nextValue = true;
                            nextCond = true;
                            tabs++;
                            break;
                        case "#FOR":
                            result = result + "for (";
                            nextFor = true;
                            nextName = false;
                            tabs++;
                            break;
                        case "OUTPUT":
                            result = result + "System.out.println(";
                            nextValue = true;
                            nextBrac = true;
                            lastOut = true;
                            break;
                        case "#ENDFOR":
                        case "#ENDWHILE":
                        case "#ENDIF":
                            tabs--;
                            result = result + "\b}\n";
                            if (tabs > 0) {
                                for (int h = 0; h < tabs; h++) {
                                    result = result + "\t";
                                }
                            }
                            break;
                        case "#ELSE":
                            tabs++;
                            result = result + "else {\n";
                            if (tabs > 0) {
                                for (int h = 0; h < tabs; h++) {
                                    result = result + "\t";
                                }
                            }
                        default:
                            break;
                    }
                }
                else if (nextFor) {
                    if (nextName) {
                        nextName = false;
                        result = result + a;
                    }
                    else if (nextComp) {
                        if (a.contains("MODIFY")) {
                            nextComp = false;
                            nextMod = true;
                            result = result + "\b; ";
                        }
                        else {
                            result = result + a + " ";
                        }
                    }
                    else if (nextMod) {
                        if (a.toCharArray()[a.length() - 1] == ':') {
                            nextMod = false;
                            nextFor = false;
                            result = result + "\b) {\n";
                            if (tabs > 0) {
                                for (int h = 0; h < tabs; h++) {
                                    result = result + "\t";
                                }
                            }
                        }
                        else {
                            result = result + a + " ";
                        }
                    }
                    else if (nextSet) {
                        if (a.contains("INTEGER")) {
                            result = result + "int ";
                            nextName = true;
                        }
                        else if (a.contains("DOUBLE")) {
                            result = result + "double ";
                            nextName = true;
                        }
                        else if (a.contains("STRING")) {
                            result = result + "String ";
                            nextName = true;
                        }
                        else if (a.contains("CHAR")) {
                            result = result + "char ";
                            nextName = true;
                        }
                        else {
                            result = result + a;
                        }
                        nextSet = false;
                    }
                    else {
                        if (a.contains("SET")) {
                            nextSet = true;
                        }
                        if (a.contains("COMP")) {
                            result = result + "; ";
                            nextComp = true;
                        }
                        if (a.contains("MODIFY")) {
                            nextMod = true;
                        }
                        if (a.contains("TO")) {
                            result = result + " = ";
                            nextName = true;
                        }
                        if (a.contains("IN")) {
                            result = result + " : ";
                            nextName = true;
                        }
                        if (a.toCharArray()[a.length() - 1] == ':') {
                            nextFor = false;
                            result = result + ") {\n";
                            if (tabs > 0) {
                                for (int h = 0; h < tabs; h++) {
                                    result = result + "\t";
                                }
                            }
                        }
                    }
                }
                else if (nextName) {
                    result = result + a;
                    if (nextLast) {
                        result = result + ";\n";
                        if (tabs > 0) {
                            for (int h = 0; h < tabs; h++) {
                                result = result + "\t";
                            }
                        }
                        nextLast = false;
                    }
                    nextName = false;
                }
                else if (nextValue) {

                    if (a.contains("INPUT") && !nextStr && !nextCond) {
                        if (a.contains("INT")) {
                            result = result + "scanner.nextInt();";
                        }
                        else if (a.contains("DOUBLE")) {
                            result = result + "scanner.nextDouble();";
                        }
                        else if (a.contains("CHAR")) {
                            result = result + "scanner.nextLine().soCharArray()[0];";
                        }
                        else if (a.contains("BOOLEAN")) {
                            result = result + "scanner.nextBoolean();";
                        }
                        else {
                            result = result + "scanner.nextLine();";
                        }
                        result = result + "\n";
                        if (tabs > 0) {
                            for (int h = 0; h < tabs; h++) {
                                result = result + "\t";
                            }
                        }
                        nextValue = false;
                        nextLast = false;
                    }
                    else {
                        if (a.contains("\"") && !lastOut && !nextCond) {
                            nextBrac = true;
                            nextStr = true;
                        }

                        if (a.contains("NOT") && !nextStr) {
                            result = result + "!";
                        }
                        else {
                            if (nextBrac) {
                                if (a.toCharArray()[a.length() - 1] == '\"') {
                                    result = result + a;
                                    if (nextStr) {
                                        result = result + ";\n";
                                        nextStr = false;
                                    }
                                    else {
                                        result = result + ");\n";
                                    }
                                    if (tabs > 0) {
                                        for (int h = 0; h < tabs; h++) {
                                            result = result + "\t";
                                        }
                                    }
                                    nextBrac = false;
                                    nextValue = false;
                                    lastOut = false;
                                }
                                else {
                                    result = result + a + " ";
                                }
                            }
                            else {

                                if (a.contains("=") && !a.contains("==")) {
                                    if (result.toCharArray()[result.length() - 1] != '!') {
                                        a = a.replaceAll("=", "==");
                                    }
                                }

                                if (a.contains("TRUE")) {
                                    result = result + "true";
                                    if (nextCond) {
                                        result = result + " ";
                                    }
                                } else if (a.contains("FALSE")) {
                                    result = result + "false";
                                    if (nextCond) {
                                        result = result + " ";
                                    }
                                } else {
                                    result = result + a;
                                }
                                nextValue = false;
                                if (nextCond) {
                                    if (a.toCharArray()[a.length() - 1] == ':') {
                                        result = result + "\b) {\n";
                                        if (tabs > 0) {
                                            for (int h = 0; h < tabs; h++) {
                                                result = result + "\t";
                                            }
                                        }
                                        nextCond = false;
                                    }
                                    else {
                                        nextValue = true;
                                        result = result + " ";
                                    }
                                }
                                if (nextLast) {
                                    result = result + ";\n";
                                    if (tabs > 0) {
                                        for (int h = 0; h < tabs; h++) {
                                            result = result + "\t";
                                        }
                                    }
                                    nextLast = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        result = result + "\b}\n}";
        return result;

    }

}