/*
 * Copyright 2016-2017 the original author or authors.
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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.configuration.ApplicationTestConfig;
import org.springframework.samples.petclinic.configuration.CacheTestConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for {@link OwnerRestController}
 *
 * @author Vitaliy Fedoriv
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationTestConfig.class, CacheTestConfiguration.class})
@WebAppConfiguration
public class OwnerRestControllerTests {

    @Autowired
    private OwnerRestController ownerRestController;

    @MockBean
    private ClinicService clinicService;

    private MockMvc mockMvc;

    private List<Owner> owners;

    @BeforeEach
    public void initOwners(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(ownerRestController)
            .setControllerAdvice(new ExceptionControllerAdvice())
            .build();
        owners = new ArrayList<>();

        Owner ownerWithPet = new Owner();
        ownerWithPet.setId(1);
        ownerWithPet.setFirstName("George");
        ownerWithPet.setLastName("Franklin");
        ownerWithPet.setAddress("110 W. Liberty St.");
        ownerWithPet.setCity("Madison");
        ownerWithPet.setTelephone("6085551023");
        ownerWithPet.addPet(getTestPetWithIdAndName(ownerWithPet));
        owners.add(ownerWithPet);

        Owner owner = new Owner();
        owner.setId(2);
        owner.setFirstName("Betty");
        owner.setLastName("Davis");
        owner.setAddress("638 Cardinal Ave.");
        owner.setCity("Sun Prairie");
        owner.setTelephone("6085551749");
        owners.add(owner);

    	owner = new Owner();
    	owner.setId(3);
    	owner.setFirstName("Eduardo");
    	owner.setLastName("Rodriquez");
    	owner.setAddress("2693 Commerce St.");
    	owner.setCity("McFarland");
    	owner.setTelephone("6085558763");
    	owners.add(owner);

    	owner = new Owner();
    	owner.setId(4);
    	owner.setFirstName("Harold");
    	owner.setLastName("Davis");
    	owner.setAddress("563 Friendly St.");
    	owner.setCity("Windsor");
    	owner.setTelephone("6085553198");
    	owners.add(owner);
    }

    private Pet getTestPetWithIdAndName(final Owner owner) {
        PetType petType = new PetType();
        petType.setId(2);
        petType.setName("dog");
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Rosy");
        pet.setBirthDate(new Date());
        pet.setOwner(owner);
        pet.setType(petType);
        pet.addVisit(getTestVisitForPet(pet));
        return pet;
    }

    private Visit getTestVisitForPet(final Pet pet) {
        Visit visit = new Visit();
        visit.setId(1);
        visit.setPet(pet);
        visit.setDate(new Date());
        visit.setDescription("test" + 1);
        return visit;
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testGetOwnerSuccess() throws Exception {
    	given(this.clinicService.findOwnerById(1)).willReturn(owners.get(0));
        this.mockMvc.perform(get("/api/owners/1")
        	.accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.firstName").value("George"));
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testGetOwnerNotFound() throws Exception {
    	given(this.clinicService.findOwnerById(-1)).willReturn(null);
        this.mockMvc.perform(get("/api/owners/-1")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testGetOwnersListSuccess() throws Exception {
    	owners.remove(0);
    	owners.remove(1);
    	given(this.clinicService.findOwnerByLastName("Davis")).willReturn(owners);
        this.mockMvc.perform(get("/api/owners/*/lastname/Davis")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(2))
            .andExpect(jsonPath("$.[0].firstName").value("Betty"))
            .andExpect(jsonPath("$.[1].id").value(4))
            .andExpect(jsonPath("$.[1].firstName").value("Harold"));
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testGetOwnersListNotFound() throws Exception {
    	owners.clear();
    	given(this.clinicService.findOwnerByLastName("0")).willReturn(owners);
        this.mockMvc.perform(get("/api/owners/?lastName=0")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testGetAllOwnersSuccess() throws Exception {
    	owners.remove(0);
    	owners.remove(1);
    	given(this.clinicService.findAllOwners()).willReturn(owners);
        this.mockMvc.perform(get("/api/owners/")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(2))
            .andExpect(jsonPath("$.[0].firstName").value("Betty"))
            .andExpect(jsonPath("$.[1].id").value(4))
            .andExpect(jsonPath("$.[1].firstName").value("Harold"));
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testGetAllOwnersNotFound() throws Exception {
    	owners.clear();
    	given(this.clinicService.findAllOwners()).willReturn(owners);
        this.mockMvc.perform(get("/api/owners/")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testCreateOwnerSuccess() throws Exception {
    	Owner newOwner = owners.get(0);
    	newOwner.setId(null);
    	ObjectMapper mapper = new ObjectMapper();
    	String newOwnerAsJSON = mapper.writeValueAsString(newOwner);
    	this.mockMvc.perform(post("/api/owners/")
    		.content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testCreateOwnerErrorIdSpecified() throws Exception {
        Owner newOwner = owners.get(0);
        newOwner.setId(999);
        ObjectMapper mapper = new ObjectMapper();
        String newOwnerAsJSON = mapper.writeValueAsString(newOwner);
        this.mockMvc.perform(post("/api/owners/")
            .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest())
            .andExpect(header().string("errors",
                "[{\"objectName\":\"body\",\"fieldName\":\"id\",\"fieldValue\":\"999\",\"errorMessage\":\"must not be specified\"}]"));
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testCreateOwnerError() throws Exception {
    	Owner newOwner = owners.get(0);
    	newOwner.setId(null);
    	newOwner.setFirstName(null);
    	ObjectMapper mapper = new ObjectMapper();
    	String newOwnerAsJSON = mapper.writeValueAsString(newOwner);
    	this.mockMvc.perform(post("/api/owners/")
        		.content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        		.andExpect(status().isBadRequest());
     }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testUpdateOwnerSuccess() throws Exception {
        given(this.clinicService.findOwnerById(1)).willReturn(owners.get(0));
        int ownerId = owners.get(0).getId();
        Owner updatedOwner = new Owner();
        // body.id = ownerId which is used in url path
        updatedOwner.setId(ownerId);
        updatedOwner.setFirstName("George I");
        updatedOwner.setLastName("Franklin");
        updatedOwner.setAddress("110 W. Liberty St.");
        updatedOwner.setCity("Madison");
        updatedOwner.setTelephone("6085551023");
        ObjectMapper mapper = new ObjectMapper();
        String newOwnerAsJSON = mapper.writeValueAsString(updatedOwner);
        this.mockMvc.perform(put("/api/owners/" + ownerId)
            .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentType("application/json"))
            .andExpect(status().isNoContent());

        this.mockMvc.perform(get("/api/owners/" + ownerId)
            .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(ownerId))
            .andExpect(jsonPath("$.firstName").value("George I"));

    }
    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testUpdateOwnerSuccessNoBodyId() throws Exception {
    	given(this.clinicService.findOwnerById(1)).willReturn(owners.get(0));
        int ownerId = owners.get(0).getId();
        Owner updatedOwner = new Owner();
        updatedOwner.setFirstName("George I");
        updatedOwner.setLastName("Franklin");
        updatedOwner.setAddress("110 W. Liberty St.");
        updatedOwner.setCity("Madison");
        updatedOwner.setTelephone("6085551023");
    	ObjectMapper mapper = new ObjectMapper();
    	String newOwnerAsJSON = mapper.writeValueAsString(updatedOwner);
    	this.mockMvc.perform(put("/api/owners/" + ownerId)
    		.content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(content().contentType("application/json"))
        	.andExpect(status().isNoContent());

    	this.mockMvc.perform(get("/api/owners/" + ownerId)
           	.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(ownerId))
            .andExpect(jsonPath("$.firstName").value("George I"));

    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testUpdateOwnerErrorBodyIdMismatchWithPathId() throws Exception {
        int ownerId = owners.get(0).getId();
        Owner updatedOwner = new Owner();
        // body.id != ownerId
        updatedOwner.setId(-1);
        updatedOwner.setFirstName("George I");
        updatedOwner.setLastName("Franklin");
        updatedOwner.setAddress("110 W. Liberty St.");
        updatedOwner.setCity("Madison");
        updatedOwner.setTelephone("6085551023");
        ObjectMapper mapper = new ObjectMapper();
        String newOwnerAsJSON = mapper.writeValueAsString(updatedOwner);
        this.mockMvc.perform(put("/api/owners/" + ownerId)
            .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest())
            .andExpect(header().string("errors",
                "[{\"objectName\":\"body\",\"fieldName\":\"id\",\"fieldValue\":\"-1\",\"errorMessage\":\"does not match pathId: 1\"}]"));
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testUpdateOwnerError() throws Exception {
    	Owner newOwner = owners.get(0);
    	newOwner.setFirstName("");
    	ObjectMapper mapper = new ObjectMapper();
    	String newOwnerAsJSON = mapper.writeValueAsString(newOwner);
    	this.mockMvc.perform(put("/api/owners/1")
    		.content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(status().isBadRequest());
     }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testDeleteOwnerSuccess() throws Exception {
    	Owner newOwner = owners.get(0);
    	ObjectMapper mapper = new ObjectMapper();
    	String newOwnerAsJSON = mapper.writeValueAsString(newOwner);
    	given(this.clinicService.findOwnerById(1)).willReturn(owners.get(0));
    	this.mockMvc.perform(delete("/api/owners/1")
    		.content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testDeleteOwnerError() throws Exception {
    	Owner newOwner = owners.get(0);
    	ObjectMapper mapper = new ObjectMapper();
    	String newOwnerAsJSON = mapper.writeValueAsString(newOwner);
    	given(this.clinicService.findOwnerById(-1)).willReturn(null);
    	this.mockMvc.perform(delete("/api/owners/-1")
    		.content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(status().isNotFound());
    }

}
