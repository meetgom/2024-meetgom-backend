package com.meetgom.backend.utils.annotation

import org.springframework.stereotype.Component
import java.lang.annotation.ElementType


@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME) 
@MustBeDocumented
@Component
annotation class Storage(
    val value: String = "" 
)