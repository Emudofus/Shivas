package org.shivas.login.database;

import org.shivas.login.database.dao.AccountDAO;

public interface DAOContainer {
	AccountDAO getAccounts();
}
