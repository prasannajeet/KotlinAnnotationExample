package com.praszapps.logcatannotationprocessor

import com.google.auto.service.AutoService
import com.praszapps.logcatannotationprocessor.LogcatAnnotationProcessor.Companion.KAPT_KOTLIN_GENERATED_OPTION_NAME
import com.praszapps.logcattagannotation.LogcatTag
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * The Annotation processor class for @LogcatTag
 *
 * @author Prasannajeet Pani
 * @since 1.0.0
 *
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(KAPT_KOTLIN_GENERATED_OPTION_NAME)
class LogcatAnnotationProcessor: AbstractProcessor() {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    private val generatedSourcesRoot by lazy { processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME].orEmpty() }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(LogcatTag::class.java.canonicalName)
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(LogcatTag::class.java).forEach {classElement ->
            if (classElement.kind != ElementKind.CLASS) { // Tag can only be applied to a class
                printError("Can only be applied to class, element: $classElement")
                return false
            } else {
                val packageOfClass = processingEnv.elementUtils.getPackageOf(classElement).toString()
                val valueOfTagAnnotation = "${classElement.simpleName}"

                val companionObject = TypeSpec.companionObjectBuilder()
                        .addFunction(FunSpec.builder("getTag")
                                .addStatement("return \"$valueOfTagAnnotation\"")
                                .build()).build()

                val className = TypeSpec.classBuilder("${classElement.simpleName}TagBuilder")
                        .addType(companionObject)
                        .build()

                // I would have used className.name as filename but the statement needs a String and className.name returns a String?
                val file = FileSpec.builder(packageOfClass, "${classElement.simpleName}TagBuilder")
                            .addType(className)
                            .build()
                file.writeTo(File(generatedSourcesRoot))
                printWarning("Look left, look right, then cross the road :)")
                return true
            }
        }

        if (generatedSourcesRoot.isEmpty()) {
            printError("Can't find the target directory for generated Kotlin files.")
            return false
        }

        return true
    }

    private fun printError(message: String) {
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, message)
    }

    private fun printWarning(message: String) {
        processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, message)
    }
}