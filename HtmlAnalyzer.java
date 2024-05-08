import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Stack;

public class HtmlAnalyzer {

    // Get HTML content from URL
    public static String getHTML(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
            content.append("\n");
        }
        reader.close();

        return content.toString();
    }

    public static void main(String[] args) {
        // Page URL
        String urlString = args[0];

        String html = "";
        Stack<String> stack = new Stack<String>();
        int depth = 0;
        int deeperNum = 0;
        String deeperText = "";

        try {
            html = getHTML(urlString);
        } catch (IOException e) {
            System.out.println("URL connection error");
            System.exit(0);
        }

        // change to array
        String[] htmlSplit = html.split("\n");

        for (String line : htmlSplit) {

            // remove left spaces
            line = line.trim();

            if (line.charAt(0) == '<' && line.charAt(line.length() - 1) == '>') {
                
                // closing tag
                if (line.charAt(1) == '/') {

                    String stackText = stack.peek().replace("<", "").replace(">", "");
                    String lineText = line.replace("<", "").replace(">", "").replace("/", "");

                    if (!stackText.equals(lineText)) {
                        System.out.println("malformed HTML");
                        System.exit(0);
                    }

                    depth--;
                    stack.pop();
                    // System.out.println("Closing tag: " + line);
                    
                // opening tag
                } else {
                    depth++;
                    stack.push(line);
                    // System.out.println("Opening tag: " + line);
                }
                // non tag
            } else {
                if (depth == 0) {
                    System.out.println("malformed HTML");
                    System.exit(0);
                    break;
                } else if (depth > deeperNum) {
                    deeperNum = depth;
                    deeperText = line;
                }
            }

        }

        System.out.println(deeperText);
    }
}
