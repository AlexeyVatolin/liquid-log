package ru.naumen.perfhouse.parser.dataParsers;

import ru.naumen.perfhouse.parser.dataParsers.DataParser;
import ru.naumen.perfhouse.parser.data.ActionDoneData;
import ru.naumen.perfhouse.parser.data.DataSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionDoneParser implements DataParser
{
    private Pattern doneRegEx = Pattern.compile("Done\\((\\d+)\\): ?(.*?Action)");
    private DataSet dataSet;

    public void parseLine(String line)
    {
        ActionDoneData actionDoneData = dataSet.getActionsDone();
        Matcher matcher = doneRegEx.matcher(line);

        if (matcher.find())
        {
            String actionInLowerCase = matcher.group(2).toLowerCase();
            if (actionDoneData.getExcludedActions().contains(actionInLowerCase))
            {
                return;
            }

            actionDoneData.addTimes(Integer.parseInt(matcher.group(1)));
            if (actionInLowerCase.equals("addobjectaction"))
            {
                actionDoneData.incrementAddObjectActions();
            }
            else if (actionInLowerCase.equals("editobjectaction"))
            {
                actionDoneData.incrementEditObjectsActions();
            }
            else if (actionInLowerCase.equals("getcatalogsaction"))
            {
                actionDoneData.incrementGetCatalogsAction();
            }
            else if (actionInLowerCase.matches("(?i)[a-zA-Z]+comment[a-zA-Z]+"))
            {
                actionDoneData.incrementCommentActions();
            }
            else if (!actionInLowerCase.contains("advlist")
                    && actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+List[a-zA-Z]+"))
            {
                actionDoneData.incrementGetListActions();
            }
            else if (actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+Form[a-zA-Z]+"))
            {
                actionDoneData.incrementGetFormActions();
            }
            else if (actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+DtObject[a-zA-Z]+"))
            {
                actionDoneData.incrementGetDtObjectActions();
            }
            else if (actionInLowerCase.matches("(?i)[a-zA-Z]+search[a-zA-Z]+"))
            {
                actionDoneData.incrementSearchActions();
            }

        }
    }

    @Override
    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }
}
