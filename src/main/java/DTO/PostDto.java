package DTO;

/**
 * Created by johnyorangeseed on 9/16/19.
 */
public class PostDto {
    public final Integer postid;
    public final Integer userid;
    public final String data;

    public PostDto(Integer postid, Integer userid, String data) {
        this.postid = postid;
        this.userid = userid;
        this.data = data;
    }

    public Integer getPostid() {
        return postid;
    }

    public Integer getUserid() {
        return userid;
    }

    public String getData() {
        return data;
    }


}
