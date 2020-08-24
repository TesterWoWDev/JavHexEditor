import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
public class runner {
    public static String fileName = "";
    public static byte[] fileContent;

    public static void main(String[] args) throws IOException {
        if (args.length > 0)
            proofOfConcept(args[0]);
        else {
            proofOfConcept("C:\\Users\\PC\\IdeaProjects\\hexEditing\\Abyssal_Outland.m2");
            System.out.println("Use this on a commandline, put your m2 as the argument.");
        }
    }

    public static void proofOfConcept(String arg1) throws IOException {
        readInFile(arg1);
        //either works
        String hex = Integer.toHexString(304);
        //String hex = "193";//dont include the 0x or the h ex.0x193h -> 193
        printAtAddress(hex, 5);
        replaceStringAtAddress(hex, "trash");
        printAtAddress(hex, 5);
        String value = getAtAddress(hex,5);
        System.out.println(value);
        replaceBytesAtAddress(hex,"885533");
        printAtAddress(hex, 5);
        insertStringAtEndOfFile("trash");
        insertBytesAtEndOfFile("772129");//no spaces for the bytes
        printAtAddress(Integer.toHexString(fileContent.length-10), 10);
        saveFile();
    }

    public static String returnString(String input){
        StringBuilder result = new StringBuilder();
        char[] charArray = input.toCharArray();
        for(int i = 0; i < charArray.length; i=i+2) {
            String st = ""+charArray[i]+""+charArray[i+1];
            char ch = (char)Integer.parseInt(st, 16);
            result.append(ch);
        }
        return result.toString();
    }

    public static void readInFile(String name) throws IOException {
        fileName = name;
        fileContent = Files.readAllBytes(Paths.get(name));
    }

    public static void replaceBytesAtAddress(String address,String replacement){
        replaceStringAtAddress(address,returnString(replacement));
    }

    public static void insertBytesAtEndOfFile(String replacement){
       insertStringAtEndOfFile(returnString(replacement));
    }
    
    public static void replaceStringAtAddress(String address,String replacement){
        byte[] replace = replacement.getBytes();
        int size = replace.length;
        int hex = Integer.parseInt(address,16);
        System.arraycopy(replace, 0, fileContent, hex, size);
    }

    public static void insertStringAtEndOfFile(String replacement){
        byte[] replace = replacement.getBytes();
        int size = replace.length;
        byte[] holder = new byte[fileContent.length + size];
        System.arraycopy(fileContent, 0, holder, 0, fileContent.length);
        System.arraycopy(replace, 0, holder, fileContent.length, size);
        fileContent = holder;
    }

    public static void printAtAddress(String address, int size) {
        int hexToInt = Integer.parseInt(address, 16);
        for (int i=0;i<size;i++) {
            System.out.printf("%02x ", fileContent[hexToInt+i]);
        }
        System.out.println();
    }

    public static String getAtAddress(String address, int size) {
        StringBuilder join = new StringBuilder();
        int hexToInt = Integer.parseInt(address, 16);
        for (int i=0;i<size;i++) {
            join.append(String.format("%02x", fileContent[hexToInt + i]));
        }
        return join.toString();
    }

    public static void saveFile() throws IOException {
        Files.write(Paths.get((fileName.split("\\\\")[fileName.split("\\\\").length-1])+1), fileContent);
    }
}
