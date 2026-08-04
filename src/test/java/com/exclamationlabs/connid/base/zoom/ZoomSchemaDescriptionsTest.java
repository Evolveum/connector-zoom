/*
    Copyright 2020 Exclamation Labs
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
        http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.exclamationlabs.connid.base.zoom;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.stream.Collectors;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.Schema;
import org.junit.jupiter.api.Test;

public class ZoomSchemaDescriptionsTest {

  @Test
  public void objectClassesHaveDescriptions() {
    Schema schema = new ZoomConnector().schema();
    Map<String, String> descriptions =
        schema.getObjectClassInfo().stream()
            .collect(Collectors.toMap(ObjectClassInfo::getType, ObjectClassInfo::getDescription));

    assertEquals("Zoom user account", descriptions.get("ZoomUser"));
    assertEquals("Zoom group", descriptions.get("ZoomGroup"));
  }
}
