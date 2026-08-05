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

import com.exclamationlabs.connid.base.connector.schema.DefaultConnectorSchemaBuilder;
import com.exclamationlabs.connid.base.zoom.configuration.ZoomConfiguration;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.identityconnectors.framework.api.operations.APIOperation;
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

  @Test
  public void addingDescriptionsPreservesGeneratedSchema() {
    Schema originalSchema = new DefaultSchemaZoomConnector().schema();
    Schema describedSchema = new ZoomConnector().schema();

    Map<String, ObjectClassInfo> originalObjectClasses = objectClassesByType(originalSchema);
    Map<String, ObjectClassInfo> describedObjectClasses = objectClassesByType(describedSchema);
    assertEquals(originalObjectClasses.keySet(), describedObjectClasses.keySet());

    for (Map.Entry<String, ObjectClassInfo> entry : originalObjectClasses.entrySet()) {
      ObjectClassInfo original = entry.getValue();
      ObjectClassInfo described = describedObjectClasses.get(entry.getKey());
      assertEquals(original.getAttributeInfo(), described.getAttributeInfo());
      assertEquals(original.isContainer(), described.isContainer());
      assertEquals(original.isAuxiliary(), described.isAuxiliary());
      assertEquals(original.isEmbedded(), described.isEmbedded());
    }

    assertEquals(originalSchema.getOperationOptionInfo(), describedSchema.getOperationOptionInfo());
    assertEquals(
        originalSchema.getSupportedOptionsByOperation(),
        describedSchema.getSupportedOptionsByOperation());
    assertEquals(
        supportedObjectClassTypes(originalSchema), supportedObjectClassTypes(describedSchema));
  }

  private Map<String, ObjectClassInfo> objectClassesByType(Schema schema) {
    return schema.getObjectClassInfo().stream()
        .collect(Collectors.toMap(ObjectClassInfo::getType, objectClassInfo -> objectClassInfo));
  }

  private Map<Class<? extends APIOperation>, Set<String>> supportedObjectClassTypes(Schema schema) {
    return schema.getSupportedObjectClassesByOperation().entrySet().stream()
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                entry ->
                    entry.getValue().stream()
                        .map(ObjectClassInfo::getType)
                        .collect(Collectors.toSet())));
  }

  private static class DefaultSchemaZoomConnector extends ZoomConnector {

    private DefaultSchemaZoomConnector() {
      setConnectorSchemaBuilder(new DefaultConnectorSchemaBuilder<ZoomConfiguration>());
    }
  }
}
