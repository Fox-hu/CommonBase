package com.fox.commonbase.ext

import android.content.Context
import java.io.FileDescriptor
import java.io.PrintWriter
import java.lang.reflect.*

/**
 * 和反射操作有关的Utils
 *
 * @author RePlugin Team
 */
@Throws(
    NoSuchMethodException::class,
    IllegalAccessException::class,
    InvocationTargetException::class,
    InstantiationException::class
)
fun <T> Class<T>.invokeConstructor(
    parameterTypes: Array<Class<*>?>,
    vararg args: Any?
): T? {
    val c = this.getConstructor(*parameterTypes)
    c.isAccessible = true
    return c.newInstance(*args)
}

// ----------------
// Field
// ----------------
fun Class<*>.getFieldFromSuperOrInterface(fieldName: String?): Field? {
    // From Apache: FieldUtils.getField()
    // check up the superclass hierarchy
    var acls: Class<*>? = this
    while (acls != null) {
        try {
            val field = acls.getDeclaredField(fieldName)
            // getDeclaredField checks for non-public scopes as well
            // and it returns accurate results
            setAccessible(field, true)
            return field
        } catch (ex: NoSuchFieldException) { // NOPMD
            // ignore
        }
        acls = acls.superclass
    }
    // check the public interface case. This must be manually searched for
    // incase there is a public supersuperclass field hidden by a private/package
    // superclass field.
    var match: Field? = null
    for (class1: Class<*> in this.interfaces) {
        try {
            val test = class1.getField(fieldName)
            Validate.isTrue(
                match == null,
                "Reference to field %s is ambiguous relative to %s"
                        + "; a matching field exists on two or more implemented interfaces.",
                fieldName,
                this
            )
            match = test
        } catch (ex: NoSuchFieldException) { // NOPMD
            // ignore
        }
    }
    return match
}

@Throws(NoSuchFieldException::class, IllegalAccessException::class)
fun Class<*>.readStaticField(fieldName: String?): Any? {
    return readField(null, fieldName)
}

@Throws(IllegalAccessException::class, NoSuchFieldException::class)
fun Any.readField(fieldName: String?): Any? {
    return javaClass.readField(this, fieldName)
}

@Throws(IllegalAccessException::class, NoSuchFieldException::class)
fun Class<*>.readField(target: Any?, fieldName: String?): Any? {
    val f = getFieldFromSuperOrInterface(fieldName)
    return readField(f, target)
}

@Throws(IllegalAccessException::class)
fun readField(field: Field?, target: Any?): Any? {
    return field!![target]
}

@Throws(NoSuchFieldException::class, IllegalAccessException::class)
fun Any.writeField(fName: String?, value: Any?) {
    javaClass.writeField(this,fName, value)
}

@Throws(NoSuchFieldException::class, IllegalAccessException::class)
fun Class<*>.writeField(any: Any?, fName: String?, value: Any?) {
    val f = getFieldFromSuperOrInterface(fName)
    writeField(f, any, value)
}

@Throws(IllegalAccessException::class)
fun writeField(field: Field?, target: Any?, value: Any?) {
    field!![target] = value
}

fun Class<*>.getAllFieldsList(): List<Field> {
    val allFields: MutableList<Field> = ArrayList()
    var currentClass: Class<*>? = this
    while (currentClass != null) {
        val declaredFields = currentClass.declaredFields
        for (field: Field in declaredFields) {
            allFields.add(field)
        }
        currentClass = currentClass.superclass
    }
    return allFields
}

fun Field.removeFinalModifier() {
    // From Apache: FieldUtils.removeFinalModifier()
    try {
        if (Modifier.isFinal(this.modifiers)) {
            // Do all JREs implement Field with a private ivar called "modifiers"?
            val modifiersField = Field::class.java.getDeclaredField("modifiers")
            val doForceAccess = !modifiersField.isAccessible
            if (doForceAccess) {
                modifiersField.isAccessible = true
            }
            try {
                modifiersField.setInt(this, this.modifiers and Modifier.FINAL.inv())
            } finally {
                if (doForceAccess) {
                    modifiersField.isAccessible = false
                }
            }
        }
    } catch (ignored: NoSuchFieldException) {
        // The field class contains always a modifiers field
    } catch (ignored: IllegalAccessException) {
        // The modifiers field is made accessible
    }
}

// ----------------
// Method
// ----------------
fun Class<*>.getMethodFromSuperOrInterface(
    methodName: String?,
    vararg parameterTypes: Class<*>?
): Method? {
    // check up the superclass hierarchy
    var acls: Class<*>? = this
    while (acls != null) {
        try {
            val method = acls.getDeclaredMethod(methodName, *parameterTypes)
            // getDeclaredField checks for non-public scopes as well
            // and it returns accurate results
            setAccessible(method, true)
            return method
        } catch (ex: NoSuchMethodException) { // NOPMD
            // ignore
        }
        acls = acls.superclass
    }
    // check the public interface case. This must be manually searched for
    // incase there is a public supersuperclass field hidden by a private/package
    // superclass field.
    var match: Method? = null
    for (class1: Class<*> in this.interfaces) {
        try {
            val test = class1.getMethod(methodName, *parameterTypes)
            Validate.isTrue(
                match == null,
                ("Reference to field %s is ambiguous relative to %s"
                        + "; a matching field exists on two or more implemented interfaces."),
                methodName,
                this
            )
            match = test
        } catch (ex: NoSuchMethodException) { // NOPMD
            // ignore
        }
    }
    return match
}

@Throws(
    NoSuchMethodException::class,
    IllegalAccessException::class,
    InvocationTargetException::class
)
fun Any.invokeMethod(
    methodName: String?,
    methodParamTypes: Array<Class<*>?>,
    vararg args: Any?
): Any? {
    val clz: Class<*> = this.javaClass
    val m = clz.getMethodFromSuperOrInterface(methodName, *methodParamTypes)
    return m?.invoke(args)
}

@Throws(
    ClassNotFoundException::class,
    NoSuchMethodException::class,
    InvocationTargetException::class,
    IllegalAccessException::class
)
fun ClassLoader.invokeMethod(
    clzName: String,
    methodName: String, methodReceiver: Any?,
    methodParamTypes: Array<Class<*>?>, vararg methodParamValues: Any?
): Any? {
    if (methodReceiver == null) {
        return null
    }
    val clz = Class.forName(clzName, false, this)
    val med = clz.getMethod(methodName, *methodParamTypes)
    med.isAccessible = true
    return med.invoke(methodReceiver, *methodParamValues)
}

fun setAccessible(ao: AccessibleObject, value: Boolean) {
    if (ao.isAccessible != value) {
        ao.isAccessible = value
    }
}

// ----------------
// Other
// ----------------
fun Any.dumpObject(fd: FileDescriptor?, writer: PrintWriter, args: Array<String?>?) {
    try {
        var c: Class<*>? = javaClass
        do {
            writer.println("c=" + c!!.name)
            val fields = c.declaredFields
            for (f: Field in fields) {
                val acc = f.isAccessible
                if (!acc) {
                    f.isAccessible = true
                }
                val o = f[this]
                writer.print(f.name)
                writer.print("=")
                if (o != null) {
                    writer.println(o.toString())
                } else {
                    writer.println("null")
                }
                if (!acc) {
                    f.isAccessible = acc
                }
            }
            c = c.superclass
        } while ((c != null) && c != Any::class.java && c != Context::class.java)
    } catch (e: Throwable) {
        //todo log
    }
}