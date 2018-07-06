/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.aevi.sdk.flow.model.config;

import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.util.HashMap;
import java.util.Map;

/**
 * Config styling model.
 */
public class ConfigStyles implements Jsonable {

    private final Map<String, Integer> colors = new HashMap<>();
    private final Map<String, String> styles = new HashMap<>();

    public boolean hasColor(String colorKey) {
        return colors.containsKey(colorKey);
    }

    public int getColor(String colorKey) {
        return colors.get(colorKey);
    }

    public void setColor(String colorKey, int color) {
        colors.put(colorKey, color);
    }

    public String getStyle(String styleKey) {
        return styles.get(styleKey);
    }

    public void setStyle(String styleKey, String style) {
        styles.put(styleKey, style);
    }

    public boolean hasStyle(String styleKey) {
        return styles.containsKey(styleKey);
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static ConfigStyles fromJson(String json) {
        return JsonConverter.deserialize(json, ConfigStyles.class);
    }
}
