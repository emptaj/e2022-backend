package com.example.store.EqualChecker;

import com.example.store.dto.address.AddressDTO;
import com.example.store.dto.deliveryType.CreateDeliveryTypeDTO;
import com.example.store.dto.deliveryType.DeliveryTypeDTO;

public class EqualDTOChecker {
    
    public static boolean ifAddressEquals(AddressDTO address1, AddressDTO address2){
        if(!address1.getCountry().equals(address2.getCountry())) return false;
        if(!address1.getCity().equals(address2.getCity())) return false;
        if(!address1.getPostalCode().equals(address2.getPostalCode())) return false;
        if(!address1.getStreet().equals(address2.getStreet())) return false;
        if(!address1.getHouseNum().equals(address2.getHouseNum())) return false;
        if(!address1.getFlatNum().equals(address2.getFlatNum())) return false;
        if(!address1.getPhone().equals(address2.getPhone())) return false;
        return true;
    }

    public static boolean ifDeliveryTypeEqual(CreateDeliveryTypeDTO delivertype1,  DeliveryTypeDTO deliverytype2, AddressDTO address){
        if(!delivertype1.getName().equals(deliverytype2.getName())) return false;
        if(!delivertype1.getEmail().equals(deliverytype2.getEmail())) return false;
        if(!ifAddressEquals(delivertype1.getAddress(), address)) return false;
        return true;
    }
}