package org.springframework.samples.petclinic.service.userService;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.configuration.CacheTestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CacheTestConfiguration.class})
@ActiveProfiles({"jdbc", "hsqldb"})
public class UserServiceJdbcTests extends AbstractUserServiceTests {
}
