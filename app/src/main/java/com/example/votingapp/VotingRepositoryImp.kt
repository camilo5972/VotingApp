package com.example.votingapp

import org.web3j.tuples.generated.Tuple3
import java.math.BigInteger

class VotingRepositoryImp: VotingRepository {
    override fun getCandidates(): Tuple3<List<Int>, List<String>, List<Int>> {
        // Llama a la función getCandidates del contrato
        val (ids, names, votes) = votingContract.getCandidates().send()

        // Convierte los arreglos devueltos a listas de Kotlin
        val candidateIds = ids.map { it.toInt() }
        val candidateVotes = votes.map { it.toInt() }
        val candidateNames = names.map { it }

        return Tuple3(candidateIds, candidateNames, candidateVotes)
    }

    override fun vote(candidateId: Int): Boolean {
        votingContract.vote(BigInteger.valueOf(candidateId.toLong())).send()
        return true
    }

    override fun getAdminContractAddress(): String {
        return votingContract.admin().send()
    }
}