package com.adp.autopay.automation.commonlibrary;

import com.adp.autopay.automation.utility.ExecutionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * This class provides a skeletal implementation of the {@link JSONComparator}
 * interface, to minimize the effort required to implement this interface. <p/>
 */
public  class AbstractComparator  {

    /**
     * Compares JSONObject provided to the expected JSONObject, and returns the results of the comparison.
     *
     * @param expected Expected JSONObject
     * @param actual   JSONObject to compare
     * @throws JSONException
     */

    public final JSONCompareResult compareJSON(JSONObject expected, JSONObject actual, JSONCompareResult result,ExecutionContext context) throws JSONException {
//        JSONCompareResult result = new JSONCompareResult();
        compareJSON("", expected, actual, result,context);
        return result;
    }

    public final JSONCompareResult compareJSON(JSONArray expected, JSONArray actual, JSONCompareResult result,ExecutionContext context) throws JSONException {
//        JSONCompareResult result = new JSONCompareResult();
        compareJSONArray("", expected, actual, result,context);
        return result;
    }

    protected void checkJsonObjectKeysActualInExpected(String prefix, JSONObject expected, JSONObject actual, JSONCompareResult result) {
        Set<String> actualKeys = getKeys(actual);
        for (String key : actualKeys) {
            if (!expected.has(key)) {
                result.unexpected(prefix, key);
            }
        }
    }

    public void compareJSON(String prefix, JSONObject expected, JSONObject actual, JSONCompareResult result,ExecutionContext context)
            throws JSONException {
        // Check that actual contains all the expected values
        checkJsonObjectKeysExpectedInActual(prefix, expected, actual, result,context);


    }

    public void compareJSONArray(String prefix, JSONArray expected, JSONArray actual, JSONCompareResult result,ExecutionContext context)
            throws JSONException {
        if (expected.length() != actual.length()) {
            result.fail(prefix + "[]: Expected " + expected.length() + " values but got " + actual.length());
            return;
        } else if (expected.length() == 0) {
            return; // Nothing to compare
        }

        if (JSONCompareUtil.allSimpleValues(expected)) {
            compareJSONArrayOfSimpleValues(prefix, expected, actual, result);
        } else if (JSONCompareUtil.allJSONObjects(expected)) {
            compareJSONArrayOfJsonObjects(prefix, expected, actual, result,context);
        } else {
            // An expensive last resort
            recursivelyCompareJSONArray(prefix, expected, actual, result,context);
        }
    }

    protected void compareJSONArrayOfJsonObjects(String key, JSONArray expected, JSONArray actual, JSONCompareResult result,ExecutionContext context) throws JSONException {
        String uniqueKey = JSONCompareUtil.findUniqueKey(expected);
        if (uniqueKey == null || !JSONCompareUtil.isUsableAsUniqueKey(uniqueKey, actual)) {
            // An expensive last resort
            recursivelyCompareJSONArray(key, expected, actual, result,context);
            return;
        }
        Map<Object, JSONObject> expectedValueMap = JSONCompareUtil.arrayOfJsonObjectToMap(expected, uniqueKey);
        Map<Object, JSONObject> actualValueMap = JSONCompareUtil.arrayOfJsonObjectToMap(actual, uniqueKey);
        for (Object id : expectedValueMap.keySet()) {
            if (!actualValueMap.containsKey(id)) {
                result.missing(JSONCompareUtil.formatUniqueKey(key, uniqueKey, id), expectedValueMap.get(id));
                continue;
            }
            JSONObject expectedValue = expectedValueMap.get(id);
            JSONObject actualValue = actualValueMap.get(id);
            compareValues(JSONCompareUtil.formatUniqueKey(key, uniqueKey, id), expectedValue, actualValue, result,context);
        }
        for (Object id : actualValueMap.keySet()) {
            if (!expectedValueMap.containsKey(id)) {
                result.unexpected(JSONCompareUtil.formatUniqueKey(key, uniqueKey, id), actualValueMap.get(id));
            }
        }
    }

    protected void compareJSONArrayOfSimpleValues(String key, JSONArray expected, JSONArray actual, JSONCompareResult result) throws JSONException {
        Map<Object, Integer> expectedCount = JSONCompareUtil.getCardinalityMap(JSONCompareUtil.jsonArrayToList(expected));
        Map<Object, Integer> actualCount = JSONCompareUtil.getCardinalityMap(JSONCompareUtil.jsonArrayToList(actual));
        for (Object o : expectedCount.keySet()) {
            if (!actualCount.containsKey(o)) {
                result.missing(key + "[]", o);
            } else if (!actualCount.get(o).equals(expectedCount.get(o))) {
                result.fail(key + "[]: Expected " + expectedCount.get(o) + " occurrence(s) of " + o
                        + " but got " + actualCount.get(o) + " occurrence(s)");
            }
        }
        for (Object o : actualCount.keySet()) {
            if (!expectedCount.containsKey(o)) {
                result.unexpected(key + "[]", o);
            }
        }
    }

    protected void checkJsonObjectKeysExpectedInActual(String prefix, JSONObject expected, JSONObject actual, JSONCompareResult result,ExecutionContext context) throws JSONException {
        Set<String> expectedKeys = getKeys(expected);
        for (String key : expectedKeys) {
            Object expectedValue = expected.get(key);
            if (actual.has(key)) {
                Object actualValue = actual.get(key);
                compareValues(qualify(prefix, key), expectedValue, actualValue, result,context);
            } else {
                result.missing(prefix, key);
            }
        }
    }

    public void compareValues(String prefix, Object expectedValue, Object actualValue, JSONCompareResult result, ExecutionContext context)
            throws JSONException {
        if (expectedValue instanceof Number && actualValue instanceof Number) {
            if (((Number)expectedValue).doubleValue() != ((Number)actualValue).doubleValue()) {
                result.fail(prefix, expectedValue, actualValue,context);
            }
        } else if (expectedValue.getClass().isAssignableFrom(actualValue.getClass())) {
            if (expectedValue instanceof JSONArray) {
                compareJSONArray(prefix, (JSONArray) expectedValue, (JSONArray) actualValue, result,context);
            } else if (expectedValue instanceof JSONObject) {
                compareJSON(prefix, (JSONObject) expectedValue, (JSONObject) actualValue, result,context);
            } else if (!expectedValue.equals(actualValue)) {
                result.fail(prefix, expectedValue, actualValue,context);
            }
        } else {
            result.fail(prefix, expectedValue, actualValue,context);
        }
    }

    public static String qualify(String prefix, String key) {
        return "".equals(prefix) ? key : prefix + "." + key;
    }

    private Set<String> getKeys(JSONObject expected) {

        Set<String> keys = new TreeSet<String>();
        Iterator<?> iter = expected.keys();
        while(iter.hasNext()) {
            keys.add((String)iter.next());
        }
        return keys;

    }

    // easy way to uniquely identify each element.
    protected void recursivelyCompareJSONArray(String key, JSONArray expected, JSONArray actual,
                                               JSONCompareResult result,ExecutionContext context) throws JSONException {
        Set<Integer> matched = new HashSet<Integer>();
        for (int i = 0; i < expected.length(); ++i) {
            Object expectedElement = expected.get(i);
            boolean matchFound = false;
            for (int j = 0; j < actual.length(); ++j) {
                Object actualElement = actual.get(j);
                if (matched.contains(j) || !actualElement.getClass().equals(expectedElement.getClass())) {
                    continue;
                }
                if (expectedElement instanceof JSONObject) {
                    if (compareJSON((JSONObject) expectedElement, (JSONObject) actualElement, result,context).passed()) {
                        matched.add(j);
                        matchFound = true;
                        break;
                    }
                } else if (expectedElement instanceof JSONArray) {
                    if (compareJSON((JSONArray) expectedElement, (JSONArray) actualElement, result,context).passed()) {
                        matched.add(j);
                        matchFound = true;
                        break;
                    }
                } else if (expectedElement.equals(actualElement)) {
                    matched.add(j);
                    matchFound = true;
                    break;
                }
            }

           /* if (!matchFound) {
                result.fail(key + "[" + i + "] Could not find match for element " + expectedElement);
                return;
            }*/
        }
    }

}

