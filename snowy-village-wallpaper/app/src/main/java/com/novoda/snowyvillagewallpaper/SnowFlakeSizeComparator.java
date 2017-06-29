package com.novoda.snowyvillagewallpaper;

import java.util.Comparator;

class SnowFlakeSizeComparator implements Comparator<SnowFlake> {

    @Override
    public int compare(SnowFlake lhs, SnowFlake rhs) {
        return lhs.getFlakeType().compareTo(rhs.getFlakeType());
    }

}
