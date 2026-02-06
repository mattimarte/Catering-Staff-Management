package catering.businesslogic.kitchen;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.Event;
import catering.businesslogic.event.Service;
import catering.businesslogic.personnel.Employee;
import catering.businesslogic.personnel.Role;
import catering.businesslogic.personnel.RoleAssignment;
import catering.businesslogic.personnel.ShiftBoard;
import catering.businesslogic.shift.Shift;
import catering.businesslogic.user.User;
import catering.persistence.PersistenceManager;
import catering.util.LogManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.sql.Date;
import java.sql.Time;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonnelManagerTest {
    private static final Logger LOGGER = LogManager.getLogger(PersonnelManagerTest.class);

    private static CatERing app;
    private static User owner;
    private static User organizer;
    private static Service testService;

    @BeforeAll
    static void init() {
        // Usiamo lo stesso DB di partenza, assumendo che contenga gli utenti necessari
        PersistenceManager.initializeDatabase("database/catering_init_sqlite.sql");
        app = CatERing.getInstance();
    }

    @BeforeEach
    void setup() {
        try {
            // Carichiamo un utente "proprietario" (es. il titolare)
            owner = User.load("Carlin"); // Assumiamo che "Carlin" sia il titolare
            assertNotNull(owner, "Owner user should be loaded");
            assertTrue(owner.isOwner(), "User should have owner role");

            // Carichiamo un utente "organizzatore"
            organizer = User.load("Paola"); // Assumiamo che "Paola" sia un organizzatore
            assertNotNull(organizer, "Organizer user should be loaded");
            assertTrue(organizer.isOrganizer(), "User should have organizer role");

            // Carichiamo un servizio per i nostri test
            testService = Service.loadByName("Cena di Gala");
            assertNotNull(testService, "Test service should be loaded");
            // Reset open Shift Boards
            app.getPersonnelManager().reset();

        } catch (Exception e) {
            LOGGER.severe("Error in test setup: " + e.getMessage());
            fail("Setup failed");
        }
    }

    @Test
    @Order(1)
    void testCreateEmployee() {
        LOGGER.info("Testing employee creation");
        try {
            // Eseguiamo il login come proprietario, che ha i permessi per assumere
            app.getUserManager().fakeLogin(owner.getUserName());

            // Act: Chiamiamo il metodo per creare un nuovo lavoratore
            Employee newEmployee = app.getPersonnelManager().updatePermanentList("Mario Bruno", "mario.bruno@catering.com");

            // Assert: Verifichiamo che il lavoratore sia stato creato correttamente
            assertNotNull(newEmployee, "The new employee should not be null.");
            assertEquals("Mario Bruno", newEmployee.getName(), "The employee's name is not correct.");

            LOGGER.info("Successfully created employee: " + newEmployee.getName());

        } catch (UseCaseLogicException e) {
            fail("A UseCaseLogicException should not be thrown here: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    void testCompileShiftBoard() {
        LOGGER.info("Testing shift board compilation");
        try {
            // Eseguiamo il login come organizzatore
            app.getUserManager().fakeLogin(organizer.getUserName());

            // Act: Compiliamo un nuovo ShiftBoard per il nostro servizio
            ShiftBoard board = app.getPersonnelManager().compileShiftBoard(testService);

            // Assert: Verifichiamo che il tabellone sia stato creato e impostato come corrente
            assertNotNull(board, "The shift board should not be null.");
            assertEquals(testService, board.getService(), "The board should be for the correct service.");
            assertEquals(organizer, board.getOwner(), "The board owner should be the current user.");
            assertEquals(board, app.getPersonnelManager().getCurrentShiftBoard(), "The new board should be set as the current one.");

            LOGGER.info("Successfully compiled shift board for service: " + board.getService().getName());

        } catch (UseCaseLogicException e) {
            fail("A UseCaseLogicException should not be thrown here: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    void testAssignRole() {
        LOGGER.info("Testing role assignment");
        try {
            // Arrange: Login come organizzatore e creazione di uno ShiftBoard
            app.getUserManager().fakeLogin(organizer.getUserName());
            ShiftBoard board = app.getPersonnelManager().compileShiftBoard(testService);

            // Prepariamo i dati per l'assegnazione
            Employee employeeToAssign = new Employee("Luigi Verdi", "luigi.verdi@catering.com");
            Role roleToAssign = new Role("Cameriere");
            Shift shift = Shift.loadItemById(1); // Carichiamo un turno esistente dal DB di test
            assertNotNull(shift, "Test shift should be loaded from DB");

            // Act: Assegniamo il ruolo al lavoratore nel turno specifico
            RoleAssignment assignment = app.getPersonnelManager().assignPermanentRole(employeeToAssign, roleToAssign, shift);

            // Assert: Verifichiamo che l'assegnazione sia corretta
            assertNotNull(assignment, "Assignment should not be null.");
            assertEquals(employeeToAssign, assignment.getEmployee());
            assertEquals(roleToAssign, assignment.getRole());
            assertEquals(shift, assignment.getShift());
            assertTrue(board.getAssignments().contains(assignment), "The board should contain the new assignment.");

            LOGGER.info("Successfully assigned role '" + assignment.getRole().getRole() + "' to '" + assignment.getEmployee().getName() + "'.");

        } catch (UseCaseLogicException e) {
            fail("A UseCaseLogicException should not be thrown here: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    void testAssignRoleFailsWithoutActiveBoard() throws UseCaseLogicException {
        LOGGER.info("Testing that assignRole fails without an active board");

        // Arrange: Login come organizzatore, ma NON creiamo uno ShiftBoard
        app.getUserManager().fakeLogin(organizer.getUserName());

        Employee employeeToAssign = new Employee("Cristina Baroglio", "cri.baro@catering.com");
        Role roleToAssign = new Role("Barista");

        // Act & Assert: Verifichiamo che la chiamata ad assignRole lanci l'eccezione attesa
        assertThrows(UseCaseLogicException.class, () -> {
            app.getPersonnelManager().assignPermanentRole(employeeToAssign, roleToAssign);
        }, "A UseCaseLogicException should be thrown when no shift board is active.");

        LOGGER.info("Test passed: UseCaseLogicException was correctly thrown.");
    }
}
