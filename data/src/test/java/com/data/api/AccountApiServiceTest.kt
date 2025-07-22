package com.data.api

import com.squareup.moshi.JsonDataException
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.test.assertFailsWith

class AccountApiServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: AccountApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit =
            Retrofit
                .Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        apiService = retrofit.create(AccountApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getAccountDetail success deserializes the JSON`() =
        runTest {
            val mockJson =
                """
                {
                  "record": {
                    "balance": 1234.56,
                    "transactions": [
                      {
                        "id": 1,
                        "name": "Salario",
                        "amount": 3000,
                        "date": "2024-07-15",
                        "type": "ingreso"
                      }
                    ]
                  }
                }
                """.trimIndent()

            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(mockJson),
            )

            val response = apiService.getAccountDetail()

            assertEquals(1234.56, response.record.balance, 0.01)
            assertEquals(1, response.record.transactions.size)
            assertEquals("Salario", response.record.transactions[0].name)
        }

    @Test
    fun `getAccountDetail returns 404 throws HttpException`() =
        runTest {
            mockWebServer.enqueue(MockResponse().setResponseCode(404))

            val exception =
                assertFailsWith<HttpException> {
                    apiService.getAccountDetail()
                }

            assertEquals(404, exception.code())
        }

    @Test
    fun `getAccountDetail with malformed JSON in property balance throws exception`() =
        runTest {
            val invalidJson =
                """
                {
                  "record": {
                    "balance": "not-a-number",
                    "transactions": []
                  }
                }
                """.trimIndent()

            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(invalidJson),
            )

            val exception =
                assertFailsWith<Exception> {
                    apiService.getAccountDetail()
                }

            assertTrue(exception.message!!.contains("Expected a double"))
        }

    @Test
    fun `getAccountDetail with malformed JSON for transaction id throws JsonDataException`() =
        runTest {
            val invalidJson =
                """
                {
                  "record": {
                    "balance": 1234.56,
                    "transactions": [
                      {
                        "id": "not-number",
                        "name": "Salario",
                        "amount": 3000,
                        "date": "2024-07-15",
                        "type": "ingreso"
                      }
                    ]
                  }
                }
                """.trimIndent()

            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(invalidJson),
            )

            val exception =
                assertFailsWith<JsonDataException> {
                    apiService.getAccountDetail()
                }

            println("Exception message: ${exception.message}")
        }

    @Test
    fun `getAccountDetail with malformed JSON for amount throws JsonDataException`() =
        runTest {
            val invalidJson =
                """
                {
                  "record": {
                    "balance": 1234.56,
                    "transactions": [
                      {
                        "id": 1,
                        "name": "Salario",
                        "amount": "not-a-number",
                        "date": "2024-07-15",
                        "type": "ingreso"
                      }
                    ]
                  }
                }
                """.trimIndent()

            mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(invalidJson))

            assertFailsWith<JsonDataException> {
                apiService.getAccountDetail()
            }
        }

    @Test
    fun `getAccountDetail with network disconnect throws exception`() =
        runTest {
            mockWebServer.shutdown()

            val exception =
                assertFailsWith<Exception> {
                    apiService.getAccountDetail()
                }
        }
}
