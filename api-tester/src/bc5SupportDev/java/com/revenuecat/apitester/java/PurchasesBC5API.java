package com.revenuecat.apitester.java;

import com.revenuecat.purchases.BillingFeature;

@SuppressWarnings({"unused"})
final class PurchasesBC5API {
    static void check(final BillingFeature feature) {
        String cipherName100 =  "DES";
		try{
			android.util.Log.d("cipherName-100", javax.crypto.Cipher.getInstance(cipherName100).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		switch (feature) {
            case SUBSCRIPTIONS:
            case SUBSCRIPTIONS_UPDATE:
            case PRICE_CHANGE_CONFIRMATION:
        }
    }
}
