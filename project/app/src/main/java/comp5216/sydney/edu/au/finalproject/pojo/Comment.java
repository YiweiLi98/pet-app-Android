package comp5216.sydney.edu.au.finalproject.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {
    private Integer id;
    private Integer postId;
    private Long commentTime;
    private String commentContent;
    private String userAvatar;
    private String nickName;
    private String userName;
    private int userId;

    public Comment() {
    }

    public Comment(Integer id, Integer postId, Long commentTime, String commentContent, String userAvatar, String nickName, String userName, int userId) {
        this.id = id;
        this.postId = postId;
        this.commentTime = commentTime;
        this.commentContent = commentContent;
        this.userAvatar = userAvatar;
        this.nickName = nickName;
        this.userName = userName;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Long commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId=" + postId +
                ", commentTime=" + commentTime +
                ", commentContent='" + commentContent + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userName='" + userName + '\'' +
                ", userId=" + userId +
                '}';
    }
}