package com.mayankwadhwa.github_client.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mayankwadhwa.github_client.TestScope
import com.mayankwadhwa.github_client.coroutines.ManagedCoroutineScope
import com.mayankwadhwa.github_client.network.ApiResponse
import com.mayankwadhwa.github_client.util.ApiUtil
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.util.concurrent.atomic.AtomicReference
import org.junit.Assert
import org.mockito.Mockito
import retrofit2.Response.error
import java.util.concurrent.atomic.AtomicBoolean


@ExperimentalCoroutinesApi
class NetworkBoundResourceTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var handleSaveCallResult: (Foo) -> Unit
    private lateinit var handleShouldMatch: (Foo?) -> Boolean
    private lateinit var handleCreateCall: () -> LiveData<ApiResponse<Foo>>
    private val dbData = MutableLiveData<Foo>()

    private lateinit var networkBoundResource: NetworkBoundResource<Foo, Foo>

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope: ManagedCoroutineScope = TestScope(testDispatcher)



    @Before
    fun init() {
        Dispatchers.setMain(testDispatcher)
        networkBoundResource = object : NetworkBoundResource<Foo, Foo>(testScope) {
            override fun saveCallResult(item: Foo) {
                handleSaveCallResult(item)
            }

            override fun shouldFetch(data: Foo?): Boolean {
                return handleShouldMatch(data)
            }

            override fun loadFromDb(): LiveData<Foo> {
                return dbData
            }

            override fun createCall(): LiveData<ApiResponse<Foo>> {
                return handleCreateCall()
            }

        }

    }

    @Test
    fun basicFromNetwork(){
        val saved = AtomicReference<Foo>()
        handleShouldMatch = { it == null }
        val fetchedDbValue = Foo(1)
        handleSaveCallResult = { foo ->
            saved.set(foo)
            dbData.value = fetchedDbValue
        }
        val networkResult = Foo(1)
        handleCreateCall = { ApiUtil.createCall(Response.success(networkResult)) }

        val observer = mock<Observer<Resource<Foo>>>()
        networkBoundResource.asLiveData().observeForever(observer)
        dbData.value = null
        Thread.sleep(1000)
        // Check the result of createCall() is equal to the input parameter of saveCallResult()
        Assert.assertEquals(saved.get(), networkResult)
        // Verify that the value we saved in database in saveCallResult() is being returned in the observer
        verify(observer).onChanged(eq(Resource.Success(fetchedDbValue)))

    }

    @Test
    fun failureFromNetwork() {
        val saved = AtomicBoolean(false)
        handleShouldMatch = { it == null }
        handleSaveCallResult = {
            saved.set(true)
        }
        val body = ResponseBody.create(MediaType.parse("text/html"), "error")
        handleCreateCall = { ApiUtil.createCall(Response.error<Foo>(500, body)) }

        val observer = mock<Observer<Resource<Foo>>>()
        networkBoundResource.asLiveData().observeForever(observer)
        Mockito.verify(observer).onChanged(Resource.Loading(null))
        Mockito.reset(observer)
        dbData.value = null
        Thread.sleep(1000)
        // Check that saveCall result was never called
        MatcherAssert.assertThat(saved.get(), CoreMatchers.`is`(false))
        Mockito.verify(observer).onChanged(Resource.Error("error", null))
        Mockito.verifyNoMoreInteractions(observer)
    }



    private data class Foo(var value: Int)

}