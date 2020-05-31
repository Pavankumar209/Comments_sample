package com.example.comments_sample;

public class RecursiveItemSet {
    int depth;
    boolean expanded;
    Object item;

    public RecursiveItemSet(Object item, boolean expanded, int depth) {
        this.item = item;
        this.expanded = expanded;
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }
    public boolean isExpanded() {
        return expanded;
    }
    public Object getItem() {
        return item;
    }

}