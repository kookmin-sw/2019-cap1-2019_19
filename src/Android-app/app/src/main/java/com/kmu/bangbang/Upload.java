package com.kmu.bangbang;


import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Upload {
    public static final String UPLOAD_URL= "http://52.78.219.61/test_video/upload.php";

    private int serverResponseCode;
    String a ;
    public String uploadVideo(String file) {

        String fileName = file.substring(file.lastIndexOf("/"));
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        File sourceFile = new File(file);
        if (!sourceFile.isFile()) {
            Log.e("Huzza", "Source File Does not exist");
            return null;
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(UPLOAD_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("myFile", fileName);
            Log.i("방방", "파일 이름 : " + fileName);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            //dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\"" + fileName + "\"" + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\"");
            dos.writeBytes(URLEncoder.encode(fileName,"utf-8"));
            dos.writeBytes("\"" + lineEnd);

            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            Log.i("방방", "Initial .available : " + bytesAvailable);

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                Log.i("방방", "바이트 리드 : " + bytesRead);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            Log.i("방방", "바이트 리드2 : " + bytesRead);
            dos.writeBytes(lineEnd);//post 데이타를 업로드 .
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            serverResponseCode = conn.getResponseCode();

            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (serverResponseCode == 200) {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                        .getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
            } catch (IOException ioex) {
            }
            return sb.toString();
        }else {
            a=""+serverResponseCode;
            return a;
        }
    }
}
