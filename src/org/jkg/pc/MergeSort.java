package org.jkg.pc;

import java.util.Date;

public class MergeSort extends Sort {
    protected Integer[] buffer;

    protected void mergeWithBuffer(int low, int mid, int high){
        int left = low;
        int right = mid;
        int to = low;
        while(left < mid && right < high){
            Integer l = content[left];
            Integer r = content[right];
            Integer selected;
            if(l<r) {
                selected = l;
                left ++;
            }else {
                selected = r;
                right ++;
            }
            buffer[to] = selected;
            to ++;
        }
        for (; left < mid; left++) {
            buffer[to] = content[left];
            to ++;
        }
        for (; right < high; right++) {
            buffer[to] = content[right];
            to ++;
        }
        for (int i = low; i < high; i++) {
            content[i] = buffer[i];
        }
    }
    protected void mergeSort(int low, int high){
        if(low + 1 < high) {
            int mid = (low + high) / 2;
            mergeSort(low, mid);
            mergeSort(mid, high);
            mergeWithBuffer(low, mid, high);
        }
    }
    public void sort(){
        long past = (new Date()).getTime();
        buffer = new Integer[length];
        mergeSort(0, length);
        long now = (new Date()).getTime();
        System.out.println("Sequential Mergesort: "+(now-past)+"ms");

    }
}
