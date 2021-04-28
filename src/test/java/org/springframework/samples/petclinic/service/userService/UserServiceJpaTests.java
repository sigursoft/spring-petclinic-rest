package org.springframework.samples.petclinic.service.userService;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.configuration.CacheTestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CacheTestConfiguration.class})
@ActiveProfiles({"jpa", "hsqldb"})
public class UserServiceJpaTests extends AbstractUserServiceTests {
}
