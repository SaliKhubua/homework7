import lombok.Data;

@Data
public class UserRequest {
    private String userName;
    private String password;

    public UserRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
