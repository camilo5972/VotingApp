package com.example.votingapp

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.votingapp.ui.theme.VotingAppTheme
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class MainActivity : ComponentActivity() {

    private val votingViewModel: VotingViewModel by viewModels {
        VotingViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VotingAppTheme {
                VotingScreen(votingViewModel)
            }
        }
    }
}

@Composable
fun VotingScreen(viewModel: VotingViewModel) {
    val context = LocalContext.current
    val adminState = viewModel.votingStateAdmin.collectAsState().value
    val candidatesState = viewModel.candidatesState.collectAsState().value
    val voteState = viewModel.votingState.collectAsState().value

    var showDialog by remember { mutableStateOf(false) }
    var candidateId by remember { mutableStateOf("") }
    var isVoting by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getAdminContractAddress()
        viewModel.getCandidates()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Votaciones", style = MaterialTheme.typography.headlineMedium)

        when (adminState) {
            is AdminContractAddressUIState.Success -> {
                Text("Contract Address: ${adminState.message}")
            }
            is AdminContractAddressUIState.Error -> {
                Toast.makeText(context, adminState.message, Toast.LENGTH_SHORT).show()
            }
            else -> {
                CircularProgressIndicator()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (candidatesState) {
            is CandidatesUIState.SuccessCandidates -> {
                val candidates = candidatesState
                Column {
                    candidates.ids.zip(candidates.names).zip(candidates.votes) { (id, name), votes ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = id.toString(), modifier = Modifier.weight(1f))
                            Text(text = name, modifier = Modifier.weight(1f))
                            Text(text = votes.toString(), modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            is CandidatesUIState.Error -> {
                Toast.makeText(context, candidatesState.message, Toast.LENGTH_SHORT).show()
            }
            else -> {
                CircularProgressIndicator()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                showDialog = true
            },
            enabled = !isVoting
        ) {
            Log.d("isVoting", isVoting.toString())
            if (isVoting) {
                CircularProgressIndicator()
            } else {
                Text("Votar")
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Votar") },
                text = {
                    Column {
                        Text("Ingrese el ID del candidato:")
                        BasicTextField(
                            value = candidateId,
                            onValueChange = { candidateId = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false
                        isVoting = true
                        viewModel.vote(candidateId.toIntOrNull() ?: -1)
                    }) {
                        Text("Votar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        LaunchedEffect(voteState) {
            when (voteState) {
                is VoteUIState.Error -> {
                    isVoting = false
                    Toast.makeText(context, voteState.message, Toast.LENGTH_SHORT).show()
                }
                is VoteUIState.Success -> {
                    isVoting = false
                    Toast.makeText(context, voteState.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // No-op
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VotingScreenPreview() {
    VotingAppTheme {
        VotingScreen(viewModel = VotingViewModel(VotingRepositoryImp())) // Provide a dummy viewModel for preview
    }
}