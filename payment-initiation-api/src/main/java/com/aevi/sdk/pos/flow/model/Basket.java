package com.aevi.sdk.pos.flow.model;

import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.annotations.Nullable;

/**
 * Represents a customer basket.
 */
public class Basket implements Jsonable {

    private final List<BasketItem> displayItems = new CopyOnWriteArrayList<>();

    public List<BasketItem> getDisplayItems() {
        return displayItems;
    }

    public void addItem(BasketItem item) {
        this.displayItems.add(0, item);
    }

    /**
     * Adds an item line to this basket and merges the count if a line of this item type already exists
     *
     * @param item The item to add
     */
    public void addItemMerge(BasketItem item) {
        if (hasItem(item.getLabel())) {
            BasketItem basketItem = getItem(item.getLabel());
            basketItem.setCount(basketItem.getCount() + item.getCount());
        } else {
            this.displayItems.add(0, item);
        }
    }

    /**
     * Add multiple items to this basket.
     *
     * @param items The displayItems to add
     */
    public void addItems(BasketItem... items) {
        this.displayItems.addAll(0, Arrays.asList(items));
    }

    /**
     * Add multiple items to this basket.
     *
     * @param items The displayItems to add
     */
    public void addItems(List<BasketItem> items) {
        this.displayItems.addAll(0, items);
    }

    /**
     * Check whether the basket already has an item with the provided label.
     *
     * @param label The label to match against
     * @return True if there is an item, false otherwise.
     */
    public boolean hasItem(String label) {
        for (BasketItem item : displayItems) {
            if (item.getLabel().equals(label)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get an item based on its label.
     *
     * @param label The label to match against
     * @return The item, if any, with the label
     */
    @Nullable
    public BasketItem getItem(String label) {
        for (BasketItem item : displayItems) {
            if (item.getLabel().equals(label)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Adds a single item of the given type to this basket
     *
     * @param basketItem The item to add
     */
    public void addOneOf(BasketItem basketItem) {
        if (hasItem(basketItem.getLabel())) {
            BasketItem itemLine = getItem(basketItem.getLabel());
            itemLine.addOne();
        } else {
            addItem(basketItem);
        }
    }

    /**
     * Removes a single item of the type given from this basket. If no more items present then the line will be removed completely
     * if the retain flag is set to false
     *
     * @param basketItem The item to remove
     * @param retain     If set to true the line item will be retained even if, after removal, the count is equal to 0
     */
    public void removeOneOf(BasketItem basketItem, boolean retain) {
        if (hasItem(basketItem.getLabel())) {
            BasketItem itemLine = getItem(basketItem.getLabel());
            if (itemLine.getCount() > 1) {
                itemLine.removeOne();
            } else {
                if (retain) {
                    itemLine.setCount(0);
                } else {
                    displayItems.remove(basketItem);
                }
            }
        }
    }

    /**
     * Removes items of the given type from the basket upto the item count. If the number of displayItems requested to be removed is greater than
     * the current count for this line item in the basket then all items are removed and the count is set to 0.
     *
     * @param item   The item to remove including potentially a count of greater than one
     * @param retain If set to true the line item will be retained even if, after removal, the count is equal to 0
     */
    public void removeItems(BasketItem item, boolean retain) {
        if (hasItem(item.getLabel())) {
            BasketItem itemLine = getItem(item.getLabel());
            int left = itemLine.getCount() - item.getCount();
            if (left > 0) {
                itemLine.setCount(left);
            } else {
                itemLine.setCount(0);
            }

            if (itemLine.getCount() == 0 && !retain) {
                displayItems.remove(itemLine);
            }
        }
    }

    /**
     * Clear all items
     */
    public void clearItems() {
        displayItems.clear();
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    @Override
    public String toString() {
        return "Basket{" + "items=" + displayItems.size() + '}';
    }
}
