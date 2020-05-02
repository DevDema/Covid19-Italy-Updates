package net.ddns.andrewnetwork.helpers.util;

public class Wrapper2<T, U> {

    T obj1;
    U obj2;

    public Wrapper2(T obj1, U obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    public T getObj1() {
        return obj1;
    }

    public U getObj2() {
        return obj2;
    }
}
