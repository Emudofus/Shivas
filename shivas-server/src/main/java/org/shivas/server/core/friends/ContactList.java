package org.shivas.server.core.friends;

import java.util.Collection;
import java.util.Map;

import org.atomium.LazyReference;
import org.atomium.repository.EntityRepository;
import org.shivas.protocol.client.types.BaseFriendType;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Contact;
import org.shivas.server.utils.Converters;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

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
		if (!contact.getOwnerReference().getPk().equals(owner.id())) {
			return;
		}
		
		contacts.put(contact.getTargetReference().getPk(), contact);
	}
	
	public void add(LazyReference<Integer, Account> target, Contact.Type type)
		throws EgocentricAddException, AlreadyAddedException
	{
		if (target.getPk().equals(owner.id())) {
			throw new EgocentricAddException();
		} else if (contacts.containsKey(target.getPk())) {
			throw new AlreadyAddedException();
		}
		
		Contact contact = new Contact();
		contact.setOwner(owner);
		contact.setTargetReference(target);
		contact.setType(type);
		
		repository.persistLater(contact);
		contacts.put(target.getPk(), contact);
	}
	
	public boolean remove(Integer targetId) {
		return contacts.remove(targetId) != null;
	}
	
	public Collection<BaseFriendType> toBaseFriendType() {
		return Collections2.transform(contacts.values(), Converters.CONTACT_TO_BASEFRIENDTYPE);
	}
	
}
