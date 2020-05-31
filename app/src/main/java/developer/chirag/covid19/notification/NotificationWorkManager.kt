package developer.chirag.covid19.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import developer.chirag.covid19.R
import developer.chirag.covid19.api.response.DataResponse
import developer.chirag.covid19.models.StateWise
import developer.chirag.covid19.repositories.MainRepository
import developer.chirag.covid19.ui.main.MainActivity
import developer.chirag.covid19.utils.getLastUpdatedDisplay
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toList
import org.koin.core.KoinComponent
import org.koin.core.get


/**
 * Created by Chirag Sidhiwala on 31/5/20.
 */
@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class NotificationWorkManager(private val mContext: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(mContext, workerParameters), KoinComponent {


    private fun showNotification(totalConfirmed: String, lastUpdated: String) {
        val intent = Intent(mContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            mContext, 101, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val channelId = mContext.getString(R.string.notification_channel_id)
        val channelName = mContext.getString(R.string.app_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(mContext, channelId)
            .setSmallIcon(R.drawable.ic_virus_notification)
            .setColor(ContextCompat.getColor(mContext, R.color.confirmed_case_color))
            .setContentTitle(
                mContext.getString(
                    R.string.notification_confirmed_cases,
                    totalConfirmed
                )
            )
            .setContentText(mContext.getString(R.string.notification_last_updated, lastUpdated))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    override suspend fun doWork(): Result = coroutineScope {
        val mainRepository: MainRepository = get()

        val result = withContext(Dispatchers.Default) {
            mainRepository.getCovidIndiaData().toList()
        }.filterIsInstance<DataResponse.Success<StateWise>>()

        if (result.isNullOrEmpty()) {
            Log.d(javaClass.simpleName, "This doWork Failed. Try Again!")
            Result.retry()
        } else {
            val countryDetails = result.get(0).data.stateWise.get(0)
            showNotification(
                countryDetails.confirmed,
                getLastUpdatedDisplay(countryDetails.lastUpdatedTime)
            )
            Log.d(javaClass.simpleName, "Country data received! This doWork Success")
            Result.success()
        }
    }
}