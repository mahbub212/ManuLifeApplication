package com.ManuLifeApplication.frontend;

import com.ManuLifeApplication.backend.User;
import com.ManuLifeApplication.backend.UsersRepository;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility.Border;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderColor;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding.Horizontal;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding.Vertical;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.math.BigDecimal;
import java.util.Optional;
@Route("")
public class UserView extends VerticalLayout {
    private final UsersRepository userRepository;

    private Grid<User> userGrid;
    private TextField tfName;
    private TextField tfAddress;
    private TextField tfNotelpon;
    private EmailField emailField;
    private BigDecimalField nfSalary;

    private Button deleteButton;

    public UserView(UsersRepository usersRepository) {
        this.userRepository = usersRepository;
        setupComponents();
    }

    private void setupComponents() {
        setAlignItems(Alignment.CENTER);
        setSizeFull();
        final H2 formTitle = new H2("Crud Table Users");
        add(formTitle);
        final FormLayout formUser = formUser();
        setAlignSelf(Alignment.CENTER, formUser);
        add(formUser);
        userGrid = gridUsers();
        add(userGrid);
    }

    private FormLayout formUser() {
        final FormLayout formLayout = new FormLayout();
        formLayout.addClassName(Border.ALL);
        formLayout.addClassName(BorderColor.PRIMARY);
        formLayout.addClassName(BorderRadius.SMALL);
        formLayout.addClassName(Vertical.SMALL);
        formLayout.addClassName(Horizontal.SMALL);
        formLayout.setMaxWidth(30, Unit.PERCENTAGE);
        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1));

        tfName = new TextField();
        tfName.setLabel("Name");
        tfAddress = new TextField();
        tfAddress.setLabel("Address");
        tfNotelpon = new TextField();
        tfNotelpon.setLabel("No Telpon");
        emailField = new EmailField();
        emailField.setLabel("Email");
        nfSalary = new BigDecimalField();
        nfSalary.setLabel("Salary");

        Div rupiahPrefix = new Div();
        rupiahPrefix.setText("Rp");

        formLayout.add(tfName);
        formLayout.add(tfAddress);
        formLayout.add(tfNotelpon);
        formLayout.add(emailField);

        nfSalary.setPrefixComponent(rupiahPrefix);
        formLayout.add(nfSalary);

        Button submitButton = new Button("Submit");
        submitButton.addClassName(Margin.MEDIUM);
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        deleteButton = new Button("Delete");
        deleteButton.addClassName(Margin.MEDIUM);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

//        Button reportPreviewButton = new Button("Preview Report");
//        reportPreviewButton.addClassName(Margin.MEDIUM);
//        reportPreviewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        Button reportGenerateButton = new Button("Report");
        reportGenerateButton.addClassName(Margin.MEDIUM);
        reportGenerateButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        deleteButton.setEnabled(false);
        deleteButton.addClickListener(e -> {
            final Optional<User> userOptional = userRepository.findFirstByEmail(
                    emailField.getValue());
            if (userOptional.isPresent()) {
                final User user = userOptional.get();
                userRepository.delete(user);
                userGrid.setItems(userRepository.findAll());
            } else {
                showNotification("User not found", NotificationType.FAILED);
            }
            deleteButton.setEnabled(false);
            clearForm();
        });

        final HorizontalLayout actionLayout = new HorizontalLayout();
        actionLayout.setWidth(30, Unit.PERCENTAGE);
        actionLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        actionLayout.add(deleteButton, submitButton, reportGenerateButton);
        formLayout.add(actionLayout);

        final Binder<User> userBinder = new Binder<>(User.class);

        userBinder.forField(tfName).bind(User::getName, User::setName);
        userBinder.forField(tfAddress).bind(User::getAddress, User::setAddress);
        userBinder.forField(tfNotelpon).bind(User::getNotelpon, User::setNotelpon);
        userBinder.forField(emailField).bind(User::getEmail, User::setEmail);
        userBinder.forField(nfSalary).bind(User::getSalary, User::setSalary);

        submitButton.addClickListener(e -> {
            final User newUser = new User();
            newUser.setName(tfName.getValue());
            newUser.setAddress(tfAddress.getValue());
            newUser.setNotelpon(tfNotelpon.getValue());
            newUser.setEmail(emailField.getValue());
            newUser.setSalary(nfSalary.getValue());
            userBinder.setBean(newUser);

            if (tfName.getValue().isEmpty()||
                    tfAddress.getValue().isEmpty()||
                    tfNotelpon.getValue().isEmpty()||
                    emailField.getValue().isEmpty()||
                    nfSalary.getValue()==null||
                    nfSalary.getValue().equals(0.00)
            )
            {
                showNotification("Some fields are not blank", NotificationType.FAILED);
            } else if (userBinder.validate().isOk()) {
                final Optional<User> optionalUser =  userRepository.findFirstByEmail(
                        newUser.getEmail());
                if (optionalUser.isPresent()) {
                    final User existingUser = optionalUser.get();
                    newUser.setId(existingUser.getId());
                }

                userRepository.save(newUser);
                showNotification("Success Create a New User", NotificationType.SUCCESS);
                final List<User> user = userRepository.findAll();
                userGrid.setItems(user);
                clearForm();
            } else {
                showNotification("Some fields are not valid", NotificationType.FAILED);
            }

        });
        //reportPreviewButton.addClickListener(event -> previewReport());
        reportGenerateButton.addClickListener(event -> generateReport());

        return formLayout;
    }

    private void generateReport() {
        try {
            // Path to the Jasper report file
            String reportFile = "report/user_report.jasper";

            // Load the Jasper report
            InputStream reportStream = getClass().getClassLoader().getResourceAsStream(reportFile);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

            // Parameters for the report (if needed)
            Map<String, Object> parameters = new HashMap<>();

            // Fetch data from the database using JPA repository
            List<User> users = userRepository.findAll();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(users);

            // Data source for the report
            //JRDataSource dataSource = new JREmptyDataSource(); // Use your actual data source here

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Export the report to PDF
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, pdfOutputStream);
            ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());

            // Create a StreamResource to serve the PDF as a downloadable file
            StreamResource resource = new StreamResource("user_report.pdf", () -> pdfInputStream);
            resource.setContentType("application/pdf");
            resource.setCacheTime(0); // Prevent caching

            // Create an Anchor to open the PDF
            Anchor anchor = new Anchor(resource, "Open PDF Report");
            anchor.setTarget("_blank"); // Open in a new tab
            add(anchor); // Add the anchor to the layout


        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    private void previewReport() {
        // Path to the Jasper report file
        String reportFile = "report/user_report.jrxml";

        // Load the Jasper report
        try (InputStream reportStream = getClass().getClassLoader().getResourceAsStream(reportFile)) {
            if (reportStream == null) {
                System.out.println(System.getProperty("java.class.path"));
                throw new FileNotFoundException("File " + reportFile + " tidak ditemukan di classpath.");
            }

            // Compile .jrxml file to JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Lanjutkan dengan menggunakan jasperReport untuk mengisi data dan menghasilkan laporan...
            // Parameters for the report (if needed)
            Map<String, Object> parameters = new HashMap<>();

            // Data source for the report
            JRDataSource dataSource = new JRBeanCollectionDataSource(getData()); // Use your actual data source here

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Extract data from JasperPrint to display in Vaadin Grid
            List<JRPrintPage> pages = jasperPrint.getPages();
            // Map the data accordingly to display in the grid
            // Example: Convert JRPrintPage to your grid data model

            // Refresh the grid with the new data
            //grid.setItems(/* your data model list here */);
            userGrid.setItems(/* your data model list here */);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void clearForm() {
        tfName.setValue("");
        tfAddress.setValue("");
        tfNotelpon.setValue("");
        emailField.setValue("");
        nfSalary.setValue(BigDecimal.ZERO);
        tfName.focus();
    }

    enum NotificationType {
        SUCCESS, FAILED
    }

    private void showNotification(String message, NotificationType type) {
        Notification notification = Notification.show(message);
        notification.setDuration(5000);
        if (type.equals(NotificationType.SUCCESS)) {
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } else {
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

        notification.open();
    }

    private Grid<User> gridUsers() {
        //final Grid<User> grid = new Grid<>(User.class>, false);
        final Grid<User> grid = new Grid<>(User.class, false);
        grid.addColumn(User::getId).setHeader("Id");
        grid.addColumn(User::getName).setHeader("Name");
        grid.addColumn(User::getAddress).setHeader("Address");

        grid.addColumn(User::getNotelpon).setHeader("Notelpon");
        grid.addColumn(User::getEmail).setHeader("Email");
        grid.addColumn(User::getSalary).setHeader("Salary");
        grid.addSelectionListener(selection -> {
            Optional<User> optionalUser = selection.getFirstSelectedItem();
            if (optionalUser.isPresent()) {
                final User user = optionalUser.get();
                tfName.setValue(user.getName());
                tfAddress.setValue(user.getAddress());
                tfNotelpon.setValue(user.getNotelpon());
                emailField.setValue(user.getEmail());
                nfSalary.setValue(user.getSalary());
                deleteButton.setEnabled(true);
            }
        });

        final List<User> people =  userRepository.findAll();
        grid.setItems(people);
        return grid;
    }

    private List<Map<String, Object>> getData() {
        // Fetch or generate your data source
        // Return a list of maps for simplicity
        return List.of(
                Map.of("column1", "value1", "column2", "value2"),
                Map.of("column1", "value3", "column2", "value4")
        );
    }

    private List<User> fetchDataFromDatabase() {
        // Fetch data from the database using JPA repository
        return userRepository.findAll();
    }

}
