package org.jkg.pc;

public class RankSortParallelRunnable implements Runnable {
    RankSortParallel rs;
    int low;
    int high;
    RankSortParallelRunnable(RankSortParallel rs, int low, int high){
        this.rs = rs;
        this.low = low;
        this.high = high;
    }
    @Override
    public void run() {
        int[] indices = new int[high-low];
        for (int i = 0; i < high-low; i++) {
            indices[i] = rs.rank(rs.content[i+low]);
        }
        rs.writeTarget(indices, low, high);
    }
}
