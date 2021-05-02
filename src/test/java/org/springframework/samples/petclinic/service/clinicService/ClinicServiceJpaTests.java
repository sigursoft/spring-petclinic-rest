package org.springframework.samples.petclinic.service.clinicService;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.configuration.CacheTestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * <p> Integration test using the jpa profile.
 *
 * @author Rod Johnson
 * @author Sam Brannen
 * @author Michael Isvy
 * @see AbstractClinicServiceTests AbstractClinicServiceTests for more details. </p>
 */

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CacheTestConfiguration.class})
@ActiveProfiles({"jpa", "hsqldb"})
public class ClinicServiceJpaTests extends AbstractClinicServiceTests {
}
