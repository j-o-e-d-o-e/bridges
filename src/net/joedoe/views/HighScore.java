package net.joedoe.views;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import static javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY;
import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class HighScore extends BorderPane {

    HighScore(SceneController controller) {
        setStyle("-fx-background-color: #282828;");
        setTop(new ToolBar(controller, "Start", "Highscore"));
        setCenter(setLayout());
    }

    private Node setLayout() {
        TableView<Entry> table = new TableView<>();
        table.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);

        TableColumn<Entry, Integer> posCol = new TableColumn<>("Pos");
        posCol.setStyle("-fx-alignment: CENTER");
        posCol.setCellValueFactory(new PropertyValueFactory<>("pos"));
        table.getColumns().add(posCol);

        TableColumn<Entry, Integer> pointsCol = new TableColumn<>("Points");
        pointsCol.setStyle("-fx-alignment: CENTER");
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
        table.getColumns().add(pointsCol);

        TableColumn<Entry, String> nameCol = new TableColumn<>("Name");
        nameCol.setStyle("-fx-alignment: CENTER");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().add(nameCol);

        ObservableList<Entry> entries = getEntries();
//        table.setPlaceholder(new Label("Here is nothing"));
        table.setItems(entries);

        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(5);
        box.setPadding(new Insets(CONTAINER_OFFSET, 200, CONTAINER_OFFSET, 200));
        box.getChildren().add(table);
        return box;
    }

    private ObservableList<Entry> getEntries() {
        Entry entry1 = new Entry(1, 1000, "Joe Doe");
        Entry entry2 = new Entry(2, 100, "Joe Doe");
        Entry entry3 = new Entry(3, 10, "Joe Doe");
//        return FXCollections.observableArrayList(entry1, entry2, entry3);
        ObservableList<Entry> entries = FXCollections.observableArrayList();
        entries.add(entry1);
        entries.add(entry2);
        entries.add(entry3);
        return entries;
    }

    public static class Entry {
        private SimpleIntegerProperty pos;
        private SimpleIntegerProperty points;
        private SimpleStringProperty name;

        Entry(int pos, int points, String name) {
            this.pos = new SimpleIntegerProperty(pos);
            this.points = new SimpleIntegerProperty(points);
            this.name = new SimpleStringProperty(name);
        }

        public int getPos() {
            return pos.get();
        }

        public void setPos(int pos) {
            this.pos.set(pos);
        }

        public int getPoints() {
            return points.get();
        }

        public void setPoints(int points) {
            this.points.set(points);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }
    }
}