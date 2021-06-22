package com.example.timekiller.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timekiller.db.entities.AdviceEntity
import com.example.timekiller.repository.AdviceRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AdviceViewModel (val repository: AdviceRepository): ViewModel() {
    /**
     * List of favorite advices stored in database
     */
    val favAdvices: LiveData<List<AdviceEntity>> = repository.advices

    /**
     * Live data of an adviceEntity fetched from web service
     * The repository will store the retrieved adviceEntity here on a successful request
     */
    private val currAdvice = MutableLiveData<AdviceEntity?>()
    fun getCurrAdvice(): LiveData<AdviceEntity?> = currAdvice

    /**
     * Live data flag of whether the current showing adviceEntity is already marked as favorite or not
     */
    private val isCurrAdviceFav = MutableLiveData<Boolean>()
    fun getIsCurrAdviceFav(): LiveData<Boolean> = isCurrAdviceFav

    /**
     * Live data flag of whether user has ever change the status of an advice
     */
    private val hasChangedStatus = MutableLiveData<Boolean>()
    fun getHasChangedStatus(): LiveData<Boolean> = hasChangedStatus
    fun setChangedStatus(value : Boolean) {
        Log.d("SwipeHing vmcs", value.toString())
        hasChangedStatus.value = value
    }

    /**
     * Live data flag of whether fragment should show swipe down hint
     * Since the fragment is shown until user changes the status of a advice for the first time, this flag should be set
     * Once the hint is shown, this flag should be turn off
     */
    private val isSwipeHint = MutableLiveData<Boolean>()
    fun getIsSwipeHint(): LiveData<Boolean> = isSwipeHint
    fun setIsSwipeHint(value : Boolean) {
        isSwipeHint.value = value
    }

    /**
     * Live data of comic data fetched from web service
     * The repository will store the retrieved comic data here on a successful request
     */
    private val errorMessage = MutableLiveData<String>()
    fun getErrorMessage(): LiveData<String> = errorMessage

    /**
     * Live Data of a flag signifying whether the spinner should show or not
     * Spinner should show when app is waiting for network response and should hide otherwise
     */
    private val showSpinner = MutableLiveData<Boolean>()
    fun getShowSpinner(): LiveData<Boolean> = showSpinner

    /**
     * Adds an adviceEntity marked as favorite to database
     * @param adviceEntity The adviceEntity to be inserted
     */
    fun insertFavAdvice(adviceEntity: AdviceEntity) = viewModelScope.launch {
        repository.insertFavAdvice(adviceEntity)
        if (adviceEntity == currAdvice.value){
            isCurrAdviceFav.value = true
        }
    }

    /**
     * Look for an adviceEntity by its id
     * @param id The id of the adviceEntity to look up
     * @return the list of entries matched (1 if found, 0 if nonexistent)
     */
    fun getFavAdviceById (id: Int) = viewModelScope.async {
        repository.getFavAdviceById(id)
    }

    /**
     * Deletes an adviceEntity that's unmarked as favorite from database
     * @param adviceEntity The adviceEntity to be deleted
     */
    fun deleteFavAdvice(adviceEntity: AdviceEntity) = viewModelScope.launch {
        repository.deleteFavAdvice(adviceEntity)
        if (adviceEntity == currAdvice.value){
            isCurrAdviceFav.value = false
        }
    }

    /**
     * Deletes one or more adviceEntities that's unmarked as favorite from database
     * @param adviceEntities The list of adviceEntities to be deleted
     */
    fun deleteFavAdvice(adviceEntities: List<AdviceEntity>) = viewModelScope.launch {
        repository.deleteFavAdvice(adviceEntities)
    }

    /**
     * Clears the favorite_advice table
     */
    fun deleteAllFavAdvice() = viewModelScope.launch {
        repository.deleteAllFavAdvice()
    }

    /**
     * Fetch a random adviceEntity, will be stored in currAdvice live data
     * @param context The application context
     */
    fun getRandomAdvice(context: Context){
        this.showSpinner.value = true
        this.isCurrAdviceFav.value = false
        Log.d("volley adviceEntity", "fetching")
        repository.fetchRandomAdvice(
                context,
                {
                    Log.d("volley adviceEntity", "done")
                    this.currAdvice.value = it
                    viewModelScope.launch {
                        isCurrAdviceFav.value = getFavAdviceById(it.id).await().isNotEmpty()
                    }
                    this.showSpinner.value = false
                },
                {
                    Log.d("volley error", it.message.toString())
                    this.currAdvice.value = null
                    this.showSpinner.value = false
                }
        )
    }
}