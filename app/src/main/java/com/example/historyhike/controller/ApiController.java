package com.example.historyhike.controller;

import com.example.historyhike.model.Objective;
import com.example.historyhike.model.Quest;

import java.util.ArrayList;

public class ApiController {
    // This will handle all HTTP tasks. For now, it's just used to test application functions

    public ArrayList<Quest> fetchTestQuests() {
        ArrayList<Quest> quests = new ArrayList<>();

        // Creating test quests
        Objective obj1 = new Objective(1, 55.6057, -4.4968, "Home", "desc");
        Objective obj2 = new Objective(1, 55.6041, -4.4963, "Mc'D's", "desc");
        obj1.setImageURL("https://historyhike.alex-mccaughran.net/objective1.png");
        obj2.setImageURL("https://historyhike.alex-mccaughran.net/objective2.png");

        ArrayList<Objective> path = new ArrayList<>();
        path.add(obj1);
        path.add(obj2);

        Quest quest = new Quest();
        quest.setTitle("Test me");
        quest.setDescription("From home to McDonald's. A Quest well completed.");
        quest.setQuestPath(path);

        quests.add(quest);
        return quests;
    }

}
