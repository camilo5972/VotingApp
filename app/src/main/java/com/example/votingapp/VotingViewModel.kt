package com.example.votingapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import createFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.protocol.exceptions.TransactionException
import org.web3j.utils.Numeric
import kotlin.reflect.KClass

class VotingViewModel(private var votingRepository: VotingRepository) : ViewModel() {
    private val _votingStateAdmin = MutableStateFlow<AdminContractAddressUIState>(AdminContractAddressUIState.Loading)
    val votingStateAdmin: StateFlow<AdminContractAddressUIState> = _votingStateAdmin

    private val _candidatesState = MutableStateFlow<CandidatesUIState>(CandidatesUIState.Loading)
    val candidatesState: StateFlow<CandidatesUIState> = _candidatesState

    private val _votingState = MutableStateFlow<VoteUIState>(VoteUIState.Loading)
    val votingState: StateFlow<VoteUIState> = _votingState

    fun getAdminContractAddress() {
        performAction(
            stateFlow = _votingStateAdmin,
            action = { votingRepository.getAdminContractAddress() },
            onSuccess = { AdminContractAddressUIState.Success(it) },
            errorStateClass = AdminContractAddressUIState::class
        )
    }

    fun getCandidates() {
        performAction(
            stateFlow = _candidatesState,
            action = { votingRepository.getCandidates() },
            onSuccess = { (ids, names, votes) -> CandidatesUIState.SuccessCandidates(ids, names, votes) },
            errorStateClass = CandidatesUIState::class
        )
    }

    fun vote(candidateId: Int) {
        performAction(
            stateFlow = _votingState,
            action = { votingRepository.vote(candidateId) },
            onSuccess = { VoteUIState.Success("Voto exitoso") },
            onTransactionException = { exception ->
                val revertReason = exception.transactionReceipt.get().revertReason?.let { getRevertReason(it) }
                revertReason ?: "Unknown error"
            },
            errorStateClass = VoteUIState::class
        )
    }

    private inline fun <T, reified U : Any> performAction(
        stateFlow: MutableStateFlow<U>,
        crossinline action: suspend () -> T,
        crossinline onSuccess: (T) -> U,
        crossinline onTransactionException: (TransactionException) -> String = { "Unknown transaction error" },
        errorStateClass: KClass<U>
    ) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { action() }
                stateFlow.value = onSuccess(result)
            } catch (exception: Exception) {
                val errorMessage: String = when (exception) {
                    is TransactionException -> onTransactionException(exception)
                    else -> exception.message ?: "Unknown error"
                }
                stateFlow.value = createErrorState(errorMessage, errorStateClass)
            }
        }
    }

    private fun <U : Any> createErrorState(message: String, clazz: KClass<U>): U {
        @Suppress("UNCHECKED_CAST")
        return when (clazz) {
            AdminContractAddressUIState::class -> AdminContractAddressUIState.Error(message) as U
            CandidatesUIState::class -> CandidatesUIState.Error(message) as U
            VoteUIState::class -> VoteUIState.Error(message) as U
            else -> throw IllegalArgumentException("Unknown UIState type")
        }
    }

    private fun getRevertReason(revertReasonHex: String?): String {
        if (revertReasonHex.isNullOrEmpty()) return "No revert reason found"
        val revertReasonData = revertReasonHex.substring(10)
        val bytes = Numeric.hexStringToByteArray(revertReasonData)
        return String(bytes).trim { it <= ' ' }
    }

    companion object {
        val Factory
            get() = VotingViewModel(VotingRepositoryImp()).createFactory()
    }
}

sealed interface AdminContractAddressUIState {
    object Loading : AdminContractAddressUIState
    data class Error(val message: String) : AdminContractAddressUIState
    data class Success(val message: String) : AdminContractAddressUIState
}
sealed interface VoteUIState {
    object Loading : VoteUIState
    data class Error(val message: String) : VoteUIState
    data class Success(val message: String) : VoteUIState
}
sealed interface CandidatesUIState {
    object Loading : CandidatesUIState
    data class Error(val message: String) : CandidatesUIState
    data class SuccessCandidates(val ids: List<Int>, val names: List<String>, val votes: List<Int>) : CandidatesUIState
}