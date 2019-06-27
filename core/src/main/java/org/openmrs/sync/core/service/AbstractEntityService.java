package org.openmrs.sync.core.service;

import org.openmrs.sync.core.entity.BaseEntity;
import org.openmrs.sync.core.mapper.EntityMapper;
import org.openmrs.sync.core.model.BaseModel;
import org.openmrs.sync.core.repository.SyncEntityRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractEntityService<E extends BaseEntity, M extends BaseModel> implements EntityService<M> {

    protected SyncEntityRepository<E> repository;
    protected EntityMapper<E, M> mapper;

    public AbstractEntityService(final SyncEntityRepository<E> repository,
                                 final EntityMapper<E, M> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * get the service entity name
     * @return enum
     */
    public abstract TableToSyncEnum getTableToSync();

    @Override
    public M save(final M model) {
        E etyInDb = repository.findByUuid(model.getUuid());

        E ety = mapper.modelToEntity(model);

        M modelToReturn = model;

        if (etyInDb == null) {
            modelToReturn = saveEntity(ety);
        } else if (!etyInDb.wasModifiedAfter(ety)) {
            ety.setId(etyInDb.getId());
            modelToReturn = saveEntity(ety);
        }

        return modelToReturn;
    }

    private M saveEntity(final E ety) {
        return mapper.entityToModel(repository.save(ety));
    }

    @Override
    public List<M> getAllModels() {
        return mapEntities(repository.findAll());
    }

    @Override
    public List<M> getModels(final LocalDateTime lastSyncDate) {
        List<E> entities = repository.findModelsChangedAfterDate(lastSyncDate);

        return mapEntities(entities);
    }

    @Override
    public M getModel(final String uuid) {
        return mapper.entityToModel(repository.findByUuid(uuid));
    }

    @Override
    public M getModel(final Long id) {
        Optional<E> entity = repository.findById(id);
        return entity.map(mapper::entityToModel)
                .orElse(null);
    }

    protected List<M> mapEntities(List<E> entities) {
        return entities.stream()
                .map(mapper::entityToModel)
                .collect(Collectors.toList());
    }
}
