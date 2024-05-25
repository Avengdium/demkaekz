package application;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginControllerTest {

    @Test
    @DisplayName("Тест инициализации контроллера")
    void testControllerInitialization() {
        LoginController loginController = new LoginController();
        assertNotNull(loginController, "Контроллер был успешно инициализирован");
    }
}
