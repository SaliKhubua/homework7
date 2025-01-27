import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest1 {
    private String userName;
    private String password;
}
