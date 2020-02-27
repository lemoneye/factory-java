package ProcessorFactory;

import DAO.UserDao;
import java.util.HashMap;
import DTO.UserDto;
import com.google.gson.*;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;

// UserProcessor Subclass
public class UserProcessor extends Processor {

    public HashMap<String, String> userMap = new HashMap<>();
    public String resultString = new String();
    private JsonObject myObj;
    String tempString = "";

    public UserProcessor(HashMap<String, String> map) {
        // Assign map as data field of PostProcessor class:
        userMap = map;

        System.out.println("Hell Yeah brother");
        System.out.println(map);
        resultString = process(map);

    }

    public String process(HashMap<String, String> map) {
        // Here is where the Data is accessed from the website
        String sURL = "http://brianparra.com/sfsu/userData.json"; //just a string
        String ResponseStr = new String();     // Final result String

        // Connect to the URL using java's native library
        try {
            System.out.println("REACHED THE TRY AREA");
            URL url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.
            System.out.println(rootobj);

            // Print out from "users" as JSON array
            JsonArray arr = rootobj.getAsJsonArray("users");
            System.out.println(arr);

            if (map.size() == 0) {
                System.out.println("THERE ARE ABSOLUTELY NO ARGUMENTS SPECIFIED");
                for (int i=0; i<arr.size();i++){
                    myObj = arr.get(i).getAsJsonObject();
                    UserDao userDao = UserDao.getInstance();
                    UserDto newUserDTO = userDao.newDTO(myObj);
                    tempString = tempString + "username:" + newUserDTO.getUsername() + "@userid:" + newUserDTO.getUserid() + "\n";
                }
                tempString = tempString + "\n " + map.toString();
                return tempString;
            }
            else

                for (int i=0; i<arr.size();i++){
                    System.out.println("TO PRINT -----" +arr.get(i).getAsJsonObject().get("userid"));
                    JsonElement postID = arr.get(i).getAsJsonObject().get("userid");
                    if(map.containsValue(postID.getAsString()) ){
                        System.out.println("Key match ID ::: " +postID);
                        myObj = arr.get(i).getAsJsonObject();
                        UserDao userDao = UserDao.getInstance();
                        UserDto newUserDTO = userDao.newDTO(myObj);
                        tempString = tempString + "username:" + newUserDTO.getUsername() + "@userid:" + newUserDTO.getUserid() + "\n";
                    }
                }

            System.out.println("TEMPSTRING: ");
            System.out.println();
            System.out.println();
            System.out.println(tempString);
            System.out.println();
            System.out.println();
            if (tempString.length() == 0) {
                System.out.println("DUDE ITS NULL");
                tempString = tempString + "400 Bad Request";
            }
            tempString = tempString + "\n " + map.toString();
            return tempString;

        }
        catch (Exception e) {
            System.out.println("Went to exception");
            System.out.println(e);

        }
        return ResponseStr;
    }


    }
