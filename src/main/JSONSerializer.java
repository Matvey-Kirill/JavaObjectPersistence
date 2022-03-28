package main;

import main.annotations.JSONAlways;
import main.annotations.JSONIgnore;
import main.annotations.JSONName;
import main.utils.print.Strings;
import main.utils.types.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.*;
import java.util.*;

public class JSONSerializer {

    /**
     * Private constructor.  A question for the future - do I want to allow options to be set on
     * an individual instance of this class to customise the serialization?
     */
    private JSONSerializer() {
    }

    /**
     * Create a JSON representation of any given object.
     *
     * @param object the object
     * @return the JSON for that object
     */
    public static JSONValue serialize(Object object) {

        // is it null?

        if (object == null)
            return null;
        Class<?> objectClass = object.getClass();

        // is it already a JSONValue?

        if (object instanceof JSONValue)
            return (JSONValue) object;

        // is it a CharSequence (e.g. String)?

        if (object instanceof CharSequence)
            return new JSONString((CharSequence) object);

        // is is a Number?

        if (object instanceof Number)
            return serializeNumberInternal(objectClass, (Number) object);

        // is it a Boolean?

        if (objectClass.equals(Boolean.class))
            return JSONBoolean.valueOf((Boolean) object);

        // is it a Character?

        if (objectClass.equals(Character.class))
            return new JSONString(object.toString());

        // is it an array of char?

        if (object instanceof char[])
            return new JSONString(new String((char[]) object));

        // is it an Object array?

        if (object instanceof Object[]) {
            JSONArray jsonArray = new JSONArray();
            for (Object item : (Object[]) object)
                jsonArray.add(serialize(item));
            return jsonArray;
        }

        // is it an array of primitive type? (other than char)

        if (objectClass.isArray())
            return serializeArray(object);


        // is it an enum?

        if (object instanceof Enum)
            return new JSONString(object.toString());

        // is it an Iterable?

        if (object instanceof Iterable)
            return serializeIterable((Iterable<?>) object);


        // is it a Map?

        if (object instanceof Map) {
            return serializeMap((Map<?, ?>) object, object.getClass().getName());
        }

        // serialize it as an Object (this may not be a satisfactory default behaviour)

        JSONObject jsonObject = new JSONObject();
        addFieldsToJSONObject(jsonObject, objectClass, object);
        return jsonObject;

    }

    /**
     * Serialize an object to its external JSON representation.  This is a convenience method
     * to allow serialization to a string form in a single call.
     *
     * @param object the object
     * @return the JSON for that object
     */
    public static String toJSON(Object object) {
        return serialize(object).toJSON();
    }


    /**
     * Serialize the various {@link Number} classes (skip null check).
     *
     * @param numberClass the class of the number
     * @param number      the {@link Number}
     * @return the JSON for that object
     */
    private static JSONNumberValue serializeNumberInternal(Class<?> numberClass,
                                                           Number number) {

        // is it an Integer?

        if (numberClass.equals(Integer.class) || numberClass.equals(Short.class) ||
                numberClass.equals(Byte.class))
            return JSONInteger.valueOf(number.intValue());

        // is it a Long?

        if (numberClass.equals(Long.class))
            return JSONLong.valueOf(number.longValue());

        // is it a Double?

        if (numberClass.equals(Double.class))
            return JSONDouble.valueOf(number.doubleValue());

        // is it a Float?

        if (numberClass.equals(Float.class))
            return JSONFloat.valueOf(number.floatValue());

        // find the best representation

        long longValue = number.longValue();
        double doubleValue = number.doubleValue();
        if (doubleValue != longValue)
            return JSONDouble.valueOf(number.doubleValue());

        int intValue = number.intValue();
        if (longValue != intValue)
            return JSONLong.valueOf(longValue);

        return intValue == 0 ? new JSONZero() : JSONInteger.valueOf(intValue);
    }

    /**
     * Serialize an array of primitive type (except for {@code char[]} which serializes as a
     * string).
     *
     * @param array the array
     * @return the JSON for that array
     * @throws JSONException if the array can't be serialized
     */
    public static JSONArray serializeArray(Object array) {

        JSONArray jsonArray = new JSONArray();

        if (array instanceof int[]) {
            for (int item : (int[]) array)
                jsonArray.addValue(item);
            return jsonArray;
        }

        if (array instanceof long[]) {
            for (long item : (long[]) array)
                jsonArray.addValue(item);
            return jsonArray;
        }

        if (array instanceof boolean[]) {
            for (boolean item : (boolean[]) array)
                jsonArray.addValue(item);
            return jsonArray;
        }

        if (array instanceof double[]) {
            for (double item : (double[]) array)
                jsonArray.addValue(item);
            return jsonArray;
        }

        if (array instanceof float[]) {
            for (float item : (float[]) array)
                jsonArray.addValue(item);
            return jsonArray;
        }

        if (array instanceof short[]) {
            for (short item : (short[]) array)
                jsonArray.addValue(item);
            return jsonArray;
        }

        if (array instanceof byte[]) {
            for (byte item : (byte[]) array)
                jsonArray.addValue(item);
            return jsonArray;
        }

        Class<?> arrayClass = array.getClass();
        throw new JSONException(!arrayClass.isArray() ? "Not an array" :
                "Can't serialize array of " + arrayClass.getComponentType());
    }

    /**
     * Serialize a {@link Collection}.
     *
     * @param collection the {@link Collection}
     * @return the JSON for that {@link Collection}
     */
    public static JSONArray serializeCollection(Collection<?> collection) {
        JSONArray jsonArray = new JSONArray();
        for (Object item : collection)
            jsonArray.add(serialize(item));
        return jsonArray;
    }

    /**
     * Serialize a {@link List}.  Synonym for {@link #serializeCollection(Collection)}.
     *
     * @param list the {@link List}
     * @return the JSON for that {@link List}
     */
    public static JSONArray serializeList(List<?> list) {
        return serializeCollection(list);
    }

    /**
     * Serialize a {@link Set}.  Synonym for {@link #serializeCollection(Collection)}.
     *
     * @param set the {@link Set}
     * @return the JSON for that {@link Set}
     */
    public static JSONArray serializeSet(Set<?> set) {
        return serializeCollection(set);
    }

    /**
     * Serialize a {@link Map} to a {@link JSONObject}.  The key is converted to a string (by
     * means of the {@link Object#toString() toString()} method), and the value is serialized
     * using this class.
     *
     * @param map the {@link Map}
     * @return the JSON for that {@link Map}
     */
    public static JSONObject serializeMap(Map<?, ?> map, String type) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", serialize(type));
        for (Map.Entry<?, ?> entry : map.entrySet())
            jsonObject.put(entry.getKey().toString(), serialize(entry.getValue()));
        return jsonObject;
    }

    /**
     * Serialize an {@link Iterable}.
     *
     * @param iterable the {@link Iterable}
     * @return the JSON for that {@link Iterable}
     */
    public static JSONArray serializeIterable(Iterable<?> iterable) {
        JSONArray jsonArray = new JSONArray();
        for (Object item : iterable)
            jsonArray.add(serialize(item));
        return jsonArray;
    }

    /**
     * Serialize an object.  This is a convenience method that bypasses some of the built-in
     * type checks, for cases where the object is known to require field-by-field serialization.
     *
     * @param object the object
     * @return the JSON for that object
     */
    public static JSONObject serializeObject(Object object) {
        if (object == null)
            return null;
        JSONObject jsonObject = new JSONObject();
        addFieldsToJSONObject(jsonObject, object.getClass(), object);
        return jsonObject;
    }

    /**
     * Add the individual serializations of the fields of an {@link Object} to a
     * {@link JSONObject}.  This method first calls itself recursively to get the fields of the
     * superclass (if any), then iterates through the declared fields of the class.
     *
     * @param jsonObject  the destination {@link JSONObject}
     * @param objectClass the {@link Class} object for the source
     * @param object      the source object
     * @throws JSONException on any errors accessing the fields
     */
    private static void addFieldsToJSONObject(JSONObject jsonObject, Class<?> objectClass,
                                              Object object) {

        // TODO check class-based main.annotations, including option to apply @JSONAlways on all

        // first deal with fields of superclass

        Class<?> superClass = objectClass.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class))
            addFieldsToJSONObject(jsonObject, superClass, object);

        // now, for each field in this class

        for (Field field : objectClass.getDeclaredFields()) {

            // ignore fields marked as static or transient, or annotated with @JSONIgnore

            if (!fieldStaticOrTransient(field) && !fieldAnnotated(field, JSONIgnore.class)) {

                // check for explicit name annotation

                String fieldName = field.getName();
                JSONName nameAnnotation = field.getAnnotation(JSONName.class);
                if (nameAnnotation != null) {
                    String nameValue = nameAnnotation.value();
                    if (nameValue != null)
                        fieldName = nameValue;
                }

                // add the field to the object if not null, or if annotated with @JSONAlways

                try {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    if (value != null) {
                        jsonObject.put(fieldName, serialize(value));
                    } else if (fieldAnnotated(field, JSONAlways.class))
                        jsonObject.putNull(fieldName);
                } catch (JSONException e) {
                    throw e;
                } catch (Exception e) {
                    throw new JSONException("Error serializing " + objectClass.getName() + '.' +
                            fieldName);
                }

            }

        }

    }

    /**
     * Test whether a field a marked with the {@code static} or {@code transient} modifiers.
     *
     * @param field the {@link Field}
     * @return {@code true} if the field has the {@code static} or {@code transient} modifiers
     */
    private static boolean fieldStaticOrTransient(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers);
    }

    /**
     * Test whether a field is annotated with a nominated annotation class.
     *
     * @param field           the {@link Field}
     * @param annotationClass the annotation class
     * @return {@code true} if the field has the nominated annotation
     */
    private static boolean fieldAnnotated(Field field,
                                          Class<? extends Annotation> annotationClass) {
        return field.getAnnotation(annotationClass) != null;
    }

}

