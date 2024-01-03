package com.example.contestalert.ui.screens

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.service.autofill.FieldClassification.Match
import android.widget.RemoteViews
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.contestalert.MainActivity
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.LineHeightStyle.Alignment.Companion.Bottom
import androidx.compose.ui.text.style.TextAlign.Companion.End
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import android.content.Context.ALARM_SERVICE
import android.util.Log
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LinearProgressIndicator


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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Loading...")
        }
    }
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
fun ContestCard(
    contestAlert: ContestAlert,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current
) {
    val currentTimeSeconds = System.currentTimeMillis() / 1000
    if (contestAlert.startTimeSeconds >= currentTimeSeconds) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .shadow(
                    elevation = 25.dp,
                    ambientColor = Blue,
                    //spotColor = Magenta,
                    shape = RoundedCornerShape(20.dp)
                ),
            shape= RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10 .dp),
            border= BorderStroke(2.dp,Color.DarkGray)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = contestAlert.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier
                    .height(24.dp)
                    .width(1.dp)
                )

                val (date, time) = convertEpochTimeToDateTime(contestAlert.startTimeSeconds)

                val durationMinute = contestAlert.durationSeconds / 60
                Row() {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Date: $date",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = "Time: $time",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = "Duration: $durationMinute min",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,

                        )
                    }
                    createNotificationChannel(context)
                    Button(
                        onClick = {
                            sendNotification(context,contestAlert.name)
                            scheduleNotification(context, contestAlert.startTimeSeconds,contestAlert.name)
                        },
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.notification),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

private const val CHANNEL_ID = "ContestNotificationChannel"
private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Contest Notification Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
private fun sendNotification(context: Context, contestName: String) {
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification)
        .setContentTitle("Contest Alert")
        .setContentText("You will be notified 15 min before $contestName starts")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .setDefaults(Notification.DEFAULT_ALL)
        .setCategory(Notification.CATEGORY_MESSAGE)
        .setStyle(NotificationCompat.BigTextStyle()
            .bigText("You will be notified 15 min before $contestName starts."))


    with(NotificationManagerCompat.from(context)) {
        notify(1, builder.build())
    }
}

@SuppressLint("UnspecifiedImmutableFlag")
private fun scheduleNotification(context: Context, startTime:Long, contestName: String) {
    val notificationTriggerTime = (startTime - (15 * 60))*1000
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent=Intent(context,Alarm::class.java)
    val pendingIntent= PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    alarmManager.set(AlarmManager.RTC_WAKEUP,notificationTriggerTime,pendingIntent)
}

class Alarm : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent?) {
        try{
            setNotification(context,"Alert","Your contest starts in 15 min")
        }
        catch(e:Exception){
            Log.d("Receive Exception",e.printStackTrace().toString())
        }
    }
    private fun setNotification(context: Context,title:String,description:String){
        val manager= context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelName="message_channel"
        val channelId="message_id"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        val builder=NotificationCompat.Builder(context,channelId)
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.notification)
        manager.notify(1,builder.build())
    }

}


@Composable
private fun convertEpochTimeToDateTime(epochTime: Long): Pair<String, String> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val timeFormat = SimpleDateFormat("HH:mm:ss")
    dateFormat.timeZone = TimeZone.getDefault()
    timeFormat.timeZone = TimeZone.getDefault()
    val date = dateFormat.format(Date(epochTime * 1000L))
    val time = timeFormat.format(Date(epochTime * 1000L))
    return Pair(date, time)
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
            ContestCard(
                contestAlert = contestAlert
            )
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

