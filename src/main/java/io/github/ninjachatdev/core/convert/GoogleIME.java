package io.github.ninjachatdev.core.convert;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GoogleIME extends URIConverter {

    protected Gson gson = new Gson();

    @Override
    public String convert(String input) {
        String encoded = URLEncoder.encode(input, StandardCharsets.UTF_8);
        URL url = null;
        HttpURLConnection con = null;
        BufferedReader bufReader = null;
        InputStreamReader inReader = null;

        try {
            url = new URL(getURI(encoded));

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setInstanceFollowRedirects(false);
            con.connect();

            inReader = new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8);
            bufReader = new BufferedReader(inReader);

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = bufReader.readLine()) != null)
                sb.append(line);

            return parse(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                con.disconnect();

            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inReader != null) {
                try {
                    inReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    @Override
    public String getURI(String input) {
        return "https://www.google.com/transliterate?langpair=ja-Hira|ja&text=" + input;
    }

    @Override
    public String parse(String input) {
        StringBuilder sb = new StringBuilder();
        for (JsonElement res : gson.fromJson(input, JsonElement.class).getAsJsonArray().get(0).getAsJsonArray().get(1).getAsJsonArray()) {
            sb.append(res.getAsJsonArray().get(1).getAsJsonArray().get(0).getAsString());
        }
        return sb.toString();
    }
}
