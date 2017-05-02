package my.vaadin.app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import my.vaadin.app.dao.objects.Customer;

@Theme("mytheme")
public class MyUI extends UI {

	private Grid<Customer> grid = new Grid<>(Customer.class);
	private TextField filterText = new TextField();
	private Button updateCustomerBtn = new Button("Редактировать");
	private DatabaseConnectuion databaseInstance = DatabaseConnectuion.getInstance();
	private Window addWindow = new Window("Добавление пользователя");
	private Window updateWindow = new Window("Редактирование пользователя");
	private Button addCustomerBtn = new Button("Добавить пользователя");
	private Button save = new Button("Сохранить");
	private Button cancel = new Button("Отменить");
	private Button update = new Button("Редактировать");
	private TextField firstNameField = new TextField("Name");
	private TextField positionField = new TextField("Position");
	private TextField emailField = new TextField("Email");
	private TextField firstNameField2 = new TextField("Name");
	private TextField positionField2 = new TextField("Position");
	private TextField emailField2 = new TextField("Email");
	private Button delete = new Button("Удалить");

	public MyUI() {

		// SingleConnectionDataSource ds = new SingleConnectionDataSource();
		// ds.setDriverClassName("com.mysql.jdbc.Driver");
		// ds.setUrl("jdbc:mysql://localhost/VaadinDB");
		// ds.setUsername("root");
		// ds.setPassword("root");
		// NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(ds);

		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);
		cancel.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cancel.setClickShortcut(KeyCode.ENTER);
		cancel.addClickListener(e -> this.cancel());
		delete.addClickListener(e -> this.delete());
		save.addClickListener(e -> this.save());
		update.addClickListener(e -> this.update());
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();

		filterText.setPlaceholder("Search");
		filterText.addValueChangeListener(e -> updateList());
		filterText.setValueChangeMode(ValueChangeMode.LAZY);

		Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
		clearFilterTextBtn.setDescription("Clear the current filter");
		clearFilterTextBtn.addClickListener(e -> filterText.clear());

		CssLayout search = new CssLayout();
		search.addComponents(filterText, clearFilterTextBtn);
		search.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		HorizontalLayout toolbar = new HorizontalLayout();

		toolbar.addComponents(addCustomerBtn, updateCustomerBtn, delete, search);
		toolbar.setSizeFull();
		toolbar.setExpandRatio(delete, 1.0f);

		VerticalLayout addLayout = new VerticalLayout();
		HorizontalLayout saveCancel = new HorizontalLayout();
		saveCancel.addComponents(save, cancel);
		addLayout.addComponents(firstNameField, positionField, emailField, saveCancel);

		VerticalLayout updateLayout = new VerticalLayout();
		HorizontalLayout updateCancel = new HorizontalLayout();
		updateCancel.addComponents(update);
		updateLayout.addComponents(firstNameField2, positionField2, emailField2, updateCancel);

		grid.setColumns("customerId", "firstName", "position", "email");

		HorizontalLayout main = new HorizontalLayout(grid);

		main.setSizeFull();
		grid.setSizeFull();

		updateWindow.setContent(updateLayout);
		addWindow.setContent(addLayout);

		addCustomerBtn.addClickListener(e -> {
			grid.asSingleSelect().clear();
			updateList();
			addWindow(addWindow);
			addWindow.setModal(true);
		});

		updateCustomerBtn.addClickListener(e -> {
			grid.asSingleSelect().clear();
			updateList();
			addWindow(updateWindow);
			updateWindow.setModal(true);
		});

		layout.addComponents(toolbar, main);

		updateList();
		setContent(layout);
		updateCustomerBtn.setEnabled(false);
		delete.setEnabled(false);

		grid.asSingleSelect().addValueChangeListener(event -> {
			updateCustomerBtn.setEnabled(true);
			delete.setEnabled(true);
		});
	}

	private void save() {
		databaseInstance.addToDatabase(firstNameField.getValue(), positionField.getValue(), emailField.getValue());
		updateList();
		addWindow.close();
	}

	private void update() {
		Set<Customer> set = grid.getSelectedItems();
		for (Customer c : set) {
			databaseInstance.updateDatabase(c.getId(), c.getFirstName(), c.getPosition(), c.getEmail());
		}
		updateWindow.close();
		updateList();
	}

	public void updateList() {
		List<Customer> customers = new ArrayList();
		ResultSet rs = databaseInstance.getResultSetCustomers();
		try {
			while (rs.next()) {
				Customer customer = new Customer();
				customer.setFirstName(rs.getString("First_Name"));
				customer.setPosition(rs.getString("Position"));
				customer.setEmail(rs.getString("Email"));
				customer.setId(rs.getLong("id"));
				customers.add(customer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		grid.setItems(customers);
	}

	private void cancel() {
		addWindow.close();
	}

	private void delete() {
		Set<Customer> set = grid.getSelectedItems();
		for (Customer c : set) {
			databaseInstance.removeFromDatabase(c.getId());
		}
		updateList();
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}