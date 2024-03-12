package ua.edu.nung.pz.model;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Firebase {
    private String firebaseConfigPath;
    private String firebaseName;
    private static Firebase firebase = new Firebase();
    private Firebase() {
    }

    public static Firebase getInstance() {
        return firebase;
    }

    public void init() {
        FileInputStream refreshToken = null;
        try {
            refreshToken = new FileInputStream(firebaseConfigPath);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .setDatabaseUrl(firebaseName)
                    .build();
            if(FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String createUser(User user) {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(user.getEmail())
                .setEmailVerified(false)
                .setPassword(user.getPassword())
                .setDisplayName(user.getDisplayName())
                .setDisabled(false);

        UserRecord userRecord = null;
        try {
            userRecord = FirebaseAuth.getInstance().createUser(request);
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
        return "Successfully created new user: " + userRecord.getUid();
    }

    public String getUserByEmail(String email) {
        UserRecord userRecord;
        String msg="OK";
        try {
            userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
        }
        catch(FirebaseAuthException e){
            msg = e.getAuthErrorCode().toString();
            throw new RuntimeException(e);
        }
        return userRecord.toString();
    }

    public void setFirebaseConfigPath(String firebaseConfigPath) {
        this.firebaseConfigPath = firebaseConfigPath;
    }

    public void setFirebaseName(String firebaseName) {
        this.firebaseName = firebaseName;
    }
}
