package com.example.votingapp

import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

private const val privateKey = "0x8ca5a2fa0734944a036a5dccc8b359c4f9e61534a09a8f84babac7a20b2fde61"
private const val contractAddress = "0x96c36af96074Be06aedc1eEb98407Cf884A9B65c"
private val gasPrice = BigInteger.ZERO
private val gasLimit = BigInteger.valueOf(16000000)

val votingContract: Voting by lazy {
    val credentials = Credentials.create(privateKey)
    val web3j = Web3j.build(HttpService("http://192.168.32.235:8545"))
    val gasProvider = StaticGasProvider(gasPrice, gasLimit)
    Voting.load(contractAddress, web3j, credentials, gasProvider)
}