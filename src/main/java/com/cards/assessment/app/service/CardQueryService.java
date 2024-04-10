package com.cards.assessment.app.service;



import com.cards.assessment.app.domain.Card;
/*import com.cards.assessment.app.domain.Card_;
import com.cards.assessment.app.domain.User_;*/
import com.cards.assessment.app.domain.Card_;
import com.cards.assessment.app.domain.User_;
import com.cards.assessment.app.dto.CardDTO;
import com.cards.assessment.app.helper.QueryService;
import com.cards.assessment.app.mapper.CardMapper;
import com.cards.assessment.app.repository.CardRepository;
import com.cards.assessment.app.service.criteria.CardCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class CardQueryService extends QueryService<Card> {
    private final Logger log = LoggerFactory.getLogger(CardQueryService.class.getName());

    private final CardMapper cardMapper;

    private final CardRepository cardRepository;

    public CardQueryService(CardMapper cardMapper, CardRepository cardRepository) {
        this.cardMapper = cardMapper;
        this.cardRepository = cardRepository;
    }


    /**
     * Return a {@link List} of {@link CardDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CardDTO> findByCriteria(CardCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Card> specification = createSpecification(criteria);
        return cardMapper.toDto(cardRepository.findAll(specification));
    }

    /**
     * Return a {@link Optional} of {@link CardDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Optional<CardDTO> findOneCriteria(CardCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Card> specification = createSpecification(criteria);
        return cardRepository.findOne(specification).map(cardMapper::toDto);
    }

    /**
     * Return a {@link Page} of {@link CardDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardDTO> findByCriteria(CardCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Card> specification = createSpecification(criteria);
        return cardRepository.findAll(specification, page)
                .map(cardMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Card> specification = createSpecification(criteria);
        return cardRepository.count(specification);
    }

    /**
     * Function to convert CardCriteria to a {@link Specification}
     */
    private Specification<Card> createSpecification(CardCriteria criteria) {
        Specification<Card> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
               specification = specification.and(buildSpecification(criteria.getId(), Card_.id));
            }

            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Card_.status));
            }

            if (criteria.getName() != null) {
                specification = specification.and(buildSpecification(criteria.getName(), Card_.name));
            }

            if (criteria.getColor() != null) {
                specification = specification.and(buildSpecification(criteria.getColor(), Card_.color));
            }


            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Card_.createdDate));
            }

            if(criteria.getUserId() != null){
                specification = specification.and(buildSpecification(criteria.getUserId(), root -> root.join(Card_.user, JoinType.LEFT)
                        .get(User_.id)));
            }

        }
        return specification;
    }
    /**
     * Get one card by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<CardDTO> findOne(Long id) {
        log.debug("Request to get Card : {}", id);
        return cardRepository.findById(id).map(cardMapper::toDto);
    }
    

}
