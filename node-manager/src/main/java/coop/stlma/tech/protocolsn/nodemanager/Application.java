package coop.stlma.tech.protocolsn.nodemanager;

import io.micronaut.runtime.Micronaut;

/**
 * Driver class for the node-manager
 *
 * @author John Meyerin
 */
public class Application {

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }
        Micronaut.run(Application.class, args);
    }
}