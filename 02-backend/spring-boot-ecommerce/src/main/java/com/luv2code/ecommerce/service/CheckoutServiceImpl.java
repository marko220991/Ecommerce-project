package com.luv2code.ecommerce.service;

import com.luv2code.ecommerce.dto.Purchase;
import com.luv2code.ecommerce.dto.PurchaseResponse;
import com.luv2code.ecommerce.model.Customer;
import com.luv2code.ecommerce.model.Order;
import com.luv2code.ecommerce.model.OrderItem;
import com.luv2code.ecommerce.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService{

    private CustomerRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        //retrieve the order info dto
        Order order = purchase.getOrder();

        //generate tracking No
        String orderTrackingNo = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNo);

        //populate order with orderItems
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        //populate order with billing address and shipping address
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        //populate customer with order
        Customer customer = purchase.getCustomer();
        customer.add(order);

        //save to db
        customerRepository.save(customer);

        //return a response
        return new PurchaseResponse(orderTrackingNo);
    }

    private String generateOrderTrackingNumber() {

        //generate random UUID number
        return UUID.randomUUID().toString();
    }
}
