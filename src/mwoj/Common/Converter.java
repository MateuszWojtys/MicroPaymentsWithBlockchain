package mwoj.Common;

import org.json.JSONObject;

public class Converter {

    public static byte[] getBytesFromJSONObject(JSONObject json)
    {
        return json.toString().getBytes();
    }

    public static JSONObject getJSONObjectFromBytes(byte[] data)
    {
        String jsonString = new String(data);
        JSONObject json = new JSONObject(jsonString);
        return json;
    }
}
