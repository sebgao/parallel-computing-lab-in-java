package org.jkg.pc;

import java.util.Date;

public class RankSort extends Sort {
    public int rank(Integer x){
        int r = 0;
        for (Integer i:
             content) {
            if(i < x) {
                r ++;
            }
        }
        return r;
    }
    public void rankSort(){
        Integer[] target = new Integer[length];
        for (Integer i:
             content) {
            int r = rank(i);
            target[r] = i;
        }
        content = target;
    }
    public void sort(){
        long past = (new Date()).getTime();
        rankSort();
        long now = (new Date()).getTime();
        System.out.println("Sequential Ranksort: "+(now-past)+"ms");
    }
}
