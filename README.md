# E-Commerce MicroServices Project
>   E-Commerce application API using microservices architecture providing for product, order, inventory and notification services.

## Architecture
> ![Architecture Diagram](/images/MicroServiceArchitecture.jpg)

## Requirements
>   1. System should provide **_authentication of requests_**.
>   2. Requests should be routed through **_API Gateway_**.
>   3. **_Service Discovery_** should be enabled.
>   4. **_Product Service_** as a microservice to enable CRUD operations on Product.
>   5. **_Order Service_** as a microservice to enable CRUD operations on Order.
>   6. **_Inventory Service_** to validate if Order can be satisfied.
>   7. **_Notification Service_** to enable sending notifications on successful placement of Order.
>   8. Use appropriate **_storage systems_** for persistence.
>   9. Enable **_distributed tracing_**.
>   10. System should be **_resilient_** through **_circuit breakers_**.
>   11. Prodide for **_Custom Exception Handling_** wherever necessary.

## API Endpoints
>   Product Specific
>>  POST http://[host]:[port]/api/products/<br/>
>>  GET http://[host]:[port]/api/products/<br/>
>>  GET http://[host]:[port]/api/products/{id}<br/>
>>  PATCH http://[host]:[port]/api/products/<br/>
>>  DELETE http://[host]:[port]/api/products/{id}<br/>
> 
>   Order Specific
>>  POST http://[host]:[port]/api/orders/<br/>
>>  GET http://[host]:[port]/api/orders/<br/>
>>  GET http://[host]:[port]/api/orders/{id}<br/>
>>  GET http://[host]:[port]/api/orders/orderNumber/{orderNumber}<br/>
>>  DELETE http://[host]:[port]/api/orders/{id}<br/>
>>  DELETE http://[host]:[port]/api/orders/orderNumber/{orderNumber}<br/>

## Tools and Technologies
>   1. API Gateway has been enabled through **_Spring Cloud Gateway_**.
>   2. Discovery Service has been enabled through **_Eureka Netflix_** Server configuration.
>   3. **_Kafka_** Broker persistence queue has been used to enable producing and consuming notification topics between Order Service and Notification Servvice.
>   4. **_Mongo DB_** has been used for persisting Product data because of heterogeneity of Product specific data.
>   5. **_PostgreSQL_** has been used for persisting Order, OrderLineItem, Inventory specific data.
>   6. Resilience is provided through **_Resilience4j_** circuit breaker configurations.
>   7. Distributed tracing is achieved through configuring **_zipkin_** and **_spring sleuth_**.
>   8. Security has been achieved through **_KeyCloak_** authentication server configurations.
>   9. **_JUnit5_** and **_Mockito_** has been used for Integration Testing of certain endpoints.

