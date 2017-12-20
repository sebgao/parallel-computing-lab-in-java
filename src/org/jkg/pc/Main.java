package org.jkg.pc;

import java.io.FileNotFoundException;
import java.util.Date;


public class Main {
    public static int MAX_THREAD = 2000;
    public static void main(String[] args) {

        TextIntReader tir = new TextIntReader();
        try {
            tir.readFile("random.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        QuickSortParallel qsp = new QuickSortParallel();
        qsp.loadContent(tir);
        qsp.sort();
        qsp.dump("order1.txt");

        QuickSort qs = new QuickSort();
        qs.loadContent(tir);
        qs.sort();
        qs.dump("order2.txt");

        MergeSortParallel msp = new MergeSortParallel();
        msp.loadContent(tir);
        msp.sort();
        msp.dump("order3.txt");

        MergeSort ms = new MergeSort();
        ms.loadContent(tir);
        ms.sort();
        ms.dump("order4.txt");

        RankSortParallel rsp = new RankSortParallel();
        rsp.loadContent(tir);
        rsp.sort();
        rsp.dump("order5.txt");

        RankSort rs = new RankSort();
        rs.loadContent(tir);
        rs.sort();
        rs.dump("order6.txt");

    }
}
