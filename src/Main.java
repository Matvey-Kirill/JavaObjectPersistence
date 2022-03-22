import main.JsonFileHelper;
import tests.Test1;
import tests.Test2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Main {

    public static void main(String[] args) throws IOException {
        int[] arr = new int[3];
        arr[0] = 1;
        arr[1] = 2;
        arr[2] = 3;
        Map<String, String> obj = new HashMap<>();
        obj.put("a", "b");
        obj.put("c", "d");
        Test1 test1 = new Test1(arr, true, "#82b92c", null, obj);

        JsonFileHelper helper = new JsonFileHelper("tests.Test1.json");
        helper.serialize(test1);

        Test1 result = helper.deserialize(Test1.class);

        assert test1.equals(result);

        Test2 test2 = new Test2(test1);
        helper.serialize(test2);
    }
}
