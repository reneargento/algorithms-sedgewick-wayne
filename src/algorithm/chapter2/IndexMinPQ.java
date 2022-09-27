package algorithm.chapter2;

/**
 * 算法2.6续
 * 索引优先队列的实现
 */
public class IndexMinPQ<Key extends Comparable<Key>> {
    private int N; // PQ中的元素数量
    private int[] pq; // 索引二叉堆,由1开始
    private int[] qp; // 逆序:qp[pq[i]] = pq[qp[i]] = i
    private Key[] keys; // 由优先级之分的元素

    public IndexMinPQ(int maxN) {
        keys = (Key[]) new Comparable[maxN + 1];
        pq = new int[maxN + 1];
        qp = new int[maxN + 1];
        for (int i = 0; i <= maxN; i++) qp[i] = -1;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public boolean contains(int k) {
        return qp[k] != -1;
    }

    public void insert(int k, Key key) {
        N++;
        qp[k] = N;
        pq[N] = k;
        keys[k] = key;
        swim(N);
    }

    public Key min() {
        return keys[pq[1]];
    }

    public int delMin() {
        int indexOfMin = pq[1];
        exch(1, N--);
        sink(1);
        keys[pq[N + 1]] = null;
        qp[pq[N + 1]] = -1;
        return indexOfMin;
    }

    private void swim(int k) {
        while (k > 1 && less(k / 2, k)) {
            exch(k / 2, k);
            k = k / 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= N) {
            int j = 2 * k;
            if (j < N && less(j, j + 1)) j++;
            if (!less(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    private boolean less(int keyIndex1, int keyIndex2) {
        return keys[pq[keyIndex1]].compareTo(keys[pq[keyIndex2]]) < 0;
    }

    private void exch(int keyIndex1, int keyIndex2) {
        int temp = pq[keyIndex1];
        pq[keyIndex1] = pq[keyIndex2];
        pq[keyIndex2] = temp;

        qp[pq[keyIndex1]] = keyIndex1;
        qp[pq[keyIndex2]] = keyIndex2;
    }
}
