package ui.data;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WebTableRecord {
    private String firstName;
    private String lastName;
    private String email;
    private String age;
    private String salary;
    private String department;
}
