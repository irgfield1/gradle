/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.artifacts;

import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.component.ComponentIdentifier;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.capabilities.Capability;
import org.gradle.api.internal.artifacts.ivyservice.resolveengine.result.ComponentIdentifierSerializer;
import org.gradle.api.internal.artifacts.metadata.ComponentArtifactIdentifierSerializer;
import org.gradle.internal.Cast;
import org.gradle.internal.component.external.model.DefaultModuleComponentArtifactIdentifier;
import org.gradle.internal.component.external.model.ImmutableCapability;
import org.gradle.internal.serialize.Decoder;
import org.gradle.internal.serialize.DefaultSerializerRegistry;
import org.gradle.internal.serialize.Encoder;
import org.gradle.internal.serialize.Serializer;
import org.gradle.internal.snapshot.impl.ValueSnapshotterSerializerRegistry;

import java.util.LinkedHashSet;
import java.util.Set;

public class DependencyManagementValueSnapshotterSerializerRegistry extends DefaultSerializerRegistry implements ValueSnapshotterSerializerRegistry {

    private static final Set<Class<?>> SUPPORTED_TYPES;

    static {
        Set<Class<?>> supportedTypes = new LinkedHashSet<>();
        supportedTypes.add(Capability.class);
        supportedTypes.add(ModuleVersionIdentifier.class);
        supportedTypes.add(ComponentIdentifier.class);
        supportedTypes.add(DefaultModuleComponentArtifactIdentifier.class);
        supportedTypes.add(AttributeContainer.class);
        SUPPORTED_TYPES = supportedTypes;
    }

    @SuppressWarnings("rawtypes")
    public DependencyManagementValueSnapshotterSerializerRegistry(
        ImmutableModuleIdentifierFactory moduleIdentifierFactory
    ) {
        super(true);
        register(Capability.class, new Serializer<Capability>() {

            @Override
            public Capability read(Decoder decoder) throws Exception {
                return new ImmutableCapability(
                    decoder.readString(),
                    decoder.readString(),
                    decoder.readNullableString()
                );
            }

            @Override
            public void write(Encoder encoder, Capability value) throws Exception {
                encoder.writeString(value.getGroup());
                encoder.writeString(value.getName());
                encoder.writeNullableString(value.getVersion());
            }
        });
        register(ModuleVersionIdentifier.class, new ModuleVersionIdentifierSerializer(moduleIdentifierFactory));
        register(ComponentIdentifier.class, new ComponentIdentifierSerializer());
        register(DefaultModuleComponentArtifactIdentifier.class, new ComponentArtifactIdentifierSerializer());
    }

    @Override
    public boolean canSerialize(Class<?> baseType) {
        return super.canSerialize(baseTypeOf(baseType));
    }

    @Override
    public <T> Serializer<T> build(Class<T> baseType) {
        return super.build(Cast.uncheckedCast(baseTypeOf(baseType)));
    }

    private static Class<?> baseTypeOf(Class<?> type) {
        for (Class<?> supportedType : SUPPORTED_TYPES) {
            if (supportedType.isAssignableFrom(type)) {
                return supportedType;
            }
        }
        return type;
    }
}
