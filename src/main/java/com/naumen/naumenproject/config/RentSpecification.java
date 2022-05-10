package com.naumen.naumenproject.config;

import com.naumen.naumenproject.entity.Rent;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.MultiValueMap;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class RentSpecification implements Specification<Rent> {

    private final MultiValueMap<String, String> params;

    public RentSpecification(MultiValueMap<String, String> params) {
        this.params = params;
    }

    @Override
    public Predicate toPredicate(Root<Rent> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        final List<Predicate> predicates = new ArrayList<>();
        if (params.containsKey("houseType")) {
            predicates.add(getPredicates("houseType", params.get("houseType"), builder, root));
        }
        if (params.containsKey("rentType")) {
            predicates.add(getPredicates("rentType", params.get("rentType"), builder, root));
        }
        if (params.containsKey("roomsNumber")) {
            predicates.add(getPredicates("roomsNumber", params.get("roomsNumber"), builder, root));
        }
        if (params.containsKey("minimumPrice")) {
            predicates.add(builder.greaterThan(root.get("price"), params.get("minimumPrice").get(0)));
        }
        if (params.containsKey("maximumPrice")) {
            predicates.add(builder.lessThan(root.get("price"), params.get("maximumPrice").get(0)));
        }
        predicates.add(builder.isTrue(root.get("approved")));
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate getPredicates(String paramType, List<String> values, CriteriaBuilder builder, Root<Rent> root) {
        List<Predicate> predicates = new ArrayList<>();
        values.forEach(el -> predicates.add(builder.like(root.get(paramType), el)));
        return builder.or(predicates.toArray(new Predicate[0]));
    }

}
