package com.cnsunrun.common.intent;

/**
 * Created by WQ on 2017/8/24.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface Key {
    String value() default "";
}
