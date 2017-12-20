package org.jkg.pc;

import java.util.Date;

public class RankSortParallel extends RankSort {
    Integer[] target;

    public void sort(){
        target = new Integer[length];
        int blockNr = 16>length?length:16;
        int blockLength = length/blockNr;
        int i;
        Thread[] threads = new Thread[blockNr];
        long past = (new Date()).getTime();
        for (i = 0; i < blockNr-1; i++) {
            threads[i] = new Thread(new RankSortParallelRunnable(this, i*blockLength, (i+1)*blockLength));
            threads[i].start();
        }
        Thread t = new Thread(new RankSortParallelRunnable(this, i*blockLength, length));
        t.start();

        for (i = 0; i < blockNr-1 ; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        writeContent();
        long now = (new Date()).getTime();
        System.out.println("Parallel Ranksort: "+(now-past)+"ms");
    }
    public void writeTarget(int[] indices, int low, int high){
        for (int i = 0; i < high-low; i++) {
            target[indices[i]] = content[low+i];
            //System.out.print("indices :"+(indices[i])+", content :"+indices[i]);
        }
    }
    public void writeContent(){
        content = target;
    }
}
