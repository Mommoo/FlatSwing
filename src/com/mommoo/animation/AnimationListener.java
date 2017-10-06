package com.mommoo.animation;

import java.util.List;

public interface AnimationListener {
    public void onStart();
    public void onAnimation(List<Double> resultList);
    public void onStop();
    public void onEnd();
}
