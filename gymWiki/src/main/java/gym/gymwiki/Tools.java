package gym.gymwiki;

import jakarta.servlet.ServletContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Tools {
    public static String getLayout(String file, ServletContext context) throws IOException {
        StringBuilder out = new StringBuilder();
        String text = "";
        InputStream is = context.getResourceAsStream("/" + file);

        if (is != null) {
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);
            while ((text = reader.readLine()) != null) {
                out.append(text).append("\n");
            }
        } else {
            out.append("File is missing: ").append(file);
        }
        return out.toString();
    }

    public static String fill(String layout, String marker, String file, ServletContext context) throws IOException {
        StringBuilder out = new StringBuilder();
        String text = "";
        InputStream is = context.getResourceAsStream("/" + file);

        if(is != null){
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);
            while ((text = reader.readLine()) != null) {
                out.append(text).append("\n");
            }
        } else {
            out.append("File is missing: ").append(file);
        }
        return layout.replace("[["+marker+"]]", out.toString());
    }
}
