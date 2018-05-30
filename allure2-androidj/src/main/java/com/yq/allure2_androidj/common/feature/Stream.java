package com.yq.allure2_androidj.common.feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.yq.allure2_androidj.common.utils.Tools.isNone;
import static com.yq.allure2_androidj.common.utils.Tools.notNone;

public class Stream <S> {

    private List<S> tList = new ArrayList<>();

    @SafeVarargs
    public static<T> Stream<T> of(T... values) {
        Stream<T> tStream = new Stream<>();
        for (T t : values) {
            if (notNone(t))
                tStream.tList.add(t);
        }
        return tStream;
    }

    public static<T> Stream<T> of(List<T> values) {
        Stream<T> tStream = new Stream<>();
        for (T t : values) {
            if (notNone(t))
                tStream.tList.add(t);
        }
        return tStream;
    }

    public <E> Stream<E> map(ToDo<S,E> toDo) {
        Stream<E> tStream = new Stream<>();
        for (S s : tList) {
            tStream.tList.add(toDo.exc(s));
        }
        return tStream;
    }

    public void forEach(ToRun<S> toRun) {
        for (S s : tList) {
            toRun.exc(s);
        }
    }

    public Stream<S> filter(Filter<S> f){
        for (S s : tList) {
            if (!f.filter(s)) {
                tList.remove(s);
            }
        }
        return this;
    }

    public S findFirst(){
        if (isNone(tList)) {
            return null;
        }
        return tList.get(0);
    }
    public List<S> toList() {
        return tList;
    }
}
