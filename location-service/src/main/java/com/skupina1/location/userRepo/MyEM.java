package com.skupina1.location.userRepo;

import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier             // Marks this annotation as a CDI qualifier
@Retention(RUNTIME)    // Available at runtime
@Target({FIELD, TYPE, METHOD, PARAMETER}) // Can be used on fields, classes, methods, parameters
public @interface MyEM {}  // Name of the qualifier
