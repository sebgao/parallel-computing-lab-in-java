package org.jkg.pc;

import java.util.Date;

public class QuickSort extends Sort{
    protected int partition(int low, int high){
        Integer pivot = content[low];
        int i = low;
        int j = low+1;
        for (;j<high; j++){
            if(content[j] <= pivot){
                i = i+1;
                swap(i, j);
            }
        }
        swap(i, low);
        return i;
    }
    protected void quickSort(int low, int high){
        if(low + 1 < high){
            int mid = partition(low, high);
            quickSort(low, mid);
            quickSort(mid+1, high);
        }
    }
    public void sort(){
        long past = (new Date()).getTime();
        quickSort(0, length);
        long now = (new Date()).getTime();
        System.out.println("Sequential Quicksort: "+(now-past)+"ms");
    }
}
