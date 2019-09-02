package com.cnsunrun.common.model;

/**
 * Created by WQ on 2016/12/8.
 */

public class ChooseEntity implements Comparable {

    public String value;

    public String title;

    public ChooseEntity() {
    }

    public ChooseEntity(String id, String val) {
        this.value = id;
        this.title = val;
    }

    public ChooseEntity(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int hashCode() {
        return String.valueOf(value).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ChooseEntity) {
            ChooseEntity ch = (ChooseEntity) o;
            return String.valueOf(ch.value).equals(value);
        } else if (o instanceof String) {
            return String.valueOf(o).equals(value);
        }
        return super.equals(o);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof ChooseEntity) {
            ChooseEntity ch = (ChooseEntity) o;
            return String.valueOf(ch.value).compareTo(value);
        } else if (o instanceof String) {
            return String.valueOf(o).compareTo(value);
        }
        return 0;
    }
}
