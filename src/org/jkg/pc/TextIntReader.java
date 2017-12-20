package org.jkg.pc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TextIntReader{
    ArrayList<Integer> content;
    TextIntReader(){
        content = new ArrayList<Integer>();
    }
    public void readFile(String pathname) throws FileNotFoundException {
        File f = new File(pathname);
        FileInputStream fin = new FileInputStream(f);
        Scanner scanner = new Scanner(fin);
        while(scanner.hasNextInt()){
            int num = scanner.nextInt();
            content.add(num);
        }
    }
    public void testPrint(){
        for (int num:
                content) {
            System.out.println(num);
        }
    }
    public int getLength(){
        return content.size();
    }

    public ArrayList<Integer> getContent() {
        return content;
    }
}
