package ru.naumen;

import org.junit.Assert;
import org.junit.Test;
import ru.naumen.perfhouse.parser.data.Data;
import ru.naumen.perfhouse.parser.data.SdngData;
import ru.naumen.perfhouse.parser.dataset_factory.DataSet;
import ru.naumen.perfhouse.parser.data_parsers.ActionDoneParser;

public class ActionDoneParserTest {

    @Test
    public void mustParseAddAction() {
        //given
        ActionDoneParser parser = new ActionDoneParser();
        SdngData dataSet = new SdngData();

        //when
        parser.parseLine(dataSet, "Done(10): AddObjectAction");

        //then
        Assert.assertEquals(1, dataSet.getActionDoneData().getAddObjectActions());
    }

    @Test
    public void mustParseFormActions() {
        //given
        ActionDoneParser parser = new ActionDoneParser();
        SdngData dataSet = new SdngData();

        //when
        parser.parseLine(dataSet, "Done(10): GetFormAction");
        parser.parseLine(dataSet, "Done(1): GetAddFormContextDataAction");

        //then
        Assert.assertEquals(2, dataSet.getActionDoneData().getFormActions());
    }

    @Test
    public void mustParseEditObject() {
        //given
        ActionDoneParser parser = new ActionDoneParser();
        SdngData dataSet = new SdngData();

        //when
        parser.parseLine(dataSet, "Done(10): EditObjectAction");

        //then
        Assert.assertEquals(1, dataSet.getActionDoneData().getEditObjectsActions());
    }

    @Test
    public void mustParseSearchObject(){
        //given
        ActionDoneParser parser = new ActionDoneParser();
        SdngData dataSet = new SdngData();

        //when
        parser.parseLine(dataSet, "Done(10): GetPossibleAgreementsChildsSearchAction");
        parser.parseLine(dataSet, "Done(10): TreeSearchAction");
        parser.parseLine(dataSet, "Done(10): GetSearchResultAction");
        parser.parseLine(dataSet, "Done(10): GetSimpleSearchResultsAction");
        parser.parseLine(dataSet, "Done(10): SimpleSearchAction");
        parser.parseLine(dataSet, "Done(10): ExtendedSearchByStringAction");
        parser.parseLine(dataSet, "Done(10): ExtendedSearchByFilterAction");

        //then
        Assert.assertEquals(7, dataSet.getActionDoneData().getSearchActions());
    }

    @Test
    public void mustParseGetList(){
        //given:
        ActionDoneParser parser = new ActionDoneParser();
        SdngData dataSet = new SdngData();

        //when:
        parser.parseLine(dataSet, "Done(10): GetDtObjectListAction");
        parser.parseLine(dataSet, "Done(10): GetPossibleCaseListValueAction");
        parser.parseLine(dataSet, "Done(10): GetPossibleAgreementsTreeListActions");
        parser.parseLine(dataSet, "Done(10): GetCountForObjectListAction");
        parser.parseLine(dataSet, "Done(10): GetDataForObjectListAction");
        parser.parseLine(dataSet, "Done(10): GetPossibleAgreementsListActions");
        parser.parseLine(dataSet, "Done(10): GetDtObjectForRelObjListAction");

        //then:
        Assert.assertEquals(7, dataSet.getActionDoneData().geListActions());
    }

    @Test
    public void mustParseComment(){
        //given:
        ActionDoneParser parser = new ActionDoneParser();
        SdngData dataSet = new SdngData();

        //when:
        parser.parseLine(dataSet, "Done(10): EditCommentAction");
        parser.parseLine(dataSet, "Done(10): ChangeResponsibleWithAddCommentAction");
        parser.parseLine(dataSet, "Done(10): ShowMoreCommentAttrsAction");
        parser.parseLine(dataSet, "Done(10): CheckObjectsExceedsCommentsAmountAction");
        parser.parseLine(dataSet, "Done(10): GetAddCommentPermissionAction");
        parser.parseLine(dataSet, "Done(10): GetCommentDtObjectTemplateAction");

        //then:
        Assert.assertEquals(6, dataSet.getActionDoneData().getCommentActions());
    }

    @Test
    public void mustParseDtObject(){
        //given:
        ActionDoneParser parser = new ActionDoneParser();
        SdngData dataSet = new SdngData();

        //when:
        parser.parseLine(dataSet, "Done(10): GetVisibleDtObjectAction");
        parser.parseLine(dataSet, "Done(10): GetDtObjectsAction");
        parser.parseLine(dataSet, "Done(10): GetDtObjectTreeSelectionStateAction");
        parser.parseLine(dataSet, "Done(10): AbstractGetDtObjectTemplateAction");
        parser.parseLine(dataSet, "Done(10): GetDtObjectTemplateAction");

        //then:
        Assert.assertEquals(5, dataSet.getActionDoneData().getDtObjectActions());
    }
}
