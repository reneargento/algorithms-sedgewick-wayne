package chapter2.section5;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 10/04/17.
 */
public class Exercise14_SortByReverseDomain {

    private class Domain implements Comparable<Domain>{

        String domainName;
        String reverseDomainName;

        Domain(String domainName) {
            this.domainName = domainName;

            String[] reverseDomain = domainName.split("\\.");
            StringBuilder reverseDomainConcatenation = new StringBuilder();
            for(int i = reverseDomain.length - 1; i >= 0; i--) {
                reverseDomainConcatenation.append(reverseDomain[i]);

                if (i != 0) {
                    reverseDomainConcatenation.append(".");
                }
            }
            this.reverseDomainName = reverseDomainConcatenation.toString();
        }

        @Override
        public int compareTo(Domain that) {
            return this.reverseDomainName.compareTo(that.reverseDomainName);
        }
    }

    public static void main(String[] args) {
        Exercise14_SortByReverseDomain sortByReverseDomain = new Exercise14_SortByReverseDomain();

        /**
         * Test case
         *
         * cs.princeton.edu
         * www.google.com
         * www.rene.argento
         * www.somewebsite.gov
         * cs.harvard.edu
         */
        String[] domainNames = StdIn.readAllLines();
        Domain[] domains = new Domain[domainNames.length];

        for(int i = 0; i < domainNames.length; i++) {
            Domain domain = sortByReverseDomain.new Domain(domainNames[i]);
            domains[i] = domain;
        }

        Arrays.sort(domains);

        for(Domain domain : domains) {
            StdOut.println(domain.reverseDomainName);
        }

        StdOut.println();
        StdOut.println("Expected:");
        StdOut.println("argento.rene.www\n" +
                "com.google.www\n" +
                "edu.harvard.cs\n" +
                "edu.princeton.cs\n" +
                "gov.somewebsite.www");
    }

}
