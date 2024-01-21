package com.hust.interviewmanagement.utils;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class GoogleApiUtil {
    private static final String APPLICATION_NAME = "IMS Recruitment";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES =
            Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static final String CREDENTIALS_FILE_PATH = "/static/credentials.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        InputStream in = GoogleApiUtil.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost("//ims-recruitment-8207c93bfae7.herokuapp.com").build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("cuden20101998@gmail.com");

    }

    public String uploadCv(MultipartFile cv) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(new NetHttpTransport(),
                JSON_FACTORY,
                getCredentials(HTTP_TRANSPORT))
                .setApplicationName("InterviewManagement")
                .build();
        File fileMetadata = new File();
        fileMetadata.setName(cv.getOriginalFilename());
        java.io.File newFile = convert(cv);
        fileMetadata.setParents(Collections.singletonList("1-1o2KnbKGTsCXVzzqRo1J7He3sLV24TW"));
        FileContent mediaContent = new FileContent(cv.getContentType(), newFile);
        try {
            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            return file.getId();
        } catch (IOException e) {
            throw e;
        }
    }



    public ByteArrayOutputStream downloadFile(String realFileId) throws IOException, GeneralSecurityException {

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(new NetHttpTransport(),
                JSON_FACTORY,
                getCredentials(HTTP_TRANSPORT))
                .setApplicationName("InterviewManagement")
                .build();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            service.files().get(realFileId)
                    .executeMediaAndDownloadTo(outputStream);
            return outputStream;
        } catch (GoogleJsonResponseException e) {
            throw e;
        }
    }

    public java.io.File convert(MultipartFile file) {
        if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".doc") ||
                Objects.requireNonNull(file.getOriginalFilename()).endsWith(".docx") ||
                Objects.requireNonNull(file.getOriginalFilename()).endsWith(".pdf")) {
            java.io.File convFile = new java.io.File(Objects.requireNonNull(file.getOriginalFilename()));
            try (FileOutputStream fos = new FileOutputStream(convFile);) {
                fos.write(file.getBytes());
                return convFile;
            } catch (IOException e) {
                throw new IllegalArgumentException("File cv not support!");
            }
        } else {
            throw new IllegalArgumentException("File cv not support!");
        }

    }
}
