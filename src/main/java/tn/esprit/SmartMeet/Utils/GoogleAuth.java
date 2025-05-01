package tn.esprit.SmartMeet.Utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleAuth {

    private static final String CLIENT_ID = "164866495803-ng9o1lvtar39kmvgd1k57l3b4lfltile.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:8888/Callback";
    //l’URL vers laquelle Google redirige l'utilisateur une fois connecté (gérée ici localement sur le port 8888).
    private static final String SCOPE = "https://www.googleapis.com/auth/calendar.events";
   // permissions demandées (pour enregistrer les events dans le calendrier )
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        // Créer un flow OAuth avec les informations d'identification et l'URL de redirection
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                CLIENT_ID,
                CLIENT_SECRET,
                java.util.Collections.singleton(SCOPE))
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .build();

        // Utiliser LocalServerReceiver pour capturer l'URL de redirection et obtenir le code
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8889).build();

        // Demander l'autorisation à l'utilisateur et récupérer le Credential
        AuthorizationCodeInstalledApp app = new AuthorizationCodeInstalledApp(flow, receiver);
        Credential credential = app.authorize("user");

        // Si l'autorisation a réussi, afficher un message
        if (credential != null) {
            System.out.println("L'authentification a réussi ! Vous pouvez maintenant utiliser l'API Google Calendar.");
            //Si l’autorisation est acceptée, un objet Credential est retourné et peut être utilisé pour accéder à l’API.
            //et genere un token
        } else {
            System.out.println("L'authentification a échoué.");
        }
    }
}
