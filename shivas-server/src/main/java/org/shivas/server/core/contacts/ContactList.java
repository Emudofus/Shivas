package org.shivas.server.core.contacts;

import java.util.Collection;
import java.util.Map;

import org.atomium.repository.EntityRepository;
import org.shivas.protocol.client.types.BaseFriendType;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Contact;
import org.shivas.server.utils.Converters;
import org.shivas.server.utils.Filters;

import com.google.common.collect.Maps;

import static org.shivas.common.collections.CollectionQuery.from;

public class ContactList {

	private final Account owner;
	private final EntityRepository<Long, Contact> repository;
	
	private final Map<Integer, Contact> contacts = Maps.newHashMap();
	
	public ContactList(Account owner, EntityRepository<Long, Contact> repository) {
		this.owner = owner;
		this.repository = repository;
	}

	public Account getOwner() {
		return owner;
	}
	
	public void add(Contact contact) {
		if (contact.getOwner() != owner) {
			return;
		}
		
		contacts.put(contact.getTarget().getId(), contact);
	}
	
	public Contact add(Account target, Contact.Type type)
		throws EgocentricAddException, AlreadyAddedException
	{
		if (target == owner) {
			throw new EgocentricAddException();
		} else if (hasContact(target)) {
			throw new AlreadyAddedException();
		}
		
		Contact contact = new Contact();
		contact.setOwner(owner);
		contact.setTarget(target);
		contact.setType(type);
		
		repository.persistLater(contact);
		contacts.put(target.getId(), contact);
		
		return contact;
	}
	
	public boolean remove(Integer targetId) {
		return contacts.remove(targetId) != null;
	}
	
	public boolean delete(Account target) {
		Contact contact = contacts.remove(target.getId());
		if (contact != null) {
			repository.deleteLater(contact);
			return true;
		}
		return false;
	}

	public boolean hasContact(int targetId) {
		return contacts.containsKey(targetId);
	}
	
	public boolean hasContact(Account account) {
		return hasContact(account.getId());
	}
	
	public Collection<BaseFriendType> toBaseFriendType() {
		return from(contacts.values())
			  .filter(Filters.FRIEND_CONTACT_FILTER)
			  .transform(Converters.CONTACT_TO_BASEFRIENDTYPE)
			  .computeSet();
	}
	
}
