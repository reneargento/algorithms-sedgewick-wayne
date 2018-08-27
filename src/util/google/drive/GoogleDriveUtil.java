package util.google.drive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import edu.princeton.cs.algs4.StdOut;
import util.FileUtil;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rene Argento on 08/08/18.
 */

// To compile this class import all libraries in libs/googledrive to the project
public class GoogleDriveUtil {
    private static final String APPLICATION_NAME = "B-tree web pages";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_NAME = "tokens";
    // To be able to add the empty folder staging/area/ to a git repository, a .gitkeep file is used.
    // This is where files are assembled before being sent to the server.
    private static final String STAGING_AREA_DIRECTORY_PATH = "./src/util/google/drive/staging/area/";
    private static final String TEXT_PLAIN_TYPE = "text/plain";
    private static final String ACCESS_TYPE_OFFLINE = "offline";
    private static final String USER_ID = "user";

    private static Drive driveService;

    /**
     * Global instance of the scopes required.
     * If modifying these scopes, delete the StoredCredential file in the /tokens directory.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_NAME = "credentials.json";

    public static void initializeGoogleDriveService() {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredential(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (Exception exception) {
            throw new RuntimeException("Error during Google Drive service initialization");
        }
    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredential(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream inputStream = GoogleDriveUtil.class.getResourceAsStream(CREDENTIALS_FILE_NAME);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_NAME)))
                .setAccessType(ACCESS_TYPE_OFFLINE)
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(USER_ID);
    }

    // Based on https://developers.google.com/drive/api/v3/manage-downloads
    public static List<String> getWebPage(String webPageId) {
        OutputStream outputStream = new ByteArrayOutputStream();

        try {
            driveService.files().get(webPageId)
                    .executeMediaAndDownloadTo(outputStream);
        } catch (IOException exception) {
            StdOut.println("There was an error when trying to get the web page.");
            return null;
        }

        return getWebPageData(outputStream.toString());
    }

    private static List<String> getWebPageData(String outputStream) {
        String[] linesData = outputStream.split("\n");
        return Arrays.asList(linesData);
    }

    // Based on https://developers.google.com/drive/api/v3/manage-uploads
    public static String createWebPage(String webPageName, List<String> data) {
        File fileMetadata = new File();
        fileMetadata.setName(webPageName);

        // Create file in staging area
        String filePath = STAGING_AREA_DIRECTORY_PATH + webPageName;
        FileUtil.writeFile(filePath, data);

        java.io.File fileInStagingArea = new java.io.File(filePath);
        FileContent fileContent = new FileContent(TEXT_PLAIN_TYPE, fileInStagingArea);

        File file;
        try {
            file = driveService.files()
                    .create(fileMetadata, fileContent)
                    .setFields("id")
                    .execute();
        } catch (IOException exception) {
            StdOut.println("There was an error when trying to create the new web page.");
            return null;
        }

        // Delete file from staging area
        FileUtil.deleteFile(filePath);

        return file.getId();
    }

    // Based on https://developers.google.com/drive/api/v3/reference/files/update
    // And https://stackoverflow.com/questions/35143283/google-drive-api-v3-migration
    public static void updateWebPage(String webPageId, List<String> data) {
        try {
            Drive.Files driveFiles = driveService.files();

            File currentFile = driveFiles.get(webPageId).execute();
            String fileName = currentFile.getName();

            // Create file in staging area
            String filePath = STAGING_AREA_DIRECTORY_PATH + fileName;
            FileUtil.writeFile(filePath, data);

            java.io.File fileInStagingArea = new java.io.File(STAGING_AREA_DIRECTORY_PATH + fileName);
            FileContent fileContent = new FileContent(TEXT_PLAIN_TYPE, fileInStagingArea);

            driveFiles.update(webPageId, new File(), fileContent).execute();

            // Delete file from staging area
            FileUtil.deleteFile(filePath);
        } catch (IOException exception) {
            StdOut.println("There was an error when trying to update the web page with id " + webPageId + ".");
        }
    }
}
