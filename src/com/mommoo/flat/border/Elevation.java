package com.mommoo.flat.border;

public enum Elevation {
    NONE(0), NORMAL(1), EXTRA(2), ULTRA(3);

    private final int INNER_SHADOW_COUNT;

    private Elevation(int innerShadowCount){
        this.INNER_SHADOW_COUNT = innerShadowCount;
    }

    int getInnerShadowCount(){
        return INNER_SHADOW_COUNT;
    }
}
