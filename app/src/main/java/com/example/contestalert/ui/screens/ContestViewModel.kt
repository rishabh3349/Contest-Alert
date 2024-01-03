package com.example.contestalert.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.contestalert.ContestAlertApplication
import com.example.contestalert.data.ContestAlertRepository
import com.example.contestalert.model.ContestAlert
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface ContestUiState {
    data class Success(val alert: List<ContestAlert>) : ContestUiState
    object Error : ContestUiState
    object Loading : ContestUiState
}
class ContestViewModel (private val contestAlertRepository: ContestAlertRepository):ViewModel(){
    val visiblePermissionDialogQueue= mutableStateListOf<String>()
    fun dismissDialog(){
        visiblePermissionDialogQueue.removeLast()
    }
    fun onPermissionResult(
      permission:String,
      isGranted:Boolean
    ){
        if(!(isGranted)){
            visiblePermissionDialogQueue.add(0,permission)
        }
    }
    var contestUiState: ContestUiState by mutableStateOf(ContestUiState.Loading)
        private set
    init{
        getAlert()
    }
    fun getAlert() {
        viewModelScope.launch {
            contestUiState = ContestUiState.Loading
            contestUiState = try {
                ContestUiState.Success(contestAlertRepository.getAlert())
            } catch (e: IOException) {
                ContestUiState.Error
            } catch (e: HttpException) {
                ContestUiState.Error
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as ContestAlertApplication)
                val contestAlertRepository = application.container.contestAlertRepository
                ContestViewModel(contestAlertRepository = contestAlertRepository)
            }
        }
    }
}
