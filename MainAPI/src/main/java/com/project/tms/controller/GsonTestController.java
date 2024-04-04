package com.project.tms.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GsonTestController {

    @ResponseBody
    @GetMapping("/gson_test")
    public String gsonTest() {
        JsonObject jsonObj = new JsonObject();

        jsonObj.addProperty("title", "GsonTest Title");
        jsonObj.addProperty("content", "GsonTest Content");

        JsonArray jsonArr = new JsonArray();
        for (int i = 0; i < 5; i++) {
            JsonObject jsonObj2 = new JsonObject();
            jsonObj2.addProperty("data", i);
            jsonArr.add(jsonObj2);
        }

        jsonObj.add("testData", jsonArr);

        return jsonObj.toString();
    }

    @ResponseBody
    @PostMapping("/gson_player")
    public Player convertStringToPlayer2(@RequestBody String jsonStr) {
        Gson gson = new Gson();
        Player player = gson.fromJson(jsonStr, Player.class);
        return player;
    }
}