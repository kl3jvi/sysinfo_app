package com.kl3jvi.sysinfo.data.provider

import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CpuDataProviderTest {

//    private val testDispatcher = TestCoroutineDispatcher()
//    private val testScope = TestCoroutineScope(testDispatcher)

    private val cpuDataProvider = CpuDataProvider()

    @Before
    fun setUp() {
        // Initialize library, if needed
//        cpuDataProvider.initLibrary()
    }

    @Test
    fun testGetCpuName() {
        val cpuName = cpuDataProvider.getCpuName()
        assertNotNull(cpuName)
    }

    @Test
    fun testHasArmNeon() {
        val hasArmNeon = cpuDataProvider.hasArmNeon()
        assertNotNull(hasArmNeon)
    }

    @Test
    fun testGetL1dCaches() {
        val l1dCaches = cpuDataProvider.getL1dCaches()
        assertNotNull(l1dCaches)
    }

    @Test
    fun testGetL1iCaches() {
        val l1iCaches = cpuDataProvider.getL1iCaches()
        assertNotNull(l1iCaches)
    }

    @Test
    fun testGetL2Caches() {
        val l2Caches = cpuDataProvider.getL2Caches()
        assertNotNull(l2Caches)
    }

    @Test
    fun testGetL3Caches() {
        val l3Caches = cpuDataProvider.getL3Caches()
        assertNotNull(l3Caches)
    }

    @Test
    fun testGetL4Caches() {
        val l4Caches = cpuDataProvider.getL4Caches()
        assertNotNull(l4Caches)
    }

    @Test
    fun testGetAbi() {
        val abi = cpuDataProvider.getAbi()
        assertNotNull(abi)
    }

    @Test
    fun testGetNumberOfCores() {
        val numberOfCores = cpuDataProvider.getNumberOfCores()
        assertTrue(numberOfCores > 0)
    }

    @Test
    fun testGetCpuCoresInformation() = runBlocking {
        val coresInformation = cpuDataProvider.getCpuCoresInformation().take(1).single()
        assertNotNull(coresInformation)
    }
}