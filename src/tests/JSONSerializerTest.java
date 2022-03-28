package tests;

import main.JsonFileHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

class JSONSerializerTest {
    @Test
    public void test1() throws IOException {
        int[] arr = new int[3];
        arr[0] = 1;
        arr[1] = 2;
        arr[2] = 3;
        Map<String, String> obj = new HashMap<>();
        obj.put("a", "b");
        obj.put("c", "d");
        Test1 test1 = new Test1(arr, true, "#82b92c", null, obj);

        JsonFileHelper helper = new JsonFileHelper("tests.Test.json");
        helper.serialize(test1);

        Test1 result = helper.deserialize(Test1.class);

        Assertions.assertEquals(test1.obj, result.obj);
        Assertions.assertArrayEquals(test1.arr, result.arr);
        Assertions.assertEquals(test1.color, result.color);
        Assertions.assertEquals(test1.n, result.n);

        Test2 test2 = new Test2(test1);
        helper.serialize(test2);

        Test2 result2 = helper.deserialize(Test2.class);

        Assertions.assertEquals(test2.test1Obj.obj, result2.test1Obj.obj);
        Assertions.assertArrayEquals(test2.test1Obj.arr, result2.test1Obj.arr);
        Assertions.assertEquals(test2.test1Obj.color, result2.test1Obj.color);
        Assertions.assertEquals(test2.test1Obj.n, result2.test1Obj.n);
        Assertions.assertArrayEquals(test2.arrayObj, result2.arrayObj);
    }

    @Test
    public void testCyclic() throws IOException {
        JsonFileHelper helper = new JsonFileHelper("tests.Test.json");

        A a = new A();
        B b = new B();
        a.str = "adcde";
        b.anInt = 12345;
        a = new A("abcde", b);
        b = new B(12345, a);
        helper.serialize(a);

        A res = helper.deserialize(A.class);
        Assertions.assertEquals(a.str, res.str);
        Assertions.assertEquals(a.b.anInt, res.b.anInt);
        Assertions.assertEquals(a.b.a, res.b.a);
    }

    @Test
    public void testFilter1() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JsonFileHelper helper = new JsonFileHelper("tests.Test.json");

        ArrayList<Person> people = new ArrayList<>();
        people.add(new Person("A", 10));
        people.add(new Person("B", 18));
        people.add(new Person("C", 25));
        people.add(new Person("D", 8));
        people.add(new Person("E", 13));
        People p = new People(people);

        helper.serialize(p);

        Predicate<Person> predicate = person -> person.age >= 10 && person.age <= 18;


        People result = helper.deserialize(People.class, predicate);


        ArrayList<Person> correct = new ArrayList<>();
        correct.add(new Person("A", 10));
        correct.add(new Person("B", 18));
        correct.add(new Person("E", 13));
        for (int i = 0; i < result.people.size(); i++) {
            Person r = result.people.get(i);
            Person c = correct.get(i);
            assertEquals(r.age, c.age);
            assertEquals(r.name, c.name);
        }

    }

    @Test
    public void testFilter2() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JsonFileHelper helper = new JsonFileHelper("tests.Test.json");
        HashSet<Test3> set = new HashSet<>();
        set.add(new Test3(3, 'a'));
        set.add(new Test3(3, 'b'));
        set.add(new Test3(9, 'c'));
        set.add(new Test3(-123, 'a'));
        set.add(new Test3(18, 'z'));
        Test3Main test = new Test3Main(set);
        helper.serialize(test);

        Predicate<Test3> pred = t -> t.number < 10 && t.c != 'a';

        Test3Main result = helper.deserialize(Test3Main.class, pred);

        Set<Test3> correct = set.stream().filter(pred).collect(Collectors.toSet());


    }
}