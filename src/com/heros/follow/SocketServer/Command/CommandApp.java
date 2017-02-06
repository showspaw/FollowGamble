package com.heros.follow.SocketServer.Command;

import java.lang.reflect.Constructor;

import static jdk.nashorn.internal.runtime.PrototypeObject.getConstructor;

/**
 * Created by Show on 2017/1/26.
 */
public class CommandApp {
    public CommandApp() {
    }

    private String name;

    public CommandApp(String name) {
        this.name = name;
    }
    public String to() {
        return name;
    }
    public static CommandApp get() {
        CommandApp app = null;
        try {
            System.out.println();
            System.out.println( CommandApp.class.getClassLoader());
            String x = CommandApp.class.getClass().getPackage().getName()+".CommandApp";
            Class aClass = Class.forName(x);
            Constructor<CommandApp> constructor = aClass.getConstructor(String.class);
            app= constructor.newInstance(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return app;
    }
    public static void main(String[] args) {
//        CommandLoader commandLoader = new CommandLoader();
//        commandLoader.setRequest("720");
//        commandLoader.call();
//        commandLoader.setRequest("710");
//        commandLoader.call();
//        commandLoader.setRequest("7200");
//        commandLoader.call();
//

        System.out.println(CommandApp.get().to());

    }

}
