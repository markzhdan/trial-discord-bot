package Utility;

import Main.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class APIConnection
{
    public static void Connect()
    {
        String inline = "";

        try
        {
            URL url = new URL("https://api.mcsrvstat.us/2/209.182.109.6:25565");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();

            if(responsecode != 200)
                throw new RuntimeException("HttpResponseCode: " +responsecode);
            else
            {
                //Scanner functionality will read the JSON data from the stream
                Scanner sc = new Scanner(url.openStream());
                while(sc.hasNext())
                {
                    inline+=sc.nextLine();
                }
                //Close the stream when reading the data has been finished
                sc.close();
            }


            JSONParser parse = new JSONParser();

            JSONObject jobj = (JSONObject)parse.parse(inline);

            JSONObject players = (JSONObject) jobj.get("players");
            JSONArray playerArray = (JSONArray) players.get("list");

            /*Main.getOnlinePlayers().clear();
            for(int i = 0; i < playerArray.size(); i++)
            {
                Main.addOnlinePlayers((String) playerArray.get(i));
            }*/

            conn.disconnect();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
