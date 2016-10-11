package Controller;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class Send {
    static String sendPost(String[] request, String data) throws Exception {
        Socket socket = new Socket(InetAddress.getByName("javarush.ru"), 80); //

        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));

        for (int i = 0; i < request.length; i++) {
            wr.write(request[i]);

        }

        wr.write("\r\n");


        wr.write(data);

        wr.flush();

        InputStream rd = (socket.getInputStream());

        byte[] buffer = new byte[10000];

        int i = rd.read(buffer);
        StringBuilder result = new StringBuilder();

        for (int j = 0; j < i; j++) result.append((char) buffer[j]);

        wr.close();
        rd.close();

        return result.toString();
    }

    public static String GET(String url) {
        try {
            URL urls = new URL(url);
            BufferedReader s = new BufferedReader(new InputStreamReader(urls.openStream()));
            return s.readLine();
        } catch (IOException ex) {
            return "false";
        }
    }


    static String sendGet(String output) {
        try (Socket socket = new Socket(InetAddress.getByName("javarush.ru"), 80)) {

            socket.getOutputStream().write((output).getBytes(StandardCharsets.US_ASCII));
            socket.getOutputStream().flush();

            InputStream is = socket.getInputStream();
            byte[] buffer = new byte[8192];

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int received = is.read(buffer);
            baos.write(buffer, 0, received);

            return baos.toString("UTF-8");
        } catch (Exception e) {
            return "null";
        }
    }
}
