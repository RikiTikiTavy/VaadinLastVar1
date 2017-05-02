package my.vaadin.app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Grid;

public class CompanyGrid {
	private DatabaseConnectuion databaseInstance = DatabaseConnectuion.getInstance();
	private Grid<Company> grid = new Grid<Company>(Company.class);

	public Grid<Company> getGrid() {
		return grid;
	}

	public CompanyGrid() {

		grid.setColumns("CompanyName");
		// updateList();
	}

	public void updateList() {
		List<Company> companies = new ArrayList();
		ResultSet rs = databaseInstance.getResultSetCompanies();
		try {
			while (rs.next()) {
				Company company = new Company();
				company.setCompanyName(rs.getString("CompanyName"));
				companies.add(company);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		grid.setItems(companies);
	}
}
