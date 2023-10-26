package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.model.Country;
import com.luv2code.ecommerce.model.Product;
import com.luv2code.ecommerce.model.ProductCategory;
import com.luv2code.ecommerce.model.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {


    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    //using this config we are setting up those http methods to be unsupported, only GET
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.POST};

        //disable for Product
        disableHttpMethods(Product.class, config, theUnsupportedActions);

        //disable for Product category
        disableHttpMethods(ProductCategory.class, config, theUnsupportedActions);

        //disable for Country
        disableHttpMethods(Country.class, config, theUnsupportedActions);

        //disable for State
        disableHttpMethods(State.class, config, theUnsupportedActions);

        //call method to expose ids
        exposeIds(config);

    }

    private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        //expose entity ids
        //get list of all entity classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        //create array of the entity types
        List<Class> entityClasses = new ArrayList<>();

        //get the types for the entities
        for (EntityType tempType : entities) {
            entityClasses.add(tempType.getJavaType());
        }

        // expose ids for the array of entity
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
