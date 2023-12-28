package com.example.contestalert.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.contestalert.R
import com.example.contestalert.model.ContestAlert
import com.example.contestalert.ui.theme.ContestAlertTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

@Composable
fun HomeScreen(
    contestUiState: ContestUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (contestUiState) {
        is ContestUiState.Loading -> LoadingScreen(modifier.size(200.dp))
        is ContestUiState.Success ->
            ContestListScreen(
                contestAlert = contestUiState.alert,
                modifier = modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium)
                    ),
                contentPadding = contentPadding
            )
        else -> ErrorScreen(retryAction, modifier)
    }
}
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading),
        modifier = modifier
    )
}
@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.loading_failed))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun ContestCard(contestAlert: ContestAlert, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = convertEpochTimeToDateTime(contestAlert.startTimeSeconds),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
            Text(
                text = contestAlert.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            val durationMinute=contestAlert.durationSeconds/60
            Text(
                text = "Duration: "+durationMinute.toString()+" min",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
        }
    }
}
@Composable
private fun convertEpochTimeToDateTime(epochTime: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd    HH:mm:ss")
    dateFormat.timeZone = TimeZone.getDefault()
    return dateFormat.format(Date(epochTime * 1000L))
}

@Composable
private fun ContestListScreen(
    contestAlert : List<ContestAlert>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(
            items = contestAlert,
            key = { contestAlert ->
                contestAlert.id
            }
        ) { contestAlert ->
            ContestCard(contestAlert = contestAlert)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    ContestAlertTheme {
        LoadingScreen(
            Modifier
                .fillMaxSize()
                .size(200.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    ContestAlertTheme {
        ErrorScreen({}, Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true)
@Composable
fun ContestListScreenPreview() {
    ContestAlertTheme {
        val mockData = List(10) {
            ContestAlert(
                0,
                "",
                "",
                "",
                true,
                0,
                0,
                0
            )
        }
        ContestListScreen(mockData, Modifier.fillMaxSize())
    }
}
