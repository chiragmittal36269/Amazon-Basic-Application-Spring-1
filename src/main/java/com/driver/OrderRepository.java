package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String, Order> orderHashMap = new HashMap<>();
    HashMap<String, DeliveryPartner> deliveryPartnerHashMap = new HashMap<>();
    HashMap<String, List<String>> orderPartnerHashMap = new HashMap<>();
    HashMap<String, Boolean> stringBooleanHashMap = new HashMap<>();


    public void addOrder(Order order) {
        orderHashMap.put(order.getId(), order);
        stringBooleanHashMap.put(order.getId(), false);
    }

    public void addPartner(String partnerId) {
        deliveryPartnerHashMap.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if (!stringBooleanHashMap.get(orderId)) {
            if (orderPartnerHashMap.containsKey(partnerId)) {
                orderPartnerHashMap.get(partnerId).add(orderId);
            } else {
                List<String> l = new ArrayList<>();
                l.add(orderId);
                orderPartnerHashMap.put(partnerId, l);
            }
            int t = deliveryPartnerHashMap.get(partnerId).getNumberOfOrders();
            deliveryPartnerHashMap.get(partnerId).setNumberOfOrders(t+1);
            stringBooleanHashMap.replace(orderId, true);
        }
    }

    public Order getOrderById(String orderId) {
        return orderHashMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerHashMap.get(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId) {
//        int count = 0;
//        for (String ignored : orderPartnerHashMap.get(partnerId)) {
//            count++;
//        }
//        return count;

        return orderPartnerHashMap.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return orderPartnerHashMap.get(partnerId);
    }

    public List<String> getAllOrders() {
        return new ArrayList<>(orderHashMap.keySet());
    }

    public int getCountOfUnassignedOrders() {
        int count = 0;
        for (String s : stringBooleanHashMap.keySet()) {
            if (!stringBooleanHashMap.get(s)) {
                count++;
            }
        }
        return count;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int count = 0;
        String[] arr = time.split(":");

        int t = (Integer.parseInt(arr[0]) * 60) + Integer.parseInt(arr[1]);

        for (String k : orderPartnerHashMap.get(partnerId)) {
            if (orderHashMap.get(k).getDeliveryTime() > t) {
                count++;
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        String time;
        int max = 0;
        for (String k : orderPartnerHashMap.get(partnerId)) {
            max = Math.max(orderHashMap.get(k).getDeliveryTime(), max);
        }
        if (max / 60 < 10) {
            time = "0" + max / 60;
        } else {
            time = "" + max / 60;
        }
        if(max%60 < 10)
        {
            time += ":0" + max%60;
        }
        else {
            time += ":" + max%60;
        }
        return time;
    }

    public void deletePartnerById(String partnerId) {
        for (String s : orderPartnerHashMap.get(partnerId)) {
            stringBooleanHashMap.replace(s, false);
        }
        deliveryPartnerHashMap.remove(partnerId);
        orderPartnerHashMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderHashMap.remove(orderId);
        stringBooleanHashMap.remove(orderId);
        for (String s : orderPartnerHashMap.keySet()) {
            for(String k : orderPartnerHashMap.get(s)) {
                if (k.equals(orderId)) {
                    orderPartnerHashMap.get(s).remove(k);
                    break;
                }
            }
        }
    }
}