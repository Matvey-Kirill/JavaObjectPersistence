package tests;

import tests.Test1;

public class Test2 {
    Test1 test1Obj;
    Integer[] arrayObj;

    public Test2() {

    }

    public Test2(Test1 test1Obj) {
        this.test1Obj = test1Obj;
        arrayObj = new Integer[4];
        arrayObj[0] = 1234456;
        arrayObj[1] = 125342;
        arrayObj[2] = 14217645;
        arrayObj[3] = 5341;
    }
}
