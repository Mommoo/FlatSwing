package com.mommoo.flat.layout.linear;

import com.mommoo.flat.layout.linear.constraints.LinearConstraints;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ConstraintsFinder {
    private final Map<Component, LinearConstraints> FINDER = new HashMap<>();

    public void put (Component component, LinearConstraints constraints) {
        FINDER.put(component, constraints);
    }

    public void remove (Component component) {
        FINDER.remove(component);
    }

    public LinearConstraints find (Component component) {
        return FINDER.getOrDefault(component, new LinearConstraints());
    }

    public int getWeightSum(){
        return FINDER.keySet()
                .stream()
                .mapToInt(component -> FINDER.get(component).getWeight())
                .sum();
    }
}
