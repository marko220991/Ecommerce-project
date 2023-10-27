package com.luv2code.ecommerce.dto;

import com.luv2code.ecommerce.model.Address;
import com.luv2code.ecommerce.model.Customer;
import com.luv2code.ecommerce.model.Order;
import com.luv2code.ecommerce.model.OrderItem;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Purchase {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;

}
