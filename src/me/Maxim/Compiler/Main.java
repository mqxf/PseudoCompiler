package me.Maxim.Compiler;

import java.net.http.HttpResponse;

public class Main {

    //Edit this for the pseudocode you want. This code currently shows off all the supported features
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
            "#IF value NOT = \"\":\n" +
            "    SET e TO TRUE\n" +
            "#ENDIF\n" +
            "#IF e = true:\n" +
            "    #IF NOT hello:\n" +
            "        SET hello TO true\n" +
            "    #ENDIF\n" +
            "    OUTPUT \"Hello\"\n" +
            "#ENDIF\n" +
            "#IF hello:\n" +
            "    OUTPUT \"Hello 2\"\n" +
            "#ENDIF\n" +
            "#ELSE\n" +
            "    OUTPUT \"Hello 3\"\n" +
            "#ENDIF\n";

    public static void main(String[] args) {
        System.out.println(decompile(inCode));
    }

    public static String decompile(String code) {
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

        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].split("@@@").length; j++) {
                String a = lines[i].split("@@@")[j];
                if (!nextName && !nextValue) {
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
                        case "#WHILE":
                            result = result + "while (";
                            nextValue = true;
                            nextCond = true;
                            tabs++;
                            break;
                        case "OUTPUT":
                            result = result + "System.out.println(";
                            nextValue = true;
                            nextBrac = true;
                            lastOut = true;
                            break;
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