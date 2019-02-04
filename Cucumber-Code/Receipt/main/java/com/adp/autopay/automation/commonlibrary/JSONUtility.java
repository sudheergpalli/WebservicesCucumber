package com.adp.autopay.automation.commonlibrary;

import com.adp.autopay.automation.utility.ExecutionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtility {

    public static JSONCompareResult assertEquals(String expectedString, String actualString, AbstractComparator comparator, ExecutionContext context) throws JSONException{
        Object expected=JSONParser.parseJSON(expectedString);
        Object actual=JSONParser.parseJSON(actualString);
        JSONCompareResult result = new JSONCompareResult();

        if ((expected instanceof JSONObject) && (actual instanceof JSONObject)) {
            return comparator.compareJSON((JSONObject)expected, (JSONObject)actual, result,context);
        }
        else if ((expected instanceof JSONArray) && (actual instanceof JSONArray)) {
            return comparator.compareJSON((JSONArray)expected, (JSONArray)actual, result,context);
        }
        else if (expected instanceof JSONObject) {
            return new JSONCompareResult().fail("", expected, actual,context);
        }
        else {
            return new JSONCompareResult().fail("", expected, actual,context);
        }

    }

}
