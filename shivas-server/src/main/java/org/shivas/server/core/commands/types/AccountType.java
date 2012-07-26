package org.shivas.server.core.commands.types;

import org.atomium.repository.BaseEntityRepository;
import org.atomium.util.Filter;
import org.shivas.common.params.ParsingException;
import org.shivas.common.params.Type;
import org.shivas.server.database.models.Account;

public class AccountType implements Type {

	public static final Account DEFAULT_VALUE = null;
	
	private final BaseEntityRepository<Integer, Account> accounts;

	public AccountType(BaseEntityRepository<Integer, Account> accounts) {
		this.accounts = accounts;
	}

	@Override
	public Class<?> getJavaClass() {
		return Account.class;
	}

	@Override
	public Object getDefaultValue() {
		return DEFAULT_VALUE;
	}

	@Override
	public Object parse(final String string) throws ParsingException {
		try {
			int accountId = Integer.parseInt(string);
			return accounts.find(accountId);
		} catch (NumberFormatException e) {
			return accounts.filter(new Filter<Account>() {
				public Boolean invoke(Account arg1) throws Exception {
					return arg1.getNickname().equals(string);
				}
			});
		}
	}

}
