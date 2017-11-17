package com.mommoo.flat.component.pager;

import com.mommoo.animation.Animator;
import com.mommoo.animation.timeInterpolator.AccelerateInterpolator;

abstract class AbstractAnimator extends Animator {
    AbstractAnimator(){
        setTimeInterpolator(new AccelerateInterpolator());
        setDuration(150);
    }
}
