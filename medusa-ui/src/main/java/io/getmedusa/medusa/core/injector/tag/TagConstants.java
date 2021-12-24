package io.getmedusa.medusa.core.injector.tag;

public class TagConstants {

    //iteration - tags
    public static final String ITERATION_TAG = "m:foreach"; //each on div
    public static final String ITERATION_TAG_COLLECTION_ATTR = "collection";
    public static final String ITERATION_TAG_EACH_ATTR = "eachName";

    //iteration - attributes
    public static final String M_EACH = "m-each"; //each on div
    public static final String TEMPLATE_ID = "template-id"; //template id on div
    public static final String INDEX = "index"; //index on div
    public static final String M_ID = "m-id"; //template id on template itself

    //text - tags
    public static final String TEXT_TAG = "m:text";
    public static final String TEXT_TAG_ITEM_ATTR = "item";
    public static final String M_VALUE = "m:value";
    public static final String FROM_VALUE = "from-value";

    //text - attributes

    //conditional - tags
    public static final String CONDITIONAL_TAG = "m:if"; //each on div
    public static final String CONDITIONAL_TAG_CONDITION_ATTR = "condition";
    public static final String CONDITIONAL_TAG_EQUALS = "eq";
    public static final String CONDITIONAL_TAG_NOT = "not";
    public static final String CONDITIONAL_TAG_GREATER_THAN = "gt";
    public static final String CONDITIONAL_TAG_GREATER_THAN_OR_EQ = "gte";
    public static final String CONDITIONAL_TAG_LESS_THAN = "lt";
    public static final String CONDITIONAL_TAG_LESS_THAN_OR_EQ = "lte";

    //conditional - attributes
    public static final String M_IF = "m-if";
    public static final String M_ELSE = "m:else";
    public static final String M_ELSEIF = "m:elseif";

    private TagConstants() {}
}