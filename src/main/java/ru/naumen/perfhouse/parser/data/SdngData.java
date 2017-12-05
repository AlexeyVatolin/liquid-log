package ru.naumen.perfhouse.parser.data;

public class SdngData extends Data {
    private ActionDoneData actionDoneData;
    private ErrorData errorData;

    public SdngData() {
        this.actionDoneData = new ActionDoneData();
        this.errorData = new ErrorData();
    }

    public ActionDoneData getActionDoneData() {
        return actionDoneData;
    }

    public ErrorData getErrorData() {
        return errorData;
    }
}
