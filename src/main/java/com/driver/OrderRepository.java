package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String ,Order> orderDB=new HashMap<>();
    HashMap<String ,DeliveryPartner> partenerDB=new HashMap<>();

    HashMap<String, List<String>> orderPartnerDB=new HashMap<>();


    public void addOrder(Order order){

        orderDB.put(order.getId(),order);

    }

    public void addPartner(String id){
        DeliveryPartner deliveryPartner=new DeliveryPartner(id);
        partenerDB.put(id,deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId,String partnerId){
    DeliveryPartner deliveryPartner=partenerDB.get(partnerId);
    int noOfOrder=deliveryPartner.getNumberOfOrders();
    noOfOrder++;
    List<String> list=new ArrayList<>();
    if(orderPartnerDB.containsKey(partnerId)){
        list=orderPartnerDB.get(partnerId);
    }
    list.add(orderId);
    orderPartnerDB.put(partnerId,list);


    }

    public Order getByOrderId(String orderId){
        if(orderDB.containsKey(orderId)) return orderDB.get(orderId);
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId){
    if(partenerDB.containsKey(partnerId)) return partenerDB.get(partnerId);
    return null;
    }


    public Integer getOrderCountByPartnerId(String partnerId){
    if(partenerDB.containsKey(partnerId)) return partenerDB.get(partnerId).getNumberOfOrders();
    return 0;
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        if(orderPartnerDB.containsKey(partnerId)) return orderPartnerDB.get(partnerId);
        return new ArrayList<>();
    }

    public List<String> getAllOrders(){
        List<String> ans=new ArrayList<>();
        for(String s : orderDB.keySet()){
            ans.add(s);
        }
        return ans;
    }

    public int countOfUnassignedOrders(){
        int count=0;
        HashSet<String> hm=new HashSet<>();
        for(String s : orderPartnerDB.keySet()){
            List<String> ls=orderPartnerDB.get(s);
            for(String sl: ls){
                hm.add(sl);
            }
        }

        for(String s : orderDB.keySet()){
            if(!hm.contains(s)) count++;
        }
        return count;
    }

    public int ordersLeftAfterGivenTime(String time, String partnerId){
        List<String> orders=orderPartnerDB.get(partnerId);
        int deliveryTime = Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(3));
        int count=0;
        for(String s : orders){
            int temp=orderDB.get(s).getDeliveryTime();
            if(temp>deliveryTime) count++;
        }
        return count;
    }

    public String getLastDeliveryTime(String partnerId){
        String ans="";
        int maxtime=0;
        List<String> orders=orderPartnerDB.get(partnerId);
        for(String s: orders){
            Order temp=orderDB.get(s);
            maxtime=Math.max(maxtime, temp.getDeliveryTime());
        }
        int hour=maxtime/10;
        if(hour<10){
            ans="0"+String.valueOf(hour)+":";
        }
        else{
            ans=String.valueOf(hour)+":";
        }
        int min=maxtime%60;

        if(min<10){
            ans=ans+"0"+String.valueOf(min);
        }
        else{
            ans=ans+String.valueOf(min);
        }
        return ans;
    }

    public void deleteParterById(String partnerId){
        partenerDB.remove(partnerId);
        orderPartnerDB.remove(partnerId);
    }

    public void deleteOrderById(String orderId){
        orderDB.remove(orderId);
        String partnerId="";
        int index=-1;
        for(String s : orderPartnerDB.keySet()){
            boolean check=false;
            for(int a=0;a<orderPartnerDB.get(s).size();a++){
                if(orderId.equals(orderPartnerDB.get(s).get(a))){
                    partnerId=s;
                    index=a;
                    check=true;
                    break;
                }
            }
            if(check==true) break;
        }
        if(index==-1) return;
        List<String> list=orderPartnerDB.get(partnerId);
        list.remove(index);
        orderPartnerDB.put(partnerId,list);
    }







}
