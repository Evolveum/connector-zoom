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

package com.exclamationlabs.connid.base.zoom.schema;

import com.exclamationlabs.connid.base.connector.BaseConnector;
import com.exclamationlabs.connid.base.connector.adapter.BaseAdapter;
import com.exclamationlabs.connid.base.connector.schema.DefaultConnectorSchemaBuilder;
import com.exclamationlabs.connid.base.zoom.configuration.ZoomConfiguration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.identityconnectors.framework.api.operations.APIOperation;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;
import org.identityconnectors.framework.common.objects.Schema;

public class ZoomSchemaBuilder extends DefaultConnectorSchemaBuilder<ZoomConfiguration> {

  private static final Map<String, String> OBJECT_CLASS_DESCRIPTIONS = new LinkedHashMap<>();

  static {
    OBJECT_CLASS_DESCRIPTIONS.put("ZoomUser", "Zoom user account");
    OBJECT_CLASS_DESCRIPTIONS.put("ZoomGroup", "Zoom group");
  }

  @Override
  public Schema build(
      BaseConnector<ZoomConfiguration> connector,
      Map<ObjectClass, BaseAdapter<?, ZoomConfiguration>> adapterMap) {
    Schema schema = super.build(connector, adapterMap);
    Map<String, ObjectClassInfo> objectClassesByType = new LinkedHashMap<>();

    for (ObjectClassInfo objectClassInfo : schema.getObjectClassInfo()) {
      ObjectClassInfo describedObjectClass = withDescription(objectClassInfo);
      objectClassesByType.put(describedObjectClass.getType(), describedObjectClass);
    }

    Map<Class<? extends APIOperation>, Set<ObjectClassInfo>> supportedObjectClasses =
        new LinkedHashMap<>();
    for (Map.Entry<Class<? extends APIOperation>, Set<ObjectClassInfo>> entry :
        schema.getSupportedObjectClassesByOperation().entrySet()) {
      Set<ObjectClassInfo> describedObjectClasses = new LinkedHashSet<>();
      for (ObjectClassInfo objectClassInfo : entry.getValue()) {
        describedObjectClasses.add(objectClassesByType.get(objectClassInfo.getType()));
      }
      supportedObjectClasses.put(entry.getKey(), describedObjectClasses);
    }

    return new Schema(
        new LinkedHashSet<>(objectClassesByType.values()),
        schema.getOperationOptionInfo(),
        supportedObjectClasses,
        schema.getSupportedOptionsByOperation());
  }

  private ObjectClassInfo withDescription(ObjectClassInfo objectClassInfo) {
    ObjectClassInfoBuilder builder = new ObjectClassInfoBuilder();
    builder.setType(objectClassInfo.getType());
    builder.setContainer(objectClassInfo.isContainer());
    builder.setAuxiliary(objectClassInfo.isAuxiliary());
    builder.setEmbedded(objectClassInfo.isEmbedded());
    builder.setDescription(
        OBJECT_CLASS_DESCRIPTIONS.getOrDefault(
            objectClassInfo.getType(), objectClassInfo.getDescription()));
    builder.addAllAttributeInfo(objectClassInfo.getAttributeInfo());
    return builder.build();
  }
}
