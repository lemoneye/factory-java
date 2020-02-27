package DAO;
import java.util.ArrayList;
import DTO.UserDto;
import com.google.gson.JsonObject;



// Takes in DTO to send out as JSON object

public class UserDao {
    public static UserDao ourInstance;

    public static UserDao getInstance() {
        if (ourInstance == null) {
            ourInstance = new UserDao();
        }

        return ourInstance;

    }

    public UserDto newDTO(JsonObject object){

        return new UserDto(object.get("username").getAsString(),object.get("userid").getAsInt());
    }

}
