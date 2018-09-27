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

/**
 * Represents the selection strategy for flow apps in a particular flow stage.
 */
public enum AppExecutionType {
    /**
     * Only a single application (the first, if there are multiple in the list) will be executed, without any operator interaction.
     */
    SINGLE,
    /**
     * If a single application is defined, it will be called directly. If more than one, the operator will be asked to choose which one to call.
     */
    SINGLE_SELECT,
    /**
     * All applications defined in the list will be called in order of definition.
     */
    MULTIPLE,
    /**
     * No applications will be called, regardless of what is in the apps list.
     */
    NONE
}
