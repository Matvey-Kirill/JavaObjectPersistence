package main;

import main.annotations.JSONIgnore;
import main.utils.types.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import static main.JSONDeserializer.deserializeCollection;
import static main.JSONDeserializer.findField;

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

    public <T, V> T deserialize(Class<T> resultClass, Predicate<V> predicate) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        JSONObject value = (JSONObject) JSON.parse(new File(fname));
        Constructor<T> constructor = resultClass.getConstructor();
        constructor.setAccessible(true);
        T result = constructor.newInstance();

        for (Map.Entry<String, JSONValue> entry : value.entrySet()) {
            String name = entry.getKey();
            Field field = findField(resultClass, name);
            if (field == null)
                throw new JSONException("Can't find field for " + name);
            Type genericType = field.getGenericType();
            Type[] typeArgs = genericType instanceof ParameterizedType ?
                    ((ParameterizedType)genericType).getActualTypeArguments() : null;
            field.setAccessible(true);
            field.set(result, deserializeCollection(field.getType(), typeArgs, (JSONArray) entry.getValue(), predicate));

        }
        return result;
    }
}
