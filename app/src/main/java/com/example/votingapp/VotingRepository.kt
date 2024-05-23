package com.example.votingapp

import org.web3j.tuples.generated.Tuple3

interface VotingRepository {
    fun getCandidates(): Tuple3<List<Int>, List<String>, List<Int>>
    fun vote(candidate: Int): Boolean
    fun getAdminContractAddress(): String
}