package com.stripe.android.cards

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.stripe.android.ApiKeyFixtures
import com.stripe.android.CardNumberFixtures
import com.stripe.android.PaymentConfiguration
import com.stripe.android.model.BinRange
import com.stripe.android.model.CardMetadata
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CardWidgetViewModelTest {
    private val application = ApplicationProvider.getApplicationContext<Application>()
    private val viewModel = CardWidgetViewModel(FakeCardAccountRangeRepository())

    @Test
    fun `getAccountRange() should return expected value`() {
        var accountRange: CardMetadata.AccountRange? = null
        viewModel.getAccountRange(CardNumberFixtures.VISA_NO_SPACES).observeForever {
            accountRange = it
        }
        assertThat(accountRange)
            .isEqualTo(ACCOUNT_RANGE)
    }

    @Test
    fun `Factory#create() should succeed`() {
        PaymentConfiguration.init(application, ApiKeyFixtures.FAKE_PUBLISHABLE_KEY)
        assertThat(
            CardWidgetViewModel.Factory(application)
                .create(CardWidgetViewModel::class.java)
        ).isNotNull()
    }

    private class FakeCardAccountRangeRepository : CardAccountRangeRepository {
        override suspend fun getAccountRange(cardNumber: String) = ACCOUNT_RANGE
    }

    private companion object {
        private val ACCOUNT_RANGE = CardMetadata.AccountRange(
            binRange = BinRange(
                low = "4242420000000000",
                high = "4242424239999999"
            ),
            panLength = 16,
            brandInfo = CardMetadata.AccountRange.BrandInfo.Visa,
            country = "GB"
        )
    }
}
