package com.example.historyhike.controller;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.historyhike.model.Artefact;
import com.example.historyhike.model.Objective;
import com.example.historyhike.model.Quest;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public interface FetchQuestsCallback {
        void onSuccess(ArrayList<Quest> quests);
        void onFailure(String errorMessage);
    }

    public void fetchUncompletedQuests(String jwt, FetchQuestsCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "quest/uncompleted");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Bearer " + jwt);
                conn.setRequestProperty("Content-Type", "application/json");

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Parse the JSON response
                    ArrayList<Quest> quests = parseQuestsFromJson(response.toString());

                    // Notify success on the main thread
                    new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(quests));
                } else {
                    // Notify failure on the main thread
                    new Handler(Looper.getMainLooper()).post(() -> callback.onFailure("Failed to fetch quests"));
                }
            } catch (Exception e) {
                Log.e("ApiController", "Error fetching quests", e);
                // Notify failure on the main thread
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure("Error: " + e.getMessage()));
            }
        }).start();
    }

    private ArrayList<Quest> parseQuestsFromJson(String jsonResponse) {
        ArrayList<Quest> quests = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject questJson = jsonArray.getJSONObject(i);
                Quest quest = new Quest();
                quest.setTitle(questJson.getString("title"));
                quest.setDescription(questJson.getString("description"));

                // Parse objectives
                JSONArray objectivesArray = questJson.getJSONArray("objectives");
                ArrayList<Objective> objectives = new ArrayList<>();
                for (int j = 0; j < objectivesArray.length(); j++) {
                    JSONObject objectiveJson = objectivesArray.getJSONObject(j);
                    Objective objective = new Objective(
                            objectiveJson.getInt("id"),
                            objectiveJson.getDouble("latitude"),
                            objectiveJson.getDouble("longitude"),
                            objectiveJson.getString("name"),
                            objectiveJson.getString("description")
                    );
                    objectives.add(objective);
                }
                quest.setQuestPath(objectives);

                // Parse artefact (reward)
                JSONObject artefactJson = questJson.getJSONObject("artefact");
                Artefact artefact = new Artefact(
                        artefactJson.getInt("id"),
                        artefactJson.getString("name"),
                        artefactJson.getString("description"),
                        artefactJson.getString("imageUrl")
                );
                quest.setReward(artefact);

                quests.add(quest);
            }
        } catch (Exception e) {
            Log.e("ApiController", "Error parsing quests JSON", e);
        }
        return quests;
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
