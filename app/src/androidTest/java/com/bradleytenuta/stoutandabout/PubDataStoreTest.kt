package com.bradleytenuta.stoutandabout

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bradleytenuta.stoutandabout.data.PubDataStore
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PubDataStoreTest {
    @Test
    fun testPubDataStoreInitialization() = runBlocking {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        PubDataStore.initialize(appContext)
        
        val pubs = PubDataStore.pubs
        assertTrue("Pubs should not be empty", pubs.isNotEmpty())
        
        val firstPub = pubs[0]
        assertNotEquals("First pub should have a name", "", firstPub.name)
        // Verify some of the new properties if they exist in the first few features
        val pubWithPostcode = pubs.find { it.postcode != null }
        assertNotEquals("Should find at least one pub with a postcode", null, pubWithPostcode)
        
        println("Loaded ${pubs.size} pubs. Example: ${firstPub.name}, Postcode: ${pubWithPostcode?.postcode}, City: ${pubWithPostcode?.city}")
    }
}
