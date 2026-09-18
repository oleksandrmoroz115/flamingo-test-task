package ui.data;

import java.util.Random;
import java.util.UUID;

public class UserDataGenerator {
    private static final Random random = new Random();

    public static StudentData generateValidStudent() {
        return StudentData.builder()
                .firstName("John_" + randomString(4))
                .lastName("Doe_" + randomString(4))
                .email("test_" + randomString(5) + "@example.com")
                .gender("Female")
                .mobileNumber(randomNumericString(10))
                .dobDay("15")
                .dobMonth("May")
                .dobYear("1995")
                .subject("Maths")
                .hobby("Sports")
                .picturePath("src/test/resources/testdata/sample-upload.txt")
                .pictureName("sample-upload.txt")
                .address("123 Random St_" + randomString(4))
                .state("NCR")
                .city("Delhi")
                .build();
    }

    public static StudentData generateInvalidStudent() {
        return StudentData.builder()
                .firstName("Invalid")
                .lastName("User")
                .email("invalid-email")
                .gender("Female")
                .mobileNumber("12345") // Too short
                .build();
    }

    public static WebTableRecord generateValidRecord() {
        return WebTableRecord.builder()
                .firstName("Alice_" + randomString(4))
                .lastName("Smith_" + randomString(4))
                .email("alice_" + randomString(5) + "@test.com")
                .age(String.valueOf(random.nextInt(40) + 20))
                .salary(String.valueOf((random.nextInt(50) + 30) * 1000))
                .department("QA")
                .build();
    }



    private static String randomString(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }

    private static String randomNumericString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
