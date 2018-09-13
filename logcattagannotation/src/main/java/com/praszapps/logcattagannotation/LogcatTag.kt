package com.praszapps.logcattagannotation

/**
 * This annotation when applied on a class will generate a Logcat tag with the
 * simple name of the class
 *
 * @author Prasannajeet Pani
 * @since 1.0.0
 *
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class LogcatTag