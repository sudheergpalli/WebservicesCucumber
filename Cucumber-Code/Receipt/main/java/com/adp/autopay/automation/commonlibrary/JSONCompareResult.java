package com.adp.autopay.automation.commonlibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.adp.autopay.automation.utility.ExecutionContext;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Bean for holding results from JSONCompare.
 */
public class JSONCompareResult {
    private boolean _success;
    private StringBuilder _message;
    private String _field;
    private Object _expected;
    private Object _actual;
    private final List<FieldComparisonFailure> _fieldFailures = new ArrayList<FieldComparisonFailure>();
    private final List<FieldComparisonFailure> _fieldMissing = new ArrayList<FieldComparisonFailure>();
    private final List<FieldComparisonFailure> _fieldUnexpected = new ArrayList<FieldComparisonFailure>();
    ExecutionContext context;
    /**
     * Default constructor.
     */
    public JSONCompareResult() {
        this(true, null);
    }

    private JSONCompareResult(boolean success, String message) {
        _success = success;
        _message = new StringBuilder(message == null ? "" : message);
    }

    /**
     * Did the comparison pass?
     * @return True if it passed
     */
    public boolean passed() {
        return _success;
    }

    /**
     * Did the comparison fail?
     * @return True if it failed
     */
    public boolean failed() {
        return !_success;
    }

    /**
     * Result message
     * @return String explaining why if the comparison failed
     */
    public String getMessage() {
        return _message.toString();
    }

    /**
     * Get the list of failures on field comparisons
     */
    public List<FieldComparisonFailure> getFieldFailures() {
        return Collections.unmodifiableList(_fieldFailures);
    }

    /**
     * Get the list of missed on field comparisons
     */
    public List<FieldComparisonFailure> getFieldMissing() {
        return Collections.unmodifiableList(_fieldMissing);
    }

    /**
     * Get the list of failures on field comparisons
     */
    public List<FieldComparisonFailure> getFieldUnexpected() {
        return Collections.unmodifiableList(_fieldUnexpected);
    }

    /**
     * Actual field value
     *
     * @return a {@code JSONObject}, {@code JSONArray} or other {@code Object}
     *         instance, or {@code null} if the comparison did not fail on a
     *         particular field
     * @deprecated Superseded by {@link #getFieldFailures()}
     */
    @Deprecated
    public Object getActual() {
        return _actual;
    }

    /**
     * Expected field value
     *
     * @return a {@code JSONObject}, {@code JSONArray} or other {@code Object}
     *         instance, or {@code null} if the comparison did not fail on a
     *         particular field
     * @deprecated Superseded by {@link #getFieldFailures()}
     */
    @Deprecated
    public Object getExpected() {
        return _expected;
    }

    /**
     * Check if comparison failed on any particular fields
     */
    public boolean isFailureOnField() {
        return !_fieldFailures.isEmpty();
    }

    /**
     * Check if comparison failed with missing on any particular fields
     */
    public boolean isMissingOnField() {
        return !_fieldMissing.isEmpty();
    }

    /**
     * Check if comparison failed with unexpected on any particular fields
     */
    public boolean isUnexpectedOnField() {
        return !_fieldUnexpected.isEmpty();
    }

    /**
     * Dot-separated path the the field that failed comparison
     *
     * @return a {@code String} instance, or {@code null} if the comparison did
     *         not fail on a particular field
     * @deprecated Superseded by {@link #getFieldFailures()}
     */
    @Deprecated
    public String getField() {
        return _field;
    }

    public void fail(String message) {
        _success = false;
        if (_message.length() == 0) {
            _message.append(message);
        } else {
            _message.append(" ; ").append(message);
        }
    }

    /**
     * Identify that the comparison failed
     * @param field Which field failed
     * @param expected Expected result
     * @param actual Actual result
     */

    /* Working version of JSONCompareResult functione below :

           this._field = field;
        this._expected = expected;
        this._actual = actual;
      /*
        if(!field.equalsIgnoreCase("data.output.generalDeductionConfiguration.itemID")
                && !field.equalsIgnoreCase("data.output.generalDeductionConfiguration.deductionCode.codeValue")
                && !field.equalsIgnoreCase("data.eventContext.generalDeductionConfiguration.itemID")) {
            _fieldFailures.add(new FieldComparisonFailure(field, expected, actual));
            fail(formatFailureMessage(field, expected, actual));
        }
        */
//    _fieldFailures.add(new FieldComparisonFailure(field, expected, actual));
//    fail(formatFailureMessage(field, expected, actual));
//    return this;


    public JSONCompareResult fail(String field, Object expected, Object actual,ExecutionContext context) {

        this._field = field;
        this._expected = expected;
        this._actual = actual;

//        List<String> ignoreTags = new ArrayList<String>();
//        ignoreTags.add("data.output.generalDeductionConfiguration.itemID");
//        ignoreTags.add("data.output.generalDeductionConfiguration.deductionCode.codeValue");
//        ignoreTags.add("data.eventContext.generalDeductionConfiguration.itemID");

        if(context.ignoreTags.contains(field))
        {
           // do nothing
           // System.out.println("**************** I am here **********************");
        }
        else
        {
            _fieldFailures.add(new FieldComparisonFailure(field, expected, actual));
             fail(formatFailureMessage(field, expected, actual));
        }

        return this;
    }

    /**
     * Identify that the comparison failed
     * @param field Which field failed
     * @param exception exception containing details of match failure
     */
    public JSONCompareResult fail(String field, ValueMatcherException exception,ExecutionContext context) {
        fail(field + ": " + exception.getMessage(), exception.getExpected(), exception.getActual() , context);
        return this;
    }

    public String formatFailureMessage(String field, Object expected, Object actual) {

            return "\n" + field
                    + "\nExpected: "
                    + describe(expected)
                    + "\n  Actual: "
                    + describe(actual)
                    + "\n";

    }

    public JSONCompareResult missing(String field, Object expected) {
        _fieldMissing.add(new FieldComparisonFailure(field, expected, null));
        fail(formatMissing(field, expected));
        return this;
    }

    private String formatMissing(String field, Object expected) {
        return field
                + "\nExpected: "
                + describe(expected)
                + "\n     but none found\n";
    }

    public JSONCompareResult unexpected(String field, Object value) {
        _fieldUnexpected.add(new FieldComparisonFailure(field, null, value));
        fail(formatUnexpected(field, value));
        return this;
    }

    private String formatUnexpected(String field, Object value) {
        return field
                + "\nUnexpected: "
                + describe(value)
                + "\n";
    }

    private static String describe(Object value) {
        if (value instanceof JSONArray) {
            return "a JSON array";
        } else if (value instanceof JSONObject) {
            return "a JSON object";
        } else {
            return value.toString();
        }
    }

    @Override
    public String toString() {
        return _message.toString();
    }
}

