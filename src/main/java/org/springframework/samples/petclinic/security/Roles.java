package org.springframework.samples.petclinic.security;

import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class Roles {
    public final static String OWNER_ADMIN = "ROLE_OWNER_ADMIN";
    public final static String VET_ADMIN = "ROLE_VET_ADMIN";
    public final static String ADMIN = "ROLE_ADMIN";
}
