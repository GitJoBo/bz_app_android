package com.liys.dialoglib;


import androidx.annotation.StringDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @Desc: 默认动画类型
 * @author: admin wsj
 * @Date: 2021/4/21 2:09 PM
 */
public interface AnimationsType {

    String DEFAULT = "default";
    String LEFT = "left";
    String RIGHT = "right";
    String TOP = "top";
    String BOTTOM = "bottom";
    String SCALE = "scale";

    @StringDef({DEFAULT, LEFT, RIGHT, TOP, BOTTOM, SCALE})
    @Retention(SOURCE)
    @Target({ElementType.PARAMETER})
    @interface AAnimationsType{}
}
