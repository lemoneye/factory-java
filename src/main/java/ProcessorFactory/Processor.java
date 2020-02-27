package ProcessorFactory;

import java.util.HashMap;

import java.io.IOException;

    // Main Processor Class
public class Processor {
    // Used to return Processor
    public static String makeProcessor(String request, HashMap<String, String> map) throws IOException {
        switch (request) {
            case "/users": {
                return new UserProcessor(map).tempString;
            }
            case "/posts": {
                return new PostProcessor(map).tempString;
            }
            default:
                throw new IllegalStateException("unexpected value: " + request);

        }


    }


}


