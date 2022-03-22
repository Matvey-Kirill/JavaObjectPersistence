package tests;

import main.annotations.JSONAlways;

import java.util.Map;

public class Test1 {
    int[] arr;
    private boolean bool;
    String color;
    @JSONAlways
    String n;
    Map<String, String> obj;

    public Test1() {

    }

    public Test1(int[] arr, boolean bool, String color, String n, Map<String, String> obj) {
        this.arr = arr;
        this.bool = bool;
        this.color = color;
        this.n = n;
        this.obj = obj;
    }
}