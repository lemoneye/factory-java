package Server;


import DTO.PostDto;
import ProcessorFactory.Processor;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.HashMap;
import com.google.gson.*;
import java.util.Arrays;

public class SimpleServer {

  public static void main(String[] args) throws IOException {
    ServerSocket ding;
    Socket dong = null;
    String myProcessorResult = null;
    String mainEndPoint = null;
    String date_str = null;

    try {
      ding = new ServerSocket(1299);
      System.out.println("Opened socket " + 1299);
      while (true) {
        // keeps listening for new clients, one at a time
        try {
          dong = ding.accept(); // waits for client here
        } catch (IOException e) {
          System.out.println("Error opening socket");
          System.exit(1);
        }

        InputStream stream = dong.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        try {
          // read the first line to get the request method, URI and HTTP version
          String line = in.readLine();
          System.out.println("----------REQUEST START---------");
          System.out.println(line);

          // START HERE
          // Endpoint of url
          String endpoint_str = line.split("\\s+")[1];
          mainEndPoint = endpoint_str;
          System.out.println(endpoint_str);

          //Time request
          LocalDateTime date = LocalDateTime.now();
          date_str = date.toString();
          System.out.println(date_str);

          // Endpoint arguments
          String arg_str = new String();
          try {
            arg_str = endpoint_str.split("\\?")[1];
            System.out.println(arg_str);
          }
          catch (ArrayIndexOutOfBoundsException e) {
//            System.exit(1);
//            break;
          }

          // Declare Hashmap
          HashMap<String, String> map = new HashMap<>();

          // Insert the endpoint in the argument as the key and value respectively
          try {
            //map.put("date", date_str);  // Generate date in response instead
            map.put(arg_str.split("\\=")[0], arg_str.split("\\=")[1]);
            System.out.println(map);
          }
          catch (ArrayIndexOutOfBoundsException e) {

            }

          // Call Processor
          ArrayList<PostDto> responseList = new ArrayList<>();
          try {
            System.out.println("Processor Called! ");
            myProcessorResult = Processor.makeProcessor(endpoint_str.split("\\?")[0], map);
          }
          catch (IllegalStateException e) {

          }

          // END HERE


          // read only headers
          line = in.readLine();
          while (line != null && line.trim().length() > 0) {
            int index = line.indexOf(": ");
            if (index > 0) {
              System.out.println(line);
            } else {
              break;
            }
            line = in.readLine();
          }
          System.out.println("----------REQUEST END---------\n\n");
        } catch (IOException e) {
          System.out.println("Error reading");
          System.exit(1);
        }

        BufferedOutputStream out = new BufferedOutputStream(dong.getOutputStream());
        PrintWriter writer = new PrintWriter(out, true);  // char output to the client

        // every response will always have the status-line, date, and server name
        writer.println("HTTP/1.1 200 OK");
        writer.println("Server: TEST");
        writer.println("Connection: close");
        writer.println("Content-type: text/html");
        writer.println("");
        String[] lines = myProcessorResult.split("\\r?\\n");  // Split by newline
        System.out.println("START HERE");
        for (int i = 0; i < lines.length - 2; i++) {
          // Cycle through chunks here
          System.out.println(lines[i]);
        }
        System.out.println("LAST ONE IS THE ARGUMENT STRING: " + lines[lines.length - 1]);  //Strip of the curly braces from beginning and end

        // If there are zero elements,
        HashMap<String, String> ResponseMap4 = new HashMap<>();
        if ((lines.length - 2) == 0) {
          //writer.println("bones");
          ResponseMap4.put("data", "{}");
          ResponseMap4.put("params", lines[lines.length - 1].replace("=", " : "));
          ResponseMap4.put("responseCode", "ERROR");
          ResponseMap4.put("response", "{}");
          System.out.println("RESPONSEMAP4: " + ResponseMap4);

        }

        // If there is only one element, return just the JSON element
        JsonObject o2 = new JsonObject();
        if ((lines.length - 2) == 1) {
          for (int i = 0; i < lines.length - 2; i++) {
            // Cycle through chunks here and add to JSONarray
            String[] lineData = lines[i].split("@");
            System.out.println("LINEDATA: " + Arrays.toString(lineData));
            for (String data : lineData) {
              String argument = data.split(":")[0];
              String value = data.split(":")[1];
              o2.addProperty(argument, value);
            }
          }
        }

        HashMap<String, String> ResponseMap3 = new HashMap<>();
        ResponseMap3.put("data", date_str);
        ResponseMap3.put("params", lines[lines.length - 1].replace("=", " : "));
        ResponseMap3.put("responseCode", "OK");
        ResponseMap3.put("response", o2.toString());
        System.out.println("RESPONSEMAP3: " + ResponseMap3);


        // If there is more than one element, return JSON array of responses
        JsonArray jArray = new JsonArray();
        if ((lines.length - 2) > 1) {
          for (int i = 0; i < lines.length - 2; i++) {
            // Cycle through chunks here and add to JSONarray
            String[] lineData = lines[i].split("@");
            System.out.println("LINEDATA: " + Arrays.toString(lineData));
            for (String data : lineData) {
              String argument = data.split(":")[0];
              String value = data.split(":")[1];
              // Create JSONElement here
              JsonObject o = new JsonObject();
              o.addProperty(argument, value);
              // Add to jArray here
              jArray.add(o);
            }
          }
        }
        System.out.println("JSONARRAY: " + jArray);
        System.out.println("JSONARRAY: " + jArray.toString());
        String tempJArray = jArray.toString();

        // Load up ResponseMap2 with its data
        HashMap<String, String> ResponseMap2 = new HashMap<>();
        ResponseMap2.put("data", date_str);
        ResponseMap2.put("params", lines[lines.length - 1].replace("=", " : "));
        ResponseMap2.put("response", tempJArray);
        ResponseMap2.put("responseCode", "OK");
        System.out.println("RESPONSEMAP2: " + ResponseMap2);

        // If there is zero chunks of data, print Responsemap4
        if ((lines.length - 2) == 0) {
          writer.println(ResponseMap4);
        }
        // If there is one chunk, print Responsemap3
        else if ((lines.length - 2) == 1) {
          writer.println(ResponseMap3);
        }
        // If there is multiple chunks of data, print Responsemap2
        else if ((lines.length - 2) > 1) {
          writer.println(ResponseMap2);
        }

        dong.close();
      }
    } catch (IOException e) {
      System.out.println("Error opening socket");
      System.exit(1);
    }
  }
}