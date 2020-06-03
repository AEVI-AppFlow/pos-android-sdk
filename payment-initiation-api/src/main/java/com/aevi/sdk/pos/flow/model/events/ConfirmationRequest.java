package com.aevi.sdk.pos.flow.model.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a request that will be shown to a user for the confirmation of something
 *
 * This class will contain either
 * <ul>
 * <li>A list of {@link ConfirmationOption} objects to allow for a selection of some value(s) from a list</li>
 * <li>A {@link ConfirmationInput} object to allow some free text/numbers to be returned</li>
 * </ul>
 */
public class ConfirmationRequest {

    @NonNull
    private final String id;

    @NonNull
    private final String type;

    @Nullable
    private String titleText;

    @Nullable
    private String description;

    @Nullable
    private ConfirmationOption[] confirmationOptions;

    private boolean multiSelect;

    @Nullable
    private ConfirmationInput confirmationInput;

    public ConfirmationRequest(@NonNull String type) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
    }

    public ConfirmationRequest(@NonNull String type, @Nullable String titleText) {
        this(type);
        this.titleText = titleText;
    }

    public ConfirmationRequest(@NonNull String type, @Nullable String titleText, @Nullable ConfirmationOption[] confirmationOptions) {
        this(type, titleText);
        this.confirmationOptions = confirmationOptions;
    }

    public ConfirmationRequest(@NonNull String type, @Nullable String titleText, @Nullable ConfirmationOption[] confirmationOptions, boolean isMultiSelect) {
        this(type, titleText, confirmationOptions);
        this.multiSelect = isMultiSelect;
    }

    public ConfirmationRequest(@NonNull String type, @Nullable String titleText, @Nullable ConfirmationInput confirmationInput) {
        this(type, titleText);
        this.confirmationInput = confirmationInput;
    }

    /**
     * The unique id of this request which should be returned in any {@link ConfirmationResponse}
     *
     * @return The id of the request
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * The type of request this is.
     *
     * @return The type
     */
    @NonNull
    public String getType() {
        return type;
    }

    /**
     * Some text that can be used to display as a title for the request
     *
     * @return The title text
     */
    @Nullable
    public String getTitleText() {
        return titleText;
    }

    /**
     * Sets some title text used when displaying this request
     *
     * @param titleText The title text to display
     */
    public void setTitleText(@Nullable String titleText) {
        this.titleText = titleText;
    }

    /**
     * Some description text to display with the request
     *
     * @return The description text
     */
    @Nullable
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description text to be used with this request, if any
     *
     * @param description The description
     */
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    /**
     * @return True if this request has a list of possible options and more than one can be selected
     */
    public boolean isMultiSelect() {
        return multiSelect;
    }

    /**
     * Sets a list of confirmation options.
     * NB: A request is EITHER a list of options or an input value. Calling this method will remove any previously
     * set {@link ConfirmationInput} value.
     *
     * @param confirmationOptions The list of options
     * @param multiSelect         True is more than one option can be selected
     */
    public void setConfirmationOptions(@Nullable ConfirmationOption[] confirmationOptions, boolean multiSelect) {
        this.confirmationInput = null;
        this.multiSelect = multiSelect;
        this.confirmationOptions = confirmationOptions;
    }

    /**
     * @return The list of confirmation options if any
     */
    @Nullable
    public ConfirmationOption[] getConfirmationOptions() {
        return confirmationOptions;
    }

    /**
     * @return The details of the input value to get for this request, if any
     */
    @Nullable
    public ConfirmationInput getConfirmationInput() {
        return confirmationInput;
    }

    /**
     * Sets this request to require the user to input some value
     *
     * @param confirmationInput Object describing the value that should be input
     */
    public void setConfirmationInput(@Nullable ConfirmationInput confirmationInput) {
        this.confirmationOptions = null;
        this.confirmationInput = confirmationInput;
    }

    /**
     * @return True if this request requires some input value
     */
    public boolean isInput() {
        return confirmationInput != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfirmationRequest that = (ConfirmationRequest) o;
        return multiSelect == that.multiSelect &&
                id.equals(that.id) &&
                type.equals(that.type) &&
                Objects.equals(titleText, that.titleText) &&
                Objects.equals(description, that.description) &&
                Arrays.equals(confirmationOptions, that.confirmationOptions) &&
                Objects.equals(confirmationInput, that.confirmationInput);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, type, titleText, description, multiSelect, confirmationInput);
        result = 31 * result + Arrays.hashCode(confirmationOptions);
        return result;
    }

    @Override
    public String toString() {
        return "ConfirmationRequest{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", titleText='" + titleText + '\'' +
                ", description='" + description + '\'' +
                ", confirmationOptions=" + Arrays.toString(confirmationOptions) +
                ", multiSelect=" + multiSelect +
                ", confirmationInput=" + confirmationInput +
                '}';
    }
}
