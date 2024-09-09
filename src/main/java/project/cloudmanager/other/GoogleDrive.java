package project.cloudmanager.other;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleDrive {
    /**
     * Application name
     */
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    /**
     * Global instance of the JSON factory
     */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart
     * If modifying these scopes, delete your previously saved tokens/ folder
     */
    private static final List<String> SCOPES =
            Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/client_secret.json";

    /**
     * Creates an authorized Credential object
     *
     * @param HTTP_TRANSPORT The network HTTP Transport
     * @return An authorized Credential object
     * @throws IOException If the credentials.json file cannot be found
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = GoogleDrive.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object. b
        return credential;
    }

    private static Drive getService() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT,
                JSON_FACTORY,
                getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        return service;
    }

//    /**
//     * Download a Workspace Document file in a specified format.
//     *
//     * @param realFileId file ID of any workspace document format file
//     * @param type type of downloaded file
//     * @param path location of download
//     * @throws IOException if service account credentials file not found
//     */
//    public static void downloadWorkspaceDoc(String realFileId, String type, String path) throws IOException {
//        // Build a new authorized API client service.
//        Drive service = null;
//        try {
//            service = getService();
//        } catch (GeneralSecurityException e) {
//            throw new RuntimeException(e);
//        }
//
//        OutputStream outputStream = new ByteArrayOutputStream();
//        try {
//            service.files().export(realFileId, type)
//                    .executeMediaAndDownloadTo(outputStream);
//        } catch (GoogleJsonResponseException e) {
//            // TODO(developer) - handle error appropriately
//            System.err.println("Unable to export file: " + e.getDetails());
//            throw e;
//        }
//
//        ByteArrayOutputStream byteArrayStream = (ByteArrayOutputStream) outputStream;
//        try (OutputStream fileStream = new FileOutputStream(path)) {
//            byteArrayStream.writeTo(fileStream);
//        } catch (IOException e) {
//            // TODO(developer) - handle error appropriately
//            e.printStackTrace();
//        }
//    }

//    /**
//     * Download a Blob file.
//     *
//     * @param realFileId file ID of any workspace document format file.
//     * @param path location of download
//     * @throws IOException if service account credentials file not found.s
//     * @throws GeneralSecurityException ?
//     */
//    public static void downloadBlobFile(String realFileId, String path) throws IOException, GeneralSecurityException {
//        // Build a new authorized API client service.
//        Drive service = getService();
//
//        OutputStream outputStream = new ByteArrayOutputStream();
//        try {
//            service.files().get(realFileId)
//                    .executeMediaAndDownloadTo(outputStream);
//        } catch (GoogleJsonResponseException e) {
//            // TODO(developer) - handle error appropriately
//            System.err.println("Unable to export file: " + e.getDetails());
//            throw e;
//        }
//
//        ByteArrayOutputStream byteArrayStream = (ByteArrayOutputStream) outputStream;
//        try (OutputStream fileStream = new FileOutputStream(path)) {
//            byteArrayStream.writeTo(fileStream);
//        } catch (IOException e) {
//            // TODO(developer) - handle error appropriately
//            e.printStackTrace();
//        }
//    }

    public static List<File> getImgFileList(int numberOfFiles) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        Drive service = getService();

        // Get the name, id, thumbnail and mimeType for up to n image files.
        FileList result;
        if (numberOfFiles > 0) {
            result = service.files().list()
                    .setQ("mimeType='image/jpeg' or mimeType='image/png' or mimeType='image/gif' or mimeType='image/tiff'")
                    .setPageSize(numberOfFiles)
                    .setFields("nextPageToken, files(id, name, thumbnailLink, mimeType)")
                    .execute();
        } else {
            result = service.files().list()
                    .setQ("mimeType='image/jpeg' or mimeType='image/png' or mimeType='image/gif' or mimeType='image/tiff'")
                    .setFields("nextPageToken, files(id, name, thumbnailLink, mimeType)")
                    .execute();
        }

        return result.getFiles();
    }

    public static void main(String... args) {
        List<File> files = null;
        try {
            files = getImgFileList(-1);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        String path = null;
        String originalPath = System.getProperty("user.dir");

        // Print list of Images
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.println(files.indexOf(file) + ") " + file.getName());
            }
        }

//        // Download chosen Images
//        for (File file : files) {
//            path = originalPath + "/downloaded/" + file.getName();
//            System.out.println(path);
//            downloadBlobFile(file.getId(), path);
//        }
    }
}
