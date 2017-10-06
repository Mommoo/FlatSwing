package com.mommoo.flat.button;

import java.awt.event.ActionEvent;
import java.util.*;
import java.util.function.Consumer;

class ButtonEventRepository {
    private Map<Object, Consumer<ActionEvent>> eventMap = new HashMap<>();

    void addEvent(Object key, Consumer<ActionEvent> event){
        eventMap.put(key, event);
    }

    void removeEvent(Object objectKey){
        eventMap.remove(objectKey);
    }

    List<Consumer<ActionEvent>> getEventList() {
        return Collections.unmodifiableList(new ArrayList<>(eventMap.values()));
    }
}
