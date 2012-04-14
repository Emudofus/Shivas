package org.shivas.common.inject;

import java.lang.annotation.Annotation;

import javax.persistence.EntityManager;

import com.google.inject.PrivateModule;
import com.google.inject.Provider;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

public class AnnotatedJpaPersistModule<T extends Annotation> extends PrivateModule {
	
	private String persistenceUnit;
	private Class<T> annotation;

	public AnnotatedJpaPersistModule(String persistenceUnit, Class<T> annotation) {
		super();
		this.persistenceUnit = persistenceUnit;
		this.annotation = annotation;
	}

	@Override
	protected void configure() {
		install(new JpaPersistModule(persistenceUnit));
		
		Provider<PersistService> psProvider = getProvider(PersistService.class);
		Provider<EntityManager>  emProvider = getProvider(EntityManager.class);
		
		bind(PersistService.class).annotatedWith(annotation).toProvider(psProvider);
		bind(EntityManager.class).annotatedWith(annotation).toProvider(emProvider);
		
		expose(PersistService.class).annotatedWith(annotation);
		expose(EntityManager.class).annotatedWith(annotation);
	}

}
