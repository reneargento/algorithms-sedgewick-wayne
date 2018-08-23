package chapter6.btrees;

import chapter3.section1.BinarySearchSymbolTable;
import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;
import util.FileUtil;
import util.google.drive.GoogleDriveUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 06/08/18.
 */

// In order to run this exercise:
// 1- Create a project on Google Cloud Platform, enable the Drive API and download the configuration file.
// To do that, follow step 1 here:
// https://developers.google.com/drive/api/v3/quickstart/java

// 2- Move the downloaded file (credentials.json) of the previous step to the
// folder src/util/google/drive inside this project.

// 3- Import the following libraries to this project (they are in the libs/googledrive folder):
// com.fasterxml.jackson.core.jar
// google-api-client-1.24.1.jar
// google-api-services-drive-v3-rev124-1.24.1.jar
// google-http-client-1.24.1.jar
// google-http-client-jackson2-1.24.1.jar
// google-oauth-client-1.24.1.jar
// google-oauth-client-java6-1.24.1.jar
// google-oauth-client-jetty-1.24.1.jar
// javax.servlet-3.0.jar
// jetty-6.1.26.jar
// jetty-util-6.1.26.jar
public class Exercise19_WebSearch {

    public class WebPage {
        private BinarySearchSymbolTable<String, String> binarySearchSymbolTable;
        private boolean isOpen;
        private boolean isExternal;

        // Reference to web pages in memory on the system
        private SeparateChainingHashTable<String, WebPage> webPagesInMemory;
        private int maxNumberOfNodes;

        private String id;
        private String name;

        private static final String EXTERNAL_PARAMETER = "external";
        private static final String NAME_PARAMETER = "name";
        private static final String DIVIDER = ":";
        private static final int EXTERNAL_PARAMETER_LINE_NUMBER = 0;
        private static final int NAME_PARAMETER_LINE_NUMBER = 1;
        private static final int KEYS_PARAMETER_START_LINE_NUMBER = 2;

        WebPage(boolean isExternal, int maxNumberOfNodes, String name,
                SeparateChainingHashTable<String, WebPage> webPagesInMemory) {
            if (maxNumberOfNodes % 2 != 0 || maxNumberOfNodes == 2) {
                throw new IllegalArgumentException("Max number of nodes must be divisible by 2 and higher than 2");
            }

            binarySearchSymbolTable = new BinarySearchSymbolTable<>();
            this.webPagesInMemory = webPagesInMemory;
            this.maxNumberOfNodes = maxNumberOfNodes;
            this.name = name;
            this.isExternal = isExternal;
        }

        // Update cache
        public void open() {
            webPagesInMemory.put(id, this);
            isOpen = true;
        }

        public void close(boolean verbose) {
            if (!isOpen) {
                return;
            }

            StringJoiner webPageContent = new StringJoiner("\n");

            for (String key : keys()) {
                String value = binarySearchSymbolTable.get(key);
                webPageContent.add(key + DIVIDER + value);
            }

            if (verbose) {
                StdOut.println("Closing web page " + name + ":\n" + webPageContent.toString());
                StdOut.println();
            }

            webPagesInMemory.delete(id);
            isOpen = false;
        }

        public String getAssociatedWebPageId(String key) {
            return binarySearchSymbolTable.get(key);
        }

        public String getId() {
            return id;
        }

        public void add(String key, String value) {
            if (!isExternal()) {
                throw new IllegalArgumentException("Cannot add key directly to an internal web page.");
            }

            binarySearchSymbolTable.put(key, value);
            getDataAndUploadWebPage();
        }

        public void add(WebPage webPage, boolean uploadToServer) {
            String minKey = webPage.binarySearchSymbolTable.min();
            binarySearchSymbolTable.put(minKey, webPage.getId());

            if (uploadToServer) {
                getDataAndUploadWebPage();
            }
        }

        // Adds a list of key-value pairs to this web page.
        // Used to improve performance and perform only one network request when splitting a web page.
        private void batchAdd(List<String> keys, List<String> values) {
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                String value = values.get(i);

                binarySearchSymbolTable.put(key, value);
            }

            getDataAndUploadWebPage();
        }

        public void batchDelete(List<String> keys) {
            for (String key : keys) {
                binarySearchSymbolTable.delete(key);
            }

            getDataAndUploadWebPage();
        }

        private void getDataAndUploadWebPage() {
            List<String> webPageData = getWebPageData();

            if (id == null) {
                id = GoogleDriveUtil.createWebPage(name, webPageData);

                if (id == null) {
                    throw new RuntimeException("Error during web page creation.");
                }
            } else {
                GoogleDriveUtil.updateWebPage(id, webPageData);
            }

            open();
        }

        // Get data in the format:
        // Line 1: external:true/false
        // Line 2: name:BTreeNode0000
        // Line 3 to N: key:value
        private List<String> getWebPageData() {
            List<String> webPageData = new ArrayList<>();

            webPageData.add(EXTERNAL_PARAMETER + DIVIDER + isExternal);
            webPageData.add(NAME_PARAMETER + DIVIDER + name);

            for (String key : binarySearchSymbolTable.keys()) {
                String value = binarySearchSymbolTable.get(key);
                webPageData.add(key + DIVIDER + value);
            }

            return webPageData;
        }

        public boolean isExternal() {
            return isExternal;
        }

        public boolean contains(String key) {
            if (!isExternal()) {
                throw new IllegalArgumentException("Cannot check contains directly on an internal web page.");
            }

            return binarySearchSymbolTable.contains(key);
        }

        public String next(String key) {
            if (isExternal()) {
                throw new IllegalArgumentException("Next cannot be called on an external web page.");
            }

            String nextKey = binarySearchSymbolTable.floor(key);

            if (nextKey == null) {
                return null;
            }

            return binarySearchSymbolTable.get(nextKey);
        }

        public boolean isFull() {
            return binarySearchSymbolTable.size() == maxNumberOfNodes;
        }

        public WebPage split(BTreeWeb bTreeWeb) {
            List<String> keysToMove = new ArrayList<>();
            int middleRank = binarySearchSymbolTable.size() / 2;

            for (int index = middleRank; index < binarySearchSymbolTable.size(); index++) {
                String keyToMove = binarySearchSymbolTable.select(index);
                keysToMove.add(keyToMove);
            }

            String newWebPageName;
            if (isExternal()) {
                newWebPageName = bTreeWeb.getNextExternalWebPageName();
            } else {
                newWebPageName = bTreeWeb.getNextInternalWebPageName();
            }

            WebPage newWebPage = new WebPage(isExternal, maxNumberOfNodes, newWebPageName, webPagesInMemory);
            List<String> valuesToMove = new ArrayList<>();

            for (String key : keysToMove) {
                String valueToMove = binarySearchSymbolTable.get(key);
                valuesToMove.add(valueToMove);
            }
            newWebPage.batchAdd(keysToMove, valuesToMove);
            batchDelete(keysToMove);

            return newWebPage;
        }

        public Iterable<String> keys() {
            return binarySearchSymbolTable.keys();
        }
    }

    public class BTreeWeb {

        private WebPage root;
        private static final int DEFAULT_MAX_NUMBER_OF_NODES_PER_PAGE = 4;
        // For web pages the default verbose is true
        private static final boolean DEFAULT_VERBOSE = true;
        private static final String SENTINEL_VALUE = "Sentinel (no content)";

        private int maxNumberOfNodesPerPage;
        private SeparateChainingHashTable<String, WebPage> webPagesInMemory;
        private boolean verbose;
        private boolean useCache;

        private static final String INTERNAL_WEB_PAGE_NAME_PREFIX = "BTreeNode";
        private static final String EXTERNAL_WEB_PAGE_NAME_PREFIX = "WebPage";
        private int nextInternalWebPageNumber;
        private int nextExternalWebPageNumber;
        private int upperLimitNumberOfInternalPages;
        private int numberOfDigitsInInternalWebPagesName;

        public BTreeWeb(String sentinel, int upperLimitNumberOfInternalPagesExponential, boolean useCache) {
            this(sentinel, upperLimitNumberOfInternalPagesExponential, useCache,
                    DEFAULT_MAX_NUMBER_OF_NODES_PER_PAGE, DEFAULT_VERBOSE);
        }

        public BTreeWeb(String sentinel, int upperLimitNumberOfInternalPagesExponential, boolean useCache,
                int maxNumberOfNodesPerPage, boolean verbose) {
            if (maxNumberOfNodesPerPage % 2 != 0 || maxNumberOfNodesPerPage == 2) {
                throw new IllegalArgumentException("Max number of nodes must be divisible by 2 and higher than 2");
            }

            webPagesInMemory = new SeparateChainingHashTable<>();
            this.useCache = useCache;

            numberOfDigitsInInternalWebPagesName = upperLimitNumberOfInternalPagesExponential;
            upperLimitNumberOfInternalPages = (int) Math.pow(10, upperLimitNumberOfInternalPagesExponential);

            GoogleDriveUtil.initializeGoogleDriveService();

            String rootName = getNextExternalWebPageName();
            root = new WebPage(true, maxNumberOfNodesPerPage, rootName, webPagesInMemory);

            this.verbose = verbose;
            this.maxNumberOfNodesPerPage = maxNumberOfNodesPerPage;

            add(sentinel, SENTINEL_VALUE);
        }

        public boolean contains(String key) {
            return contains(root, key);
        }

        private boolean contains(WebPage webPage, String key) {
            if (webPage.isExternal()) {
                return webPage.contains(key);
            }

            String nextWebPageId = webPage.next(key);
            WebPage nextWebPage = getWebPage(nextWebPageId);

            if (nextWebPage == null) {
                return false;
            }

            return contains(nextWebPage, key);
        }

        public String get(String key) {
            return get(root, key);
        }

        private String get(WebPage webPage, String key) {
            if (webPage.isExternal()) {
                // The web page ids in external pages are the person's web page searched
                return webPage.getAssociatedWebPageId(key);
            }

            String nextWebPageId = webPage.next(key);
            WebPage nextWebPage = getWebPage(nextWebPageId);

            if (nextWebPage == null) {
                return null;
            }

            return get(nextWebPage, key);
        }

        // Get web page with the specified id.
        // First check if it is in memory. If it is, return it.
        // Otherwise, get the web page from the server, save it in memory and return it.
        private WebPage getWebPage(String webPageId) {
            if (useCache && webPagesInMemory.contains(webPageId)) {
                return webPagesInMemory.get(webPageId);
            }

            WebPage webPage = getWebPageFromServer(webPageId);
            webPage.open();
            return webPage;
        }

        private WebPage getWebPageFromServer(String webPageId) {
            List<String> webPageData = GoogleDriveUtil.getWebPage(webPageId);

            if (webPageData == null) {
                throw new RuntimeException("There was an error when trying to get the web page.");
            }

            String externalParameterLine = webPageData.get(WebPage.EXTERNAL_PARAMETER_LINE_NUMBER);
            int externalParameterDividerIndex = externalParameterLine.indexOf(WebPage.DIVIDER);
            boolean isExternalPage =
                    Boolean.parseBoolean(externalParameterLine.substring(externalParameterDividerIndex + 1));

            String nameParameterLine = webPageData.get(WebPage.NAME_PARAMETER_LINE_NUMBER);
            int nameParameterDividerIndex = nameParameterLine.indexOf(WebPage.DIVIDER);
            String webPageName = nameParameterLine.substring(nameParameterDividerIndex + 1);

            WebPage webPage = new WebPage(isExternalPage, maxNumberOfNodesPerPage, webPageName, webPagesInMemory);

            for (int dataLineNumber = WebPage.KEYS_PARAMETER_START_LINE_NUMBER; dataLineNumber < webPageData.size();
                 dataLineNumber++) {
                String keyValueInformation = webPageData.get(dataLineNumber);

                int keyValueDividerIndex = keyValueInformation.indexOf(WebPage.DIVIDER);
                String key = keyValueInformation.substring(0, keyValueDividerIndex);
                String value = keyValueInformation.substring(keyValueDividerIndex + 1);
                webPage.binarySearchSymbolTable.put(key, value);
            }

            webPage.id = webPageId;
            return webPage;
        }

        public void add(String key, String value) {
            add(root, key, value);

            if (root.isFull()) {
                WebPage leftHalf = root;
                WebPage rightHalf = root.split(this);

                root = new WebPage(false, maxNumberOfNodesPerPage, getNextInternalWebPageName(), webPagesInMemory);
                // Only upload changes to server after adding both pages, doing 1 network request instead of 2
                root.add(leftHalf, false);
                root.add(rightHalf, true);
            }
        }

        public void add(WebPage webPage, String key, String value) {
            if (webPage.isExternal()) {
                webPage.add(key, value);
                return;
            }

            String nextWebPageId = webPage.next(key);
            WebPage nextWebPage = getWebPage(nextWebPageId);

            if (nextWebPage == null) {
                throw new IllegalStateException("Internal web page cannot be null during add.");
            }

            add(nextWebPage, key, value);

            if (nextWebPage.isFull()) {
                WebPage newWebPage = nextWebPage.split(this);
                webPage.add(newWebPage, true);
            }
            nextWebPage.close(verbose);
        }

        private String getNextInternalWebPageName() {
            if (nextInternalWebPageNumber == upperLimitNumberOfInternalPages) {
                throw new RuntimeException("Max number of internal nodes reached. Contact your system administrator " +
                        "to increase this number.");
            }

            String nextInternalWebPageNameDigits = getNextInternalWebPageNameDigits();

            nextInternalWebPageNumber++;
            return INTERNAL_WEB_PAGE_NAME_PREFIX + nextInternalWebPageNameDigits;
        }

        private String getNextInternalWebPageNameDigits() {
            StringBuilder nextWebPageNameDigits = new StringBuilder(String.valueOf(nextInternalWebPageNumber));
            int numberOfDigitsInName = nextWebPageNameDigits.length();

            while (numberOfDigitsInName < numberOfDigitsInInternalWebPagesName) {
                nextWebPageNameDigits.insert(0, "0");
                numberOfDigitsInName++;
            }

            return nextWebPageNameDigits.toString();
        }

        private String getNextExternalWebPageName() {
            String nextExternalWebPageName = EXTERNAL_WEB_PAGE_NAME_PREFIX + nextExternalWebPageNumber;
            nextExternalWebPageNumber++;
            return nextExternalWebPageName;
        }

        public void clearCache() {
            List<String> keysToDelete = new ArrayList<>();

            for (String key : webPagesInMemory.keys()) {
                keysToDelete.add(key);
            }

            for (String key : keysToDelete) {
                webPagesInMemory.delete(key);
            }
        }
    }

    // Parameters example: 4 true
    public static void main(String[] args) {
        Exercise19_WebSearch exercise19 = new Exercise19_WebSearch();

        int upperLimitNumberOfInternalPagesExponential = Integer.parseInt(args[0]);
        boolean useCache = Boolean.parseBoolean(args[1]);

        BTreeWeb bTreeWeb = exercise19.new BTreeWeb("*", upperLimitNumberOfInternalPagesExponential, useCache);

// Input example (copy & paste friendly):
/**
Argento
Rene Argento
Sedgewick
Robert Sedgewick
Wayne
Kevin Wayne
Gates
Bill Gates
Zuckerberg
Mark Zuckerberg
Musk
Elon Musk
Page
Larry Page
Brin
Sergey Brin
Pichai
Sundar Pichai
*/
        while (!StdIn.isEmpty()) {
            String webPageKey = StdIn.readLine();
            String webPageValue = StdIn.readLine();

            bTreeWeb.add(webPageKey, webPageValue);
        }

        // Looking for myself and friends on my university's (represented here by Google Drive) website
        String searchTermsFilePath = Constants.FILES_PATH + Constants.BTREE_SEARCH_TERMS_FILE;
        List<String> searchTerms = FileUtil.getAllLinesFromFile(searchTermsFilePath);

        if (searchTerms != null) {
            for (String key : searchTerms) {
                StdOut.println("Get " + key + ": " + bTreeWeb.get(key));
            }
        }

        bTreeWeb.clearCache();

      //  exercise19.tests(upperLimitNumberOfInternalPagesExponential, useCache);
    }

    private void tests(int upperLimitNumberOfInternalPagesExponential, boolean useCache) {
        BTreeWeb bTreeWeb = new BTreeWeb("*", upperLimitNumberOfInternalPagesExponential, useCache);

        bTreeWeb.add("Argento", "Rene Argento");
        bTreeWeb.add("Sedgewick", "Robert Sedgewick");
        bTreeWeb.add("Wayne", "Kevin Wayne");
        bTreeWeb.add("Gates", "Bill Gates");
        bTreeWeb.add("Zuckerberg", "Mark Zuckerberg");
        bTreeWeb.add("Musk", "Elon Musk");
        bTreeWeb.add("Page", "Larry Page");
        bTreeWeb.add("Brin", "Sergey Brin");
        bTreeWeb.add("Pichai", "Sundar Pichai");

        StdOut.println("*** Contains tests ***");

        StdOut.println("Contains Musk: " + bTreeWeb.contains("Musk") + " Expected: true");
        StdOut.println("Contains Zuckerberg: " + bTreeWeb.contains("Zuckerberg") + " Expected: true");
        StdOut.println("Contains Gates: " + bTreeWeb.contains("Gates") + " Expected: true");
        StdOut.println("Contains Argento: " + bTreeWeb.contains("Argento") + " Expected: true");
        StdOut.println("Contains Zuckerburg: " + bTreeWeb.contains("Zuckerburg") + " Expected: false");
        StdOut.println("Contains Argent: " + bTreeWeb.contains("Argent") + " Expected: false");
        StdOut.println("Contains Pages: " + bTreeWeb.contains("Pages") + " Expected: false");

        StdOut.println("\n*** Get tests ***");

        StdOut.println("Get Argento: " + bTreeWeb.get("Argento") + " Expected: Rene Argento");
        StdOut.println("Get Musk: " + bTreeWeb.get("Musk") + " Expected: Elon Musk");
        StdOut.println("Get Gates: " + bTreeWeb.get("Gates") + " Expected: Bill Gates");
        StdOut.println("Get Pichai: " + bTreeWeb.get("Pichai") + " Expected: Sundar Pichai");
        StdOut.println("Get Gatess: " + bTreeWeb.get("Gatess") + " Expected: null");

        StdOut.println("\n*** Test add and cache invalidation / update ***");

        bTreeWeb.add("Cook", "Tim Cook");
        StdOut.println("Cook: " + bTreeWeb.get("Cook") + " Expected: Tim Cook");

        StdOut.println("Get Argento: " + bTreeWeb.get("Argento") + " Expected: Rene Argento\n");
        bTreeWeb.add("Argento", "Rene Argento updated");
        StdOut.println("Get Argento: " + bTreeWeb.get("Argento") + " Expected: Rene Argento updated");

        bTreeWeb.clearCache();
    }

}
