package ProcessorFactory;

import DAO.PostDao;
import DTO.PostDto;
import java.util.HashMap;
import com.google.gson.*;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;



// PostProcessor subclass
public class PostProcessor extends Processor {

    public HashMap<String, String> postMap = new HashMap<>();
    public String resultString = new String();
    private JsonObject myObj;
    String tempString = "";

    public PostProcessor(HashMap<String, String> map) {
        // Assign map as data field of PostProcessor class:
        postMap = map;

        System.out.println("Hell Yeah");
        System.out.println(map);
        // If there are no arguments in the url, return the array of all posts
        resultString = process(map);


    }


    public String process(HashMap<String, String> map) {
        // Here is where the Data is accessed from the website
        String sURL = "http://brianparra.com/sfsu/postData.json";
        String ResponseStr = new String(); // String that holds the response portion of the resulting JSON
        // Connect to the URL using java's native library
        try {
            URL url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.
            System.out.println(rootobj);

            // Print out from "posts" as JSON array
            JsonArray arr = rootobj.getAsJsonArray("posts");
            System.out.println(arr);

            if (map.size() == 0) {
                // If no arguments are specified, set ResponseStr to entire JSON object here as a string
                System.out.println("THERE ARE ABSOLUTELY NO ARGUMENTS SPECIFIED");
                for (int i=0; i<arr.size();i++){
                    myObj = arr.get(i).getAsJsonObject();
                    PostDao postDao = PostDao.getInstance();
                    PostDto newPostDTO = postDao.newDTO(myObj);
                    tempString = tempString + "postid:" + newPostDTO.getPostid() + "@userid:" + newPostDTO.getUserid() + "@data:" + newPostDTO.getData() + "\n";
                }
                tempString = tempString + "\n " + map.toString();
                return tempString;

            }
            else

                for (int i=0; i<arr.size();i++){
                    System.out.println("ARGUMENT: " + map.keySet().toArray()[0].toString() + " VALUE: " + map.values().toArray()[0].toString());
                    Integer result = Integer.valueOf(map.values().toArray()[0].toString());
                    System.out.println(result);

                    System.out.println();
                    System.out.println();

                    String argument = map.keySet().toArray()[0].toString();

                    System.out.println("DISREGARD: TO PRINT -----" +arr.get(i).getAsJsonObject().get("postid"));
                    JsonElement postID = arr.get(i).getAsJsonObject().get(argument);
                    if(map.containsValue(postID.getAsString()) ){
                        System.out.println("Key match ID ::: " +postID);
                        myObj = arr.get(i).getAsJsonObject();
                        PostDao postDao = PostDao.getInstance();
                        PostDto newPostDTO = postDao.newDTO(myObj);
                        tempString = tempString + "postid:" + newPostDTO.getPostid() + "@userid:" + newPostDTO.getUserid() + "@data:" + newPostDTO.getData() + "\n";
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

        System.out.println("FINAL STRING: " + ResponseStr);
        return null;

    }



}
