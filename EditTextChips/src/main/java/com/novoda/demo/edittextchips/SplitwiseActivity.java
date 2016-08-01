package com.novoda.demo.edittextchips;

import com.tokenautocomplete.FilteredArrayAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.io.Serializable;

public class SplitwiseActivity extends Activity {

    static Intent intent(Context context) {
        return new Intent(context, SplitwiseActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splitwise);
        Tag[] tags = new Tag[]{
                new Tag("tagWeir"),
                new Tag("tagSmith"),
                new Tag("tagJordan"),
                new Tag("tagPeterson"),
                new Tag("tagJohnson"),
                new Tag("tagAnderson")
        };

        ArrayAdapter<Tag> adapter = new FilteredArrayAdapter<Tag>(this, android.R.layout.simple_list_item_1, tags) {
            @Override
            protected boolean keepObject(Tag tag, String mask) {
                return tag.getName().startsWith(mask.replaceFirst("#", ""));
            }
        };
        SplitwiseTagsView tagsView = (SplitwiseTagsView) findViewById(R.id.tags);
        tagsView.setAdapter(adapter);
        tagsView.setPrefix("Your bestest friends: "); // Doesn't seem to allow suffix.
        tagsView.allowDuplicates(false);
    }

    // If your objects cannot be made Serializable, please look at
    // https://github.com/splitwise/TokenAutoComplete#restoring-the-view-state
    static class Tag implements Serializable {

        private final String name;

        Tag(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tag tag = (Tag) o;
            return name.equals(tag.name);

        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "#" + getName();
        }
    }

}
