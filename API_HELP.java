package io.github.bumblesoftware.fastload.api.events;

import io.github.bumblesoftware.fastload.events.FLEvents;

/**
 * This is an unregistered example event for people who want to use or build upon this api. Now most of the internal workings
 * are supplied by the javadocs in the api classes, so I won't explain that here. Instead, read that if you need help on it.
 *
 */
public class ExampleEvent {
    /**
     * To start by making a new event, just make a record with your desired params
     */
    public record ExampleRecord(String string) {}

    /**
     * Then make a new field of Generic Event with the type being your record.
     */
    private static final GenericEvent<ExampleRecord> EXAMPLE_EVENT = new GenericEvent<>();

    /**
     * Then you go into the method body of your choice (This works with mixin) and call fireEvent() with a new instance
     * of your custom record in the params. And your event is set up. Now if you scroll down, you can see how to register
     * events to your custom implementation
     */
    static void init() {
        EXAMPLE_EVENT.fireEvent(new ExampleRecord("HELLO THERE!"));
    }

    /**
     * Call this method to register it if you are that type of person.
     */
    public static void initClass() {
        init();
        registerRecursiveEvent();
    }

    /**
     * This is registering a RECURSIVE method, which means that the return type is provided in the params. I'm assuming that
     * you already know how recursion works. But here it's slightly different. Instead of the method directly calling itself,
     * it calls itself upon return with a processed `Object closer`, which is then passed to the next call so that eventually it
     * will reach the guard clause, causing it to return null. Once that happens, the main for() loops will return a void in order to
     * finish the method call. This is done for every instance.
     */
    private static void registerRecursiveEvent() {
        FLEvents.SET_SCREEN_EVENT.registerRecursive((eventContext, abstractParent, closer, eventArgs) -> {
            int i = 1;
            if (closer instanceof Integer integer) {
                i = integer + 1;
            }
            System.out.println(i);
            if (i >= 10) {
                return null;
            } else return eventArgs.recurse(eventContext, abstractParent, i, eventArgs);
        }, 1);

        ExampleEvent.EXAMPLE_EVENT.registerRecursive((eventContext, abstractParent, closer, eventArgs) -> {
            int i = 1;
            if (closer instanceof Integer integer) {
                i = integer + 1;
            }
            System.out.println(i * 10);
            if (i >= 10) {
                return null;
            } else return eventArgs.recurse(eventContext, abstractParent, i, eventArgs);
        }, 1);
    }

    /**
     * A normal event is registered like this, it only gets fired once per event and when its fired is dependent upon the priority.
     * Higher priority means it'll be called earlier. You can even call the recursive/normal method from the same or a new instance 
     * of EventArgs to fire a new implementation and to form highly complex event bodies.
     */
    private static void registerEvent() {
        FLEvents.SET_SCREEN_EVENT.register((eventContext, abstractEvent, eventArgs) -> System.out.println("Hello World"), 1);
        ExampleEvent.EXAMPLE_EVENT.register((eventContext, abstractEvent, eventArgs) -> System.out.println("Custom event!"), 69);
    }
}