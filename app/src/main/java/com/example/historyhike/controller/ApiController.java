package com.example.historyhike.controller;

import android.os.Handler;
import android.os.Looper;

import com.example.historyhike.model.Artefact;
import com.example.historyhike.model.Objective;
import com.example.historyhike.model.Quest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ApiController {
    private final String BASE_URL = "https://history-hike.alexs-apis.xyz/";

    public interface LoginCallback {
        void onSuccess(String jwt);
        void onFailure(String errorMessage);
    }

    public void login(String email, String password, LoginCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "auth/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String inputJson = "{\"email\":\"" + email + "\",\"passwordHash\":\"" + password + "\"}";

                OutputStream os = conn.getOutputStream();
                os.write(inputJson.getBytes());
                os.flush();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Notify success on the main thread
                    new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(response.toString()));
                } else {
                    // Notify failure on the main thread
                    new Handler(Looper.getMainLooper()).post(() -> callback.onFailure("Login failed"));
                }
            } catch (Exception e) {
                // Notify failure on the main thread
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure("Login failed: " + e.getMessage()));
            }
        }).start();
    }


    public ArrayList<Quest> fetchTestQuests() {
        ArrayList<Quest> quests = new ArrayList<>();

        // Creating test quests
        Objective obj1 = new Objective(1, 55.6057, -4.4968, "Home", "desc");
        Objective obj2 = new Objective(1, 55.6041, -4.4963, "Mc'D's", "desc");
        obj1.setImageURL("https://historyhike.alex-mccaughran.net/objective1.png");
        obj2.setImageURL("https://historyhike.alex-mccaughran.net/objective2.png");

        ArrayList<Objective> path = new ArrayList<>();
        path.add(obj1);
        // path.add(obj2);

        Quest quest = new Quest();
        quest.setTitle("Test me");
        quest.setDescription("From home to McDonald's. A Quest well completed.");
        quest.setQuestPath(path);

        Artefact artefact = new Artefact(1, "Burger", "mmm, tasty", "https://historyhike.alex-mccaughran.net/artefact.png");
        quest.setReward(artefact);

        quests.add(quest);
        return quests;
    }

}
