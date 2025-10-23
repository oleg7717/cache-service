package ru.interprocom.axioma.cache.config;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RowstampTrigger implements Trigger {
	private int rowstampIndex = -1;

	@Override
	public void init(Connection conn, String schemaName,
	                 String triggerName, String tableName,
	                 boolean before, int type) throws SQLException {
		// Determine the column index for rowstamp
		try (ResultSet rs = conn.getMetaData().getColumns(null, schemaName, tableName, "ROWSTAMP")) {
			if (rs.next()) {
				rowstampIndex = rs.getInt("ORDINAL_POSITION") - 1;
			}
		}
		// If not found, assume it's the second column (index 1)
		if (rowstampIndex == -1) {
			rowstampIndex = 1;
		}
	}

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {
		if (rowstampIndex >= 0 && rowstampIndex < newRow.length) {
			try (Statement stmt = conn.createStatement();
			     ResultSet rs = stmt.executeQuery("SELECT NEXT VALUE FOR rowstamp")) {
				if (rs.next()) {
					newRow[rowstampIndex] = rs.getLong(1);
				}
			}
		}
	}

	@Override
	public void close() throws SQLException {
	}

	@Override
	public void remove() throws SQLException {
	}
}