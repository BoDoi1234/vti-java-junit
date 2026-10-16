package vn.vti;

import java.util.stream.Stream;

public class DataProviderMethod {
    public static Stream<String> students() {
        return Stream.of("Anh", "Tho", "Thu");
    }


}
