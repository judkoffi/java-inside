package fr.umlv.java.inside.lab2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD }) //Annotion valable que sur les methodes
@Retention(RetentionPolicy.RUNTIME) // Visible a l'execution sans changer le point class

public @interface JSONProperty {
	String value() default "";
}
