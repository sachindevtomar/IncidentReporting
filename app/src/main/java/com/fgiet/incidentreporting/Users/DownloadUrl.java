package com.fgiet.incidentreporting.Users;

/**
 * Created by Admin on 3/30/2018.
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUrl {
    public String readUrl(String myUrl) throws IOException {
        String data="";
        InputStream inputStream=null;
        HttpURLConnection urlConnection=null;
        try {
            URL url=new URL(myUrl);
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.connect();
            Log.d("Finding Error","Got an error1");
            inputStream=urlConnection.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb=new StringBuffer();

            String line="";
            while((line=br.readLine())!=null){
                sb.append(line);
            }
            data=sb.toString();
            Log.d("Finding Error","Got an error2"+data);
            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("Finding Error","Got an error3");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Finding Error","Got an error4");
        }
        finally {
            inputStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
