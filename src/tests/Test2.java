package tests;

import tests.Test1;

public class Test2 {
    Test1 test1Obj;
    Object[] arrayObj;

    public Test2() {

    }

    public Test2(Test1 test1Obj) {
        this.test1Obj = test1Obj;
        arrayObj = new Object[4];
        arrayObj[0] = 1234456;
        arrayObj[1] = "abcdef";
        arrayObj[2] = 1421.7645;
        arrayObj[3] = false;
    }
}
