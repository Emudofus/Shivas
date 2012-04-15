package org.shivas.server.database.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.google.inject.BindingAnnotation;

@Retention(RetentionPolicy.RUNTIME) @BindingAnnotation
public @interface StaticDatabase {

}
