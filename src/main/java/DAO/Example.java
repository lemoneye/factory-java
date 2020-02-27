package DAO;

import DTO.UserDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Example {
    public static void main(String[] args) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      BufferedReader br;
      try {
          br = new BufferedReader(new FileReader("demoData.json"));
          JsonParser jsonParser = new JsonParser();
          JsonObject obj = jsonParser.parse(br).getAsJsonObject();
          UserDto[] users = gson.fromJson(obj.get("users"), UserDto[].class);
          System.out.println(gson.toJson(users[0]));
          //System.out.println(gson.toJson(users[0].));

      } catch (
              FileNotFoundException e) {
          e.printStackTrace();
      }
    }
}
