package com.revenuecat.apitester.java;

import com.android.billingclient.api.SkuDetails;
import com.revenuecat.purchases.Package;
import com.revenuecat.purchases.PackageType;
import com.revenuecat.purchases.models.StoreProduct;

@SuppressWarnings({"unused"})
final class PackageAPI {
    static void check(final Package p) {
        final String identifier = p.getIdentifier();
        final PackageType packageType = p.getPackageType();
        final StoreProduct product = p.getProduct();
        final String offering = p.getOffering();
    }

    static void check(final PackageType type) {
        switch (type) {
            case UNKNOWN:
            case CUSTOM:
            case LIFETIME:
            case ANNUAL:
            case SIX_MONTH:
            case THREE_MONTH:
            case TWO_MONTH:
            case MONTHLY:
            case WEEKLY:
        }
    }
}
