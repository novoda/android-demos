package com.novoda.demo.edittextchips;

import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

public class SplitwiseActivity extends Activity {

    private static final Tag[] TAGS = new Tag[]{
            new Tag("tagWeir"),
            new Tag("tagSmith"),
            new Tag("tagJordan"),
            new Tag("tagPeterson"),
            new Tag("tagJohnson"),
            new Tag("tagAnderson")
    };
    private static final char[] SPLIT_CHARS = new char[]{',', ';', '\n'};

    static Intent intent(Context context) {
        return new Intent(context, SplitwiseActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splitwise);
        SplitwiseTagsView tagsView = (SplitwiseTagsView) findViewById(R.id.tags);

        tagsView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.SelectDeselect);
        tagsView.setDeletionStyle(TokenCompleteTextView.TokenDeleteStyle.Clear); // TODO: fix this
        tagsView.setSplitChar(SPLIT_CHARS);

        tagsView.setAdapter(new TagAdapter(this, android.R.layout.simple_list_item_1, TAGS));
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
        public String toString() {
            return "#" + getName();
        }
    }

    private static class TagAdapter extends FilteredArrayAdapter<Tag> {

        TagAdapter(Context context, int resource, Tag[] objects) {
            super(context, resource, objects);
        }

        @Override
        protected boolean keepObject(Tag tag, String mask) {
            return tag.getName().startsWith(mask.replaceFirst("#", ""));
        }
    }

}
