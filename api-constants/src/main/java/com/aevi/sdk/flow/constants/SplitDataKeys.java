package com.aevi.sdk.flow.constants;


public interface SplitDataKeys {

    String DATA_KEY_SPLIT_TXN = "splitTxn"; // boolean. true if part of a split, false or not set otherwise
    String DATA_KEY_NUM_SPLITS = "numSplits"; // int. The number of splits in total
    String DATA_KEY_SPLIT_TYPE = "splitType"; // String. The type of split

    String SPLIT_TYPE_BASKET = "splitBasketItems";
    String SPLIT_TYPE_AMOUNTS = "splitAmounts";

}
