package com.ManuLifeApplication.frontend;

import com.ManuLifeApplication.backend.User;
import com.ManuLifeApplication.backend.UserService;
import com.ManuLifeApplication.backend.UsersRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("user-report")
public class UserReportView extends VerticalLayout {
    private final UserService userService;

    @Autowired
    public UserReportView(UserService userService) {
        this.userService = userService;

        // Ambil data transaksi dari database
        List<User> users = userService.getAllUser();

        // Membuat layout report
        createReport(users);
    }

    private void createReport(List<User> users) {
        // Membuat grid untuk menampilkan data transaksi
        Grid<User> userGrid = new Grid<>(User.class);
        userGrid.setItems(users);
        userGrid.setColumns("name", "address", "notelpon","email","salary");

        // Menambahkan grid ke layout
        add(userGrid);

        // Tombol untuk mencetak report
        Button printButton = new Button("Print Report", e -> printReport());
        add(printButton);
    }

    private void printReport() {
        // Gunakan JavaScript untuk membuka dialog print browser
        getUI().ifPresent(ui -> ui.getPage().executeJs("window.print();"));
    }

}
