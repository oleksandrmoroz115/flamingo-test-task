package ui.data;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudentData {
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String mobileNumber;
    private String dobDay;
    private String dobMonth;
    private String dobYear;
    private String subject;
    private String hobby;
    private String picturePath;
    private String pictureName;
    private String address;
    private String state;
    private String city;

    public String getExpectedFullName() {
        return firstName + " " + lastName;
    }

    public String getExpectedDob() {
        return dobDay + " " + dobMonth + "," + dobYear;
    }

    public String getExpectedStateCity() {
        return state + " " + city;
    }
}
