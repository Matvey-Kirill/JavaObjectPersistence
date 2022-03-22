package main;

import main.utils.types.JSON;
import main.utils.types.JSONValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonFileHelper {
    private String fname;

    public JsonFileHelper(String fname) {
        this.fname = fname;
    }

    public void serialize(Object obj) throws IOException {
        JSONValue json = JSONSerializer.serialize(obj);
        String text = json.toJSON();
        File out = new File(fname);
        out.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(out);
        outputStream.write(text.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }

    public <T> T deserialize(Class<T> resultClass) throws IOException {
        JSONValue value = JSON.parse(new File(fname));
        return JSONDeserializer.deserialize(resultClass, value);

    }
}
