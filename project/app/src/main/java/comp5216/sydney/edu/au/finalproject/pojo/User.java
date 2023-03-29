package comp5216.sydney.edu.au.finalproject.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

//用户信息
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private Integer id;
    private String userName;
    private String password;
    private String email;
    private String nickName;
    private String description;
    private String uuid;
    private String userImageAddress;
    private String Tag;
    private List<Post> postList;
    private List<Pet> petList;
    private List<User> friendRecordList;

    public User(Integer id, String userName, String password, String email, String nickName, String description, String uuid, String userImageAddress, String tag, List<Post> postList, List<Pet> petList, List<User> friendRecordList) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.nickName = nickName;
        this.description = description;
        this.uuid = uuid;
        this.userImageAddress = userImageAddress;
        Tag = tag;
        this.postList = postList;
        this.petList = petList;
        this.friendRecordList = friendRecordList;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserImageAddress() {
        return userImageAddress;
    }

    public void setUserImageAddress(String userImageAddress) {
        this.userImageAddress = userImageAddress;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public void setPetList(List<Pet> petList) {
        this.petList = petList;
    }

    public List<User> getFriendRecordList() {
        return friendRecordList;
    }

    public void setFriendRecordList(List<User> friendRecordList) {
        this.friendRecordList = friendRecordList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", nickName='" + nickName + '\'' +
                ", description='" + description + '\'' +
                ", uuid='" + uuid + '\'' +
                ", userImageAddress='" + userImageAddress + '\'' +
                ", Tag='" + Tag + '\'' +
                ", postList=" + postList +
                ", petList=" + petList +
                ", friendRecordList=" + friendRecordList +
                '}';
    }
}