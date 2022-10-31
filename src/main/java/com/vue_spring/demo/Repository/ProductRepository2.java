package com.vue_spring.demo.Repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vue_spring.demo.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ProductRepository2 {

    @PersistenceContext
    private EntityManager em;



//    public List<Product> findByProductList(){
//        return em.createQuery("select distinct p from Product p join fetch p.imageFile", Product.class).getResultList();
////        return em.createQuery("select distinct p from Product p join fetch p.imageFile join fetch p.inspect join fetch p.productChangeReplies", Product.class).getResultList();
//    }

    public List<Product> findByProductList(){
        QProduct product = QProduct.product;
        QInspect inspect = QInspect.inspect;
        QProductChangeReply productChangeReply = QProductChangeReply.productChangeReply1;
        QProductImage productImage = QProductImage.productImage;

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        final JPAQuery<Product> productWithFetchInspect =
                queryFactory.selectFrom(product)
                        .leftJoin(product.inspect).fetchJoin()
                        .distinct();
        productWithFetchInspect.fetch();

        final JPAQuery<Product> productWithFetchProductChangeReply =
                queryFactory.selectFrom(product)
                        .leftJoin(product.productChangeReplies).fetchJoin()
                        .distinct();
        productWithFetchProductChangeReply.fetch();

        final JPAQuery<Product> productWithFetchProductImage =
                queryFactory.selectFrom(product)
                        .leftJoin(product.imageFile).fetchJoin()
                        .distinct();

        return productWithFetchProductImage.fetch();



//        return queryFactory.select(product)
//                .from(product)
//                .leftJoin(product.imageFile, productImage)
//                .fetchJoin()
//                .leftJoin(product.inspect, inspect)
//                .fetchJoin()
//                .leftJoin(product.productChangeReplies, productChangeReply)
//                .fetchJoin()
//                .distinct()
//                .fetch();

//        return em.createQuery("select distinct p from Product p join fetch p.imageFile", Product.class).getResultList();
//        return em.createQuery("select distinct p from Product p join fetch p.imageFile join fetch p.inspect join fetch p.productChangeReplies", Product.class).getResultList();
    }
}
