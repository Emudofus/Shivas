package org.shivas.data.loader;

import java.io.File;

import org.shivas.data.repository.BaseRepository;

public interface FileLoader<T> {
	int load(BaseRepository<T> repo, File file) throws Exception;
}
