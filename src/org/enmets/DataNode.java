package org.enmets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by luoluyao on 2017/11/21.
 */
public class DataNode {
    private int type;
    private List<Float> attribute;

    public DataNode() {
        attribute = new ArrayList<Float>();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Float> getAttribute() {
        return attribute;
    }

    public void setAttribute(List<Float> attribute) {
        this.attribute = attribute;
    }

    public void addAttribute(Float value) {
        attribute.add(value);
    }

    @Override
    public String toString() {
        return "[" + "type:" + type + " " + "attribute:" + attribute + "]" + "\n";
    }
}
