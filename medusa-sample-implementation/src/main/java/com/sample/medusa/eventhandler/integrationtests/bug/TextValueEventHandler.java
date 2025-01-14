package com.sample.medusa.eventhandler.integrationtests.bug;

import io.getmedusa.medusa.core.annotation.PageAttributes;
import io.getmedusa.medusa.core.annotation.UIEventPage;
import io.getmedusa.medusa.core.injector.DOMChanges;

@UIEventPage(path = "/bug/text-value", file = "pages/integration-tests/bug/text-value.html")
public class TextValueEventHandler {

    private Counter counter = new Counter();

    public PageAttributes setupAttributes() {
        return new PageAttributes().with("counter", counter);
    }

    public DOMChanges increment(){
        return DOMChanges.of("counter", counter.increment());
    }

    public DOMChanges reset(){
        return DOMChanges.of("counter", counter.reset());
    }
}

class Counter {
    private int value = 0;
    private char key = 'a';

    public Counter increment() {
        value++;
        key = (char)(97+value);
        return this;
    }

    public Counter reset(){
        value = 0;
        key = 'a';
        return this;
    }

    public int getValue() {
        return value;
    }

    public int getCount() {
        return value;
    }

    public char getKey() {
        return key;
    }
}