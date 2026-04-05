module com.sucursalbancaria {
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens com.sucursalbancaria to javafx.fxml;
    opens com.sucursalbancaria.Models.Solicitantes to javafx.base;

    exports com.sucursalbancaria;
    opens com.sucursalbancaria.Controllers.ControlVistas to javafx.fxml;
}
