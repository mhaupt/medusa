package io.getmedusa.medusa.core.injector.tag;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

class ClickTagTest extends AbstractTest {

    public static final String HTML = "<button m:click=\"sayHelloTo('John Doe')\">Hello!</button>";
    public static final String HTML_DOUBLE_QUOTES = "<button m:click='sayHelloTo(\"Jane Doe\", 2)'>Hello!</button>";
    public static final String HTML_BOOLEAN = "<button m:click='turnOn(true)'>Lightswitch ON</button>";
    public static final String HTML_INTEGER = "<button m:click='increaseCount(2)'>Add two people</button>";
    public static final String HTML_OBJECT = "<button m:click='addName(person.name)'>Add name</button>";
    public static final String HTML_OBJECT_MULTI = "<button m:click='addName(1, person.name, 1)'>Add name</button>";
    private static final String HTML_EACH_ITERATION = """
                    <m:foreach collection="people" eachName="person">
                        <button m:click='addName("你好世界", 1)'>Add name</button>
                    </m:foreach>
            """;

    private static final String HTML_EACH_ITERATION_W_OBJECT = """
                    <m:foreach collection="people" eachName="person">
                        <button m:click='addName(person.name, 1)'>Add name</button>
                    </m:foreach>
            """;

    public static final String HTML_DIFF_BETWEEN_LITERAL_AND_OBJECT = """
                    <m:foreach collection="names" eachName="person4">
                    <m:foreach collection="people" eachName="person3">
                        <button m:click="person(1, person.name, 'person', 2, person2, 'person2', 3, person3.name, 'person3', 4, person4, 'person4')">_</button>
                    </m:foreach>
                    </m:foreach>
            """;

    @Test
    void testSimple() {
        Document document = inject(HTML, Collections.emptyMap());
        String html = document.html();
        System.out.println(html);

        Assertions.assertFalse(html.contains("m:click"), "m:click should be replaced with an onclick");
        Assertions.assertTrue(html.contains("onclick=\"_M.sendEvent(this, 'sayHelloTo(\\'John Doe\\')')\""));
    }

    @Test
    void testDoubleQuotes() {
        Document document = inject(HTML_DOUBLE_QUOTES, Collections.emptyMap());
        String html = document.html();
        System.out.println(html);

        Assertions.assertFalse(html.contains("m:click"), "m:click should be replaced with an onclick");
        Assertions.assertTrue(html.contains("onclick=\"_M.sendEvent(this, 'sayHelloTo(\\'Jane Doe\\', 2)')\""));
    }

    @Test
    void testBoolean() {
        Document document = inject(HTML_BOOLEAN, Collections.emptyMap());
        String html = document.html();
        System.out.println(html);

        Assertions.assertFalse(html.contains("m:click"), "m:click should be replaced with an onclick");
        Assertions.assertTrue(html.contains("onclick=\"_M.sendEvent(this, 'turnOn(true)')\""));
    }

    @Test
    void testInteger() {
        Document document = inject(HTML_INTEGER, Collections.emptyMap());
        String html = document.html();
        System.out.println(html);

        Assertions.assertFalse(html.contains("m:click"), "m:click should be replaced with an onclick");
        Assertions.assertTrue(html.contains("onclick=\"_M.sendEvent(this, 'increaseCount(2)')\""));
    }

    @Test
    void testObjectSingleParam() {
        Document document = inject(HTML_OBJECT, Map.of("person", new Person("안녕하세요 세계")));
        String html = document.html();
        System.out.println(html);

        Assertions.assertFalse(html.contains("m:click"), "m:click should be replaced with an onclick");
        Assertions.assertTrue(html.contains("onclick=\"_M.sendEvent(this, 'addName(person.name)')"));
    }

    @Test
    void testObjectMultipleParams() {
        Document document = inject(HTML_OBJECT_MULTI, Map.of("person", new Person("안녕하세요 세계")));
        String html = document.html();
        System.out.println(html);

        Assertions.assertFalse(html.contains("m:click"), "m:click should be replaced with an onclick");
        Assertions.assertTrue(html.contains("onclick=\"_M.sendEvent(this, 'addName(1, person.name, 1)')\""));
    }

    @Test
    void testIteration() {
        Document document = inject(HTML_EACH_ITERATION, Map.of("people", List.of(new Person(""))));
        removeNonDisplayedElements(document);

        String html = document.html();
        System.out.println(html);

        Assertions.assertFalse(html.contains("m:click"), "m:click should be replaced with an onclick");
        Assertions.assertTrue(html.contains("onclick=\"_M.sendEvent(this, 'addName(\\'你好世界\\', 1)"));
    }

    @Test
    void testIterationEach() {
        Document document = inject(HTML_EACH_ITERATION_W_OBJECT, Map.of("people", List.of(new Person("안녕하세요 세계"))));
        removeNonDisplayedElements(document);

        String html = document.html();
        System.out.println(html);

        Assertions.assertFalse(html.contains("m:click"), "m:click should be replaced with an onclick");
        Assertions.assertTrue(html.contains("onclick=\"_M.sendEvent(this, 'addName(person.name, 1)"));
    }

    //TODO this is wrong, we should be referring to variables where possible - so that the changes are reflected when the variable changes (via JS, for example)
    @Test
    void testUnderstandTheDifferenceBetweenLiteralsAndObjects() {
        Document document = inject(HTML_DIFF_BETWEEN_LITERAL_AND_OBJECT,
                Map.of("person", new Person("person A"),
                        "person2", "person (B)",
                        "people", List.of(new Person("person - C")),
                        "names", List.of("D's person")));
        String html = document.html();
        System.out.println(html);

        String onClick = document.getElementsByTag("button").last().attr("onclick");

        Assertions.assertFalse(html.contains("m:click"), "m:click should be replaced with an onclick");
        Assertions.assertEquals("_M.sendEvent(this, 'person(1, person.name, \\'person\\', 2, person2, \\'person2\\', 3, person3.name, \\'person3\\', 4, person4, \\'person4\\')')", onClick);
    }


}
