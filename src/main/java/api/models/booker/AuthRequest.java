package api.models.booker;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class AuthRequest {
    private String username;
    private String password;
}
