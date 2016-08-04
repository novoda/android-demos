package com.novoda.demo.edittextchips.splitwise;

import java.io.Serializable;

// If your objects cannot be made Serializable, please look at
// https://github.com/splitwise/TokenAutoComplete#restoring-the-view-state
class Tag implements Serializable {

    private final String name;

    Tag(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "#" + getName();
    }

}
