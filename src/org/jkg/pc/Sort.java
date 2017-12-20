package org.jkg.pc;

import com.sun.istack.internal.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public abstract class Sort {
    Integer[] content;
    int length;
    Sort(){

    }
    public void testPrint(){
        for (int num:
                content) {
            System.out.println(num);
        }
    }
    public void dump(String path){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintStream printStream = new PrintStream(fileOutputStream);
        for (int num:
                content) {
            printStream.print(num);
            printStream.print(" ");
        }
    }
    public void loadContent(TextIntReader tir){
        length = tir.getLength();
        content = tir.getContent().toArray(new Integer[length]);
    }
    public void swap(int i, int j){
        int temp = content[i];
        content[i] = content[j];
        content[j] = temp;
    }
    public abstract void sort();

}
