package chapter4.section2;

import chapter1.section3.Queue;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rene Argento on 31/10/17.
 */
// Based on https://algs4.cs.princeton.edu/42digraph/WebCrawler.java.html
public class Exercise44_WebCrawler {

    public void crawlWeb(String sourceWebPage) {

        // timeout connection after 500 milliseconds
        System.setProperty("sun.net.client.defaultConnectTimeout", "500");
        System.setProperty("sun.net.client.defaultReadTimeout",    "1000");

        Queue<String> webPagesQueue = new Queue<>();
        webPagesQueue.enqueue(sourceWebPage);

        HashSet<String> visited = new HashSet<>();
        visited.add(sourceWebPage);

        while (!webPagesQueue.isEmpty()) {
            String currentWebPage = webPagesQueue.dequeue();

            String webPageContent;
            try {
                In in = new In(currentWebPage);
                webPageContent = in.readAll();
            } catch (IllegalArgumentException exception) {
                StdOut.println("Could not open " + currentWebPage);
                continue;
            }

            StdOut.println(currentWebPage + " crawled");

            /*************************************************************
             *  Find links of the form: http://xxx.yyy.zzz or https://xxx.yyy.zzz
             *  s? for either http or https
             *  \\w+ for one or more alpha-numeric characters
             *  \\. for dot
             *************************************************************/
            String regexp = "http(s?)://(\\w+\\.)+(\\w+)";
            Pattern pattern = Pattern.compile(regexp);

            Matcher matcher = pattern.matcher(webPageContent);

            // Find all matches
            while (matcher.find()) {
                String webPage = matcher.group();

                if (!visited.contains(webPage)) {
                    webPagesQueue.enqueue(webPage);
                    visited.add(webPage);
                }
            }
        }
    }

    public static void main(String[] args) {
        String sourceWebPage = "https://google.com";
        new Exercise44_WebCrawler().crawlWeb(sourceWebPage);
    }

}
