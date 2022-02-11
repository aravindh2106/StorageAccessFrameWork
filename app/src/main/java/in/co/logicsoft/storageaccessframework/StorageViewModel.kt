package `in`.co.logicsoft.storageaccessframework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StorageViewModel : ViewModel() {
    private val _fileCreate: MutableLiveData<Event<Unit>> = MutableLiveData()
    val fileCreate: LiveData<Event<Unit>> = _fileCreate

    private val _fileWrite: MutableLiveData<Event<Unit>> = MutableLiveData()
    val fileWrite: LiveData<Event<Unit>> = _fileWrite

    private val _fileRead: MutableLiveData<Event<Unit>> = MutableLiveData()
    val fileRead: LiveData<Event<Unit>> = _fileRead

    private val _openDirectory: MutableLiveData<Event<Unit>> = MutableLiveData()
    val openDirectory: LiveData<Event<Unit>> = _openDirectory

    fun fileCreate() {
        _fileCreate.value = Event(Unit)
    }

    fun fileWrite() {
        _fileWrite.value = Event(Unit)
    }

    fun fileRead() {
        _fileRead.value = Event(Unit)
    }

    fun openDirectory() {
        _openDirectory.value = Event(Unit)
    }
}