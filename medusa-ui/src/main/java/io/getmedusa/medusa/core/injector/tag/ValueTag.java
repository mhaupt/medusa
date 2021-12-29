package io.getmedusa.medusa.core.injector.tag;

import io.getmedusa.medusa.core.injector.tag.meta.InjectionResult;
import io.getmedusa.medusa.core.registry.EachValueRegistry;
import io.getmedusa.medusa.core.util.ExpressionEval;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;
import java.util.Map;

public class ValueTag extends AbstractTag {

    protected static final List<String> M_ATTR_TO_REPLACE = List.of(
        TagConstants.M_CLICK,
        TagConstants.M_CHANGE,
        TagConstants.M_ONENTER
    );

    @Override
    public InjectionResult inject(InjectionResult result, Map<String, Object> variables, ServerRequest request) {
        handleMTextTag(result, variables, request);
        handleMValueAttribute(result, variables, request);
        handleMIfCondition(result, variables, request);

        for(String tagAttr : M_ATTR_TO_REPLACE) {
            handleMAttr(result, variables, request, tagAttr);
        }

        return result;
    }

    private void handleMAttr(InjectionResult result, Map<String, Object> variables, ServerRequest request, String attr) {
        Elements mClickTags = result.getDocument().getElementsByAttribute(attr);

        for (Element mClickTag : mClickTags) {
            String item = mClickTag.attr(attr).trim();
            for(String parameterRough : splitParameterString(item)) {
                final String parameter = parameterRough.trim();

                Object variableValue = getPossibleEachValue(mClickTag, parameter, request);
                if(null == variableValue) variableValue = variableToString(parameter, variables);
                if(null == variableValue) variableValue = parameter;

                final String replacementValue = variableValue.toString().trim();
                if(!replacementValue.equals(parameter.trim())) {
                    item = item.replace(parameter.trim(), "'" + replacementValue+ "'");
                }
            }
           mClickTag.attr(attr, item);
        }
    }

    private String[] splitParameterString(String item) {
        String parametersAsOneString = item.substring(item.indexOf('(') + 1, item.indexOf(')'));
        return parametersAsOneString.split(",");
    }

    private void handleMIfCondition(InjectionResult result, Map<String, Object> variables, ServerRequest request) {
        Elements mIfTags = result.getDocument().getElementsByTag(TagConstants.CONDITIONAL_TAG);
        Elements mElseIfTags = result.getDocument().getElementsByTag(TagConstants.M_ELSEIF);
        mIfTags.addAll(mElseIfTags);

        for (Element mIfTag : mIfTags) {
            final String item = mIfTag.attr(TagConstants.CONDITIONAL_TAG_CONDITION_ATTR).trim();
            Object variableValue = getPossibleEachValue(mIfTag, item, request);
            if(null != variableValue) mIfTag.attr(TagConstants.CONDITIONAL_TAG_CONDITION_ATTR, "'" + variableValue + "'");
        }
    }

    private void handleMTextTag(InjectionResult result, Map<String, Object> variables, ServerRequest request) {
        //<m:text item="counter-value" /> w/ "counter-value" = 123
        //becomes
        //<span from-value="counter-value">123</span>

        Elements mTextTags = result.getDocument().getElementsByTag(TagConstants.TEXT_TAG);
        for (Element mTextTag : mTextTags) {
            final String item = mTextTag.attr(TagConstants.TEXT_TAG_ITEM_ATTR).trim();
            Object variableValue = getPossibleEachValue(mTextTag, item, request);
            if(null == variableValue) variableValue = variableToString(item, variables);
            if(null == variableValue) variableValue = item;
            mTextTag.replaceWith(createSpan(item, variableValue.toString()));
        }
    }

    private Object getPossibleEachValue(Element currentElem, String eachName, ServerRequest request) {
        String nameToSearch = eachName;
        String restOfValue = null;
        boolean requiresObjectIntrospection = nameToSearch.contains(".");
        if(requiresObjectIntrospection) {
            final String[] split = nameToSearch.split("\\.", 2);
            nameToSearch = split[0];
            restOfValue = split[1];
        }

        Element parentWithEachName = findParentWithEachName(currentElem.parents(), nameToSearch);
        if(null != parentWithEachName) {
            int index = Integer.parseInt(parentWithEachName.attr(TagConstants.INDEX));
            Object valueToReturn = EachValueRegistry.getInstance().get(request, nameToSearch, index);
            if(requiresObjectIntrospection) {
                valueToReturn = ExpressionEval.evalObject(restOfValue, valueToReturn);
            }
            return valueToReturn;
        }
        return null;
    }

    private Element findParentWithEachName(Elements parents, String eachName) {
        for(Element parent : parents) {
            if(parent.hasAttr(TagConstants.M_EACH) && parent.attr(TagConstants.M_EACH).equals(eachName)) return parent;
        }
        return null;
    }

    private void handleMValueAttribute(InjectionResult result, Map<String, Object> variables, ServerRequest request) {
        //<input type="text" m:value="counter-value" /> w/ "counter-value" = 123
        //becomes
        //<input type="text" from-value="counter-value" value="123" />

        Elements tagsWithMValue = result.getDocument().getElementsByAttribute(TagConstants.M_VALUE);
        for (Element tagWithMValue : tagsWithMValue) {
            final String item = tagWithMValue.attr(TagConstants.M_VALUE).trim();
            Object variableValue = getPossibleEachValue(tagWithMValue, item, request);
            if(null == variableValue) variableValue = variableToString(item, variables);
            if(null == variableValue) variableValue = item;

            tagWithMValue.removeAttr("m:value");
            tagWithMValue.val(variableValue.toString());
            tagWithMValue.attr(TagConstants.FROM_VALUE, item);
        }
    }

    private Node createSpan(String item, String variableValue) {
        return new Element(Tag.valueOf("span"), "")
                .text(variableValue)
                .attr(TagConstants.FROM_VALUE, item);
    }
}
