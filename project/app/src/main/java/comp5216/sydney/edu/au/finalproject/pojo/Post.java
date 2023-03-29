package comp5216.sydney.edu.au.finalproject.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Serializable {
    private Integer postId;
    private Integer userId;
    private Integer love;
    private String postContent;
    private String tag;
    private String topic;
    private Long posTime; //sava timestamp
    private List<Comment> commentList;
    private List<String> videoUrlList;
    private List<String> imageUrlList;
    private String userAvatar;
    private boolean loved;
    private Integer totalPosts;
    private String nickName;
    private String userName;
    private Integer visitCount;

    public Post() {
    }

    public Post(Integer postId, Integer userId, Integer love, String postContent, String tag, String topic, Long posTime, List<Comment> commentList, List<String> videoUrlList, List<String> imageUrlList, String userAvatar, boolean loved, Integer totalPosts, String nickName, String userName, Integer visitCount) {
        this.postId = postId;
        this.userId = userId;
        this.love = love;
        this.postContent = postContent;
        this.tag = tag;
        this.topic = topic;
        this.posTime = posTime;
        this.commentList = commentList;
        this.videoUrlList = videoUrlList;
        this.imageUrlList = imageUrlList;
        this.userAvatar = userAvatar;
        this.loved = loved;
        this.totalPosts = totalPosts;
        this.nickName = nickName;
        this.userName = userName;
        this.visitCount = visitCount;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLove() {
        return love;
    }

    public void setLove(Integer love) {
        this.love = love;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getPosTime() {
        return posTime;
    }

    public void setPosTime(Long posTime) {
        this.posTime = posTime;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<String> getVideoUrlList() {
        return videoUrlList;
    }

    public void setVideoUrlList(List<String> videoUrlList) {
        this.videoUrlList = videoUrlList;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public boolean isLoved() {
        return loved;
    }

    public void setLoved(boolean loved) {
        this.loved = loved;
    }

    public Integer getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(Integer totalPosts) {
        this.totalPosts = totalPosts;
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

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", love=" + love +
                ", postContent='" + postContent + '\'' +
                ", tag='" + tag + '\'' +
                ", topic='" + topic + '\'' +
                ", posTime=" + posTime +
                ", commentList=" + commentList +
                ", videoUrlList=" + videoUrlList +
                ", imageUrlList=" + imageUrlList +
                ", userAvatar='" + userAvatar + '\'' +
                ", loved=" + loved +
                ", totalPosts=" + totalPosts +
                ", nickName='" + nickName + '\'' +
                ", userName='" + userName + '\'' +
                ", visitCount=" + visitCount +
                '}';
    }
}