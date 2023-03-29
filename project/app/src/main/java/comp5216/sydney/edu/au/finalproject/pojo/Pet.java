package comp5216.sydney.edu.au.finalproject.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pet implements Serializable {
    private Integer petId;
    private Integer userId;
    private String petName;
    private Integer age;
    private String category;
    private String petImageAddress;
    private String petDescription;
    private List<String> petImageList;

    public Pet() {
    }

    public Pet(Integer petId, Integer userId, String petName, Integer age, String category, String petImageAddress, String petDescription, List<String> petImageList) {
        this.petId = petId;
        this.userId = userId;
        this.petName = petName;
        this.age = age;
        this.category = category;
        this.petImageAddress = petImageAddress;
        this.petDescription = petDescription;
        this.petImageList = petImageList;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPetImageAddress() {
        return petImageAddress;
    }

    public void setPetImageAddress(String petImageAddress) {
        this.petImageAddress = petImageAddress;
    }

    public String getPetDescription() {
        return petDescription;
    }

    public void setPetDescription(String petDescription) {
        this.petDescription = petDescription;
    }

    public List<String> getPetImageList() {
        return petImageList;
    }

    public void setPetImageList(List<String> petImageList) {
        this.petImageList = petImageList;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "petId=" + petId +
                ", userId=" + userId +
                ", petName='" + petName + '\'' +
                ", age=" + age +
                ", category='" + category + '\'' +
                ", petImageAddress='" + petImageAddress + '\'' +
                ", petDescription='" + petDescription + '\'' +
                ", petImageList=" + petImageList +
                '}';
    }

}