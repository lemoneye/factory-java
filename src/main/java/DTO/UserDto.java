package DTO;

public class UserDto {
    public final String username;
    public final Integer userid;



  public UserDto(String username, Integer userid){
      this.username = username;
      this.userid = userid;
  }

    public UserDto(String username){
        this.username = username;
        this.userid = null;
    }

    public UserDto(Integer userid) {
        this.username = null;
        this.userid = userid;
    }

  public String getUsername() {
        return username;
  }

  public int getUserid() {
      return userid;
  }



}
