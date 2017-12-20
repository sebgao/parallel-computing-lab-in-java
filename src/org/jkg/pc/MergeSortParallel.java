package org.jkg.pc;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MergeSortParallel extends MergeSort {
    Future[] futures;
    ExecutorService executorService;
    protected void mergeWithBufferParallel(int low, int mid, int high){
        for (int i = low; i < high; i++) {
            futures[i] = executorService.submit(new RankUnit(content, buffer, low, mid, high, i));
        }
        //barrier
        for (int i = low; i < high; i++) {
            try {
                futures[i].get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.arraycopy(buffer, low, content, low, high-low);
    }
    protected void mergeSortParallel(int low, int high){
        if(low + 1 < high) {
            int mid = (low + high) / 2;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    mergeSortParallel(mid, high);
                }
            };
            mergeSortParallel(low, mid);
            Future f = executorService.submit(r);
            try {
                f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            mergeWithBufferParallel(low, mid, high);
        }
    }
    @Override
    public void sort() {
        executorService = Executors.newFixedThreadPool(Main.MAX_THREAD);
        futures = new Future[length];
        buffer = new Integer[length];
        long past = (new Date()).getTime();
        mergeSortParallel(0, length);
        long now = (new Date()).getTime();
        System.out.println("Parallel Mergesort: "+(now-past)+"ms");
        executorService.shutdown();
    }

}

class RankUnit implements Runnable{
    int low;
    int mid;
    int high;
    int i;
    Integer[] content;
    Integer[] buf;
    RankUnit(Integer[] content, Integer[] buf, int low, int mid, int high, int i){
        this.low = low;
        this.mid = mid;
        this.high = high;
        this.i = i;
        this.content = content;
        this.buf = buf;
    }
    public int binaryLarger(int low, int high, Integer x){
        int l = 1;
        int i = 0;
        if(content[low]>x){
            return 0;
        }
        if(content[high-1]<x){
            return (high-low);
        }
        while (true){
            l = l*2;
            if (l>(high-low))
                break;
        }
        l/=2;
        while(true){
            if(low+i+l<high && content[low+i+l]<x){
                i += l;
            }
            l = l/2;
            if(l==0)
                break;
        }
        return i+1;
    }
    @Override
    public void run() {
        if(i<mid){
            int bias = i - low;
//            for (int j = mid; j < high; j++) {
//                if(content[j]<content[i]){
//                    bias ++;
//                }else break;
//            }
            bias += binaryLarger(mid, high, content[i]);

            buf[bias+low] = content[i];
        }else{
            int bias = i - mid;
//            for (int j = low; j < mid; j++) {
//                if(content[j]<content[i]){
//                    bias ++;
//                }else break;
//            }
            bias += binaryLarger(low, mid, content[i]);
            buf[bias+low] = content[i];
        }
    }
}