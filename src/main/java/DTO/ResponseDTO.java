package DTO;

/**
 * Created by johnyorangeseed on 9/18/19.
 */
public class ResponseDTO {
    String date;
    String params;
    String responseCode;
    String response;

    public ResponseDTO(){}

    public ResponseDTO(String response) {
        this.response = response;
    }

}
