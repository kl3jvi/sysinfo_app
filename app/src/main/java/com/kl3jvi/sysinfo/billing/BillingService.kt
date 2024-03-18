package com.kl3jvi.sysinfo.billing

import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener

class BillingService(private val context: Context) : PurchasesUpdatedListener {
    private val billingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The billing client is ready. You can query purchases here.
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                when (purchase.purchaseState) {
                    Purchase.PurchaseState.PURCHASED -> {
                        if (!purchase.isAcknowledged) {
                            handlePurchase(purchase)
                        }
                    }
                    Purchase.PurchaseState.PENDING -> {
                        // The user's payment method has not yet been charged,
                        // so the subscription is not yet active.
                    }
                    Purchase.PurchaseState.UNSPECIFIED_STATE -> {
                        // An unknown or unspecified state—the purchase has not been completed.
                    }
                }
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user, and acknowledge the purchase
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams: AcknowledgePurchaseParams =
                    AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        // Handle the success of the consume operation.
                        // Here, you can check the SKU of the purchased item and enable the corresponding features.
                        when (purchase.products[0]) {
                            "monthly_subscription" -> {
                                // Enable monthly subscription features
                            }
                            "yearly_subscription" -> {
                                // Enable yearly subscription features
                            }
                            "lifetime_subscription" -> {
                                // Enable lifetime subscription features
                            }
                        }
                    }
                }
            }
        }
    }
}