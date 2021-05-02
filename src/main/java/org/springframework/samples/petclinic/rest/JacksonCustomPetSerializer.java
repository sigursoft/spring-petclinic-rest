/*
 * Copyright 2016 the original author or authors.
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

package org.springframework.samples.petclinic.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * @author Vitaliy Fedoriv
 *
 */
@SuppressWarnings("SameParameterValue")
public class JacksonCustomPetSerializer extends StdSerializer<Pet> {

	public JacksonCustomPetSerializer() {
		this(null);
	}

	protected JacksonCustomPetSerializer(Class<Pet> t) {
		super(t);
	}

    @Override
    public void serialize(Pet pet, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        Format formatter = new SimpleDateFormat("yyyy/MM/dd");
        jsonGenerator.writeStartObject(); // pet
        if (pet.getId() == null) {
            jsonGenerator.writeNullField("id");
        } else {
            jsonGenerator.writeNumberField("id", pet.getId());
        }
        jsonGenerator.writeStringField("name", pet.getName());
        jsonGenerator.writeStringField("birthDate", formatter.format(pet.getBirthDate()));

        PetType petType = pet.getType();
        jsonGenerator.writeObjectFieldStart("type");
        jsonGenerator.writeNumberField("id", petType.getId());
        jsonGenerator.writeStringField("name", petType.getName());
        jsonGenerator.writeEndObject(); // type

        Owner owner = pet.getOwner();
        jsonGenerator.writeObjectFieldStart("owner");
        jsonGenerator.writeNumberField("id", owner.getId());
        jsonGenerator.writeStringField("firstName", owner.getFirstName());
        jsonGenerator.writeStringField("lastName", owner.getLastName());
        jsonGenerator.writeStringField("address", owner.getAddress());
        jsonGenerator.writeStringField("city", owner.getCity());
        jsonGenerator.writeStringField("telephone", owner.getTelephone());
        jsonGenerator.writeEndObject(); // owner
        // write visits array
        jsonGenerator.writeArrayFieldStart("visits");
        for (Visit visit : pet.getVisits()) {
            jsonGenerator.writeStartObject(); // visit
            jsonGenerator.writeNumberField("id", visit.getId());
            jsonGenerator.writeStringField("date", formatter.format(visit.getDate()));
            jsonGenerator.writeStringField("description", visit.getDescription());
            jsonGenerator.writeNumberField("pet", visit.getPet().getId());
            jsonGenerator.writeEndObject(); // visit
        }
        jsonGenerator.writeEndArray(); // visits
        jsonGenerator.writeEndObject(); // pet
    }

}
