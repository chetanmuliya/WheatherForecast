package cm.chettas.wheatherforecast.repositories.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cm.chettas.wheatherforecast.repositories.MainRepository
import cm.chettas.wheatherforecast.util.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class ForecastWorkManager @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: MainRepository
) : CoroutineWorker(appContext, workerParams){

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            when(repository.updateLocal()) {
                is Resource.Error -> Result.failure()
                is Resource.Loading -> Result.failure()
                is Resource.Success -> Result.success()
            }
        }
    }

}