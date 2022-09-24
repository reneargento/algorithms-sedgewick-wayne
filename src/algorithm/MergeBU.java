package algorithm;

import static algorithm.Merge.merge;

/**
 * 算法2.4
 * 自底向上的归并排序
 */
public class MergeBU {
    private static Comparable[] aux;

    public static void sort(Comparable[] a) {
        int N = a.length;
        aux = new Comparable[N];
        for (int sz = 1; sz < N; sz = sz + sz)
            for (int lo = 0; lo < N - sz; lo += sz + sz)
                merge(a, lo, lo - sz - 1, Math.min(lo + sz + sz - 1, N - 1));
    }
}
