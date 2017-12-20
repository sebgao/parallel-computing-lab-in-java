package org.jkg.pc;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
class FilterUnit implements Runnable{
    Integer content[];
    Integer base;
    int cond[];
    boolean larger;
    int i;
    FilterUnit(Integer content[], Integer base, int cond[], boolean larger, int i){
        this.content = content;
        this.base = base;
        this.cond = cond;
        this.larger = larger;
        this.i = i;
    }
    @Override
    public void run() {
        if ((content[i] <= base) ^ (larger))
            cond[i] = 1;
        else
            cond[i] = 0;
    }
}
class SumUnit implements Runnable{
    int sum[];
    int i;
    int stride;
    SumUnit(int sum[], int i, int stride){
        this.sum = sum;
        this.i = i;
        this.stride = stride;
    }
    @Override
    public void run() {
        sum[i] = sum[i] + sum[i-stride];
    }
}
class GotoUnit implements Runnable{
    int cond[];
    int indices[];
    Integer source[];
    Integer target[];
    int i;
    int base;
    GotoUnit(int[] cond, int[] indices, Integer[] source, Integer[] target, int base, int i){
        this.cond = cond;
        this.indices = indices;
        this.source = source;
        this.target = target;
        this.i = i;
        this.base = base;
    }
    @Override
    public void run() {
        if(cond[i]==1){
            target[base + indices[i] - 1] = source[i];
        }
    }
}
public class QuickSortParallel extends QuickSort {
    int indices[];
    int cond[];
    int sum[];
    Integer buffer[];
    Future[] futures;
    ExecutorService executorService;
    public void prefixSum(int[] sum, int low, int high, int stride){
        if (low + 1 >= high)
            return;
        if (stride >= high - low)
            return;


        for (int i = low+stride; i < high; i+=stride*2) {
            futures[i] = executorService.submit(new SumUnit(sum, i, stride));
        }

        //barrier
        for (int i = low+stride; i < high; i+=stride*2) {
            try {
                futures[i].get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        prefixSum(sum, low+stride, high, stride*2);

        for (int i = low+2*stride; i < high; i+=stride*2) {
            futures[i] = executorService.submit(new SumUnit(sum, i, stride));
        }

        //barrier
        for (int i = low+2*stride; i < high; i+=stride*2) {
            try {
                futures[i].get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }
    public void check(Integer content[], Integer base, int cond[], boolean larger, int low, int high){
        for (int i = low; i < high; i++) {
            futures[i] = executorService.submit(new FilterUnit(content, base, cond, larger, i));
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
    }
    public void transfer(int[] cond, int[] indices, Integer[] source, Integer[] target, int base, int low, int high){
        for (int i = low; i < high; i++) {
            futures[i] = executorService.submit(new GotoUnit(cond, indices, source, target, base, i));
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
    }
    public void swapBuffer(int i, int j){
        int temp = buffer[i];
        buffer[i] = buffer[j];
        buffer[j] = temp;
    }
    public int partitionParallel(int low, int high){
        Integer pivot = content[low];
        check(content, pivot, sum, false, low, high);
        System.arraycopy(sum, low, cond, low, high-low);
        prefixSum(sum, low, high, 1);
        int base = sum[high-1];

        transfer(cond, sum, content, buffer, low, low, high);

        check(content, pivot, sum, true, low, high);
        System.arraycopy(sum, low, cond, low, high-low);
        prefixSum(sum, low, high, 1);
        transfer(cond, sum, content, buffer, low + base, low, high);

        swapBuffer(low, low+base-1);

        System.arraycopy(buffer, low, content, low, high-low);

        return low + base - 1;
    }
    public void quickSortAndSpawn(int low, int high){
        if(low + 1 < high){
            int mid = partitionParallel(low, high);
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    quickSortAndSpawn(mid+1, high);
                }
            };
            Future future = executorService.submit(r);

            quickSortAndSpawn(low, mid);

            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            //System.arraycopy(buffer, low, content, low, high-low);
        }
    }
    @Override
    public void sort() {
        sum = new int[length];
        futures = new Future[length];
        cond = new int[length];
        buffer = new Integer[length];
        for (int i = 0; i < length; i++) {
            buffer[i] = -100;
        }
        indices = new int[length];
        executorService = Executors.newFixedThreadPool(Main.MAX_THREAD);

        long past = (new Date()).getTime();
        quickSortAndSpawn(0, length);
        long now = (new Date()).getTime();
        System.out.println("Parallel Quicksort: "+(now-past)+"ms");
        executorService.shutdown();

    }
}
