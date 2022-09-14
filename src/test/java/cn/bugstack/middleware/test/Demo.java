package cn.bugstack.middleware.test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.function.Consumer;

/**
 * @author 徐明龙 XuMingLong 2022-02-17
 */
public class Demo {
    public static void main(String[] args) throws Exception {
        Consumer<String> c1 = (Consumer<String> & Serializable)s -> System.out.println(s); // non-capturing lambda
        Consumer<String> c2 = (Consumer<String> & Serializable) System.out::println;        // instance method reference
        PrintStream sysout = System.out; // PrintStream doesn't implement Serializable
        Consumer<String> c3 = (Consumer<String> & Serializable) s -> sysout.println(s);     // capturing lambda

        // all print true because VoidFunction extends Serializable
        System.out.println(c1 instanceof Serializable);
        System.out.println(c2 instanceof Serializable);
        System.out.println(c3 instanceof Serializable);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // try serializing the non-capturing lambda
        boolean success = false;
        try {
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(c1); // succeed
            success = true;
        } catch (Exception e) {
            System.out.println("c1 serialization failed");
            e.printStackTrace(System.out);
        }
        if (success) System.out.println("c1 serialization succeeded");

        // try serializing the instance method reference
        success = false;
        try {
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(c2); // fail
            success = true;
        } catch (Exception e) {
            System.out.println("c2 serialization failed");
            e.printStackTrace(System.out);
        }
        if (success) System.out.println("c2 serialization succeeded");

        // try serializing the capturing lambda
        success = false;
        try {
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(c3); // fail
            success = true;
        } catch (Exception e) {
            System.out.println("c3 serialization failed");
            e.printStackTrace(System.out);
        }
        if (success) System.out.println("c3 serialization succeeded");
    }
}
