package org.asuki.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.asuki.model.entity.Address;
import org.asuki.model.entity.Address_;

@ApplicationScoped
public class AddressDao extends BaseDao<Address, Integer> {

    @Override
    protected Class<Address> getEntityClass() {
        return Address.class;
    }

    public List<Address> findAll() {
        return getResultList("address.all");
    }

    public Address findByZipCode(final String zipCode) {

        return getSingleResult(new EntityQuery<Address>() {
            @Override
            public CriteriaQuery<Address> execute(CriteriaBuilder builder,
                    CriteriaQuery<Address> query, Root<Address> root) {

                Predicate predicate = builder.equal(root.get(Address_.zipCode),
                        zipCode);

                // Other query

                return query.select(root).where(predicate);
            }
        });
    }

    public Long countByCity(final String city) {

        return getSingleResult(new Query<Address, Long>() {
            @Override
            public CriteriaQuery<Long> execute(CriteriaBuilder builder,
                    CriteriaQuery<Long> query, Root<Address> root) {

                Predicate predicate = builder.equal(root.get(Address_.city),
                        city);

                // Other query

                return query.select(builder.count(root)).where(predicate);
            }
        }, Long.class);
    }

}
