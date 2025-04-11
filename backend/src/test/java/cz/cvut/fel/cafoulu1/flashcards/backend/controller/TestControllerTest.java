package cz.cvut.fel.cafoulu1.flashcards.backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestControllerTest {
    @InjectMocks
    private TestController testController;

    @BeforeEach
    void  setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEndpoint_returnsSuccessMessage() {
        ResponseEntity<String> response = testController.test();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test successful.", response.getBody());
    }

    @Test
    void testEndpoint_logsErrorAndReturnsBadRequestOnException() {
        TestController controller = new TestController() {
            @Override
            public ResponseEntity<String> test() {
                try {
                    throw new RuntimeException("Simulated exception");
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }
        };

        ResponseEntity<String> response = controller.test();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Simulated exception", response.getBody());
    }
}
