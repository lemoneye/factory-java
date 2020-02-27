package DAO;

import DTO.PostDto;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 * Created by johnyorangeseed on 9/19/19.
 */
public class PostDao {
    public static PostDao ourInstance;

    public static PostDao getInstance() throws IOException {
        if (ourInstance == null) {
            ourInstance = new PostDao();
        }
        return ourInstance;
    }

    public PostDto newDTO(JsonObject object){

        return new PostDto(object.get("postid").getAsInt(),object.get("userid").getAsInt(),object.get("data").getAsString());
    }

}
