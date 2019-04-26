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

package com.aevi.sdk.flow.model;

import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

/**
 * Audit entry for flow/request audit log.
 */
public class AuditEntry implements Jsonable {

    private static final int MAX_CHARS = 80;

    /**
     * The severity of the audit entry.
     */
    public enum AuditSeverity {
        INFO,
        WARNING,
        ERROR
    }

    private final AuditSeverity auditSeverity;
    private final String auditMessage;

    /**
     * Initialise.
     *
     * @param auditSeverity The severity of the entry
     * @param auditMessage  The message
     */
    public AuditEntry(AuditSeverity auditSeverity, String auditMessage) {
        this.auditSeverity = auditSeverity;
        if (auditMessage.length() > MAX_CHARS) {
            auditMessage = auditMessage.substring(0, MAX_CHARS) + "...";
        }
        this.auditMessage = auditMessage;
    }

    /**
     * Get the severity of the message.
     *
     * @return The severity of the message
     */
    public AuditSeverity getAuditSeverity() {
        return auditSeverity;
    }

    /**
     * Get the audit message.
     *
     * @return The audit message
     */
    public String getAuditMessage() {
        return auditMessage;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static AuditEntry fromJson(String json) {
        return JsonConverter.deserialize(json, AuditEntry.class);
    }
}
