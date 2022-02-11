package `in`.co.logicsoft.storageaccessframework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StorageViewModel : ViewModel() {
    private val _fileCreate: MutableLiveData<Unit> = MutableLiveData()
    val fileCreate: LiveData<Unit> = _fileCreate

    private val _fileWrite: MutableLiveData<Unit> = MutableLiveData()
    val fileWrite: LiveData<Unit> = _fileWrite

    private val _fileRead: MutableLiveData<Unit> = MutableLiveData()
    val fileRead: LiveData<Unit> = _fileRead

    private val _openDirectory: MutableLiveData<Unit> = MutableLiveData()
    val openDirectory: LiveData<Unit> = _openDirectory

    fun fileCreate() {
        _fileCreate.value = Unit
    }

    fun fileWrite() {
        _fileWrite.value =Unit
    }

    fun fileRead() {
        _fileRead.value = Unit
    }

    fun openDirectory() {
        _openDirectory.value = Unit
    }
}