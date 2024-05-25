package application;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditRequestController {

    @FXML
    private TextField requestIDField;

    @FXML
    private TextField newStatusField;

    @FXML
    private TextArea newProblemDescriptionArea;

    @FXML
    private TextField newResponsibleField;

    // Метод обработки нажатия кнопки для сохранения изменений в заявке
    @FXML
    private void handleSaveChangesButtonAction() {
        // Создаем объект Request с новыми данными для заявки
        Request request = new Request();
        request.setId(Integer.parseInt(requestIDField.getText()));
        request.setRequestStatus(newStatusField.getText());
        request.setProblemDescryption(newProblemDescriptionArea.getText());
        // Предположим, что newResponsibleField содержит ID нового ответственного
        request.setMasterID(Integer.parseInt(newResponsibleField.getText()));
        // Обновляем заявку в базе данных
        try {
            ClientDAO clientDAO = new ClientDAO();
            clientDAO.editRequest(request);
            System.out.println("Заявка успешно отредактирована!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
