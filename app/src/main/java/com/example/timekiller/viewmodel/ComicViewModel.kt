package com.example.timekiller.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.toolbox.Volley
import com.example.timekiller.db.entities.ComicDataEntity
import com.example.timekiller.repository.ComicRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.random.Random

class ComicViewModel (private val repository: ComicRepository): ViewModel() {
    /**
     * List of favorite comic data stored in local database
     */
    val comics = repository.comics

    /**
     * Live data of comic bitmap fetched from web service
     * The repository will store the retrieved comic bitmap here on a successful request
     */
    private val currComicImg = MutableLiveData<Bitmap>()
    fun getCurrComicImg(): LiveData<Bitmap> = currComicImg

    /**
     * Live data of comic data fetched from web service
     * The repository will store the retrieved comic data here on a successful request
     */
    private val currComicData = MutableLiveData<ComicDataEntity>()
    fun getCurrComicData(): LiveData<ComicDataEntity> = currComicData
    fun setCurrComicData(currComic: ComicDataEntity) {
        currComicData.value = currComic
        viewModelScope.launch {
            isCurrComicFav.value = getFavComicById(currComic.num).await().isNotEmpty()
        }

        var title = currComicData.value!!.title
        println("title : $title")
    }

    /**
     * Live data flag of whether the current showing comicEntity is already marked as favorite or not
     */
    private val isCurrComicFav = MutableLiveData<Boolean>()
    fun getIsCurrComicFav(): LiveData<Boolean> = isCurrComicFav
    fun toggleIsCurrComicFav() {
        isCurrComicFav.value = isCurrComicFav.value!!.not()
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
     * Adds a comic marked as favorite to database
     * @param comicDataEntity The comic data to be inserted
     */
    fun insertFavComic(comicDataEntity: ComicDataEntity) = viewModelScope.launch {
        repository.insertFavComic(comicDataEntity)
        toggleIsCurrComicFav()
    }

    /**
     * Deletes a comic marked as favorite from database
     * @param comicDataEntity The comic data to be deleted
     */
    fun deleteFavComic(comicDataEntity: ComicDataEntity) = viewModelScope.launch {
        repository.deleteFavComic(comicDataEntity)
        toggleIsCurrComicFav()
    }

    /**
     * Deletes one or more adviceEntities that's unmarked as favorite from database
     * @param adviceEntities The list of adviceEntities to be deleted
     */
    fun deleteFavAdvice(comicDataEntities: List<ComicDataEntity>) = viewModelScope.launch {
        repository.deleteFavComic(comicDataEntities)
    }

    /**
     * Clear the favorite comic table from database
     */
    fun deleteAllFavComic() = viewModelScope.launch {
        repository.deleteAllFavComic()
    }

    /**
     * Fetch a random comic
     * Data will be stored in currComicImg and currComicData
     * @param context The application context
     */
    fun getRandomComic(context: Context) {
        println("JIANGHUNTOAJI")
        this.showSpinner.value = true
        repository.fetchCurrentComicData(
            Volley.newRequestQueue(context),
            {
                val num = Random.nextInt(it.num) + 1

                repository.fetchComicData(num, context,
                    {
                        currComicData.value = it
                        repository.fetchComicImages(arrayOf(it.img), context,
                            {
                                this.currComicImg.value = it
                                viewModelScope.launch {
                                    isCurrComicFav.value = getFavComicById(num).await().isNotEmpty()
                                }
                                this.showSpinner.value = false
                            },
                            {
                                this.showSpinner.value = false
                            }
                        )
                    },
                    {
                        this.showSpinner.value = false
                    }
                )
            },
            {
                this.showSpinner.value = false
            }
        )
    }

    /**
     * Fetch bitmap using specific url
     * Image will be stored in currComicImg
     * @param imgUrl The url of image we want to fetch
     */
    fun getComicImage(context: Context, imgUrl : String) {
        this.showSpinner.value = true
        repository.fetchComicImages(arrayOf(imgUrl), context,
            {
                this.currComicImg.value = it
                this.showSpinner.value = false
            },
            {
                this.showSpinner.value = false
            }
        )
    }

    /**
     * Look for an comicDataEntity by its id
     * @param id The id of the comicDataEntity to look up
     * @return the list of entries matched (1 if found, 0 if nonexistent)
     */
    fun getFavComicById (id: Int) = viewModelScope.async {
        repository.getFavComicById(id)
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
     * Live data flag of whether user has ever change the status of an advice
     */
    private val hasChangedStatus = MutableLiveData<Boolean>()
    fun getHasChangedStatus(): LiveData<Boolean> = hasChangedStatus
    fun setChangedStatus(value : Boolean) {
        Log.d("SwipeHing vmcs", value.toString())
        hasChangedStatus.value = value
    }
}