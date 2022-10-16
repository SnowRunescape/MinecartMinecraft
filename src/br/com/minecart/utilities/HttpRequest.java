package br.com.minecart.utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import br.com.minecart.Minecart;

public class HttpRequest
{
    public static HttpResponse httpRequest(String Url, Map<String, String> params) throws HttpRequestException
    {
        String urlParameters = null;

        if (params != null) {
            urlParameters = buildPostData(params);
        }

        try {
            URL url = new URL(Url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Minecart");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Authorization", Minecart.instance.MinecartAutorization);
            connection.addRequestProperty("ShopServer", Minecart.instance.MinecartShopServer);
            connection.addRequestProperty("PluginVersion", Minecart.instance.VERSION);

            connection.setUseCaches(false);

            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            connection.setRequestMethod("POST");

            if (urlParameters != null) {
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                connection.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

                writer.write(urlParameters);
                writer.flush();
            }

            InputStream InputStream = (connection.getResponseCode() == HttpURLConnection.HTTP_OK) ?
                connection.getInputStream() :
                connection.getErrorStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(InputStream, "UTF-8"));
            StringBuilder sb = new StringBuilder();

            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            br.close();

            String response = sb.toString().replaceAll("\\s+", " ");

            return new HttpResponse(connection.getResponseCode(), response);
        } catch (Exception e) {
            throw new HttpRequestException(new HttpResponse(500, ""));
        }
    }

    private static String buildPostData(Map<String, String> params)
    {
        StringBuilder postData = new StringBuilder();

        for (Entry<String, String> param : params.entrySet()) {
            try {
                if (postData.length() != 0) {
                    postData.append("&");
                }

                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));

                postData.append("=");
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            } catch(UnsupportedEncodingException e) {
                return null;
            }
        }

        return postData.toString();
    }
}