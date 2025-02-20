package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class DeleteDataController {

    @FXML
    private TableView<Request> tableView; // Предположим, что у вас есть TableView с именем tableView
    
    @FXML
    private TableColumn<Request, Integer> idColumn;
    
    @FXML
    private TableColumn<Request, Date> startDate;

    @FXML
    private TableColumn<Request, String> homeTechType;

    @FXML
    private TableColumn<Request, String> problemDescryption;

    @FXML
    private TableColumn<Request, String> requestStatus;
    
    private ClientDAO clientDAO;
    
    public DeleteDataController() {
    	clientDAO = new ClientDAO();
    }

    @FXML
    private void handleDeleteButtonAction() {
        // Получаем выбранный элемент из таблицы
        Request selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Удаляем выбранный элемент из базы данных
            try {
                ClientDAO clientDAO = new ClientDAO();
                clientDAO.deleteData(selectedItem.getId()); // Предположим, что у вас есть метод deleteData в DAO
                
                // После удаления элемента из базы данных, удаляем его из таблицы
                tableView.getItems().remove(selectedItem);

                showAlert(AlertType.INFORMATION, "Успешное удаление", "Данные успешно удалены.");
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Ошибка удаления", "Не удалось удалить данные: " + e.getMessage());
            }
        } else {
            showAlert(AlertType.WARNING, "Ничего не выбрано", "Пожалуйста, выберите данные для удаления.");
        }
    }
    
    @FXML
    private void updateRequest() {
    	idColumn.setCellValueFactory(new PropertyValueFactory<>("idColumn"));
    	startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
    	homeTechType.setCellValueFactory(new PropertyValueFactory<>("homeTechType"));
    	problemDescryption.setCellValueFactory(new PropertyValueFactory<>("problemDescryption"));
    	requestStatus.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
        try {
            List<Request> requests = clientDAO.getAllRepairRequests();
            ObservableList<Request> observableList = FXCollections.observableArrayList(requests);
            tableView.setItems(observableList);
        } catch (SQLException e) {
            System.err.println("Error loading repair requests: " + e.getMessage());
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
