/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frame;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author FITIA ARIVONY
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.TYPE)  
public @interface AnnotMap {
    String nomTable() default "";
}
